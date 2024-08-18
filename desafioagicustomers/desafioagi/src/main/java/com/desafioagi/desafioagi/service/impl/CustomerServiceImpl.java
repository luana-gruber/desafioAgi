package com.desafioagi.desafioagi.service.impl;
import com.desafioagi.desafioagi.builder.CustomerBuilder;
import com.desafioagi.desafioagi.dto.CustomerRequestDTO;
import com.desafioagi.desafioagi.dto.CustomerResponseDTO;
import com.desafioagi.desafioagi.exception.*;
import com.desafioagi.desafioagi.mapper.CustomerMapper;
import com.desafioagi.desafioagi.model.CustomerEntity;
import com.desafioagi.desafioagi.repository.CustomerRepository;
import com.desafioagi.desafioagi.service.CustomerService;
import com.desafioagi.desafioagi.util.CpfValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerBuilder customerBuilder;
    private final CustomerMapper customerMapper;


    private Mono<CustomerEntity> saveCustomer(CustomerEntity customerEntity) {
        return customerRepository.save(customerEntity)
                .onErrorMap(ex -> new DatabaseConnectionException("Erro ao conectar com o banco de dados", ex));
    }

    public Mono<Object> recordCustomer(CustomerRequestDTO customerRequestDTO) {
        return validateRequiredFields(customerRequestDTO)
                .then(Mono.just(customerRequestDTO.getCpf()))
                .filter(CpfValidator::isValidCPF)
                .switchIfEmpty(Mono.error(new InvalidCpfException("CPF informado não é válido.")))
                .flatMap(cpf -> customerRepository.findCustomerByCpf(cpf)
                        .flatMap(existingCustomer -> Mono.error(new CpfAlreadyRegisteredException("CPF " + cpf + " já possui cadastro.")))
                        .switchIfEmpty(customerBuilder.toCustomerEntity(customerRequestDTO)
                                .flatMap(this::saveCustomer)
                                .map(customerMapper::toCustomerResponseDTO)
                        )
                        .onErrorMap(ex -> ex instanceof CpfAlreadyRegisteredException ? ex : new DatabaseConnectionException("Erro ao conectar com o banco de dados", ex)));
    }

    public Mono<CustomerResponseDTO> updateCustomer(String id, CustomerRequestDTO customerRequestDTO) {
        return validateRequiredFields(customerRequestDTO)
                .then(customerRepository.findById(id)
                        .switchIfEmpty(Mono.error(new CustomerNotFoundException("Cliente com ID " + id + " não encontrado.")))
                )
                .flatMap(existingCustomer ->
                        customerBuilder.toCustomerEntity(customerRequestDTO)
                                .doOnNext(customer -> customer.setIdCustomer(id))
                                .flatMap(this::saveCustomer)
                )
                .map(customerMapper::toCustomerResponseDTO)
                .onErrorMap(ex -> ex instanceof CustomerNotFoundException ? ex : new DatabaseConnectionException("Erro ao conectar com o banco de dados", ex));

    }

    private Mono<Void> validateRequiredFields(CustomerRequestDTO customerRequestDTO) {
        Map<String, Predicate<CustomerRequestDTO>> validations = Map.of(
                "CPF", dto -> dto.getCpf() != null && !dto.getCpf().isEmpty(),
                "Nome", dto -> dto.getName() != null && !dto.getName().isEmpty(),
                "Telefone", dto -> dto.getPhone() != null && !dto.getPhone().isEmpty(),
                "Data de Nascimento", dto -> dto.getBirthDate() != null
        );

        return validations.entrySet().stream()
                .filter(entry -> !entry.getValue().test(customerRequestDTO))
                .findFirst()
                .map(entry -> Mono.error(new RequiredFieldMissingException(entry.getKey() + " é obrigatório.")))
                .orElse(Mono.empty()).then();
    }

    public Mono<CustomerResponseDTO> getCustomerById(String id) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Cliente com ID " + id + " não encontrado.")))
                .onErrorMap(ex -> ex instanceof CustomerNotFoundException ? ex : new DatabaseConnectionException("Erro ao conectar com o banco de dados", ex))
                .map(customerMapper::toCustomerResponseDTO);
    }

    public Mono<CustomerResponseDTO> getCustomerByCpf(String cpf) {
        return customerRepository.findCustomerByCpf(cpf)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Cliente com CPF " + cpf + " não encontrado.")))
                .map(customerMapper::toCustomerResponseDTO)
                .onErrorMap(ex -> ex instanceof CustomerNotFoundException ? ex : new DatabaseConnectionException("Erro ao conectar com o banco de dados", ex));
    }

    @Transactional
    public Mono<Void> deleteCustomer(String id) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Cliente com ID " + id + " não encontrado.")))
                .flatMap(customerRepository::delete)
                .onErrorMap(ex -> ex instanceof CustomerNotFoundException ? ex : new DatabaseConnectionException("Erro ao conectar com o banco de dados", ex));
    }

}
