package com.example.backend.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Aplicação Backend")
                        .version("1.0")
                        .description("Apresenta a documentação da API Backend para Gerenciar Pessoas." +
                                "Dispõe operações CRUD para Pessoa. "));
    }
}
