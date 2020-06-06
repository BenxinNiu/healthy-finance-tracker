package com.brewery.app.finance.tracker.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class Balance {
    private String clientId;
    private String accountId;
    private BigDecimal amount;
    private LocalDate lastUpdated;
    private Enums.BalanceType balanceType;
}
