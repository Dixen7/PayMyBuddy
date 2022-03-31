package com.example.PayMyBuddy.controller;


import java.math.BigDecimal;
import java.util.Set;

import com.example.PayMyBuddy.model.Account;
import com.example.PayMyBuddy.model.Transaction;
import com.example.PayMyBuddy.model.Type;
import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.model.dto.TransactionDto;
import com.example.PayMyBuddy.model.dto.UserConnectionDto;
import com.example.PayMyBuddy.service.Interface.AccountServiceInterface;
import com.example.PayMyBuddy.service.Interface.ConnectionServiceInterface;
import com.example.PayMyBuddy.service.Interface.TransactionServiceInterface;
import com.example.PayMyBuddy.service.Interface.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/transfer")
public class TransferController {

    @Autowired
    UserServiceInterface userServiceInterface;

    @Autowired
    TransactionServiceInterface transactionServiceInterface;

    @Autowired
    ConnectionServiceInterface connectionServiceInterface;

    @Autowired
    AccountServiceInterface accountServiceInterface;

    @ModelAttribute("transaction")
    public TransactionDto transactionDto() {
        return new TransactionDto();
    }

    /**
     * Transfer page, display form transfer, connection and all transfers
     * @param model
     * @return
     */
    @GetMapping
    public String transfer(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userServiceInterface.findOne(username);
        Account account = accountServiceInterface.findByUserAccountId(user);

        Iterable<Transaction> listTransaction = null;
        listTransaction = transactionServiceInterface.findAllBySenderIdAndType(account, Type.USER_TO_USER);
        Set<User> contacts = user.getContacts();
        model.addAttribute("transactions", listTransaction);
        model.addAttribute("contacts", contacts);

        return "transfer";
    }

    /**
     * Form to add connection
     * @param userConnectionDto
     * @return
     */
    @PostMapping("/connection")
    public String addConnection(@ModelAttribute("user") UserConnectionDto userConnectionDto) {
        if (userServiceInterface.existsUserByEmail(userConnectionDto.getEmail())) {
            connectionServiceInterface.add(userConnectionDto);
            return "redirect:/transfer?successAddConnection";
        } else {
            return "redirect:/transfer?errorAddConnection";
        }
    }

    /**
     * Form transfer
     * @param transactionDto
     * @return
     */
    @PostMapping("/transfer")
    public String transfer(@ModelAttribute("transaction") TransactionDto transactionDto) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userServiceInterface.findOne(username);
        Account account = accountServiceInterface.findByUserAccountId(user);

        if (transactionDto.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            transactionDto.setSenderId(account);
            transactionDto.setType(Type.USER_TO_USER);

            String response = transactionServiceInterface.save(transactionDto);

            log.debug("transactionController : " + transactionDto.getSenderId());
            log.debug("response : "+ response);
            log.debug("transactionDto : "+ transactionDto);

            if (response == "success") {
                return "redirect:/transfer?successPayment";

            } else if (response == "errorNotEnoughMoney") {
                return "redirect:/transfer?errorNotEnoughMoney";

            } else if (response == "inactive") {
                return "redirect:/transfer?errorInactive";
            }
        }
        return "redirect:/transfer?errorZero";

    }
}