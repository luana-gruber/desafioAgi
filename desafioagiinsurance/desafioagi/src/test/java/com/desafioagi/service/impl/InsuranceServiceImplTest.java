package com.desafioagi.service.impl;

import com.desafioagi.builder.InsuranceBuilder;
import com.desafioagi.client.ClientApi;
import com.desafioagi.dto.CustomerResponseDTO;
import com.desafioagi.dto.InsuranceRequestDTO;
import com.desafioagi.dto.InsuranceResponseDTO;
import com.desafioagi.exception.ClientAccessException;
import com.desafioagi.exception.RequestException;
import com.desafioagi.exception.ServerErrorException;
import com.desafioagi.exception.StartFallBackException;
import com.desafioagi.mapper.InsuranceMapper;
import com.desafioagi.model.InsuranceEntity;
import com.desafioagi.repository.InsuranceRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;
import reactor.test.StepVerifier;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class InsuranceServiceImplTest {

    private static final String ID_CUSTOMER = "433ab90f-38fd-41a8-a28b-8c150558b272";


    @Autowired
    private InsuranceServiceImpl insuranceService;

    @MockBean
    private ClientApi clientApi;

    @MockBean
    private CircuitBreaker circuitBreaker;

    @MockBean
    private InsuranceBuilder insuranceBuilder;

    @MockBean
    private InsuranceMapper insuranceMapper;

    @MockBean
    private InsuranceRepository insuranceRepository;

    @Test
    @DisplayName("Deve retornar OK se o cliente for validado com sucesso e se o seguro foi contratado.")
    public void fallbackHireInsurance_shouldReturnOkIfCustomerAndInsuranceHaveBeenSucceffullyValidated() {
        CustomerResponseDTO mockCustomer = new CustomerResponseDTO(ID_CUSTOMER);
        InsuranceEntity mockInsuranceEntity = new InsuranceEntity();
        InsuranceRequestDTO mockInsuranceRequest = new InsuranceRequestDTO(ID_CUSTOMER, "Bronze");
        InsuranceResponseDTO mockResponse = new InsuranceResponseDTO(ID_CUSTOMER, "Bronze", "Plano Bronze", 100.00);


        when(clientApi.getCustomerById(ID_CUSTOMER))
                .thenReturn(Mono.just(mockCustomer));

        when(insuranceBuilder.toInsuranceEntity(mockInsuranceRequest))
                .thenReturn(Mono.just(mockInsuranceEntity));

        when(insuranceRepository.save(mockInsuranceEntity))
                .thenReturn(Mono.just(mockInsuranceEntity));

        when(insuranceMapper.toInsuranceResponseDTO(mockInsuranceEntity))
                .thenReturn(mockResponse);

        Mono<InsuranceResponseDTO> resultMono = insuranceService.fallbackHireInsurance(mockInsuranceRequest, new Throwable());

        StepVerifier.create(resultMono)
                .expectNextMatches(response -> {
                    assertEquals(ID_CUSTOMER, response.getIdCustomer());
                    assertEquals("Bronze", response.getPlan());
                    assertEquals("Plano Bronze", response.getDescription());
                    return true;
                })
                .verifyComplete();
    }


    @Test
    @DisplayName("Deve retornar ClientAccessException quando API cliente retornar ClientAccessException")
    public void fallbackHireInsurance_shouldReturnClientAccessException() {
        InsuranceRequestDTO mockInsuranceRequest = new InsuranceRequestDTO(ID_CUSTOMER, "Bronze");

        when(clientApi.getCustomerById(anyString()))
                .thenReturn(Mono.error(new ClientAccessException("Erro ao acessar cliente, verifique o id: " + ID_CUSTOMER)));

        Mono<InsuranceResponseDTO> resultMono = insuranceService.fallbackHireInsurance(mockInsuranceRequest, new Throwable());

        StepVerifier.create(resultMono)
                .expectErrorMatches(throwable -> throwable instanceof ClientAccessException &&
                        throwable.getMessage().equals("Erro ao acessar cliente, verifique o id: " + ID_CUSTOMER))
                .verify();
    }


    @Test
    @DisplayName("Deve retornar ServerErrorException quando API cliente retornar ServerErrorException")
    public void fallbackHireInsurance_shouldReturnServerErrorException() {
        InsuranceRequestDTO mockInsuranceRequest = new InsuranceRequestDTO(ID_CUSTOMER, "Bronze");

        when(clientApi.getCustomerById(anyString()))
                .thenReturn(Mono.error(new ServerErrorException("Cliente com ID: " + ID_CUSTOMER + " não cadastrado.")));

        Mono<InsuranceResponseDTO> resultMono = insuranceService.fallbackHireInsurance(mockInsuranceRequest, new Throwable());

        StepVerifier.create(resultMono)
                .expectErrorMatches(throwable -> throwable instanceof ServerErrorException &&
                        throwable.getMessage().equals("Cliente com ID: " + ID_CUSTOMER + " não cadastrado."))
                .verify();
        }

    @Test
    @DisplayName("Deve retornar RequestException quando API cliente retornar RequestException")
    public void fallbackHireInsurance_shouldReturnRequestException() {
        InsuranceRequestDTO mockInsuranceRequest = new InsuranceRequestDTO(ID_CUSTOMER, "Bronze");

        when(clientApi.getCustomerById(anyString()))
                .thenReturn(Mono.error(new RequestException("Não foi possível acessar API de cadastro no momento.")));

        Mono<InsuranceResponseDTO> resultMono = insuranceService.fallbackHireInsurance(mockInsuranceRequest, new Throwable());

        StepVerifier.create(resultMono)
                .expectErrorMatches(throwable -> throwable instanceof RequestException &&
                        throwable.getMessage().equals("Não foi possível acessar API de cadastro no momento."))
                .verify();
    }
}

