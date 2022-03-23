package com.example.PayMyBuddy.repository;

import com.example.PayMyBuddy.model.Account;
import com.example.PayMyBuddy.model.Transaction;
import com.example.PayMyBuddy.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Iterable<Transaction> findAllBySenderId(Account account);
    Iterable<Transaction> findAllBySenderIdAndType(Account account, Type userToUser);

}
