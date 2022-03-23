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

    private BankAccountRepository bankAccountRepository;
    private UserServiceInterface userServiceI;
    private UserRepository userRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository, UserServiceInterface userServiceI, UserRepository userRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.userServiceI = userServiceI;
        this.userRepository = userRepository;
    }

    @Override
    public String save(BankAccountDto bankAccountDto) {
        BankAccount bankAccount = new BankAccount();

        bankAccount.setAccountNumber(bankAccountDto.getAccountNumber());
        bankAccount.setIban(bankAccountDto.getIban());
        bankAccount.setBic(bankAccountDto.getBic());
        bankAccount.setHolder(bankAccountDto.getHolder());

        log.debug("bankAccount" + bankAccount);

        bankAccountRepository.save(bankAccount);

        User user = userServiceI.findOne(bankAccountDto.getEmail());
        Long id = user.getId();

        // add bank account to entity user
        User userToUpdate = userRepository.getOne(id);
        log.debug("userToUpdate" + userToUpdate);
        userToUpdate.setBankAccount(bankAccount);
        userRepository.save(userToUpdate);
        return "success";
    }
}
