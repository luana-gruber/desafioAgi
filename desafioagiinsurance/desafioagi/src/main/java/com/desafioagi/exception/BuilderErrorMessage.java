package com.desafioagi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
public class BuilderErrorMessage {
    public static Mono<ModelErrorResponse> buildErrorResponse(HttpStatus status, String message, ServerHttpRequest request) {
        ModelErrorResponse errorResponse = ModelErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .message(message)
                .path(request.getURI().getPath())
                .build();
        return Mono.just(errorResponse);
    }
}