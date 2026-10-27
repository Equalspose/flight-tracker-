package com.example.flighttracker.entity;

/** Статус рейса. */
public enum FlightStatus {
    SCHEDULED,   // по расписанию
    BOARDING,    // идёт посадка
    DELAYED,     // задержан
    IN_AIR,      // в полёте
    LANDED,      // приземлился
    CANCELLED,   // отменён
    DIVERTED     // ушёл на запасной аэродром
}
