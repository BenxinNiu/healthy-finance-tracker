package com.brewery.app.finance.tracker.transformer;

import com.brewery.app.finance.tracker.codegen.model.Balance;
import com.brewery.app.finance.tracker.model.BalanceModel;
import com.brewery.app.finance.tracker.model.Enums;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BalanceTransformer {

    public List<BalanceModel> toModelList(List<Balance> balanceList) {
        return balanceList.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    public BalanceModel toModel(Balance balance) {
        return BalanceModel.builder()
                .amount(balance.getAmount())
                .lastUpdated(LocalDateTime.now())
                .balanceType(Enums.toEnum(Enums.BalanceType.class, balance.getType().toString()))
                .build();
    }

    public List<Balance> toDtoList(List<BalanceModel> balanceModelList) {
        return balanceModelList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private Balance toDto(BalanceModel balanceModel) {
        Balance balance = new Balance();
        balance.setAmount(balanceModel.getAmount());
        balance.setLastUpdated(balanceModel.getLastUpdated().toString());
        balance.setType(Enums.toEnum(Balance.TypeEnum.class, balanceModel.getBalanceType().toString()));

        return balance;
    }
}
