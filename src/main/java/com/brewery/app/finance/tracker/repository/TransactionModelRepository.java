package com.brewery.app.finance.tracker.repository;

import com.brewery.app.finance.tracker.model.TransactionModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TransactionModelRepository extends ReactiveMongoRepository<TransactionModel, String> {
}
