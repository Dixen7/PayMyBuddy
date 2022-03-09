package com.example.PayMyBuddy.service.Interface;

import com.example.PayMyBuddy.model.Account;
import com.example.PayMyBuddy.model.dto.TransactionDto;
import com.example.PayMyBuddy.model.Transaction;
import com.example.PayMyBuddy.model.Type;

public interface TransactionServiceInterface {

    Iterable<Transaction> findAllBySenderId(Account account);
    String save(TransactionDto transactionDto);
    Iterable<Transaction> findAllBySenderIdAndType(Account account, Type userToUser);
}
