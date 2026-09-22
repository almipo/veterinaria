package com.vet_system.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI vetSystemOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Api VetSystem - Veterinaria Mascotitas")
                        .description("Documentacion de la api Clinica Veterinaria")
                        .version("1.0")
                        .contact(new Contact().name("Alan Pozzi")
                                .email("alanpozzi@gmail.com"))
                        .license(new License().name("Uso Academico")));
    }
}
