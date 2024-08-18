package com.desafioagi.desafioagi.service;

import com.desafioagi.desafioagi.dto.CustomerRequestDTO;
import com.desafioagi.desafioagi.dto.CustomerResponseDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface CustomerService {

    Mono<Object> recordCustomer(CustomerRequestDTO customerRequestDTO);

    Mono<CustomerResponseDTO> getCustomerById(String id);

    Mono<CustomerResponseDTO> getCustomerByCpf(String cpf);

    Mono<CustomerResponseDTO> updateCustomer(String id, CustomerRequestDTO newCustomer);

    Mono<Void> deleteCustomer(String id);
}
