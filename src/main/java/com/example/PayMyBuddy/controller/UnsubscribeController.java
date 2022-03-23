package com.example.PayMyBuddy.controller;

import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.model.dto.UserDto;
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
@RequestMapping("/unsubscribe")
public class UnsubscribeController {

    @Autowired
    UserServiceInterface userServiceI;

    /**
     * Unsuscribe page
     * @param model
     * @return
     */
    @GetMapping
    public String unsuscribe(Model model) {

        return "unsuscribe";
    }

    /**
     * Form to unsuscribe
     * @param userDto
     * @return
     */
    @PostMapping
    public String transfer(@ModelAttribute("user") UserDto userDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        userDto.setEmailConfirm(username);

        if (!userDto.getEmail().equalsIgnoreCase(userDto.getEmailConfirm())) {
            return "redirect:/unsuscribe?error";
        }
        User responseUser = userServiceI.unsubscribe(userDto);
        if (responseUser == null) {
            return "redirect:/unsuscribe?errorMonney";
        }
        return "redirect:/login?unsuscribe";
    }
}
