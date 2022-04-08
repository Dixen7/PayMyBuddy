package com.example.PayMyBuddy.service.Interface;

import com.example.PayMyBuddy.model.Account;
import com.example.PayMyBuddy.model.dto.TransactionDto;
import com.example.PayMyBuddy.model.Transaction;
import com.example.PayMyBuddy.model.Type;

public interface TransactionServiceInterface {

    String save(TransactionDto transactionDto) throws Exception;
    Iterable<Transaction> findAllBySenderIdAndType(Account account, Type userToUser);
}
