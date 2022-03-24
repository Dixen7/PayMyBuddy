package com.example.PayMyBuddy.Integration;

import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;

import com.example.PayMyBuddy.model.Account;
import com.example.PayMyBuddy.model.Transaction;
import com.example.PayMyBuddy.model.Type;
import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.repository.AccountRepository;
import com.example.PayMyBuddy.repository.TransactionRepository;
import com.example.PayMyBuddy.repository.UserRepository;
import com.example.PayMyBuddy.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionIT {

    @Autowired
    TransactionService transactionService;

    @Autowired
    TransactionRepository transactionRpository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
    @WithMockUser(username = "admin@gmail.com")
    void testFindAllTransfer() {

        User user = userRepository.findByEmail("admin@gmail.com");
        Account account = accountRepository.findByUser(user);

        Iterable<Transaction> transactions = transactionService.findAllBySenderIdAndType(account, Type.USER_TO_USER);

        assertThat(transactions.toString()).contains("coucou");
    }

}