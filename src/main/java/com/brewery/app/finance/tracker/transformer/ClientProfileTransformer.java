package com.brewery.app.finance.tracker.transformer;

import com.brewery.app.finance.tracker.codegen.model.ClientCreationPostRequest;
import com.brewery.app.finance.tracker.codegen.model.ClientProfile;
import com.brewery.app.finance.tracker.model.ClientProfileModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;

@Component
@AllArgsConstructor
public class ClientProfileTransformer {

    private final BalanceTransformer balanceTransformer;
    private final ClientCreditAccountTransformer clientCreditAccountTransformer;

    public Mono<ClientProfileModel> toClientProfileModel(ClientCreationPostRequest request) {
        return Mono.just(ClientProfileModel.builder()
                .email(request.getEmail())
                .clientCreditAccountModelList(new ArrayList<>())
                .lastUpdated(LocalDate.now())
                .build()
        );
    }

    public Mono<ClientProfile> toDto(ClientProfileModel clientProfileModel) {
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setId(clientProfileModel.getId());
        clientProfile.setFirstName(clientProfileModel.getFirstName());
        clientProfile.setLastName(clientProfileModel.getLastName());
        clientProfile.setNickName("");
        clientProfile.setEmail(clientProfileModel.getEmail());
        clientProfile.setAssetHistory(balanceTransformer.toDtoList(clientProfileModel.getTotalAssetHistory()));
        clientProfile.setDebtHistory(balanceTransformer.toDtoList(clientProfileModel.getTotalDebtHistory()));
        clientProfile.setCreditInstitutionList(clientCreditAccountTransformer.toDtoList(clientProfileModel.getClientCreditAccountModelList()));

        return Mono.just(clientProfile);
    }
}
