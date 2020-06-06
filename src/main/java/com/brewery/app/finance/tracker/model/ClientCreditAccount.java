package com.brewery.app.finance.tracker.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ClientCreditAccount {

    private String accountId;
    private String accountName;
    private String imageUrl;
    private Enums.ClientAccountType accountType;
    private List<Balance> balanceList;
    private LocalDate expiryDate;
    private int maxBalanceRecord;
}
