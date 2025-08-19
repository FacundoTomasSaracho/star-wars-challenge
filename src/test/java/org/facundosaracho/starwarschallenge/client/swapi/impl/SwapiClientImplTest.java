package org.facundosaracho.starwarschallenge.client.swapi.impl;

import org.facundosaracho.starwarschallenge.client.impl.SwapiClientImpl;
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
        SwapiPeopleByIdResponseDto mockResponse = new SwapiPeopleByIdResponseDto();
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
        SwapiPeopleByNameResponseDto mockResponse = new SwapiPeopleByNameResponseDto();
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
        PaginatedPeopleResponse mockResponse = new PaginatedPeopleResponse();
        String url = baseUrl + "/people?page=" + page + "&limit=" + size;
        when(restTemplate.getForObject(url, PaginatedPeopleResponse.class)).thenReturn(mockResponse);

        // When
        PaginatedPeopleResponse response = swapiClient.findAllPeople(size, page);

        // Then
        assertNotNull(response);
        verify(restTemplate, times(1)).getForObject(url, PaginatedPeopleResponse.class);
    }
}
