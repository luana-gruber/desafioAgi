package com.desafioagi.service.impl;
import com.desafioagi.builder.InsuranceBuilder;
import com.desafioagi.client.ClientApi;
import com.desafioagi.dto.InsuranceRequestDTO;

import com.desafioagi.dto.InsuranceResponseDTO;
import com.desafioagi.exception.*;
import com.desafioagi.mapper.InsuranceMapper;
import com.desafioagi.model.InsuranceEntity;
import com.desafioagi.repository.InsuranceRepository;
import com.desafioagi.service.InsuranceService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

import java.time.Duration;


@Service
@RequiredArgsConstructor
@Slf4j
public class InsuranceServiceImpl implements InsuranceService {

    private final ClientApi clientApi;
    private final InsuranceRepository insuranceRepository;
    private final InsuranceMapper insuranceMapper;
    private final InsuranceBuilder insuranceBuilder;

    private Mono<InsuranceEntity> saveInsurance(InsuranceEntity insuranceEntity) {
        return insuranceRepository.save(insuranceEntity)
                .onErrorMap(ex -> new DatabaseConnectionException("Erro ao conectar com o banco de dados", ex));

    }

    @CircuitBreaker(name = "insurance", fallbackMethod = "fallbackHireInsurance")
    public Mono<InsuranceResponseDTO> hireInsurance(InsuranceRequestDTO insuranceRequestDTO) {
        return Mono.error(new StartFallBackException("Entrando no fluxo de exceção do fallback"));

        }

    public Mono<InsuranceResponseDTO> fallbackHireInsurance(InsuranceRequestDTO insuranceRequestDTO, Throwable ex) {
        return clientApi.getCustomerById(insuranceRequestDTO.getIdCustomer())
                .flatMap(customer -> insuranceBuilder.toInsuranceEntity(insuranceRequestDTO)
                        .flatMap(insuranceEntity ->
                                saveInsurance(insuranceEntity)
                                        .map(insuranceMapper::toInsuranceResponseDTO)
                        )
                )
                .onErrorMap(e -> e instanceof ServerErrorException || e instanceof ClientAccessException
                        || e instanceof RequestException ? e : new Exception("Não foi possível contratar o seguro no momento."));
    }




}

