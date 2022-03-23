package com.example.PayMyBuddy.controller;


import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.model.dto.UserProfileDto;
import com.example.PayMyBuddy.service.Interface.UserServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.Valid;

@Controller
@RequestMapping("/profile")
@Slf4j
public class ProfileController implements WebMvcConfigurer {

    @Autowired
    UserServiceInterface userServiceInterface;

    @ModelAttribute("user")
    public UserProfileDto userProfilDto() {
        return new UserProfileDto();
    }
    /**
     * Profile page, allows to view profile
     * @param model
     * @return
     */
    @GetMapping
    public String profile(Model model) {
        log.info("Request get /profile called");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userServiceInterface.findOne(username);
        model.addAttribute("user", user);
        return "profile";
    }

    /**
     * Form Profile page to update profile
     * @param userDto
     * @param bindingResult
     * @return
     */
    @PostMapping("/update")
    public String transfer(@Valid @ModelAttribute("user") UserProfileDto userDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "profile";
        }
        userServiceInterface.save(userDto);
        return "redirect:/profile?success";
    }

}
