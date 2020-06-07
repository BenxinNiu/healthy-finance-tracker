package com.brewery.app.finance.tracker.service;

import com.brewery.app.finance.tracker.codegen.model.ClientProfile;
import com.brewery.app.finance.tracker.model.BalanceModel;
import com.brewery.app.finance.tracker.model.ClientCreditAccountModel;
import com.brewery.app.finance.tracker.model.ClientProfileModel;
import com.brewery.app.finance.tracker.model.Enums;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BalanceCalculationService {

    public void updateFinancialSummary(ClientProfile clientProfile, ClientProfileModel clientProfileModel) {
        clientProfile.setTotalDebt(this.calculateTotalDebt(clientProfileModel.getClientCreditAccountModelList()));
        clientProfile.setTotalAsset(this.calculateTotalAsset(clientProfileModel.getClientCreditAccountModelList()));
        BigDecimal totalDeficit = clientProfile.getTotalAsset().subtract(clientProfile.getTotalDebt());
        clientProfile.setTotalDeficit(BigDecimal.ZERO);
        if (totalDeficit.compareTo(BigDecimal.ZERO) < 0) {
            clientProfile.setTotalDeficit(totalDeficit.abs());
        }
    }

    ClientProfileModel updateClientProfileBalanceHistory(ClientProfileModel profile) {
        BigDecimal newTotalAsset = calculateTotalAsset(profile.getClientCreditAccountModelList());
        BigDecimal newTotalDebt = calculateTotalDebt(profile.getClientCreditAccountModelList());
        // Add new total debt
        profile.getTotalDebtHistory().add(BalanceModel.builder()
                .balanceType(Enums.BalanceType.DEBT)
                .lastUpdated(LocalDateTime.now())
                .amount(newTotalDebt).build());
        // Add new total asset
        profile.getTotalAssetHistory().add(BalanceModel.builder()
                .balanceType(Enums.BalanceType.ASSET)
                .lastUpdated(LocalDateTime.now())
                .amount(newTotalAsset).build());
        return profile;
    }

    protected BigDecimal calculateTotalAsset(List<ClientCreditAccountModel> accountModelList) {
        return accountModelList.stream()
                .filter(account -> account.getBalanceModelList() != null && account.getBalanceModelList().size() > 0)
                .filter(account -> getTheLatestBalance(account.getBalanceModelList()).getBalanceType() == Enums.BalanceType.ASSET)
                .map(account -> getTheLatestBalance(account.getBalanceModelList()).getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    protected BigDecimal calculateTotalDebt(List<ClientCreditAccountModel> accountModelList) {
        return accountModelList.stream()
                .filter(account -> account.getBalanceModelList() != null && account.getBalanceModelList().size() > 0)
                .filter(account -> getTheLatestBalance(account.getBalanceModelList()).getBalanceType() == Enums.BalanceType.DEBT)
                .map(account -> getTheLatestBalance(account.getBalanceModelList()).getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BalanceModel getTheLatestBalance(List<BalanceModel> list) {
        return list != null
                ? list.get(list.size() - 1)
                : null;
    }
}
