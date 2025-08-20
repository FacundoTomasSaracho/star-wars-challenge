package org.facundosaracho.starwarschallenge.client.swapi.impl;

import org.facundosaracho.starwarschallenge.client.impl.SwapiClientImpl;
import org.facundosaracho.starwarschallenge.model.domain.Properties;
import org.facundosaracho.starwarschallenge.model.domain.Result;
import org.facundosaracho.starwarschallenge.model.dto.SwapiPeopleByNameResponseDto;
import org.facundosaracho.starwarschallenge.model.domain.PaginatedPeopleResponse;
import org.facundosaracho.starwarschallenge.model.dto.SwapiPeopleByIdResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class SwapiClientImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SwapiClientImpl swapiClient;

    private final String baseUrl = "https://swapi.dev/api";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(swapiClient, "swapiBaseUrl", baseUrl);
    }

    @Test
    @DisplayName("Caso de éxito - SwapiClient - findPeopleById.")
    void findPeopleById_ReturnsOk() {
        // Given
        Long id = 1L;
        SwapiPeopleByIdResponseDto mockResponse = mockByIdDto();
        String url = baseUrl + "/people/" + id + "/";
        when(restTemplate.getForObject(url, SwapiPeopleByIdResponseDto.class)).thenReturn(mockResponse);

        // When
        SwapiPeopleByIdResponseDto response = swapiClient.findPeopleById(id);

        // Then
        assertNotNull(response);
        verify(restTemplate, times(1)).getForObject(url, SwapiPeopleByIdResponseDto.class);
    }

    @Test
    @DisplayName("Caso de éxito - SwapiClient - findPeopleByName")
    void findPeopleByName_ReturnsOk() {
        // Given
        String name = "Luke";
        SwapiPeopleByNameResponseDto mockResponse = mockByNameDto();
        String url = baseUrl + "/people?name=" + name;
        when(restTemplate.getForObject(url, SwapiPeopleByNameResponseDto.class)).thenReturn(mockResponse);

        // When
        SwapiPeopleByNameResponseDto response = swapiClient.findPeopleByName(name);

        // Then
        assertNotNull(response);
        verify(restTemplate, times(1)).getForObject(url, SwapiPeopleByNameResponseDto.class);
    }

    @Test
    @DisplayName("Caso éxito - SwapiService - FindAllPeople")
    void findAllPeople_ReturnsResponse() {
        // Given
        String page = "1";
        String size = "10";
        PaginatedPeopleResponse mockResponse = mockPaginatedResponse();
        String url = baseUrl + "/people?page=" + page + "&limit=" + size;
        when(restTemplate.getForObject(url, PaginatedPeopleResponse.class)).thenReturn(mockResponse);

        // When
        PaginatedPeopleResponse response = swapiClient.findAllPeople(size, page);

        // Then
        assertNotNull(response);
        verify(restTemplate, times(1)).getForObject(url, PaginatedPeopleResponse.class);
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

    private Result lukeResult() {
        return new Result(
                lukeProperties(),
                "abc123",                               // _id
                "Human male Jedi Knight",               // description
                "uid-001",                              // uid
                1,                                      // __v
                "https://swapi.dev/api/people/1/"       // url
        );
    }

    private SwapiPeopleByNameResponseDto mockByNameDto() {
        return new SwapiPeopleByNameResponseDto(
                List.of(lukeResult()),
                "ok"
        );
    }

    // (si lo necesitás en otros tests por ID)
    private SwapiPeopleByIdResponseDto mockByIdDto() {
        return new SwapiPeopleByIdResponseDto(
                lukeResult(),
                "ok"
        );
    }

    private PaginatedPeopleResponse mockPaginatedResponse() {
        PaginatedPeopleResponse.Result r = new PaginatedPeopleResponse.Result(
                "uid-001",
                "Luke Skywalker",
                "https://swapi.dev/api/people/1/"
        );

        return new PaginatedPeopleResponse(
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
