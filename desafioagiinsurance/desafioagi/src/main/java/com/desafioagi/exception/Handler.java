package com.desafioagi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import reactor.core.publisher.Mono;

@Slf4j
@ControllerAdvice
public class Handler {

    @ExceptionHandler(ClientAccessException.class)
    public Mono<ResponseEntity<ModelErrorResponse>> clientAccessException(ClientAccessException ex, ServerHttpRequest request) {
        return BuilderErrorMessage.buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request)
                .doOnNext(errorResponse -> log.error("Erro ao processar a API de cadastro de clientes: {}", ex.getMessage()))
                .map(errorResponse -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
    }

    @ExceptionHandler(DatabaseConnectionException.class)
    public Mono<ResponseEntity<ModelErrorResponse>> dataBaseConnectionException(DatabaseConnectionException ex,ServerHttpRequest request) {
        return BuilderErrorMessage.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request)
                .doOnNext(errorResponse -> log.error("Erro ao conectar com o banco de dados: {}", ex.getMessage()))
                .map(errorResponse -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
    }

    @ExceptionHandler(ServerErrorException.class)
    public Mono<ResponseEntity<ModelErrorResponse>> serverErrorException(ServerErrorException ex, ServerHttpRequest request) {
        return BuilderErrorMessage.buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request)
                .map(errorResponse -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse));
    }

    @ExceptionHandler(RequestException.class)
    public Mono<ResponseEntity<ModelErrorResponse>> requestException(RequestException ex, ServerHttpRequest request) {
        return BuilderErrorMessage.buildErrorResponse(HttpStatus.TOO_MANY_REQUESTS, ex.getMessage(), request)
                .doOnNext(errorResponse -> log.error("API de cadastro atingiu o Rate Limit: {}", ex.getMessage()))
                .map(errorResponse -> ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponse));
    }

    @ExceptionHandler(StartFallBackException.class)
    public Mono<ResponseEntity<ModelErrorResponse>> startFallBackException(StartFallBackException ex, ServerHttpRequest request) {
        return BuilderErrorMessage.buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request)
                .doOnNext(errorResponse -> log.error("Fallback: {}", ex.getMessage()))
                .map(errorResponse -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
    }


    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ModelErrorResponse>> handleGenericException(Exception ex, ServerHttpRequest request) {
        return BuilderErrorMessage.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request)
                .doOnNext(errorResponse -> log.error("Erro interno da aplicação oriundo da classe Exception: {}", ex.getMessage(), ex))
                .map(errorResponse -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
    }
}
