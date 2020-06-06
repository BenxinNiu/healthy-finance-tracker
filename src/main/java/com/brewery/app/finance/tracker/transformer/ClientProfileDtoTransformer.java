package com.brewery.app.finance.tracker.transformer;

import com.brewery.app.finance.tracker.codegen.model.ClientCreationPostRequest;
import com.brewery.app.finance.tracker.codegen.model.ClientProfile;
import com.brewery.app.finance.tracker.model.ClientProfileModel;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;

@Component
public class ClientProfileDtoTransformer {

    public Mono<ClientProfileModel> toClientProfileModel(ClientCreationPostRequest request) {
        return Mono.just(ClientProfileModel.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .totalAssetHistory(new ArrayList<>())
                .totalDebtHistory(new ArrayList<>())
                .clientCreditAccountList(new ArrayList<>())
                .lastUpdated(LocalDate.now())
                .build()
        );
    }

    public Mono<ClientProfile> toDto(ClientProfileModel clientProfileModel) {
        return Mono.just(new ClientProfile().email("benxin niu"));
    }
}
