package com.desafioagi.desafioagi.controller;

import com.desafioagi.desafioagi.dto.CustomerRequestDTO;
import com.desafioagi.desafioagi.dto.CustomerResponseDTO;
import com.desafioagi.desafioagi.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/clientes")
@AllArgsConstructor
public class CustomerController {

    private CustomerService customerService;

    @Operation(summary = "Criar cadastro de novo cliente")
    @PostMapping
    public Mono<ResponseEntity<CustomerResponseDTO>> createCustomer(@Valid @RequestBody CustomerRequestDTO customerRequestDTO) {
        return customerService.recordCustomer(customerRequestDTO)
                .map(savedCustomer -> ResponseEntity.status(HttpStatus.CREATED).body((CustomerResponseDTO) savedCustomer));
    }

    @Operation(summary = "Busca por id do cliente cadastrado")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CustomerResponseDTO>> getCustomerById(@PathVariable String id) {
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Busca por CPF do cliente cadastrado")
    @GetMapping("/cpf/{cpf}")
    public Mono<ResponseEntity<CustomerResponseDTO>> getCustomerByCpf(@PathVariable String cpf) {
        return customerService.getCustomerByCpf(cpf)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Atualização do cliente cadastrado por id")
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CustomerResponseDTO>> updateCustomer(@PathVariable String id, @Valid @RequestBody CustomerRequestDTO customerRequestDTO) {
        return customerService.updateCustomer(id, customerRequestDTO)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Deleção do cliente cadastrado por id")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> deleteCustomer(@PathVariable String id) {
        return customerService.deleteCustomer(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
