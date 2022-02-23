package com.example.PayMyBuddy.service.Interface;

import com.example.PayMyBuddy.model.Dto.UserDto;
import com.example.PayMyBuddy.model.Dto.UserProfileDto;
import com.example.PayMyBuddy.model.Dto.UserRegistrationDto;
import com.example.PayMyBuddy.model.User;

import java.util.List;

public interface UserServiceInterface {

    User save(UserRegistrationDto userRegistrationDto);
    List<User> findAll();
    User findOne(String email);
    boolean existsUserByEmail(String email);
    User save(UserProfileDto userDto);
    User unsuscribe(UserDto userDto);
}
