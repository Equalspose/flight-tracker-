package com.example.flighttracker.dto;

import com.example.flighttracker.entity.Airline;

public record AirlineDto(Long id, String iataCode, String name, String country) {

    public static AirlineDto from(Airline a) {
        return new AirlineDto(a.getId(), a.getIataCode(), a.getName(), a.getCountry());
    }
}
