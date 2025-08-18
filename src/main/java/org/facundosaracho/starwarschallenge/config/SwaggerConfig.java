package org.facundosaracho.starwarschallenge.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private Info info() {
        return new Info()
                .title("Star Wars challenge microservice")
                .description("Este microservicio permite manejar distintas operaciones contra la api de StarWars.")
                .version("1.0.0")
                .contact(linkedIn());
    }

    private Contact linkedIn() {
        return new Contact().name("LinkedIn").url("https://www.linkedin.com/in/facundo-tomas-saracho/");
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(info())
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                ).addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
