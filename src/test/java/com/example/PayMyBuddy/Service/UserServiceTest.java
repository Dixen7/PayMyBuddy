package com.example.PayMyBuddy.Service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import com.example.PayMyBuddy.model.Account;
import com.example.PayMyBuddy.model.Dto.UserDto;
import com.example.PayMyBuddy.model.Dto.UserProfileDto;
import com.example.PayMyBuddy.model.Dto.UserRegistrationDto;
import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.repository.UserRepository;
import com.example.PayMyBuddy.service.AccountService;
import com.example.PayMyBuddy.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebMvcTest(UserService.class)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    AccountService accountServiceInterface;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
    User user = new User();
    User userSave = new User();
    Account account = new Account();

    @Test
    void testUserSaveAndCreateAnAccount() {

        userRegistrationDto.setEmail("user1@gmail.com");
        userRegistrationDto.setPassword("user");
        userSave.setEmail("user1@gmail.com");
        userSave.setPassword("0000");
        account.setUser(userSave);

        when(userRepository.save(Mockito.any(User.class))).thenReturn(userSave);
        userService.register(userRegistrationDto);

        verify(userRepository, times(1)).save(Mockito.any(User.class));
        verify(accountServiceInterface, times(1)).save(Mockito.any(User.class));

    }

    @Test
    void testFindAll() {
        userService.findAll();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindOne() {
        userService.findOne("mail");
        verify(userRepository, times(1)).findByEmail("mail");
    }

    @Test
    void userSet() {
        userRegistrationDto.setEmail("user1@gmail.com");
        userRegistrationDto.setPassword("user");

        User user = userService.userSet(userRegistrationDto);

        assertThat(user.getPassword()).isNotEqualToIgnoringCase("user");
        assertThat(user.getRoles()).toString().contains("ROLE_USER");
    }

    @Test
    void testUserProfileSave() {

        UserProfileDto userDto = new UserProfileDto();
        userDto.setEmail("clement@gmail.com");
        userDto.setFirstName("clement");
        userDto.setLastName("balestrino");

        user.setId(1L);
        user.setEmail("clement@gmail.com");
        user.setPassword("0000");

        userSave.setId(1L);
        userSave.setEmail("Clement@gmail.com");
        userSave.setPassword("0000");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(userRepository.getOne(user.getId())).thenReturn(userSave);
        when(userRepository.save(userSave)).thenReturn(new User());

        userService.save(userDto);

        assertThat(userSave.getFirstName()).isEqualToIgnoringCase("clement");
        verify(userRepository).save(userSave);

    }

    @Test
    void testExistUserByEmail() {
        userService.existsUserByEmail("mail");
        verify(userRepository, times(1)).existsUserByEmail("mail");
    }

    @Test
    void testUnsuscribe() {

        UserDto userDto = new UserDto();
        userDto.setEmail("email");
        userDto.setEmailConfirm("email");

        User user = new User();
        user.setActive(true);
        user.setEmail("email");
        user.setPassword("0000");

        Account account = new Account();
        account.setUser(user);
        account.setBalance(new BigDecimal("0.00"));

        User userToSave = new User();
        userToSave.setActive(true);
        userToSave.setEmail("email");
        userToSave.setPassword("0000");

        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(user);
        when(userRepository.getOne(user.getId())).thenReturn(userToSave);
        when(userRepository.save(userToSave)).thenReturn(userToSave);
        when(accountServiceInterface.findByUserAccountId(user)).thenReturn(account);

        User userSave = userService.unsuscribe(userDto);
        assertThat(userSave.isActive()).isEqualTo(false);
    }

}