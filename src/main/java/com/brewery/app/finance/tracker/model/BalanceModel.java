package com.brewery.app.finance.tracker.model;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Setter
public class BalanceModel {
//    private String accountId;
    private BigDecimal amount;
    private LocalDateTime lastUpdated;
    private Enums.BalanceType balanceType;
}
