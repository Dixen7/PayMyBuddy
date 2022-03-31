package com.example.PayMyBuddy.controller;

import com.example.PayMyBuddy.model.dto.UserRegistrationDto;
import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.service.Interface.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/subscribe")
@Slf4j
public class SubscribeController {

    @Autowired
    UserServiceInterface userServiceInterface;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm() {
        log.info("Request get /subscribe called");
        return "subscribe";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto userRegistrationDto) {

        User user = userServiceInterface.findOne(userRegistrationDto.getEmail());

        if (userServiceInterface.existsUserByEmail(userRegistrationDto.getEmail())) {
            Boolean active = user.isActive();
            if(!active) {
                return "redirect:/subscribe?inactive";
            }
            return "redirect:/subscribe?error";
        }
        else {
            userServiceInterface.register(userRegistrationDto);
            return "redirect:/subscribe?successRegistration";
        }

    }

}