package com.example.PayMyBuddy.Service;

import com.example.PayMyBuddy.model.Account;
import com.example.PayMyBuddy.model.Transaction;
import com.example.PayMyBuddy.model.Type;
import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.model.dto.TransactionDto;
import com.example.PayMyBuddy.repository.AccountRepository;
import com.example.PayMyBuddy.repository.TransactionRepository;
import com.example.PayMyBuddy.service.Interface.AccountServiceInterface;
import com.example.PayMyBuddy.service.Interface.BankPaymentInterface;
import com.example.PayMyBuddy.service.Interface.UserServiceInterface;
import com.example.PayMyBuddy.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    private UserServiceInterface userServiceI;
    private AccountServiceInterface accountServiceI;
    private BankPaymentInterface bankPaymentI;
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;
    private TransactionService transactionService;

    @BeforeEach
    void setup() {
        accountRepository = mock(AccountRepository.class);
        bankPaymentI = mock(BankPaymentInterface.class);
        accountServiceI = mock(AccountServiceInterface.class);
        userServiceI = mock(UserServiceInterface.class);
        transactionRepository = mock(TransactionRepository.class);
        transactionService = new TransactionService(transactionRepository, userServiceI, accountServiceI, bankPaymentI, accountRepository);
    }

    @Test
    void testTransactionUserToUserWhenRequestAuthorizationIsFalse() throws Exception {

        Account account = new Account();
        account.setBalance(new BigDecimal("105"));
        account.setAccountId(1);

        User user = new User();
        user.setEmail("user1@gmail.com");
        account.setUser(user);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(new BigDecimal("100"));
        transactionDto.setType(Type.USER_TO_USER);
        transactionDto.setDescription("remboursement");
        transactionDto.setSenderId(account);
        transactionDto.setMailBeneficiary("user2@gmail.com");

        User otherUser = new User();
        otherUser.setEmail("user2@gmail.com");

        Account otherAccount = new Account();
        otherAccount.setBalance(new BigDecimal("100"));

        when(bankPaymentI.requestAuthorization(Mockito.any(Transaction.class))).thenReturn(false);
        when(userServiceI.findOne("user2@gmail.com")).thenReturn(otherUser);

        String response = transactionService.save(transactionDto);

        assertThat(response).isEqualToIgnoringCase("error");
        assertThat(otherAccount.getBalance()).isEqualTo(new BigDecimal("100"));
        assertThat(account.getBalance()).isEqualTo(new BigDecimal("105"));
    }

    @Test
    void testTransactionUserToUserInSuccessAccountBalanceAndFeeOk() throws Exception {

        Account account = new Account();
        account.setBalance(new BigDecimal("105"));
        account.setAccountId(1);

        User user = new User();
        user.setEmail("user1@gmail.com");
        account.setUser(user);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(new BigDecimal("100"));
        transactionDto.setType(Type.USER_TO_USER);
        transactionDto.setDescription("remboursement");
        transactionDto.setSenderId(account);
        transactionDto.setMailBeneficiary("user2@gmail.com");

        User otherUser = new User();
        otherUser.setEmail("user2@gmail.com");

        Account otherAccount = new Account();
        otherAccount.setBalance(new BigDecimal("100"));

        when(bankPaymentI.requestAuthorization(Mockito.any(Transaction.class))).thenReturn(true);
        when(userServiceI.findOne("user2@gmail.com")).thenReturn(otherUser);
        when(accountServiceI.findByUserAccountId(otherUser)).thenReturn(otherAccount);
        when(accountRepository.getOne(account.getAccountId())).thenReturn(account);
        when(accountRepository.getOne(otherAccount.getAccountId())).thenReturn(otherAccount);

        String response = transactionService.save(transactionDto);

        assertThat(response).isEqualToIgnoringCase("success");
        assertThat(otherAccount.getBalance()).isEqualTo(new BigDecimal("200"));
        assertThat(account.getBalance()).isEqualTo(new BigDecimal("4.50"));
    }

    @Test
    void testTransactionUserToUserNotEnoughMoney() throws Exception {

        Account account = new Account();
        account.setBalance(new BigDecimal("100"));
        account.setAccountId(1);

        User user = new User();
        user.setEmail("user1@gmail.com");
        account.setUser(user);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(new BigDecimal("100"));
        transactionDto.setType(Type.USER_TO_USER);
        transactionDto.setDescription("remboursement");
        transactionDto.setSenderId(account);
        transactionDto.setMailBeneficiary("user2@gmail.com");

        User otherUser = new User();
        otherUser.setEmail("user2@gmail.com");

        Account otherAccount = new Account();
        otherAccount.setBalance(new BigDecimal("100"));

        when(bankPaymentI.requestAuthorization(Mockito.any(Transaction.class))).thenReturn(true);
        when(userServiceI.findOne("user2@gmail.com")).thenReturn(otherUser);

        String response = transactionService.save(transactionDto);

        assertThat(response).isEqualToIgnoringCase("errorNotEnoughMoney");
        assertThat(account.getBalance()).isEqualTo(new BigDecimal("100"));
    }

    @Test
    void testTransactionBankPaymentBalanceUpdateOk() throws Exception {

        Account account = new Account();
        account.setBalance(new BigDecimal("100"));
        account.setAccountId(1);

        User user = new User();
        user.setEmail("user1@gmail.com");
        account.setUser(user);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(new BigDecimal("100"));
        transactionDto.setDescription("payment");
        transactionDto.setSenderId(account);

        when(bankPaymentI.requestAuthorization(Mockito.any(Transaction.class))).thenReturn(true);
        when(accountRepository.getOne(account.getAccountId())).thenReturn(account);

        String response = transactionService.save(transactionDto);

        assertThat(response).isEqualToIgnoringCase("success");
        assertThat(account.getBalance()).isEqualTo(new BigDecimal("200"));

    }

    @Test
    void testTransactionBankWithraw() throws Exception {

        Account account = new Account();
        account.setBalance(new BigDecimal("100"));
        account.setAccountId(1);

        User user = new User();
        user.setEmail("user1@gmail.com");
        account.setUser(user);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(new BigDecimal("100"));
        transactionDto.setDescription("withdraw");
        transactionDto.setSenderId(account);

        when(bankPaymentI.requestAuthorization(Mockito.any(Transaction.class))).thenReturn(true);
        when(accountRepository.getOne(account.getAccountId())).thenReturn(account);

        String response = transactionService.save(transactionDto);

        assertThat(response).isEqualToIgnoringCase("success");
        assertThat(account.getBalance()).isEqualTo(new BigDecimal("0"));

    }

    @Test
    void testTransactionBankWithrawNotEnoughMoney() throws Exception {

        Account account = new Account();
        account.setBalance(new BigDecimal("100"));
        account.setAccountId(1);
        User user = new User();
        user.setEmail("user1@gmail.com");
        account.setUser(user);

        TransactionDto transac = new TransactionDto();
        transac.setAmount(new BigDecimal("105"));
        transac.setDescription("withdraw");
        transac.setSenderId(account);

        when(bankPaymentI.requestAuthorization(Mockito.any(Transaction.class))).thenReturn(true);
        String response = transactionService.save(transac);

        assertThat(response).isEqualToIgnoringCase("errorNotEnoughMoney");
        assertThat(account.getBalance()).isEqualTo(new BigDecimal("100"));

    }

}
