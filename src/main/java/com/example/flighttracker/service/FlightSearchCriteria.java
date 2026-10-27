package com.example.flighttracker.service;

import com.example.flighttracker.entity.FlightStatus;

import java.time.OffsetDateTime;

/** Параметры поиска рейсов; любое поле может быть null. */
public record FlightSearchCriteria(
        String flightNumber,
        String airline,
        String from,
        String to,
        FlightStatus status,
        OffsetDateTime departureFrom,
        OffsetDateTime departureTo) {
}
