package com.desafioagi.desafioagi.exception;

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

    @ExceptionHandler(RequiredFieldMissingException.class)
    public Mono<ResponseEntity<ModelErrorResponse>> requiredFieldMissingException(RequiredFieldMissingException ex, ServerHttpRequest request) {
        return BuilderErrorMessage.buildErrorResponse(HttpStatus.BAD_REQUEST, "Campo " + ex.getMessage(), request)
                .doOnNext(errorResponse -> log.error("Erro de campo obrigatório: {}", ex.getMessage()))
                .map(errorResponse -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public Mono<ResponseEntity<ModelErrorResponse>> customerNotFoundException(CustomerNotFoundException ex, ServerHttpRequest request) {
        return BuilderErrorMessage.buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request)
                .doOnNext(errorResponse -> log.error("ID/CPF do cliente não encontrado: {}", ex.getMessage()))
                .map(errorResponse -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse));
    }

    @ExceptionHandler(DatabaseConnectionException.class)
    public Mono<ResponseEntity<ModelErrorResponse>> dataBaseConnectionException(DatabaseConnectionException ex,ServerHttpRequest request) {
        return BuilderErrorMessage.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request)
                .doOnNext(errorResponse -> log.error("Erro ao conectar com o banco de dados: {}", ex.getMessage()))
                .map(errorResponse -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
    }

    @ExceptionHandler(CpfAlreadyRegisteredException.class)
    public Mono<ResponseEntity<ModelErrorResponse>> cpfAlreadyRegisteredException(CpfAlreadyRegisteredException ex, ServerHttpRequest request) {
        return BuilderErrorMessage.buildErrorResponse(HttpStatus.CONFLICT, "CPF informado já possui cadastro.", request)
                .doOnNext(errorResponse -> log.error("O CPF já existe na base de dados: {}", ex.getMessage()))
                .map(errorResponse -> ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse));
    }

    @ExceptionHandler(InvalidCpfException.class)
    public Mono<ResponseEntity<ModelErrorResponse>> invalidCpfException(InvalidCpfException ex, ServerHttpRequest request) {
        return BuilderErrorMessage.buildErrorResponse(HttpStatus.BAD_REQUEST, "CPF informado não é válido.", request)
                .doOnNext(errorResponse -> log.error("O cpf não foi considerado válido: {}", ex.getMessage()))
                .map(errorResponse -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ModelErrorResponse>> handleGenericException(Exception ex, ServerHttpRequest request) {
        return BuilderErrorMessage.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor:" + ex.getMessage(), request)
                .doOnNext(errorResponse -> log.error("Erro interno da aplicação oriundo da classe Exception: {}", ex.getMessage(), ex))
                .map(errorResponse -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
    }
}
