package com.desafioagi.desafioagi.repository;

import com.desafioagi.desafioagi.model.CustomerEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends ReactiveMongoRepository<CustomerEntity, String> {
    Mono<CustomerEntity> findCustomerByCpf(String cpf);
}
