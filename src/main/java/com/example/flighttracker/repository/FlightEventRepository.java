package com.example.flighttracker.repository;

import com.example.flighttracker.entity.FlightEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightEventRepository extends JpaRepository<FlightEvent, Long> {
    List<FlightEvent> findByFlightIdOrderByCreatedAtAsc(Long flightId);
}
