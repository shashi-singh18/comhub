package com.shashi.comhub.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI comHubOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("ComHub E-Commerce Backend API")
                        .description("""
                                REST APIs for the ComHub E-Commerce Backend.
                                
                                This project is built as a production-style backend
                                to demonstrate modern Spring Boot development practices.
                                """)
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Shashi Singh")
                                .email("shashisingh5101@gmail.com")
                                .url("https://github.com/shashi-singh18")
                        )
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Project Repository")
                        .url("https://github.com/shashi-singh18/comhub"));
    }
}