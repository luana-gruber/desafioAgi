package com.desafioagi.desafioagi.config;

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
                        .title("API de Cadastro de clientes")
                        .version("1.0")
                        .description("Documentação da API que realiza operações de consulta, criação, atualização e deleção de clientes"));
    }
}
