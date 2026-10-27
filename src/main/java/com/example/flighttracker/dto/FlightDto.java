package com.example.flighttracker.dto;

import com.example.flighttracker.entity.Flight;
import com.example.flighttracker.entity.FlightStatus;

import java.time.Duration;
import java.time.OffsetDateTime;

public record FlightDto(
        Long id,
        String flightNumber,
        AirlineDto airline,
        AirportDto departureAirport,
        AirportDto arrivalAirport,
        OffsetDateTime scheduledDeparture,
        OffsetDateTime scheduledArrival,
        OffsetDateTime actualDeparture,
        OffsetDateTime actualArrival,
        FlightStatus status,
        String gate,
        String aircraftType,
        long scheduledDurationMinutes,
        Long departureDelayMinutes) {

    public static FlightDto from(Flight f) {
        long duration = Duration.between(f.getScheduledDeparture(), f.getScheduledArrival()).toMinutes();
        Long delay = f.getActualDeparture() == null ? null
                : Duration.between(f.getScheduledDeparture(), f.getActualDeparture()).toMinutes();
        return new FlightDto(
                f.getId(),
                f.getFlightNumber(),
                AirlineDto.from(f.getAirline()),
                AirportDto.from(f.getDepartureAirport()),
                AirportDto.from(f.getArrivalAirport()),
                f.getScheduledDeparture(),
                f.getScheduledArrival(),
                f.getActualDeparture(),
                f.getActualArrival(),
                f.getStatus(),
                f.getGate(),
                f.getAircraftType(),
                duration,
                delay);
    }
}
