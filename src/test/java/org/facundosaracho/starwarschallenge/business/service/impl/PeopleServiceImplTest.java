package org.facundosaracho.starwarschallenge.business.service.impl;

import org.facundosaracho.starwarschallenge.business.model.PaginatedPeopleResponse;
import org.facundosaracho.starwarschallenge.business.model.PeopleResponse;
import org.facundosaracho.starwarschallenge.business.model.SwapiPeopleByIdResponse;
import org.facundosaracho.starwarschallenge.business.model.SwapiPeopleByNameResponse;
import org.facundosaracho.starwarschallenge.exception.BusinessException;
import org.facundosaracho.starwarschallenge.proxy.swapi.SwapiClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.facundosaracho.starwarschallenge.exception.ErrorCode.MANDATORY_PARAMETER_IS_MISSING;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeopleServiceImplTest {

    @Mock
    private SwapiClient swapiClient;

    @InjectMocks
    private PeopleServiceImpl peopleService;

    @Test
    @DisplayName("Caso éxito - PeopleService - findById")
    void findPeopleById_ReturnsPeopleResponse() {
        // Given
        Long id = 1L;
        SwapiPeopleByIdResponse mockResponse = new SwapiPeopleByIdResponse();
        when(swapiClient.findPeopleById(id)).thenReturn(mockResponse);

        // When
        PeopleResponse response = peopleService.findPeopleByIdOrName(id, null);

        // Then
        assertNotNull(response);
        verify(swapiClient, times(1)).findPeopleById(id);
        verify(swapiClient, never()).findPeopleByName(any());
    }

    @Test
    @DisplayName("Caso éxito - PeopleService - findByName")
    void findPeopleByName_ReturnsPeopleResponse() {
        // Given
        String name = "Luke";
        SwapiPeopleByNameResponse mockResponse = new SwapiPeopleByNameResponse();
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
        PaginatedPeopleResponse mockResponse = new PaginatedPeopleResponse();
        when(swapiClient.findAllPeople(size, page)).thenReturn(mockResponse);

        // When
        PeopleResponse response = peopleService.findAllPeople(page, size);

        // Then
        assertNotNull(response);
        verify(swapiClient, times(1)).findAllPeople(size, page);
    }

}
