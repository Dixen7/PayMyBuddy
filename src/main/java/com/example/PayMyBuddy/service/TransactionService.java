package com.example.PayMyBuddy.service;

import com.example.PayMyBuddy.constant.TransactionConstant;
import com.example.PayMyBuddy.model.Account;
import com.example.PayMyBuddy.model.dto.TransactionDto;
import com.example.PayMyBuddy.model.Transaction;
import com.example.PayMyBuddy.model.Type;
import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.repository.AccountRepository;
import com.example.PayMyBuddy.repository.TransactionRepository;
import com.example.PayMyBuddy.service.Interface.AccountServiceInterface;
import com.example.PayMyBuddy.service.Interface.BankPaymentInterface;
import com.example.PayMyBuddy.service.Interface.TransactionServiceInterface;
import com.example.PayMyBuddy.service.Interface.UserServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
@Slf4j
public class TransactionService implements TransactionServiceInterface {

    private TransactionRepository transactionRepository;
    private UserServiceInterface userServiceInterface;
    private AccountServiceInterface accountServiceInterface;
    private BankPaymentInterface bankPaymentInterface;
    private AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, UserServiceInterface userServiceInterface, AccountServiceInterface accountServiceInterface, BankPaymentInterface bankPaymentInterface, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.userServiceInterface = userServiceInterface;
        this.accountServiceInterface = accountServiceInterface;
        this.bankPaymentInterface = bankPaymentInterface;
        this.accountRepository = accountRepository;
    }

    @Override
    public Iterable<Transaction> findAllBySenderId(Account account) {
        return transactionRepository.findAllBySenderId(account);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(TransactionDto transactionDto)  throws Exception {

        Transaction transactionPayment = new Transaction();
        Account account = transactionDto.getSenderId();
        transactionPayment.setAmount(transactionDto.getAmount());
        transactionPayment.setDateTransaction(LocalDate.now());
        transactionPayment.setDescription(transactionDto.getDescription());
        transactionPayment.setType(transactionDto.getType());
        transactionPayment.setSenderId(account);
        Account accountB = new Account();

        if (transactionDto.getType() == Type.USER_TO_USER || transactionDto.getType() == Type.TO_ADMIN_ACCOUNT) {

            String email = transactionDto.getMailBeneficiary();
            User userB = userServiceInterface.findOne(email);

            if(!userB.isActive()) {
                return "inactive";
            }
            accountB = accountServiceInterface.findByUserAccountId(userB);

            transactionPayment.setBeneficiaryId(accountB);
            transactionPayment.setFee(transactionDto.getAmount().multiply(new BigDecimal(TransactionConstant.FEE)).setScale(2, RoundingMode.HALF_UP));
            log.debug("frais : " + transactionPayment.getFee());

            if (transactionDto.getType().equals(Type.USER_TO_USER) && account.getBalance().compareTo(transactionDto.getAmount().add(transactionPayment.getFee())) < 0) {
                return "errorNotEnoughMoney";
            }

        } else {

            if (transactionDto.getDescription().equalsIgnoreCase("withdraw") && account.getBalance().compareTo(transactionDto.getAmount()) < 0) {
                return "errorNotEnoughMoney";
            }
            transactionPayment.setType(Type.BANK_TRANSFER);
            transactionPayment.setBeneficiaryId(account);
            transactionPayment.setFee(new BigDecimal("0"));
        }

        if (bankPaymentInterface.requestAuthorization(transactionPayment)) {

            transactionRepository.save(transactionPayment);

            Account accountToUpdate = accountRepository.getOne(account.getAccountId());

            if (transactionPayment.getDescription().equalsIgnoreCase("withdraw")) {
                accountToUpdate.setBalance(account.getBalance().subtract(transactionPayment.getAmount()));

            } else if (transactionPayment.getDescription().equalsIgnoreCase("payment")) {
                accountToUpdate.setBalance(account.getBalance().add(transactionPayment.getAmount()));

            } else {
                accountToUpdate.setBalance(account.getBalance().subtract(transactionPayment.getAmount()));
                accountToUpdate.setBalance(account.getBalance().subtract(transactionPayment.getFee()));
                Account accountBeneficiary = accountRepository.getOne(accountB.getAccountId());
                accountBeneficiary.setBalance(accountB.getBalance().add(transactionPayment.getAmount()));
                accountRepository.save(accountBeneficiary);
            }
            accountRepository.save(accountToUpdate);
            return "success";
        } else {
            return "error";
        }
    }

    @Override
    public Iterable<Transaction> findAllBySenderIdAndType(Account account, Type userToUser) {
        return transactionRepository.findAllBySenderIdAndType(account, userToUser);

    }

}