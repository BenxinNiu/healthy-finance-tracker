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

    public ClientProfileModel newProfileModel(ClientCreationPostRequest request) {
        return ClientProfileModel.builder()
                .email(request.getEmail())
                .clientCreditAccountModelList(new ArrayList<>())
                .lastUpdated(LocalDate.now())
                .build();
    }

    public ClientProfile toDto(ClientProfileModel clientProfileModel) {
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setId(clientProfileModel.getId());
        clientProfile.setFirstName(clientProfileModel.getFirstName());
        clientProfile.setLastName(clientProfileModel.getLastName());
        clientProfile.setNickName("");
        clientProfile.setEmail(clientProfileModel.getEmail());
        clientProfile.setAssetHistory(balanceTransformer.toDtoList(clientProfileModel.getTotalAssetHistory()));
        clientProfile.setDebtHistory(balanceTransformer.toDtoList(clientProfileModel.getTotalDebtHistory()));
        clientProfile.setCreditInstitutionList(clientCreditAccountTransformer.toDtoList(clientProfileModel.getClientCreditAccountModelList()));

        if (clientProfileModel.getTotalAssetHistory().size() > 0){
            clientProfile.setTotalAsset(clientProfileModel.getTotalAssetHistory().get(clientProfileModel.getTotalAssetHistory().size() -1 ).getAmount());
        }
        if (clientProfileModel.getTotalDebtHistory().size() > 0) {
            clientProfile.setTotalDebt(clientProfileModel.getTotalDebtHistory().get(clientProfileModel.getTotalDebtHistory().size() -1 ).getAmount());
        }
        return clientProfile;
    }
}
