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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

        TransactionDto transac = new TransactionDto();
        transac.setAmount(new BigDecimal("100"));
        transac.setType(Type.USER_TO_USER);
        transac.setDescription("remboursement");
        transac.setSenderId(account);
        transac.setMailBeneficiary("user2@gmail.com");

        User userB = new User();
        userB.setEmail("user2@gmail.com");

        Account accountB = new Account();
        accountB.setBalance(new BigDecimal("100"));

        when(userServiceI.findOne("user1@gmail.com")).thenReturn(user);
        when(accountServiceI.findByUserAccountId(user)).thenReturn(account);
        when(userServiceI.findOne("user2@gmail.com")).thenReturn(userB);
        when(accountServiceI.findByUserAccountId(userB)).thenReturn(accountB);
        when(bankPaymentI.requestAuthorization(Mockito.any(Transaction.class))).thenReturn(false);
        when(accountRepository.getOne(account.getAccountId())).thenReturn(account);
        when(accountRepository.getOne(accountB.getAccountId())).thenReturn(accountB);

        String reponse = transactionService.save(transac);

        assertThat(reponse).isEqualToIgnoringCase("error");
        assertThat(accountB.getBalance()).isEqualTo(new BigDecimal("100"));
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

        TransactionDto transac = new TransactionDto();
        transac.setAmount(new BigDecimal("100"));
        transac.setType(Type.USER_TO_USER);
        transac.setDescription("remboursement");
        transac.setSenderId(account);
        transac.setMailBeneficiary("user2@gmail.com");

        User userB = new User();
        userB.setEmail("user2@gmail.com");

        Account accountB = new Account();
        accountB.setBalance(new BigDecimal("100"));

        when(userServiceI.findOne("user1@gmail.com")).thenReturn(user);
        when(accountServiceI.findByUserAccountId(user)).thenReturn(account);
        when(userServiceI.findOne("user2@gmail.com")).thenReturn(userB);
        when(accountServiceI.findByUserAccountId(userB)).thenReturn(accountB);
        when(bankPaymentI.requestAuthorization(Mockito.any(Transaction.class))).thenReturn(true);
        when(accountRepository.getOne(account.getAccountId())).thenReturn(account);
        when(accountRepository.getOne(accountB.getAccountId())).thenReturn(accountB);

        String reponse = transactionService.save(transac);

        assertThat(reponse).isEqualToIgnoringCase("success");
        assertThat(accountB.getBalance()).isEqualTo(new BigDecimal("200"));
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

        TransactionDto transac = new TransactionDto();
        transac.setAmount(new BigDecimal("100"));
        transac.setType(Type.USER_TO_USER);
        transac.setDescription("remboursement");
        transac.setSenderId(account);
        transac.setMailBeneficiary("user2@gmail.com");

        User userB = new User();
        userB.setEmail("user2@gmail.com");

        Account accountB = new Account();
        accountB.setBalance(new BigDecimal("100"));

        when(userServiceI.findOne("user1@gmail.com")).thenReturn(user);
        when(accountServiceI.findByUserAccountId(user)).thenReturn(account);
        when(userServiceI.findOne("user2@gmail.com")).thenReturn(userB);
        when(accountServiceI.findByUserAccountId(userB)).thenReturn(accountB);
        when(bankPaymentI.requestAuthorization(Mockito.any(Transaction.class))).thenReturn(true);
        when(accountRepository.getOne(account.getAccountId())).thenReturn(account);
        when(accountRepository.getOne(accountB.getAccountId())).thenReturn(accountB);

        String reponse = transactionService.save(transac);

        assertThat(reponse).isEqualToIgnoringCase("errorNotEnoughMoney");
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

        TransactionDto transac = new TransactionDto();
        transac.setAmount(new BigDecimal("100"));
        transac.setDescription("payment");
        transac.setSenderId(account);


        when(userServiceI.findOne("user1@gmail.com")).thenReturn(user);
        when(accountServiceI.findByUserAccountId(user)).thenReturn(account);
        when(bankPaymentI.requestAuthorization(Mockito.any(Transaction.class))).thenReturn(true);
        when(accountRepository.getOne(account.getAccountId())).thenReturn(account);

        String reponse = transactionService.save(transac);

        assertThat(reponse).isEqualToIgnoringCase("success");
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

        TransactionDto transac = new TransactionDto();
        transac.setAmount(new BigDecimal("100"));
        transac.setDescription("withdraw");
        transac.setSenderId(account);


        when(userServiceI.findOne("user1@gmail.com")).thenReturn(user);
        when(accountServiceI.findByUserAccountId(user)).thenReturn(account);
        when(bankPaymentI.requestAuthorization(Mockito.any(Transaction.class))).thenReturn(true);
        when(accountRepository.getOne(account.getAccountId())).thenReturn(account);

        String reponse = transactionService.save(transac);

        assertThat(reponse).isEqualToIgnoringCase("success");
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


        when(userServiceI.findOne("user1@gmail.com")).thenReturn(user);
        when(accountServiceI.findByUserAccountId(user)).thenReturn(account);
        when(bankPaymentI.requestAuthorization(Mockito.any(Transaction.class))).thenReturn(true);
        when(accountRepository.getOne(account.getAccountId())).thenReturn(account);

        String reponse = transactionService.save(transac);

        assertThat(reponse).isEqualToIgnoringCase("errorNotEnoughMoney");
        assertThat(account.getBalance()).isEqualTo(new BigDecimal("100"));

    }

}
