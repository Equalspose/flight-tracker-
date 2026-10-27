package com.example.flighttracker.repository;

import com.example.flighttracker.entity.Flight;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long>, JpaSpecificationExecutor<Flight> {

    @Override
    @EntityGraph(attributePaths = {"airline", "departureAirport", "arrivalAirport"})
    List<Flight> findAll(Specification<Flight> spec, org.springframework.data.domain.Sort sort);

    @Override
    @EntityGraph(attributePaths = {"airline", "departureAirport", "arrivalAirport"})
    Optional<Flight> findById(Long id);
}
