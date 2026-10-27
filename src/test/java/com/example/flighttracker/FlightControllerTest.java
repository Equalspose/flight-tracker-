package com.example.flighttracker;

import com.example.flighttracker.controller.FlightController;
import com.example.flighttracker.dto.AirlineDto;
import com.example.flighttracker.dto.AirportDto;
import com.example.flighttracker.dto.FlightDto;
import com.example.flighttracker.entity.FlightStatus;
import com.example.flighttracker.exception.NotFoundException;
import com.example.flighttracker.service.FlightService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlightController.class)
class FlightControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FlightService flightService;

    @Test
    void searchReturnsFlights() throws Exception {
        OffsetDateTime dep = OffsetDateTime.parse("2026-10-14T08:00:00+03:00");
        FlightDto dto = new FlightDto(1L, "SU6",
                new AirlineDto(1L, "SU", "Аэрофлот", "Россия"),
                new AirportDto(1L, "SVO", "Шереметьево", "Москва", "Россия", "Europe/Moscow"),
                new AirportDto(3L, "LED", "Пулково", "Санкт-Петербург", "Россия", "Europe/Moscow"),
                dep, dep.plusMinutes(90), null, null, FlightStatus.SCHEDULED, "B12", "Airbus A320", 90, null);
        when(flightService.search(any())).thenReturn(List.of(dto));

        mvc.perform(get("/api/flights").param("from", "SVO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].flightNumber").value("SU6"))
                .andExpect(jsonPath("$[0].arrivalAirport.iataCode").value("LED"))
                .andExpect(jsonPath("$[0].scheduledDurationMinutes").value(90));
    }

    @Test
    void unknownFlightGives404() throws Exception {
        when(flightService.get(99L)).thenThrow(new NotFoundException("Рейс не найден: id=99"));

        mvc.perform(get("/api/flights/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Рейс не найден: id=99"));
    }

    @Test
    void invalidCreateRequestGives400() throws Exception {
        mvc.perform(post("/api/flights").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
    }
}
