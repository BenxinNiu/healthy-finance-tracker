package com.brewery.app.finance.tracker.controller;

import com.brewery.app.finance.tracker.codegen.api.ClientsApi;
import com.brewery.app.finance.tracker.codegen.model.ClientAccountPatchRequest;
import com.brewery.app.finance.tracker.codegen.model.ClientCreationPostRequest;
import com.brewery.app.finance.tracker.codegen.model.ClientProfile;
import com.brewery.app.finance.tracker.service.ClientProfileService;
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
public class ClientsController implements ClientsApi {

    private final ClientProfileService clientProfileService;

    @Override
    public Mono<ResponseEntity<ClientProfile>> clientsClientIdProfileGet(String clientId, ServerWebExchange exchange) {
        return clientProfileService.getClientProfileById(clientId)
                .map(clientProfile -> new ResponseEntity<>(clientProfile, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
    }

    @Override
    public Mono<ResponseEntity<ClientProfile>> clientsNewPost(Mono<ClientCreationPostRequest> clientCreationPostRequest, ServerWebExchange exchange) {
        log.info("\"Received request\\n\"");
        return clientProfileService.createNewClientProfile(clientCreationPostRequest)
                .map(clientProfile -> new ResponseEntity<>(clientProfile, HttpStatus.CREATED));
    }

    @Override
    public Mono<ResponseEntity<ClientProfile>> clientsClientIdProfilePatch(String clientId, Mono<ClientAccountPatchRequest> clientAccountPatchRequest, ServerWebExchange exchange) {
        return clientAccountPatchRequest
                .flatMap(patch -> clientProfileService.patchClientProfile(clientId, patch.getClientAccountList()))
                .map(clientProfile -> new ResponseEntity<>(clientProfile, HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
    }

}