package com.brewery.app.finance.tracker.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "transaction")
public class TransactionModel {
    @Id
    private String id;
    private String creditAccountId;
    private LocalDateTime postedTime;
    private BigDecimal amount;
    private Enums.TransactionType type;
    private String category;
    private String purpose;
    private String notes;
    private boolean isEssential;
    private boolean isUnexpected;

}
