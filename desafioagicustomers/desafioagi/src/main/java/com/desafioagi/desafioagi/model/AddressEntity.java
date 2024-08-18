package com.desafioagi.desafioagi.model;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressEntity {

    private String street;

    private String number;

    private String city;

    private String state;

    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP inválido. O formato esperado é XXXXX-XXX.")
    private String zipCode;

    private String country;
}
