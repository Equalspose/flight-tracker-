package com.example.flighttracker.service;

import com.example.flighttracker.dto.AirportDto;
import com.example.flighttracker.dto.AirportRequest;
import com.example.flighttracker.entity.Airport;
import com.example.flighttracker.exception.ConflictException;
import com.example.flighttracker.exception.NotFoundException;
import com.example.flighttracker.repository.AirportRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AirportService {

    private final AirportRepository repository;

    public AirportService(AirportRepository repository) {
        this.repository = repository;
    }

    public List<AirportDto> findAll() {
        return repository.findAll(Sort.by("iataCode")).stream().map(AirportDto::from).toList();
    }

    public AirportDto get(String iataCode) {
        return AirportDto.from(getEntity(iataCode));
    }

    public Airport getEntity(String iataCode) {
        return repository.findByIataCodeIgnoreCase(iataCode)
                .orElseThrow(() -> new NotFoundException("Аэропорт не найден: " + iataCode));
    }

    @Transactional
    public AirportDto create(AirportRequest request) {
        if (repository.findByIataCodeIgnoreCase(request.iataCode()).isPresent()) {
            throw new ConflictException("Аэропорт уже существует: " + request.iataCode());
        }
        ZoneId.of(request.timezone()); // проверка корректности часового пояса
        Airport airport = new Airport();
        airport.setIataCode(request.iataCode().toUpperCase());
        airport.setName(request.name());
        airport.setCity(request.city());
        airport.setCountry(request.country());
        airport.setTimezone(request.timezone());
        return AirportDto.from(repository.save(airport));
    }
}
