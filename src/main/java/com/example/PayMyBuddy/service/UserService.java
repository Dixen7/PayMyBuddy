package com.example.PayMyBuddy.service;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.example.PayMyBuddy.model.Account;
import com.example.PayMyBuddy.model.dto.UserDto;
import com.example.PayMyBuddy.model.dto.UserProfileDto;
import com.example.PayMyBuddy.model.dto.UserRegistrationDto;
import com.example.PayMyBuddy.model.Role;
import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.repository.UserRepository;
import com.example.PayMyBuddy.service.Interface.AccountServiceInterface;
import com.example.PayMyBuddy.service.Interface.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserService implements UserServiceInterface {

    private UserRepository userRepository;
    private AccountServiceInterface accountServiceInterface;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, AccountServiceInterface accountServiceInterface, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountServiceInterface = accountServiceInterface;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Service for registration new user
     * @param userRegistrationDto
     * @return userBuddy
     */
    @Override
    @Transactional
    public User register(UserRegistrationDto userRegistrationDto) {

        User userSet = userSet(userRegistrationDto);
        User userBuddy = userRepository.save(userSet);
        accountServiceInterface.save(userBuddy);

        return userBuddy;
    }

    /**
     * Set Email and encode password before save in database and set the role
     * @param userRegistrationDto
     * @return user to save
     */
    public User userSet(UserRegistrationDto userRegistrationDto) {
        User user = new User();
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));

        if(userRegistrationDto.getEmail().equalsIgnoreCase("admin@paymybuddy.com")) {
            user.setRoles(Arrays.asList(new Role("ROLE_ADMIN")));

        } else {
            user.setRoles(Arrays.asList(new Role("ROLE_USER")));
        }

        return user;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findOne(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    /**
     * To update user profile
     * @param userDto
     */
    @Override
    public User save(UserProfileDto userDto) {
        User user = new User();
        user = userRepository.findByEmail(userDto.getEmail());
        User userToUpdate = userRepository.getOne(user.getId());
        log.debug("userToUpdate : " + userToUpdate);
        userToUpdate.setLastName(userDto.getLastName());
        userToUpdate.setFirstName(userDto.getFirstName());
        User userSave = userRepository.save(userToUpdate);

        return userSave;
    }

    /**
     * Service for unsuscribe user and set inactive profile
     * @return
     */
    @Override
    public User unsubscribe(UserDto userDto) {
        User user = new User();
        user = userRepository.findByEmail(userDto.getEmail());
        Account account = accountServiceInterface.findByUserAccountId(user);

        if(!account.getBalance().equals(new BigDecimal("0.00")) ){
            return null;
        }
        User userToUpdate = userRepository.getOne(user.getId());
        userToUpdate.setActive(false);
        User userSave =  userRepository.save(userToUpdate);
        log.debug("user inactive: " + userSave);

        return userSave;
    }

}