package com.example.PayMyBuddy.controller;
import java.math.BigDecimal;
import java.util.Collection;

import com.example.PayMyBuddy.model.Account;
import com.example.PayMyBuddy.model.BankAccount;
import com.example.PayMyBuddy.model.Role;
import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.model.dto.BankAccountDto;
import com.example.PayMyBuddy.model.dto.TransactionDto;
import com.example.PayMyBuddy.service.Interface.AccountServiceInterface;
import com.example.PayMyBuddy.service.Interface.BankAccountServiceInterface;
import com.example.PayMyBuddy.service.Interface.TransactionServiceInterface;
import com.example.PayMyBuddy.service.Interface.UserServiceInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/home")
@Validated
public class HomeController {

    @Autowired
    UserServiceInterface userServiceInterface;

    @Autowired
    TransactionServiceInterface transactionServiceInterface;

    @Autowired
    BankAccountServiceInterface bankAccountServiceInterface;

    @Autowired
    AccountServiceInterface accountServiceInterface;

    @ModelAttribute("bankAccountadd")
    public BankAccountDto bankAccountDto() {
        return new BankAccountDto();
    }

    @ModelAttribute("withdraw")
    public TransactionDto transactionDto() {
        return new TransactionDto();
    }

    /**
     * Home page, diplay balance account, allows you to make payments and
     * withdrawals, add bank details, as well as access to all the pages of the app
     *
     * @param model
     * @return home page or admin page according to role
     */
    @GetMapping
    public String home(Model model) {
        log.info("Request get /home called");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userServiceInterface.findOne(username);
        Account account = accountServiceInterface.findByUserAccountId(user);
        BankAccount bankAccount = user.getBankAccount();

        model.addAttribute("account", account);
        model.addAttribute("bankAccount", bankAccount);

        Collection<Role> roles = user.getRoles();
        if (roles.toString().contains("ROLE_ADMIN")) {
            return "admin";
        }
        return "home";
    }

    /**
     * Form method post for transfer, payment or withdraw
     *
     * @param transactionDto
     * @return
     */
    @PostMapping
    public String transfer(@ModelAttribute("transaction") TransactionDto transactionDto) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userServiceInterface.findOne(username);
        Account account = accountServiceInterface.findByUserAccountId(user);

        if (transactionDto.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            transactionDto.setSenderId(account);
            String reponse = transactionServiceInterface.save(transactionDto);

            if (reponse == "success") {
                log.info("Success payment, home page post transfer");
                return "redirect:/home?successPayment";

            } else if (reponse == "errorNotEnoughMoney") {
                log.error("error not enough money, home page post transfer");
                return "redirect:/home?errorNotEnoughMoney";
            }
        }
        log.error("amount = 0, home page post transfer");
        return "redirect:/home?errorZero";

    }

    /**
     * Form method post to add bank account
     *
     * @param bankAccountDto
     * @return
     */
    @PostMapping("/addBankAccount")
    public String addBankAccount(@ModelAttribute("bankAccountadd") BankAccountDto bankAccountDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        bankAccountDto.setEmail(username);
        bankAccountServiceInterface.save(bankAccountDto);
        return "redirect:/home?successAddBankAccount";
    }
}
