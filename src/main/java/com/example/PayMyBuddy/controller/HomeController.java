package com.example.PayMyBuddy.controller;
import java.util.Collection;

import com.example.PayMyBuddy.model.Role;
import com.example.PayMyBuddy.model.User;
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
    UserServiceInterface userServiceInterface;

    @GetMapping
    public String home(Model model) {
        log.info("Request get /home called");

        // Get the user authenticate, need to find this account, info....
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userServiceInterface.findOne(username);

        // Return home page or admin page according to role
        Collection<Role> roles = user.getRoles();
        if (roles.toString().contains("ROLE_ADMIN")) {
            return "admin";
        }
        return "home";
    }
}
