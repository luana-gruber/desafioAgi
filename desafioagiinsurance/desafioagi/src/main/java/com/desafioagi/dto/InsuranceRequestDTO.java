package com.desafioagi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class InsuranceRequestDTO {

    @JsonProperty("id_cliente")
    @NotBlank(message = "ID do cliente é obrigatório")
    private String idCustomer;

    @JsonProperty("plano")
    @NotBlank(message = "Plano é obrigatório")
    private String plan;
}
