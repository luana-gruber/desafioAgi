package com.desafioagi.desafioagi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class CustomerRequestDTO {

    @JsonProperty("cpf")
    private String cpf;

    @JsonProperty("nome_completo")
    private String name;

    @JsonProperty("data_nascimento")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;

    @JsonProperty("telefone")
    private String phone;

    @JsonProperty("endereco")
    private AddressRequestDTO addressEntity;

}
