package com.example.flighttracker.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "flight")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flight_number", nullable = false, length = 10)
    private String flightNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "airline_id")
    private Airline airline;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "departure_airport_id")
    private Airport departureAirport;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "arrival_airport_id")
    private Airport arrivalAirport;

    @Column(name = "scheduled_departure", nullable = false)
    private OffsetDateTime scheduledDeparture;

    @Column(name = "scheduled_arrival", nullable = false)
    private OffsetDateTime scheduledArrival;

    @Column(name = "actual_departure")
    private OffsetDateTime actualDeparture;

    @Column(name = "actual_arrival")
    private OffsetDateTime actualArrival;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FlightStatus status;

    private String gate;

    @Column(name = "aircraft_type")
    private String aircraftType;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }
    public Airline getAirline() { return airline; }
    public void setAirline(Airline airline) { this.airline = airline; }
    public Airport getDepartureAirport() { return departureAirport; }
    public void setDepartureAirport(Airport departureAirport) { this.departureAirport = departureAirport; }
    public Airport getArrivalAirport() { return arrivalAirport; }
    public void setArrivalAirport(Airport arrivalAirport) { this.arrivalAirport = arrivalAirport; }
    public OffsetDateTime getScheduledDeparture() { return scheduledDeparture; }
    public void setScheduledDeparture(OffsetDateTime scheduledDeparture) { this.scheduledDeparture = scheduledDeparture; }
    public OffsetDateTime getScheduledArrival() { return scheduledArrival; }
    public void setScheduledArrival(OffsetDateTime scheduledArrival) { this.scheduledArrival = scheduledArrival; }
    public OffsetDateTime getActualDeparture() { return actualDeparture; }
    public void setActualDeparture(OffsetDateTime actualDeparture) { this.actualDeparture = actualDeparture; }
    public OffsetDateTime getActualArrival() { return actualArrival; }
    public void setActualArrival(OffsetDateTime actualArrival) { this.actualArrival = actualArrival; }
    public FlightStatus getStatus() { return status; }
    public void setStatus(FlightStatus status) { this.status = status; }
    public String getGate() { return gate; }
    public void setGate(String gate) { this.gate = gate; }
    public String getAircraftType() { return aircraftType; }
    public void setAircraftType(String aircraftType) { this.aircraftType = aircraftType; }
}
