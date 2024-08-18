package com.desafioagi.service;

import com.desafioagi.dto.InsuranceRequestDTO;
import com.desafioagi.dto.InsuranceResponseDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface InsuranceService {
    Mono<InsuranceResponseDTO> hireInsurance (InsuranceRequestDTO insuranceRequestDTO);
}
