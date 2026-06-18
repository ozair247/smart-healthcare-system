package com.smarthealth.healthcarebackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI smartHealthOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Smart Healthcare API")
                        .description("Smart Healthcare Management System API Documentation")
                        .version("v1.0"));
    }
}