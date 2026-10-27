package com.example.flighttracker.dto;

import com.example.flighttracker.entity.Airport;

public record AirportDto(Long id, String iataCode, String name, String city, String country, String timezone) {

    public static AirportDto from(Airport a) {
        return new AirportDto(a.getId(), a.getIataCode(), a.getName(), a.getCity(), a.getCountry(), a.getTimezone());
    }
}
