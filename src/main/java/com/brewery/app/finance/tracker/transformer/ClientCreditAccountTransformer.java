package com.brewery.app.finance.tracker.transformer;

import com.brewery.app.finance.tracker.codegen.model.ClientAccountUpdate;
import com.brewery.app.finance.tracker.codegen.model.ClientCreditAccount;
import com.brewery.app.finance.tracker.model.BalanceModel;
import com.brewery.app.finance.tracker.model.ClientCreditAccountModel;
import com.brewery.app.finance.tracker.model.Enums;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ClientCreditAccountTransformer {

    private final BalanceTransformer balanceTransformer;

    public void updateModel(ClientAccountUpdate update, ClientCreditAccountModel existing) {
        existing.setAccountName(update.getName());
        existing.setExpiryDate(update.getExpiryDate());
        existing.setImageUrl(update.getImageUrl());

        if (update.getBalanceHistory() != null && update.getBalanceHistory().get(0) != null) {
            BalanceModel balanceModel = balanceTransformer.toModel(update.getBalanceHistory().get(0));
            balanceModel.setAccountId(existing.getAccountId());
            existing.getBalanceModelList().add(balanceModel);
        }
    }

    public ClientCreditAccountModel toNewModel(ClientAccountUpdate update) {
        return ClientCreditAccountModel.builder()
                .accountId(UUID.randomUUID().toString())
                .accountName(update.getName())
                .accountType(Enums.toEnum(Enums.ClientAccountType.class, update.getType().toString()))
                .expiryDate(update.getExpiryDate())
                .maxBalanceRecord(60)
                .imageUrl(update.getImageUrl())
                .balanceModelList(balanceTransformer.toModelList(update.getBalanceHistory()))
                .build();
    }

    public List<ClientCreditAccount> toDtoList(List<ClientCreditAccountModel> clientCreditAccountModelList) {
        return clientCreditAccountModelList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ClientCreditAccount toDto(ClientCreditAccountModel clientCreditAccountModel) {
        ClientCreditAccount clientCreditAccount = new ClientCreditAccount();
        clientCreditAccount.setId(clientCreditAccountModel.getAccountId());
        clientCreditAccount.setName(clientCreditAccountModel.getAccountName());
        clientCreditAccount.setImageUrl(clientCreditAccountModel.getImageUrl());
        clientCreditAccount.setType(Enums.toEnum(ClientCreditAccount.TypeEnum.class, clientCreditAccountModel.getAccountType().toString()));
        clientCreditAccount.setBalanceHistory(balanceTransformer.toDtoList(clientCreditAccountModel.getBalanceModelList()));
        clientCreditAccount.setExpiryDate(clientCreditAccountModel.getExpiryDate());

        return clientCreditAccount;
    }
}
