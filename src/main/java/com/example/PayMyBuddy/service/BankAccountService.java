package com.example.PayMyBuddy.service;

import com.example.PayMyBuddy.model.BankAccount;
import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.model.dto.BankAccountDto;
import com.example.PayMyBuddy.repository.BankAccountRepository;
import com.example.PayMyBuddy.repository.UserRepository;
import com.example.PayMyBuddy.service.Interface.BankAccountServiceInterface;
import com.example.PayMyBuddy.service.Interface.UserServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BankAccountService implements BankAccountServiceInterface {

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    UserServiceInterface userBuddyServiceI;

    @Autowired
    UserRepository userBuddyRepository;

    @Override
    public String save(BankAccountDto bankAccountDto) {
        BankAccount bankAccount = new BankAccount();

        bankAccount.setAccountNumber(bankAccountDto.getAccountNumber());
        bankAccount.setIban(bankAccountDto.getIban());
        bankAccount.setBic(bankAccountDto.getBic());
        bankAccount.setHolder(bankAccountDto.getHolder());

        log.debug("bankAccount" + bankAccount);

        bankAccountRepository.save(bankAccount);

        User user = userBuddyServiceI.findOne(bankAccountDto.getEmail());
        Long id = user.getId();

        // add bank account to entity user
        User userToUpdate = userBuddyRepository.getOne(id);
        log.debug("userToUpdate" + userToUpdate);
        userToUpdate.setBankAccount(bankAccount);
        userBuddyRepository.save(userToUpdate);
        return "success";
    }
}
