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

        Transaction transactionPayment = setTransaction(transactionDto);
        Account accountSender = transactionPayment.getSenderId();
        String email = transactionDto.getMailBeneficiary();
        Account accountBeneficiary = new Account();
        User beneficiary = userServiceInterface.findOne(email);

        if (transactionPayment.getType() == Type.USER_TO_USER) {
            userToUserTransfer(transactionDto, transactionPayment,  accountSender, beneficiary);
        } else {
            bankTransfer(transactionDto, transactionPayment,  accountSender);
        }

        if (bankPaymentInterface.requestAuthorization(transactionPayment)) {
            transactionRepository.save(transactionPayment);
            updateAccountBalance(transactionPayment, accountSender, accountBeneficiary);
            return "success";
        } else {
            return "error";
        }
    }

    private String userToUserTransfer(TransactionDto transactionDto, Transaction transactionPayment,  Account accountSender, User beneficiary) {

        if(!beneficiary.isActive()) return "inactive";

        Account accountBeneficiary = accountServiceInterface.findByUserAccountId(beneficiary);
        transactionPayment.setBeneficiaryId(accountBeneficiary);
        transactionPayment.setFee(transactionDto.getAmount().multiply(new BigDecimal(TransactionConstant.FEE)).setScale(2, RoundingMode.HALF_UP));
        log.debug("frais : " + transactionPayment.getFee());

        if (transactionDto.getType().equals(Type.USER_TO_USER) && accountSender.getBalance().compareTo(transactionDto.getAmount().add(transactionPayment.getFee())) < 0) {
            return "errorNotEnoughMoney";
        }
        return "success";
    }

    private String bankTransfer(TransactionDto transactionDto, Transaction transactionPayment,  Account accountSender) {

        if (transactionDto.getDescription().equalsIgnoreCase("withdraw") && accountSender.getBalance().compareTo(transactionDto.getAmount()) < 0) {
            return "errorNotEnoughMoney";
        }

        transactionPayment.setType(Type.BANK_TRANSFER);
        transactionPayment.setBeneficiaryId(accountSender);
        transactionPayment.setFee(new BigDecimal("0"));

        return "success";
    }

    private void updateAccountBalance(Transaction transactionPayment,  Account accountSender, Account accountBeneficiary) {
        Account accountToUpdate = accountRepository.getOne(accountSender.getAccountId());
        if (transactionPayment.getDescription().equalsIgnoreCase("withdraw")) {
            accountToUpdate.setBalance(accountSender.getBalance().subtract(transactionPayment.getAmount()));
        } else if (transactionPayment.getDescription().equalsIgnoreCase("payment")) {
            accountToUpdate.setBalance(accountSender.getBalance().add(transactionPayment.getAmount()));
        } else {
            accountToUpdate.setBalance(accountSender.getBalance().subtract(transactionPayment.getAmount()));
            accountToUpdate.setBalance(accountSender.getBalance().subtract(transactionPayment.getFee()));

            accountBeneficiary = accountRepository.getOne(accountBeneficiary.getAccountId());
            accountBeneficiary.setBalance(accountBeneficiary.getBalance().add(transactionPayment.getAmount()));
            accountRepository.save(accountBeneficiary);
        }
        accountRepository.save(accountToUpdate);
    }

    private Transaction setTransaction(TransactionDto transactionDto) {
        Transaction transactionPayment = new Transaction();
        transactionPayment.setAmount(transactionDto.getAmount());
        transactionPayment.setDateTransaction(LocalDate.now());
        transactionPayment.setDescription(transactionDto.getDescription());
        transactionPayment.setType(transactionDto.getType());
        transactionPayment.setSenderId(transactionDto.getSenderId());
        return transactionPayment;
    }

    @Override
    public Iterable<Transaction> findAllBySenderIdAndType(Account account, Type userToUser) {
        return transactionRepository.findAllBySenderIdAndType(account, userToUser);

    }

}