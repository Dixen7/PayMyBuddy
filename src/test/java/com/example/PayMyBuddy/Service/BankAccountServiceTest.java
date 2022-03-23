package com.example.PayMyBuddy.Service;

import com.example.PayMyBuddy.model.BankAccount;
import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.model.dto.BankAccountDto;
import com.example.PayMyBuddy.repository.BankAccountRepository;
import com.example.PayMyBuddy.repository.UserRepository;
import com.example.PayMyBuddy.service.BankAccountService;
import com.example.PayMyBuddy.service.Interface.UserServiceInterface;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BankAccountServiceTest {

    private static BankAccountRepository bankAccountRepository;
    private static UserServiceInterface userServiceI;
    private static UserRepository userRepository;
    private static BankAccountService bankAccountService;

    @BeforeAll
    private static void setup() {
        bankAccountRepository = mock(BankAccountRepository.class);
        userServiceI = mock(UserServiceInterface.class);
        userRepository = mock(UserRepository.class);
        bankAccountService = new BankAccountService(bankAccountRepository, userServiceI, userRepository);
    }

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
