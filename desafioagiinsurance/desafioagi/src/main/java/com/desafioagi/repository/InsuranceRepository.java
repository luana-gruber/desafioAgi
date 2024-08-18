package com.desafioagi.repository;

import com.desafioagi.model.InsuranceEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface InsuranceRepository extends ReactiveMongoRepository<InsuranceEntity, String> {
}
