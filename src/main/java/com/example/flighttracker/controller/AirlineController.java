package com.example.flighttracker.controller;

import com.example.flighttracker.dto.AirlineDto;
import com.example.flighttracker.dto.AirlineRequest;
import com.example.flighttracker.service.AirlineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/airlines")
public class AirlineController {

    private final AirlineService airlineService;

    public AirlineController(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    @GetMapping
    public List<AirlineDto> list() {
        return airlineService.findAll();
    }

    @GetMapping("/{code}")
    public AirlineDto get(@PathVariable String code) {
        return airlineService.get(code);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AirlineDto create(@Valid @RequestBody AirlineRequest request) {
        return airlineService.create(request);
    }
}
