package org.facundosaracho.starwarschallenge.presentation.controller.integration;

import org.facundosaracho.starwarschallenge.business.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PeopleControllerIntegrationTest {

    @Autowired
    private PeopleService peopleService;
    
}
