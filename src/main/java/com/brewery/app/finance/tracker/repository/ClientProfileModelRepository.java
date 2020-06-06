package com.brewery.app.finance.tracker.repository;

import com.brewery.app.finance.tracker.model.ClientProfileModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ClientProfileModelRepository extends ReactiveMongoRepository<ClientProfileModel, String> {
}
