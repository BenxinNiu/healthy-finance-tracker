package com.brewery.app.finance.tracker.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class BalanceModel {
    private String accountId;
    private BigDecimal amount;
    private LocalDateTime lastUpdated;
    private Enums.BalanceType balanceType;
}
