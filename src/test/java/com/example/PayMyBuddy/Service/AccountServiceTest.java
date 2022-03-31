package com.example.PayMyBuddy.Service;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.repository.AccountRepository;
import com.example.PayMyBuddy.service.AccountService;
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