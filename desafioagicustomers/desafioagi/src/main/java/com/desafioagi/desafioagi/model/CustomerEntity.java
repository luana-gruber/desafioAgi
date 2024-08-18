package com.desafioagi.desafioagi.model;

import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Document(collection="customer")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerEntity {

    @Id
    private String idCustomer;

    @NotBlank(message = "CPF não pode estar em branco")
    @Size(min = 11, max = 11)
    @Pattern(regexp = "\\d{11}", message = "CPF deve ter 11 dígitos")
    private String cpf;

    @NotBlank(message = "Nome não pode estar em branco")
    private String name;

    @NotBlank(message = "Data de nascimento não pode estar em branco")
    private LocalDate birthDate;

    @NotBlank(message = "Telefone não pode estar em branco")
    private String phone;

    private AddressEntity addressEntity;

}
