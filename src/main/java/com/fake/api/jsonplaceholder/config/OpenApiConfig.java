package com.fake.api.jsonplaceholder.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("JSONPlaceholder API")
                        .version("1.0.0")
                        .description("This is the API documentation for the Post Management System that consumes resources from https://jsonplaceholder.typicode.com/.")
                        .contact(new Contact()
                                .name("Francisco José Becerra Vázquez")
                                .email("franjbec@gmail.com")));
    }
}
