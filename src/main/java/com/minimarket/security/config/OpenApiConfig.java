package com.minimarket.security.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI minimarketOpenAPI() {

        return new OpenAPI()

                .info(

                        new Info()

                                .title("Minimarket Plus API")

                                .description(
                                        "API REST para la gestión de productos, inventario, ventas, usuarios y carritos."
                                )

                                .version("1.0")

                                .contact(
                                        new Contact()
                                                .name("Grupo 11")
                                                .email("crij.torres@duocuc.cl")
                                )

                                .license(
                                        new License()
                                                .name("Uso Académico")
                                )
                );
    }

}
