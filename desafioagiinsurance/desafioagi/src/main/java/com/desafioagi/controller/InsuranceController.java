package com.desafioagi.controller;

import com.desafioagi.contansts.PlanEnum;
import com.desafioagi.dto.InsuranceRequestDTO;
import com.desafioagi.dto.InsuranceResponseDTO;
import com.desafioagi.service.InsuranceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/seguros")
@AllArgsConstructor
public class InsuranceController {

    private InsuranceService insuranceService;

    @Operation(summary = "Contratar seguro caso o cliente esteja cadastrado")
    @PostMapping("/contratar/{id_cliente}")
    public Mono<ResponseEntity<InsuranceResponseDTO>> hireInsurance(@PathVariable("id_cliente") String idCustomer, @RequestParam("plano") String plan) {
        InsuranceRequestDTO insuranceRequestDTO = new InsuranceRequestDTO(idCustomer, plan);
        return insuranceService.hireInsurance(insuranceRequestDTO)
                .map(savedInsurance -> ResponseEntity.status(HttpStatus.CREATED).body(savedInsurance));
    }

    @Operation(summary = "Simulação de contratação de seguro para qualquer cliente")
    @GetMapping("/simulacao")
    public Mono<ResponseEntity<InsuranceResponseDTO>> simulateInsurance(@RequestParam("plano") String plan) {
        PlanEnum planEnum = PlanEnum.fromPlanName(plan);
        double planValue = planEnum.getValue();
        String description = planEnum.getDescription();

        InsuranceResponseDTO responseDTO = new InsuranceResponseDTO(
                "Cliente não cadastrado",
                plan.toUpperCase(),
                description,
                planValue
        );
        return Mono.just(ResponseEntity.ok(responseDTO));
    }

}
