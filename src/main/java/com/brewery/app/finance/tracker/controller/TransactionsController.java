package com.brewery.app.finance.tracker.controller;

import com.brewery.app.finance.tracker.codegen.api.TransactionsApi;
import com.brewery.app.finance.tracker.codegen.model.ClientProfile;
import com.brewery.app.finance.tracker.codegen.model.TransactionsPostRequest;
import com.brewery.app.finance.tracker.service.TransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Slf4j
@Controller
public class TransactionsController implements TransactionsApi {

    private final TransactionService transactionService;

    public Mono<ResponseEntity<ClientProfile>> transactionsClientsClientIdPost(String clientId, Mono<TransactionsPostRequest> transactionsPostRequest, ServerWebExchange exchange) {
        return transactionsPostRequest
                .flatMap(req -> transactionService.saveTransactionsAndUpdateProfile(clientId, req.getTransactions()))
                .map(profile -> new ResponseEntity<>(profile, HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
    }

}
