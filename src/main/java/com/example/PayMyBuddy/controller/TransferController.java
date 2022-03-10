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
    UserServiceInterface userServiceI;

    @Autowired
    TransactionServiceInterface transactionServiceI;

    @Autowired
    ConnectionServiceInterface connectionServiceI;

    @Autowired
    AccountServiceInterface accountServiceI;

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
        User user = userServiceI.findOne(username);
        Account account = accountServiceI.findByUserAccountId(user);

        //To display transfers and connections
        Iterable<Transaction> listTransaction = null;
        listTransaction = transactionServiceI.findAllBySenderIdAndType(account, Type.USER_TO_USER);
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
        if (userServiceI.existsUserByEmail(userConnectionDto.getEmail())) {
            connectionServiceI.add(userConnectionDto);
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
    public String transfer(@ModelAttribute("transaction") TransactionDto transactionDto) {

        // recovery identity user connected, the entity and this account
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userServiceI.findOne(username);
        Account account = accountServiceI.findByUserAccountId(user);
        if (transactionDto.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            transactionDto.setSenderId(account);
            transactionDto.setType(Type.USER_TO_USER);
            log.debug("transactionController : " + transactionDto.getSenderId());
            String reponse = transactionServiceI.save(transactionDto);
            log.debug("reponse : "+ reponse);
            log.debug("transactionDto : "+ transactionDto);

            if (reponse == "success") {
                return "redirect:/transfer?successPayment";
            } else if (reponse == "errorNotEnoughMoney") {
                return "redirect:/transfer?errorNotEnoughMoney";
            } else if (reponse == "inactive") {
                return "redirect:/transfer?errorInactive";
            }
        }
        return "redirect:/transfer?errorZero";

    }
}