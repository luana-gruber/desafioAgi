package com.desafioagi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SweggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Simulação e Contratação de Seguros")
                        .version("1.0")
                        .description("Documentação da API que realiza a simulação de uma contratação de seguros e também realiza a contratação efetiva, caso o cliente esteja cadastrado em nossa API de cadastros"));
    }
}
