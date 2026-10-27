package com.example.flighttracker.controller;

import com.example.flighttracker.dto.FlightDto;
import com.example.flighttracker.dto.FlightEventDto;
import com.example.flighttracker.dto.FlightRequest;
import com.example.flighttracker.dto.StatusUpdateRequest;
import com.example.flighttracker.entity.FlightStatus;
import com.example.flighttracker.service.FlightSearchCriteria;
import com.example.flighttracker.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    /**
     * Поиск рейсов. Все параметры необязательны:
     * GET /api/flights?number=SU6&airline=SU&from=SVO&to=LED&status=SCHEDULED
     *     &departureFrom=2026-10-14T00:00:00%2B03:00&departureTo=2026-10-15T00:00:00%2B03:00
     */
    @GetMapping
    public List<FlightDto> search(
            @RequestParam(required = false) String number,
            @RequestParam(required = false) String airline,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) FlightStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime departureFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime departureTo) {
        return flightService.search(
                new FlightSearchCriteria(number, airline, from, to, status, departureFrom, departureTo));
    }

    @GetMapping("/{id}")
    public FlightDto get(@PathVariable Long id) {
        return flightService.get(id);
    }

    /** История изменений статуса рейса. */
    @GetMapping("/{id}/history")
    public List<FlightEventDto> history(@PathVariable Long id) {
        return flightService.history(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FlightDto create(@Valid @RequestBody FlightRequest request) {
        return flightService.create(request);
    }

    @PutMapping("/{id}")
    public FlightDto update(@PathVariable Long id, @Valid @RequestBody FlightRequest request) {
        return flightService.update(id, request);
    }

    /** Смена статуса: PATCH /api/flights/1/status {"status":"IN_AIR","comment":"Вылет"} */
    @PatchMapping("/{id}/status")
    public FlightDto updateStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest request) {
        return flightService.updateStatus(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        flightService.delete(id);
    }
}
