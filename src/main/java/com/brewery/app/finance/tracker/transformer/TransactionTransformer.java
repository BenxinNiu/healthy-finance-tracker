package com.brewery.app.finance.tracker.transformer;

import com.brewery.app.finance.tracker.codegen.model.Transaction;
import com.brewery.app.finance.tracker.model.Enums;
import com.brewery.app.finance.tracker.model.TransactionModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionTransformer {

    public List<Transaction> toDtoList(List<TransactionModel> transactionModelList) {
        return transactionModelList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Transaction toDto(TransactionModel transactionModel) {
        Transaction transaction = new Transaction();
        transaction.setId(null);
        transaction.setCreditAccountId(transactionModel.getCreditAccountId());
        transaction.setPostedDate(transactionModel.getPostedTime().toString());
        transaction.setAmount(transactionModel.getAmount());
        transaction.setType(Enums.toEnum(Transaction.TypeEnum.class, transactionModel.getType().toString()));
        transaction.setCategory(transactionModel.getCategory());
        transaction.setPurpose(transactionModel.getPurpose());
        transaction.setIsEssential(transactionModel.isEssential());
        transaction.setIsUnexpected(transactionModel.isUnexpected());
        transaction.setNotes(transactionModel.getNotes());

        return transaction;
    }
}
