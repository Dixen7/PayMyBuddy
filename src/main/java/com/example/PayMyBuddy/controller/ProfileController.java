package com.example.PayMyBuddy.controller;


import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.model.dto.UserProfileDto;
import com.example.PayMyBuddy.service.Interface.UserServiceInterface;
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
public class ProfileController implements WebMvcConfigurer {

    @Autowired
    UserServiceInterface userServiceI;

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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userServiceI.findOne(username);
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
        userServiceI.save(userDto);
        return "redirect:/profile?success";
    }

}
