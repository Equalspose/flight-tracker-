package com.example.flighttracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AirportRequest(
        @NotBlank @Pattern(regexp = "[A-Za-z]{3}", message = "IATA-код аэропорта — 3 латинские буквы") String iataCode,
        @NotBlank String name,
        @NotBlank String city,
        @NotBlank String country,
        @NotBlank String timezone) {
}
