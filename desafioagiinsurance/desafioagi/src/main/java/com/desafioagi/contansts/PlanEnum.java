package com.desafioagi.contansts;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum PlanEnum {
    BRONZE("Plano Bronze", 100.0),
    PRATA("Plano Prata", 200.0),
    OURO("Plano Ouro", 300.0);

    private final String description;
    private final double value;

    private static final Map<String, PlanEnum> PLAN_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(PlanEnum::name, Function.identity()));

    public static PlanEnum fromPlanName(String plan) {
        PlanEnum planEnum = PLAN_MAP.get(plan.toUpperCase());
        if (planEnum == null) {
            throw new IllegalArgumentException("Plano desconhecido: " + plan);
        }
        return planEnum;
    }
}

