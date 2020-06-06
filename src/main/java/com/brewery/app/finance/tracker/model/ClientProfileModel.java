package com.brewery.app.finance.tracker.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@Document(collection = "clientProfile")
public class ClientProfileModel {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private List<Balance> totalAssetHistory;
    private List<Balance> totalDebtHistory;
    private List<ClientCreditAccount> clientCreditAccountList;
    private LocalDate lastUpdated;
}
