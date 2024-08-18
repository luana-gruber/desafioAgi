package com.desafioagi.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceResponseDTO {

    @JsonProperty("id_cliente")
    private String idCustomer;

    @JsonProperty("plano")
    private String plan;

    @JsonProperty("descricao_plano")
    private String description;

    @JsonProperty("valor_plano")
    private double planValue;

    public InsuranceResponseDTO(String plan, String description, double planValue) {
        this.plan = plan;
        this.description = description;
        this.planValue = planValue;
    }

}
