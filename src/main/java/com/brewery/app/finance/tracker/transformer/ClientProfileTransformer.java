package com.brewery.app.finance.tracker.transformer;

import com.brewery.app.finance.tracker.codegen.model.ClientCreationPostRequest;
import com.brewery.app.finance.tracker.codegen.model.ClientProfile;
import com.brewery.app.finance.tracker.model.ClientProfileModel;
import com.brewery.app.finance.tracker.service.BalanceCalculationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
@AllArgsConstructor
public class ClientProfileTransformer {

    private final BalanceTransformer balanceTransformer;
    private final ClientCreditAccountTransformer clientCreditAccountTransformer;
    private final BalanceCalculationService balanceCalculationService;

    public ClientProfileModel newProfileModel(ClientCreationPostRequest request) {
        return ClientProfileModel.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .nickName(request.getNickName())
                .clientCreditAccountModelList(new ArrayList<>())
                .totalAssetHistory(new ArrayList<>())
                .totalDebtHistory(new ArrayList<>())
                .lastUpdated(LocalDateTime.now())
                .build();
    }

    public ClientProfile toDto(ClientProfileModel clientProfileModel) {
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setId(clientProfileModel.getId());
        clientProfile.setFirstName(clientProfileModel.getFirstName());
        clientProfile.setLastName(clientProfileModel.getLastName());
        clientProfile.setNickName(clientProfileModel.getNickName());
        clientProfile.setEmail(clientProfileModel.getEmail());
        clientProfile.setAssetHistory(balanceTransformer.toDtoList(clientProfileModel.getTotalAssetHistory()));
        clientProfile.setDebtHistory(balanceTransformer.toDtoList(clientProfileModel.getTotalDebtHistory()));
        clientProfile.setCreditInstitutionList(clientCreditAccountTransformer.toDtoList(clientProfileModel.getClientCreditAccountModelList()));

        balanceCalculationService.updateFinancialSummary(clientProfile, clientProfileModel);

        return clientProfile;
    }
}
