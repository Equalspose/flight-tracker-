package com.example.flighttracker.controller;

import com.example.flighttracker.dto.AirportDto;
import com.example.flighttracker.dto.AirportRequest;
import com.example.flighttracker.dto.FlightDto;
import com.example.flighttracker.service.AirportService;
import com.example.flighttracker.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/airports")
public class AirportController {

    private final AirportService airportService;
    private final FlightService flightService;

    public AirportController(AirportService airportService, FlightService flightService) {
        this.airportService = airportService;
        this.flightService = flightService;
    }

    @GetMapping
    public List<AirportDto> list() {
        return airportService.findAll();
    }

    @GetMapping("/{code}")
    public AirportDto get(@PathVariable String code) {
        return airportService.get(code);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AirportDto create(@Valid @RequestBody AirportRequest request) {
        return airportService.create(request);
    }

    /** Табло вылетов: GET /api/airports/SVO/departures?date=2026-10-14 */
    @GetMapping("/{code}/departures")
    public List<FlightDto> departures(@PathVariable String code,
                                      @RequestParam(required = false)
                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return flightService.departures(code, date);
    }

    /** Табло прилётов: GET /api/airports/SVO/arrivals?date=2026-10-14 */
    @GetMapping("/{code}/arrivals")
    public List<FlightDto> arrivals(@PathVariable String code,
                                    @RequestParam(required = false)
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return flightService.arrivals(code, date);
    }
}
