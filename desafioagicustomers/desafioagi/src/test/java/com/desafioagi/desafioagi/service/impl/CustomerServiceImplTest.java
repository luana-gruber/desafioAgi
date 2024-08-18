package com.desafioagi.desafioagi.service.impl;

import com.desafioagi.desafioagi.builder.CustomerBuilder;
import com.desafioagi.desafioagi.dto.AddressRequestDTO;
import com.desafioagi.desafioagi.dto.AddressResponseDTO;
import com.desafioagi.desafioagi.dto.CustomerRequestDTO;
import com.desafioagi.desafioagi.dto.CustomerResponseDTO;
import com.desafioagi.desafioagi.exception.CpfAlreadyRegisteredException;
import com.desafioagi.desafioagi.exception.DatabaseConnectionException;
import com.desafioagi.desafioagi.exception.InvalidCpfException;
import com.desafioagi.desafioagi.exception.RequiredFieldMissingException;
import com.desafioagi.desafioagi.mapper.CustomerMapper;
import com.desafioagi.desafioagi.model.AddressEntity;
import com.desafioagi.desafioagi.model.CustomerEntity;
import com.desafioagi.desafioagi.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.time.LocalDate;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CustomerServiceImplTest {

    private static final String ID_CUSTOMER = "433ab90f-38fd-41a8-a28b-8c150558b272";


    @Autowired
    private CustomerServiceImpl customerService;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private CustomerBuilder customerBuilder;

    @MockBean
    private CustomerMapper customerMapper;


    @Test
    @DisplayName("Deve retornar OK se o cliente for gravado com sucesso.")
    void recordCustomer_shouldReturnOkIfCustomerAndInsuranceHaveBeenSucceffullyValidated() {
        AddressResponseDTO addressResponseDTO = new AddressResponseDTO("rua", "12", "Campinas", "SP", "000000", "Brasil");
        AddressRequestDTO addressRequestDTO = new AddressRequestDTO("rua", "12", "Campinas", "SP", "000000", "Brasil");
        AddressEntity addressEntity = new AddressEntity("rua", "12", "Campinas", "SP", "000000", "Brasil");
        CustomerRequestDTO requestDTO = new CustomerRequestDTO("096.536.170-52", "Luana", LocalDate.of(2020, 10, 12), "1999999999", addressRequestDTO);
        CustomerEntity customerEntity = new CustomerEntity(ID_CUSTOMER, "096.536.170-52", "Luana", LocalDate.of(2020, 10, 12), "1999999999", addressEntity);
        CustomerResponseDTO responseDTO = new CustomerResponseDTO(ID_CUSTOMER, "Luana", "1999999999", addressResponseDTO);

        when(customerRepository.findCustomerByCpf("096.536.170-52")).thenReturn(Mono.empty());

        when(customerBuilder.toCustomerEntity(requestDTO)).thenReturn(Mono.just(customerEntity));

        when(customerRepository.save(customerEntity)).thenReturn(Mono.just(customerEntity));

        when(customerMapper.toCustomerResponseDTO(customerEntity)).thenReturn(responseDTO);

        Mono<Object> result = customerService.recordCustomer(requestDTO);

        StepVerifier.create(result)
                .expectNext(responseDTO)
                .verifyComplete();

    }

    @Test
    @DisplayName("Deve retornar RequiredFieldMissingException se o campo nome não for preenchido.")
    void recordCustomer_shouldReturnRequiredFieldMissingException() {
        AddressRequestDTO addressRequestDTO = new AddressRequestDTO("rua", "12", "Campinas", "SP", "000000", "Brasil");
        CustomerRequestDTO requestDTO = new CustomerRequestDTO("096.536.170-52", "", LocalDate.of(2020, 10, 12), "1999999999", addressRequestDTO);


        Mono<Object> result = customerService.recordCustomer(requestDTO);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RequiredFieldMissingException  &&
                        throwable.getMessage().equals("Nome é obrigatório."))
                .verify();
    }

    @Test
    @DisplayName("Deve retornar InvalidCpfException se o CPF não é válido.")
    void recordCustomer_shouldReturnInvalidCpfException() {
        AddressRequestDTO addressRequestDTO = new AddressRequestDTO("rua", "12", "Campinas", "SP", "000000", "Brasil");
        CustomerRequestDTO requestDTO = new CustomerRequestDTO("000000", "Luana", LocalDate.of(2020, 10, 12), "1999999999", addressRequestDTO);

        Mono<Object> result = customerService.recordCustomer(requestDTO);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof InvalidCpfException &&
                        throwable.getMessage().equals("CPF informado não é válido."))
                .verify();
    }

    @Test
    @DisplayName("Deve retornar CpfAlreadyRegisteredException se o cliente já tem cadastro na API.")
    void recordCustomer_shouldReturnCpfAlreadyRegisteredException() {
        AddressRequestDTO addressRequestDTO = new AddressRequestDTO("rua", "12", "Campinas", "SP", "000000", "Brasil");
        AddressEntity addressEntity = new AddressEntity("rua", "12", "Campinas", "SP", "000000", "Brasil");
        CustomerRequestDTO requestDTO = new CustomerRequestDTO("096.536.170-52", "Luana", LocalDate.of(2020, 10, 12), "1999999999", addressRequestDTO);
        CustomerEntity customerEntity = new CustomerEntity(ID_CUSTOMER, "096.536.170-52", "Luana", LocalDate.of(2020, 10, 12), "1999999999", addressEntity);

        when(customerRepository.findCustomerByCpf(requestDTO.getCpf()))
                .thenReturn(Mono.just(customerEntity));

        when(customerBuilder.toCustomerEntity(requestDTO)).thenReturn(Mono.just(customerEntity));

        Mono<Object> result = customerService.recordCustomer(requestDTO);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof CpfAlreadyRegisteredException &&
                            throwable.getMessage().equals("CPF " + requestDTO.getCpf() + " já possui cadastro."))
                .verify();
    }

    @Test
    @DisplayName("Deve retornar DatabaseConnectionException porque não conseguiu converter DTO em Entity")
    void recordCustomer_shouldReturnDatabaseConnectionException() {
        AddressRequestDTO addressRequestDTO = new AddressRequestDTO("rua", "12", "Campinas", "SP", "000000", "Brasil");
        CustomerRequestDTO requestDTO = new CustomerRequestDTO("096.536.170-52", "Luana", LocalDate.of(2020, 10, 12), "1999999999", addressRequestDTO);

        when(customerRepository.findCustomerByCpf("096.536.170-52")).thenReturn(Mono.empty());

        when(customerBuilder.toCustomerEntity(requestDTO)).thenReturn(Mono.error(new RuntimeException("Erro ao conectar com o banco de dados")));

        Mono<Object> result = customerService.recordCustomer(requestDTO);

        StepVerifier.create(result).expectErrorMatches(throwable -> throwable instanceof DatabaseConnectionException &&
                        throwable.getMessage().equals("Erro ao conectar com o banco de dados"))
                .verify();
    }
}


