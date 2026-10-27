package com.example.flighttracker.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "airline")
public class Airline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "iata_code", nullable = false, unique = true, length = 2)
    private String iataCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String country;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIataCode() { return iataCode; }
    public void setIataCode(String iataCode) { this.iataCode = iataCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
}
