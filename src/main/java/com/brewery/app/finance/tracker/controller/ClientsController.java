package com.brewery.app.finance.tracker.controller;

import com.brewery.app.finance.tracker.codegen.api.ClientsApi;
import com.brewery.app.finance.tracker.codegen.model.ClientCreationPostRequest;
import com.brewery.app.finance.tracker.codegen.model.ClientProfile;
import com.brewery.app.finance.tracker.service.ClientProfileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Slf4j
@Controller
public class ClientsController implements ClientsApi {

    private final ClientProfileService clientProfileService;

    @Override
    public Mono<ResponseEntity<ClientProfile>> clientsClientIdProfileGet(String clientId, ServerWebExchange exchange) {
        // return Mono.just(new ResponseEntity<>(new ClientProfile().firstName("ben"), HttpStatus.OK));
        return clientProfileService.createNewClientProfile(Mono.just(new ClientCreationPostRequest().firstName("benxin")))
                .map(clientProfile -> {
                    log.info(clientProfile.getEmail());
                    return new ResponseEntity<>(clientProfile, HttpStatus.CREATED);})
                .defaultIfEmpty(new ResponseEntity<>(new ClientProfile().firstName("ben"), HttpStatus.CREATED))
                ;
    }

    @Override
    public Mono<ResponseEntity<ClientProfile>> clientsNewPost(Mono<ClientCreationPostRequest> clientCreationPostRequest, ServerWebExchange exchange) {
        log.info("\"Received request\\n\"");
        return clientProfileService.createNewClientProfile(clientCreationPostRequest)
                .defaultIfEmpty(new ClientProfile().firstName("ben"))
                .map(clientProfile -> new ResponseEntity<>(clientProfile, HttpStatus.CREATED));

    }

}