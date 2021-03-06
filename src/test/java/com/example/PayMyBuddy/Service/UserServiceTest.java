package com.example.PayMyBuddy.Service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import com.example.PayMyBuddy.model.Account;
import com.example.PayMyBuddy.model.dto.UserDto;
import com.example.PayMyBuddy.model.dto.UserProfileDto;
import com.example.PayMyBuddy.model.dto.UserRegistrationDto;
import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.repository.UserRepository;
import com.example.PayMyBuddy.service.AccountService;
import com.example.PayMyBuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserRepository userRepository;
    private AccountService accountServiceInterface;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        accountServiceInterface = mock(AccountService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserService(userRepository, accountServiceInterface, passwordEncoder);
    }

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

        verify(userRepository).save(Mockito.any(User.class));
        verify(accountServiceInterface).save(Mockito.any(User.class));

    }

    @Test
    void testFindAll() {
        userService.findAll();
        verify(userRepository).findAll();
    }

    @Test
    void testFindOne() {
        userService.findOne("mail");
        verify(userRepository).findByEmail("mail");
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
        userSave.setEmail("clement@gmail.com");
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
        verify(userRepository).existsUserByEmail("mail");
    }

    @Test
    void testUnsubscribe() {

        UserDto userDto = new UserDto();
        userDto.setEmail("email@email.fr");
        userDto.setEmailConfirm("email@email.fr");

        User user = new User();
        user.setActive(true);
        user.setEmail("email@email.fr");
        user.setPassword("0000");

        Account account = new Account();
        account.setUser(user);
        account.setBalance(new BigDecimal("0.00"));

        User userToSave = new User();
        userToSave.setActive(true);
        userToSave.setEmail("email@email.fr");
        userToSave.setPassword("0000");

        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(user);
        when(userRepository.getOne(user.getId())).thenReturn(userToSave);
        when(userRepository.save(userToSave)).thenReturn(userToSave);
        when(accountServiceInterface.findByUserAccountId(user)).thenReturn(account);

        User userSave = userService.unsubscribe(userDto);
        assertThat(userSave.isActive()).isEqualTo(false);
    }

}