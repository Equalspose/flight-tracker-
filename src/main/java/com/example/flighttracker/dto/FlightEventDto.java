package com.example.flighttracker.dto;

import com.example.flighttracker.entity.FlightEvent;
import com.example.flighttracker.entity.FlightStatus;

import java.time.OffsetDateTime;

public record FlightEventDto(Long id, FlightStatus oldStatus, FlightStatus newStatus, String comment,
                             OffsetDateTime createdAt) {

    public static FlightEventDto from(FlightEvent e) {
        return new FlightEventDto(e.getId(), e.getOldStatus(), e.getNewStatus(), e.getComment(), e.getCreatedAt());
    }
}
