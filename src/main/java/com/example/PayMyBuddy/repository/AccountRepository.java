package com.example.PayMyBuddy.repository;

import com.example.PayMyBuddy.model.Account;
import com.example.PayMyBuddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByUser(User user);

}