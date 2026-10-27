package com.example.flighttracker.repository;

import com.example.flighttracker.entity.Flight;
import com.example.flighttracker.entity.FlightStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;

/** Условия для гибкого поиска рейсов. Каждое условие пропускается, если параметр не задан. */
public final class FlightSpecifications {

    private FlightSpecifications() {
    }

    public static Specification<Flight> flightNumber(String number) {
        return (root, q, cb) -> number == null ? null
                : cb.equal(cb.upper(root.get("flightNumber")), number.toUpperCase());
    }

    public static Specification<Flight> airline(String iata) {
        return (root, q, cb) -> iata == null ? null
                : cb.equal(cb.upper(root.get("airline").get("iataCode")), iata.toUpperCase());
    }

    public static Specification<Flight> from(String iata) {
        return (root, q, cb) -> iata == null ? null
                : cb.equal(cb.upper(root.get("departureAirport").get("iataCode")), iata.toUpperCase());
    }

    public static Specification<Flight> to(String iata) {
        return (root, q, cb) -> iata == null ? null
                : cb.equal(cb.upper(root.get("arrivalAirport").get("iataCode")), iata.toUpperCase());
    }

    public static Specification<Flight> status(FlightStatus status) {
        return (root, q, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Flight> departureAfter(OffsetDateTime time) {
        return (root, q, cb) -> time == null ? null
                : cb.greaterThanOrEqualTo(root.get("scheduledDeparture"), time);
    }

    public static Specification<Flight> departureBefore(OffsetDateTime time) {
        return (root, q, cb) -> time == null ? null
                : cb.lessThan(root.get("scheduledDeparture"), time);
    }
}
