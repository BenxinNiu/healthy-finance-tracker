package com.brewery.app.finance.tracker.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ClientCreditAccountModel {

    private String accountId;
    private String accountName;
    private String imageUrl;
    private Enums.ClientAccountType accountType;
    private List<BalanceModel> balanceModelList;
    private String expiryDate;
    private int maxBalanceRecord;
}
