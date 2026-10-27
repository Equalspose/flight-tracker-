package com.example.flighttracker.dto;

import com.example.flighttracker.entity.FlightStatus;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

/**
 * Смена статуса рейса.
 * eventTime — фактическое время события (для IN_AIR — время вылета, для LANDED — посадки);
 * если не задано, берётся текущее время.
 */
public record StatusUpdateRequest(
        @NotNull FlightStatus status,
        String comment,
        OffsetDateTime eventTime) {
}
