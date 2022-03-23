package com.example.PayMyBuddy.Service;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import com.example.PayMyBuddy.model.Account;
import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.repository.AccountRepository;
import com.example.PayMyBuddy.service.AccountService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    private static AccountRepository accountRepository;
    private static AccountService accountService;
    Account account2 = new Account();

    @BeforeAll
    private static void setup() {
        accountRepository = mock(AccountRepository.class);
        accountService = new AccountService(accountRepository);
    }

    @Test
    void testNewAccountSuccess() {

        Account account = new Account();
        User user = new User();
        user.setId(1L);
        user.setEmail("user@gmail.com");
        user.setPassword("user");

        account2.setAccountId(1L);
        account2.setBalance(new BigDecimal("0"));
        account2.setUser(user);

        when(accountRepository.save(account)).thenReturn(account2);

        String result = accountService.save(user);

        assertThat(account2.getUser()).isEqualTo(user);
        assertThat(result).isEqualToIgnoringCase("success");

    }

}