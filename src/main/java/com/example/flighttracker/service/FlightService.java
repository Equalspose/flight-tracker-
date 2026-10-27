package com.example.flighttracker.service;

import com.example.flighttracker.dto.FlightDto;
import com.example.flighttracker.dto.FlightEventDto;
import com.example.flighttracker.dto.FlightRequest;
import com.example.flighttracker.dto.StatusUpdateRequest;
import com.example.flighttracker.entity.Airport;
import com.example.flighttracker.entity.Flight;
import com.example.flighttracker.entity.FlightEvent;
import com.example.flighttracker.entity.FlightStatus;
import com.example.flighttracker.exception.ConflictException;
import com.example.flighttracker.exception.NotFoundException;
import com.example.flighttracker.repository.FlightEventRepository;
import com.example.flighttracker.repository.FlightRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static com.example.flighttracker.repository.FlightSpecifications.*;

@Service
@Transactional(readOnly = true)
public class FlightService {

    /** Статусы, после которых рейс больше не меняется. */
    private static final Set<FlightStatus> FINAL_STATUSES =
            EnumSet.of(FlightStatus.LANDED, FlightStatus.CANCELLED, FlightStatus.DIVERTED);

    private final FlightRepository flightRepository;
    private final FlightEventRepository eventRepository;
    private final AirportService airportService;
    private final AirlineService airlineService;

    public FlightService(FlightRepository flightRepository, FlightEventRepository eventRepository,
                         AirportService airportService, AirlineService airlineService) {
        this.flightRepository = flightRepository;
        this.eventRepository = eventRepository;
        this.airportService = airportService;
        this.airlineService = airlineService;
    }

    public List<FlightDto> search(FlightSearchCriteria c) {
        Specification<Flight> spec = Specification.where(flightNumber(c.flightNumber()))
                .and(airline(c.airline()))
                .and(from(c.from()))
                .and(to(c.to()))
                .and(status(c.status()))
                .and(departureAfter(c.departureFrom()))
                .and(departureBefore(c.departureTo()));
        return flightRepository.findAll(spec, Sort.by("scheduledDeparture"))
                .stream().map(FlightDto::from).toList();
    }

    /** Табло вылетов аэропорта за дату (дата — по местному времени аэропорта). */
    public List<FlightDto> departures(String airportCode, LocalDate date) {
        Airport airport = airportService.getEntity(airportCode);
        OffsetDateTime[] day = dayBounds(airport, date);
        return search(new FlightSearchCriteria(null, null, airport.getIataCode(), null, null, day[0], day[1]));
    }

    /** Табло прилётов аэропорта за дату (по плановому времени прилёта). */
    public List<FlightDto> arrivals(String airportCode, LocalDate date) {
        Airport airport = airportService.getEntity(airportCode);
        OffsetDateTime[] day = dayBounds(airport, date);
        Specification<Flight> spec = Specification.where(to(airport.getIataCode()))
                .and((root, q, cb) -> cb.between(root.get("scheduledArrival"), day[0], day[1]));
        return flightRepository.findAll(spec, Sort.by("scheduledArrival"))
                .stream().map(FlightDto::from).toList();
    }

    public FlightDto get(Long id) {
        return FlightDto.from(getEntity(id));
    }

    public List<FlightEventDto> history(Long id) {
        getEntity(id);
        return eventRepository.findByFlightIdOrderByCreatedAtAsc(id).stream().map(FlightEventDto::from).toList();
    }

    @Transactional
    public FlightDto create(FlightRequest request) {
        Flight flight = new Flight();
        apply(flight, request);
        flight.setStatus(FlightStatus.SCHEDULED);
        flight = flightRepository.save(flight);
        logEvent(flight, null, FlightStatus.SCHEDULED, "Рейс создан");
        return FlightDto.from(flight);
    }

    @Transactional
    public FlightDto update(Long id, FlightRequest request) {
        Flight flight = getEntity(id);
        apply(flight, request);
        return FlightDto.from(flight);
    }

    @Transactional
    public FlightDto updateStatus(Long id, StatusUpdateRequest request) {
        Flight flight = getEntity(id);
        FlightStatus old = flight.getStatus();
        FlightStatus target = request.status();

        if (FINAL_STATUSES.contains(old)) {
            throw new ConflictException("Рейс в конечном статусе " + old + " и не может быть изменён");
        }
        if (old == target) {
            throw new ConflictException("Рейс уже в статусе " + target);
        }

        OffsetDateTime eventTime = request.eventTime() != null ? request.eventTime() : OffsetDateTime.now();
        if (target == FlightStatus.IN_AIR && flight.getActualDeparture() == null) {
            flight.setActualDeparture(eventTime);
        }
        if (target == FlightStatus.LANDED || target == FlightStatus.DIVERTED) {
            if (flight.getActualDeparture() == null) {
                throw new ConflictException("Нельзя приземлиться, не вылетев: сначала переведите рейс в IN_AIR");
            }
            if (eventTime.isBefore(flight.getActualDeparture())) {
                throw new IllegalArgumentException("Время посадки не может быть раньше фактического вылета");
            }
            flight.setActualArrival(eventTime);
        }

        flight.setStatus(target);
        logEvent(flight, old, target, request.comment());
        return FlightDto.from(flight);
    }

    @Transactional
    public void delete(Long id) {
        flightRepository.delete(getEntity(id));
    }

    private Flight getEntity(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Рейс не найден: id=" + id));
    }

    private void apply(Flight flight, FlightRequest r) {
        if (r.departureAirportCode().equalsIgnoreCase(r.arrivalAirportCode())) {
            throw new IllegalArgumentException("Аэропорты вылета и прилёта должны различаться");
        }
        if (!r.scheduledArrival().isAfter(r.scheduledDeparture())) {
            throw new IllegalArgumentException("Время прилёта должно быть позже времени вылета");
        }
        flight.setFlightNumber(r.flightNumber().toUpperCase().replace(" ", ""));
        flight.setAirline(airlineService.getEntity(r.airlineCode()));
        flight.setDepartureAirport(airportService.getEntity(r.departureAirportCode()));
        flight.setArrivalAirport(airportService.getEntity(r.arrivalAirportCode()));
        flight.setScheduledDeparture(r.scheduledDeparture());
        flight.setScheduledArrival(r.scheduledArrival());
        flight.setGate(r.gate());
        flight.setAircraftType(r.aircraftType());
    }

    private void logEvent(Flight flight, FlightStatus old, FlightStatus target, String comment) {
        FlightEvent event = new FlightEvent();
        event.setFlight(flight);
        event.setOldStatus(old);
        event.setNewStatus(target);
        event.setComment(comment);
        event.setCreatedAt(OffsetDateTime.now());
        eventRepository.save(event);
    }

    private static OffsetDateTime[] dayBounds(Airport airport, LocalDate date) {
        ZoneId zone = ZoneId.of(airport.getTimezone());
        LocalDate d = date != null ? date : LocalDate.now(zone);
        return new OffsetDateTime[]{
                d.atStartOfDay(zone).toOffsetDateTime(),
                d.plusDays(1).atStartOfDay(zone).toOffsetDateTime()
        };
    }
}
