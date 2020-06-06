package com.brewery.app.finance.tracker.service;

import com.brewery.app.finance.tracker.codegen.model.ClientCreationPostRequest;
import com.brewery.app.finance.tracker.codegen.model.ClientProfile;
import com.brewery.app.finance.tracker.model.ClientProfileModel;
import com.brewery.app.finance.tracker.repository.ClientProfileModelRepository;
import com.brewery.app.finance.tracker.transformer.ClientProfileDtoTransformer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@AllArgsConstructor
@Slf4j
@Service
public class ClientProfileService {

    private final ClientProfileModelRepository clientProfileModelRepository;
    private final ClientProfileDtoTransformer clientProfileDtoTransformer;

    public Mono<ClientProfile> createNewClientProfile(Mono<ClientCreationPostRequest> clientCreationPostRequest) {
        return clientCreationPostRequest
                .flatMap(clientProfileDtoTransformer::toClientProfileModel)
                .flatMap(clientProfileModelRepository::save)
                .flatMap(clientProfileDtoTransformer::toDto);
    }
}
