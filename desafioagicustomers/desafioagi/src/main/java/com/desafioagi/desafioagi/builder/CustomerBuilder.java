package com.desafioagi.desafioagi.builder;


import com.desafioagi.desafioagi.dto.AddressRequestDTO;
import com.desafioagi.desafioagi.dto.CustomerRequestDTO;
import com.desafioagi.desafioagi.model.AddressEntity;
import com.desafioagi.desafioagi.model.CustomerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerBuilder {

    public Mono<CustomerEntity> toCustomerEntity(CustomerRequestDTO customerRequestDTO) {
        return Mono.just(customerRequestDTO)
                .map(dto -> {
                    AddressEntity addressEntity = toAddress(dto.getAddressEntity());
                    return CustomerEntity.builder()
                            .idCustomer(UUID.randomUUID().toString())
                            .cpf(dto.getCpf())
                            .name(dto.getName())
                            .birthDate(dto.getBirthDate())
                            .phone(dto.getPhone())
                            .addressEntity(addressEntity)
                            .build();
                });
    }

    private AddressEntity toAddress(AddressRequestDTO addressRequestDTO) {
        return AddressEntity.builder()
                .street(addressRequestDTO.getStreet())
                .number(addressRequestDTO.getNumber())
                .city(addressRequestDTO.getCity())
                .state(addressRequestDTO.getState())
                .zipCode(addressRequestDTO.getZipCode())
                .country(addressRequestDTO.getCountry())
                .build();
    }
}
