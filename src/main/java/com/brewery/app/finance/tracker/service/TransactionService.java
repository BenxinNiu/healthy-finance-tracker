package com.brewery.app.finance.tracker.service;

import com.brewery.app.finance.tracker.codegen.model.ClientProfile;
import com.brewery.app.finance.tracker.codegen.model.Transaction;
import com.brewery.app.finance.tracker.model.*;
import com.brewery.app.finance.tracker.repository.TransactionModelRepository;
import com.brewery.app.finance.tracker.transformer.TransactionTransformer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionTransformer transactionTransformer;
    private final ClientProfileService clientProfileService;
    private final TransactionModelRepository transactionModelRepository;
    private final BalanceCalculationService balanceCalculationService;

    public Flux<TransactionModel> saveTransactionModels(List<TransactionModel> transactionList) {
        return transactionModelRepository.saveAll(transactionList);
    }

    public Mono<ClientProfile> saveTransactionsAndUpdateProfile(String clientId, List<Transaction> transactionList) {
        List<TransactionModel> transactionModels = transactionTransformer.toModelList(transactionList);
        return clientProfileService.getClientProfileModelById(clientId)
                .flatMap(profile -> updateClientProfileModel(profile, transactionModels))
                .flatMap(clientProfileService::saveClientProfile);
    }

    private Mono<ClientProfileModel> updateClientProfileModel(ClientProfileModel profile, List<TransactionModel> transactionModels) {

        Map<String, ClientCreditAccountModel> accountModelMap = profile.getClientCreditAccountModelList().stream()
                .collect(Collectors.toMap(ClientCreditAccountModel::getAccountId, Function.identity()));

        return saveTransactionModels(transactionModels)
                .map(transaction -> updateAccountBalanceHistory(accountModelMap, transaction))
                .collectList()
                .map(list -> profile)
                .map(balanceCalculationService::updateClientProfileBalanceHistory);
    }


    private TransactionModel updateAccountBalanceHistory(Map<String, ClientCreditAccountModel> accountModelMap, TransactionModel transaction) {
        ClientCreditAccountModel account = accountModelMap.get(transaction.getCreditAccountId());
        BalanceModel currentBalance = balanceCalculationService.getTheLatestBalance(account.getBalanceModelList());
        BalanceModel newBalance = BalanceModel.builder()
                .lastUpdated(LocalDateTime.now())
                .balanceType(getBalanceType(account))
                .build();

        if (shouldSubtract(transaction, account)) {
            newBalance.setAmount(currentBalance.getAmount().subtract(transaction.getAmount()));
        } else {
            newBalance.setAmount(currentBalance.getAmount().add(transaction.getAmount()));
        }
        account.getBalanceModelList().add(newBalance);
        return transaction;
    }

    private Enums.BalanceType getBalanceType(ClientCreditAccountModel account) {
        return isCreditAccount(account)
                ? Enums.BalanceType.DEBT
                : Enums.BalanceType.ASSET;
    }

    private boolean shouldSubtract(TransactionModel transaction, ClientCreditAccountModel account) {
        return (transaction.getType() == Enums.TransactionType.DEPOSIT && isCreditAccount(account)) ||
                (transaction.getType() == Enums.TransactionType.EXPENSE && !isCreditAccount(account));
    }

    private boolean isCreditAccount(ClientCreditAccountModel account) {
        switch (account.getAccountType()) {
            case AMEX:
            case VISA:
            case MASTERCARD:
                return true;
            default:
                return false;
        }
    }

}