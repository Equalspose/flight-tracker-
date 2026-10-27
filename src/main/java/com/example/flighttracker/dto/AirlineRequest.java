package com.example.flighttracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AirlineRequest(
        @NotBlank @Pattern(regexp = "[A-Za-z0-9]{2}", message = "IATA-код авиакомпании — 2 символа") String iataCode,
        @NotBlank String name,
        @NotBlank String country) {
}
