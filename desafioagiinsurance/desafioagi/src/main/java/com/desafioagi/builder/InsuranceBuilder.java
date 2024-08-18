package com.desafioagi.builder;

import com.desafioagi.contansts.PlanEnum;
import com.desafioagi.dto.InsuranceRequestDTO;
import com.desafioagi.model.InsuranceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InsuranceBuilder {

    public Mono<InsuranceEntity> toInsuranceEntity(InsuranceRequestDTO insuranceRequestDTO) {
        return Mono.defer(() -> {
            try {
                PlanEnum planEnum = PlanEnum.fromPlanName(insuranceRequestDTO.getPlan());
                double planValue = planEnum.getValue();
                String description = planEnum.getDescription();

                InsuranceEntity insuranceEntity = InsuranceEntity.builder()
                        .idInsurance(UUID.randomUUID().toString())
                        .idCustomer(insuranceRequestDTO.getIdCustomer())
                        .plan(insuranceRequestDTO.getPlan().toUpperCase())
                        .description(description)
                        .planValue(planValue)
                        .build();

                return Mono.just(insuranceEntity);
            } catch (IllegalArgumentException e) {
                return Mono.error(new IllegalArgumentException("Plano desconhecido: " + insuranceRequestDTO.getPlan()));
            }
        });
    }
}

