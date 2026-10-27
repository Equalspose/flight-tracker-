package com.example.flighttracker.service;

import com.example.flighttracker.dto.AirlineDto;
import com.example.flighttracker.dto.AirlineRequest;
import com.example.flighttracker.entity.Airline;
import com.example.flighttracker.exception.ConflictException;
import com.example.flighttracker.exception.NotFoundException;
import com.example.flighttracker.repository.AirlineRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AirlineService {

    private final AirlineRepository repository;

    public AirlineService(AirlineRepository repository) {
        this.repository = repository;
    }

    public List<AirlineDto> findAll() {
        return repository.findAll(Sort.by("iataCode")).stream().map(AirlineDto::from).toList();
    }

    public AirlineDto get(String iataCode) {
        return AirlineDto.from(getEntity(iataCode));
    }

    public Airline getEntity(String iataCode) {
        return repository.findByIataCodeIgnoreCase(iataCode)
                .orElseThrow(() -> new NotFoundException("Авиакомпания не найдена: " + iataCode));
    }

    @Transactional
    public AirlineDto create(AirlineRequest request) {
        if (repository.findByIataCodeIgnoreCase(request.iataCode()).isPresent()) {
            throw new ConflictException("Авиакомпания уже существует: " + request.iataCode());
        }
        Airline airline = new Airline();
        airline.setIataCode(request.iataCode().toUpperCase());
        airline.setName(request.name());
        airline.setCountry(request.country());
        return AirlineDto.from(repository.save(airline));
    }
}
