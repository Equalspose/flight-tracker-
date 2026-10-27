package com.example.flighttracker.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

/** Запись журнала: смена статуса рейса. */
@Entity
@Table(name = "flight_event")
public class FlightEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_status", length = 20)
    private FlightStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false, length = 20)
    private FlightStatus newStatus;

    private String comment;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Flight getFlight() { return flight; }
    public void setFlight(Flight flight) { this.flight = flight; }
    public FlightStatus getOldStatus() { return oldStatus; }
    public void setOldStatus(FlightStatus oldStatus) { this.oldStatus = oldStatus; }
    public FlightStatus getNewStatus() { return newStatus; }
    public void setNewStatus(FlightStatus newStatus) { this.newStatus = newStatus; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
