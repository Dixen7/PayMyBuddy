package com.example.PayMyBuddy.Controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.PayMyBuddy.configuration.SecurityConfig;
import com.example.PayMyBuddy.controller.SuscribeController;
import com.example.PayMyBuddy.model.Dto.UserRegistrationDto;
import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.service.Interface.UserServiceInterface;
import com.example.PayMyBuddy.service.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SecurityConfig.class)
@WebMvcTest(controllers = SuscribeController.class)
@Import(SuscribeController.class)
class SuscribeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    @MockBean
    UserServiceInterface userServiceInterface;

    @MockBean
    RoleService roleService;

    @Test
    void testShouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/suscribe")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("suscribe")));

    }

    @Test
    void testPostNewUserSuccess() throws Exception {
        // ARRANGE
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();

        User user = new User();
        user.setActive(true);
        user.setEmail("user1@gmail.com");
        user.setPassword("user");

        when(userServiceInterface.findOne(userRegistrationDto.getEmail())).thenReturn(user);

        RequestBuilder request = post("/suscribe")
                .param("email", userRegistrationDto.getEmail())
                .param("password", userRegistrationDto.getPassword());

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(redirectedUrl("/suscribe?successRegistration"));
    }

    @Test
    void testPostNewUserIfExist() throws Exception {
        // ARRANGE
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("user1@gmail.com");
        userRegistrationDto.setPassword("user");

        User user = new User();
        user.setActive(true);
        user.setEmail("user1@gmail.com");
        user.setPassword("user");

        when(userServiceInterface.findOne(userRegistrationDto.getEmail())).thenReturn(user);

        when(userServiceInterface.existsUserByEmail(userRegistrationDto.getEmail())).thenReturn(true);

        RequestBuilder request = post("/suscribe")
                .param("email", userRegistrationDto.getEmail())
                .param("password", userRegistrationDto.getPassword());

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(redirectedUrl("/suscribe?error"));
    }

}