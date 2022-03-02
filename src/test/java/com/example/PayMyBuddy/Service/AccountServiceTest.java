package com.example.PayMyBuddy.Service;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import com.example.PayMyBuddy.model.Account;
import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.repository.AccountRepository;
import com.example.PayMyBuddy.service.AccountService;
import com.example.PayMyBuddy.service.Interface.UserServiceInterface;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebMvcTest(AccountService.class)
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    AccountRepository accountRepository;

    @MockBean
    UserServiceInterface userBuddyServiceInterface;

    @Autowired
    AccountService accountService;

    Account account2 = new Account();

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