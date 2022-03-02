package com.example.PayMyBuddy.controller;
import java.math.BigDecimal;
import java.util.Collection;

import com.example.PayMyBuddy.model.Account;
import com.example.PayMyBuddy.model.BankAccount;
import com.example.PayMyBuddy.model.Role;
import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.service.Interface.AccountServiceInterface;
import com.example.PayMyBuddy.service.Interface.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/home")
@Validated
public class HomeController {

    @Autowired
    UserServiceInterface userServiceI;

    @Autowired
    AccountServiceInterface accountServiceI;

    @GetMapping
    public String home(Model model) {
        log.info("Request get /home called");

        // Get the user authenticate, need to find this account, info....
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userServiceI.findOne(username);
        Account account = accountServiceI.findByUserAccountId(user);
        BankAccount bankAccount = user.getBankAccount();

/*
        // Serve user information to view
        model.addAttribute("account", account);
        model.addAttribute("bankAccount", bankAccount);
*/

        // Return home page or admin page according to role
        Collection<Role> roles = user.getRoles();
        if (roles.toString().contains("ROLE_ADMIN")) {
            return "admin";
        }
        return "home";
    }
}
