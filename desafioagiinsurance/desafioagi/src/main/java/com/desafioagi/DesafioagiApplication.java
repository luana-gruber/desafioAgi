package com.desafioagi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class DesafioagiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioagiApplication.class, args);
	}

}
