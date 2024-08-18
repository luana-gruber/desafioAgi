package com.desafioagi.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import jakarta.validation.constraints.NotBlank;

@Document(collection="insurance")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsuranceEntity {

    @Id
    private String idInsurance;

    private String idCustomer;

    @NotBlank(message = "Plano não pode estar em branco")
    private String plan;

    private String description;

    private double planValue;

}
