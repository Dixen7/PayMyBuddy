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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
@Slf4j
public class TransactionService implements TransactionServiceInterface {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserServiceInterface userServiceI;

    @Autowired
    AccountServiceInterface accountServiceI;

    BankPaymentInterface bankPaymentInterface;

    @Autowired
    AccountRepository accountRepository;

    @Override
    public Iterable<Transaction> findAllBySenderId(Account account) {
        return transactionRepository.findAllBySenderId(account);
    }

    @Override
    @Transactional
    public String save(TransactionDto transactionDto) {
        // preparing the entity to persist
        Transaction transactionPayment = new Transaction();

        Account account = transactionDto.getSenderId();
        // update transaction with Dto received
        transactionPayment.setAmount(transactionDto.getAmount());
        transactionPayment.setDateTransaction(LocalDate.now());
        transactionPayment.setDescription(transactionDto.getDescription());
        transactionPayment.setType(transactionDto.getType());
        transactionPayment.setSenderId(account);

        // preparing entity beneficiary
        Account accountB = new Account();

        // different type transaction
        if (transactionDto.getType() == Type.USER_TO_USER) {

            // recovery beneficiary mail and the user to find this account
            String email = transactionDto.getMailBeneficiary();
            User userB = userServiceI.findOne(email);
            // Verify if account beneficiary is active before transaction
            if(!userB.isActive()) {
                return "inactive";
            }
            accountB = accountServiceI.findByUserAccountId(userB);

            // update beneficiary account
            transactionPayment.setBeneficiaryId(accountB);

            transactionPayment
                    .setFee(transactionDto.getAmount().multiply(new BigDecimal(TransactionConstant.FEE)).setScale(2, RoundingMode.HALF_UP));
            log.debug("frais : " + transactionPayment.getFee());

            // verify amount and balance account before doing the transaction
            if (transactionDto.getType().equals(Type.USER_TO_USER) && account.getBalance()
                    .compareTo(transactionDto.getAmount().add(transactionPayment.getFee())) < 0) {
                return "errorNotEnoughMoney";
            }

        } else {
            // verify amount and balance account before doing the transaction
            if (transactionDto.getDescription().equalsIgnoreCase("withdraw")
                    && account.getBalance().compareTo(transactionDto.getAmount()) < 0) {
                return "errorNotEnoughMoney";
            }
            transactionPayment.setType(Type.BANK_TRANSFER);
            transactionPayment.setBeneficiaryId(account);
            transactionPayment.setFee(new BigDecimal("0"));
        }

        // TODO send requestAuthorization just for type with bank account and send money
        // to admin bank
        // bankPayment is an interface before real implementation link with bank of
        // users and admin webApp, just return true for now
        if (bankPaymentInterface.requestAuthorization(transactionPayment)) {

            // if bank return true save transaction else return error
            transactionRepository.save(transactionPayment);

            // update account balance for withdraw, payment and between user
            Account accountToUpdate = accountRepository.getOne(account.getAccountId());
            // for withdraw
            if (transactionPayment.getDescription().equalsIgnoreCase("withdraw")) {
                accountToUpdate.setBalance(account.getBalance().subtract(transactionPayment.getAmount()));
                // for payment
            } else if (transactionPayment.getDescription().equalsIgnoreCase("payment")) {
                accountToUpdate.setBalance(account.getBalance().add(transactionPayment.getAmount()));
                // for transfer between account user
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