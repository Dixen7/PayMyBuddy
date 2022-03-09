package com.example.PayMyBuddy.service.Interface;

import com.example.PayMyBuddy.model.dto.UserDto;
import com.example.PayMyBuddy.model.dto.UserProfileDto;
import com.example.PayMyBuddy.model.dto.UserRegistrationDto;
import com.example.PayMyBuddy.model.User;

import java.util.List;

public interface UserServiceInterface {

    User register(UserRegistrationDto userRegistrationDto);
    List<User> findAll();
    User findOne(String email);
    boolean existsUserByEmail(String email);
    User save(UserProfileDto userDto);
    User unsubscribe(UserDto userDto);
}
