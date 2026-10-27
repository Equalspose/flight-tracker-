package com.example.flighttracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record FlightRequest(
        @NotBlank String flightNumber,
        @NotBlank String airlineCode,
        @NotBlank String departureAirportCode,
        @NotBlank String arrivalAirportCode,
        @NotNull OffsetDateTime scheduledDeparture,
        @NotNull OffsetDateTime scheduledArrival,
        String gate,
        String aircraftType) {
}
