//package org.facundosaracho.starwarschallenge.presentation.controller;
//
//import org.facundosaracho.starwarschallenge.model.domain.PeopleResponse;
//import org.facundosaracho.starwarschallenge.business.service.PeopleService;
//import org.facundosaracho.starwarschallenge.model.dto.PaginatedPeopleDto;
//import org.facundosaracho.starwarschallenge.model.dto.PeopleDTO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(MockitoExtension.class)
//class PeopleControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private PeopleService peopleService;
//
//    @InjectMocks
//    private PeopleController peopleController;
//
//    @BeforeEach
//    void setUp() {
//        //No molesta con métodos estáticos de Mapper.
//        mockMvc = MockMvcBuilders.standaloneSetup(peopleController).build();
//    }
//
//    @Test
//    @DisplayName("Caso de éxito - PeopleController - id y name")
//    void searchPeople_ReturnsOk() {
//        // given
//        Long id = 1L;
//        String name = "Luke";
//        PeopleResponse mockResponse = new PeopleResponse();
//        when(peopleService.findPeopleByIdOrName(id, name)).thenReturn(mockResponse);
//
//        // when
//        ResponseEntity<PeopleDTO> response = peopleController.findPeople(id, name);
//
//        // then
//        verify(peopleService, times(1)).findPeopleByIdOrName(id, name);
//        assertNotNull(response);
//
//    }
//
//    @Test
//    @DisplayName("Caso de éxito - PeopleController - id y name mediante mvc.")
//    void findPeopleWithIdOrName_ReturnsOk() throws Exception {
//
//        mockMvc.perform(get("/people/find")
//                        .param("id", ""))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/people/find").param("name", "Luke")
//                        .param("id", ""))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/people/find")
//                        .param("name", "Luke"))
//                .andExpect(status().isOk());
//
//    }
//
//
//    /*
//    ---------------------------------------------------------------------------------------------------------------
//     */
//
//
//    @Test
//    @DisplayName("Caso de éxito - PeopleController - /find-all")
//    void findAllPeople_ReturnsOk() {
//        // given
//        String page = "1";
//        String size = "10";
//        PeopleResponse mockResponse = new PeopleResponse();
//        when(peopleService.findAllPeople(page, size)).thenReturn(mockResponse);
//
//        // when
//        ResponseEntity<PaginatedPeopleDto> response = peopleController.findAllPeople(size, page);
//
//        // then
//        verify(peopleService, times(1)).findAllPeople(page, size);
//        assertNotNull(response);
//    }
//
//    @Test
//    @DisplayName("Caso de error - PeopleController - size o page nulos.")
//    void findAllPeople_EmptySizeOrPage_ReturnsBadRequest() throws Exception {
//
//        mockMvc.perform(get("/people/find-all")
//                        .param("size", "")
//                        .param("page", "1"))
//                .andExpect(status().isBadRequest());
//
//
//        mockMvc.perform(get("/people/find-all")
//                        .param("size", "10")
//                        .param("page", ""))
//                .andExpect(status().isBadRequest());
//
//
//        mockMvc.perform(get("/people/find-all")
//                        .param("size", "")
//                        .param("page", ""))
//                .andExpect(status().isBadRequest());
//    }
//}
