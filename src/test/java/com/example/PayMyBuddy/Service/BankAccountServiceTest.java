package com.example.PayMyBuddy.Service;

import com.example.PayMyBuddy.model.BankAccount;
import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.model.dto.BankAccountDto;
import com.example.PayMyBuddy.repository.BankAccountRepository;
import com.example.PayMyBuddy.repository.UserRepository;
import com.example.PayMyBuddy.service.BankAccountService;
import com.example.PayMyBuddy.service.Interface.UserServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
@WebMvcTest(BankAccountService.class)
@ExtendWith(MockitoExtension.class)
public class BankAccountServiceTest {

    @MockBean
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    @MockBean
    BankAccountRepository bankAccountRepository;

    @MockBean
    UserServiceInterface userServiceI;

    @MockBean
    UserRepository userRepository;

    @Autowired
    BankAccountService bankAccountService;

    @Test
    void testNewBankAccountSetSuccess() {

        BankAccountDto bankAccountDto = new BankAccountDto();
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber("100s85s6669800000");
        bankAccount.setBankAccountId(1L);
        bankAccount.setBic("psstrf520pptd");
        bankAccount.setHolder("user user");
        bankAccount.setIban("100s85s6669800000");

        bankAccountDto.setAccountNumber("100s85s6669800000");
        bankAccountDto.setBankAccountId(1L);
        bankAccountDto.setBic("psstrf520pptd");
        bankAccountDto.setEmail("user@gmail.com");
        bankAccountDto.setHolder("user user");
        bankAccountDto.setIban("100s85s6669800000");

        User user = new User();
        user.setEmail("user@gmail.com");
        user.setId(1L);

        User userSave = new User();
        userSave.setEmail("user@gmail.com");
        userSave.setId(1L);

        when(bankAccountRepository.save(bankAccount)).thenReturn(bankAccount);
        when(userServiceI.findOne(bankAccountDto.getEmail())).thenReturn(user);
        when(userRepository.getOne(user.getId())).thenReturn(userSave);
        when(userRepository.save(userSave)).thenReturn(userSave);

        String result = bankAccountService.save(bankAccountDto);
        assertThat(result).isEqualToIgnoringCase("success");

        BankAccount bankA = userSave.getBankAccount();

        assertThat(bankA.getHolder()).isEqualToIgnoringCase("user user");

    }
}
