package com.example.PayMyBuddy.controller;


import com.example.PayMyBuddy.model.Dto.UserRegistrationDto;
import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.service.Interface.RoleServiceInterface;
import com.example.PayMyBuddy.service.Interface.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/suscribe")
@Slf4j
public class SuscribeController {

    @Autowired
    UserServiceInterface userServiceInterface;

    RoleServiceInterface roleServiceInterface;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "suscribe";
    }

    @PostMapping
    public String registerUserAccount(
            @ModelAttribute("user") UserRegistrationDto userRegistrationDto) {

        //Verify if user already exist or account inactive
        User user = userServiceInterface.findOne(userRegistrationDto.getEmail());

        if (userServiceInterface.existsUserByEmail(userRegistrationDto.getEmail())) {
            Boolean active = user.isActive();
            if(!active) {
                return "redirect:/suscribe?inactive";
            }
            return "redirect:/suscribe?error";
        }
        else {
            userServiceInterface.save(userRegistrationDto);
            return "redirect:/suscribe?successRegistration";
        }

    }


}
