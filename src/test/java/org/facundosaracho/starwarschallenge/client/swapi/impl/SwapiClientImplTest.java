package org.facundosaracho.starwarschallenge.client.swapi.impl;

import org.facundosaracho.starwarschallenge.client.impl.SwapiClientImpl;
import org.facundosaracho.starwarschallenge.config.SwapiFeignClient;
import org.facundosaracho.starwarschallenge.model.domain.Properties;
import org.facundosaracho.starwarschallenge.model.dto.PaginatedPeopleResponseDto;
import org.facundosaracho.starwarschallenge.model.dto.SwapiPeopleByIdResponseDto;
import org.facundosaracho.starwarschallenge.model.dto.SwapiPeopleByNameResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class SwapiClientImplTest {

    @Mock
    private SwapiFeignClient swapiFeignClient;

    @InjectMocks
    private SwapiClientImpl swapiClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Caso de éxito - SwapiClient - findPeopleById.")
    void findPeopleById_ReturnsOk() {
        // Given
        Long id = 1L;
        SwapiPeopleByIdResponseDto mockResponse = mockByIdDto();
        when(swapiFeignClient.findPeopleById(id)).thenReturn(mockResponse);

        // When
        SwapiPeopleByIdResponseDto response = swapiClient.findPeopleById(id);

        // Then
        assertNotNull(response);
        verify(swapiFeignClient, times(1)).findPeopleById(1L);
    }

    @Test
    @DisplayName("Caso de éxito - SwapiClient - findPeopleByName")
    void findPeopleByName_ReturnsOk() {
        // Given
        String name = "Luke";
        SwapiPeopleByNameResponseDto mockResponse = mockByNameDto();
        when(swapiFeignClient.findPeopleByName(name)).thenReturn(mockResponse);

        // When
        SwapiPeopleByNameResponseDto response = swapiClient.findPeopleByName(name);

        // Then
        assertNotNull(response);
        verify(swapiFeignClient, times(1)).findPeopleByName(name);
    }

    @Test
    @DisplayName("Caso éxito - SwapiService - FindAllPeople")
    void findAllPeople_ReturnsResponse() {
        // Given
        int page = 1;
        int size = 10;
        PaginatedPeopleResponseDto mockResponse = mockPaginatedResponse();
        when(swapiFeignClient.findAllPeople(page, size)).thenReturn(mockResponse);

        // When
        PaginatedPeopleResponseDto response = swapiFeignClient.findAllPeople(page,size);

        // Then
        assertNotNull(response);
        verify(swapiFeignClient, times(1)).findAllPeople(page, size);
    }

    // ---- Helpers DTOs de SWAPI (records) ----
    private Properties lukeProperties() {
        return new Properties(
                "2025-01-01",     // created
                "2025-01-02",     // edited
                "Luke Skywalker", // name
                "male",           // gender
                "light",          // skin_color
                "blond",          // hair_color
                "172",            // height
                "blue",           // eye_color
                "77",             // mass
                "Tatooine",       // homeworld
                "19BBY",          // birth_year
                List.of("Snowspeeder"),
                List.of("X-wing"),
                List.of("A New Hope"),
                "https://swapi.dev/api/people/1/"
        );
    }

    private SwapiPeopleByNameResponseDto mockByNameDto() {
        return new SwapiPeopleByNameResponseDto(
                List.of(mockResult()),
                "ok"
        );
    }

    // (si lo necesitás en otros tests por ID)
    private SwapiPeopleByIdResponseDto mockByIdDto() {
        return new SwapiPeopleByIdResponseDto(
                "ok",
                mockPeopleResult()
        );
    }

    private SwapiPeopleByNameResponseDto.Result mockResult() {
        return new SwapiPeopleByNameResponseDto.Result(mockProperties(), "asd", "asd", "asd", 1, "google.com");
    }

    private SwapiPeopleByNameResponseDto.Properties mockProperties() {
        return new SwapiPeopleByNameResponseDto.Properties("1", "brown", "brown",
                "brown", "male", "Name", "155", "123", "landlord",
                "aSD", "ASd", List.of("asd"), List.of("Asd"), List.of("asd"), "www.google.com");
    }

    private SwapiPeopleByIdResponseDto.PeopleResult mockPeopleResult() {
        return new SwapiPeopleByIdResponseDto.PeopleResult(mockPeopleProperties(), "hello",
                "1", "1", 1);

    }

    private SwapiPeopleByIdResponseDto.PeopleProperties mockPeopleProperties() {
        return new SwapiPeopleByIdResponseDto.PeopleProperties("1", "brown", "brown",
                "brown", "male", "Name", "155", "123", "landlord",
                List.of("asd"), List.of("QAsd"), List.of("asd"), "today", "now", "www.google.com");
    }

    private PaginatedPeopleResponseDto mockPaginatedResponse() {
        PaginatedPeopleResponseDto.Result r = new PaginatedPeopleResponseDto.Result(
                "uid-001",
                "Luke Skywalker",
                "https://swapi.dev/api/people/1/"
        );

        return new PaginatedPeopleResponseDto(
                // results
                "ok",         // message
                1L,            // total_records
                1L,            // total_pages
                null,         // previous
                null,// next
                List.of(r)
        );
    }
}
