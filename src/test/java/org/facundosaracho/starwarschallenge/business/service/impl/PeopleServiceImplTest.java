package org.facundosaracho.starwarschallenge.business.service.impl;

import org.facundosaracho.starwarschallenge.config.SwapiFeignClient;
import org.facundosaracho.starwarschallenge.exception.BusinessException;
import org.facundosaracho.starwarschallenge.model.domain.PeopleResponse;
import org.facundosaracho.starwarschallenge.model.domain.Properties;
import org.facundosaracho.starwarschallenge.model.domain.Result;
import org.facundosaracho.starwarschallenge.model.dto.PaginatedPeopleResponseDto;
import org.facundosaracho.starwarschallenge.model.dto.SwapiPeopleByIdResponseDto;
import org.facundosaracho.starwarschallenge.model.dto.SwapiPeopleByNameResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.facundosaracho.starwarschallenge.exception.dto.ErrorCodeDto.MANDATORY_PARAMETER_IS_MISSING;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeopleServiceImplTest {

    @Mock
    private SwapiFeignClient swapiClient;

    @InjectMocks
    private PeopleServiceImpl peopleService;

    @Test
    @DisplayName("Caso éxito - PeopleService - findById")
    void findPeopleById_ReturnsPeopleResponse() {
        // Given
        Long id = 1L;

        SwapiPeopleByIdResponseDto mockResponse = mockByIdDto();

        when(swapiClient.findPeopleById(id)).thenReturn(mockResponse);

        // When
        PeopleResponse response = peopleService.findPeopleByIdOrName(id, null);

        // Then
        assertNotNull(response);
        assertEquals("ok", response.message()); // opcional, validar el message
        assertFalse(response.results().isEmpty()); // opcional, validar contenido
        verify(swapiClient, times(1)).findPeopleById(id);
        verify(swapiClient, never()).findPeopleByName(any());
    }

    @Test
    @DisplayName("Caso éxito - PeopleService - findByName")
    void findPeopleByName_ReturnsPeopleResponse() {
        // Given
        String name = "Luke";
        SwapiPeopleByNameResponseDto mockResponse = mockByNameDto(); // <- helper
        when(swapiClient.findPeopleByName(name)).thenReturn(mockResponse);

        // When
        PeopleResponse response = peopleService.findPeopleByIdOrName(null, name);

        // Then
        assertNotNull(response);
        verify(swapiClient, times(1)).findPeopleByName(name);
        verify(swapiClient, never()).findPeopleById(any());
    }

    @Test
    @DisplayName("Caso de error - PeopleService - id y name nulos.")
    void findPeopleByIdOrName_NoIdNoName_ThrowsBusinessException() {
        // Given
        Long id = null;
        String name = null;

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> peopleService.findPeopleByIdOrName(id, name));

        assertEquals(MANDATORY_PARAMETER_IS_MISSING.getMessage(), exception.getMessage());
    }

//------------------------------------ FindAllPeople -------------------------------------------------------------------//

    @Test
    @DisplayName("Caso éxito - PeopleService - findAllPeople")
    void findAllPeople_ReturnsPeopleResponse() {
        // Given
        String page = "1";
        String size = "10";
        PaginatedPeopleResponseDto mockResponse = mockPaginatedResponse();
        when(swapiClient.findAllPeople(Integer.parseInt(page), Integer.parseInt(size))).thenReturn(mockResponse);

        // When
        PeopleResponse response = peopleService.findAllPeople(page, size);

        // Then
        assertNotNull(response);
        verify(swapiClient, times(1)).findAllPeople(Integer.parseInt(page), Integer.parseInt(size));
    }

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
        return new SwapiPeopleByNameResponseDto(List.of(mockResult()), "message");
    }

    private SwapiPeopleByNameResponseDto.Result mockResult() {
        return new SwapiPeopleByNameResponseDto.Result(mockProperties(), "asd", "asd", "asd", 1, "google.com");
    }

    private SwapiPeopleByNameResponseDto.Properties mockProperties() {
        return new SwapiPeopleByNameResponseDto.Properties("1", "brown", "brown",
                "brown", "male", "Name", "155", "123", "landlord",
                "aSD", "ASd", List.of("asd"), List.of("Asd"), List.of("asd"), "www.google.com");
    }

    // (si lo necesitás en otros tests por ID)
    private SwapiPeopleByIdResponseDto mockByIdDto() {
        return new SwapiPeopleByIdResponseDto("ok",
                new SwapiPeopleByIdResponseDto.PeopleResult(mockPeopleProperties(), "desc", "1", "1", 1)
        );
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
