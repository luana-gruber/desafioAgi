package com.desafioagi.client;

import com.desafioagi.dto.CustomerResponseDTO;
import com.desafioagi.exception.ClientAccessException;
import com.desafioagi.exception.RequestException;
import com.desafioagi.exception.ServerErrorException;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator;
import io.netty.handler.timeout.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@Slf4j
public class ClientApi {

    private final WebClient webClient;
    private final RateLimiter rateLimiter;

    public ClientApi(WebClient.Builder webClientBuilder,  RateLimiter rateLimiter) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
        this.rateLimiter = rateLimiter;
    }

    public Mono<CustomerResponseDTO> getCustomerById(String id) {
        return webClient.get()
                .uri("/clientes/{id}", id)
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, clientResponse ->
                        Mono.error(new ClientAccessException("Erro ao acessar cliente, verifique o id: " + id))
                )
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse ->
                        Mono.error(new ServerErrorException("Cliente com ID: " + id + " não cadastrado."))
                )
                .onStatus(HttpStatus.TOO_MANY_REQUESTS::equals, clientResponse ->
                        Mono.error(new RequestException("Não foi possível acessar API de cadastro no momento."))
                )
                .bodyToMono(CustomerResponseDTO.class)
                .transformDeferred(RateLimiterOperator.of(rateLimiter))
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("Erro de resposta do WebClient: ", e);
                    return Mono.error(new ServerErrorException("Erro ao acessar cliente. Erro de resposta do WebClient."));
                })
                .onErrorResume(TimeoutException.class, e -> {
                    log.error("Tempo de resposta excedido: ", e);
                    return Mono.error(new ServerErrorException("Tempo de resposta excedido ao acessar cliente."));
                })
                .doOnError(error -> log.error("Erro ao processar resposta para o id {}: ", id, error));
    }

}
