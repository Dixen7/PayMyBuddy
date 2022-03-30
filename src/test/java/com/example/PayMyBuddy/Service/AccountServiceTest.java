package com.example.PayMyBuddy.Service;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.example.PayMyBuddy.model.Account;
import com.example.PayMyBuddy.model.BankAccount;
import com.example.PayMyBuddy.model.Role;
import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.repository.AccountRepository;
import com.example.PayMyBuddy.service.AccountService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    private AccountRepository accountRepository;
    private AccountService accountService;


    @BeforeEach
    void setup() {
        accountRepository = mock(AccountRepository.class);
        accountService = new AccountService(accountRepository);
    }

    @Test
    void testNewAccountCreate() {

        User user = new User();
        user.setId(1L);
        user.setEmail("user@gmail.com");
        user.setPassword("user");

        String result = accountService.save(user);

        assertThat(result).isEqualToIgnoringCase("success");
    }

}