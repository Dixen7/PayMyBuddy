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
import com.example.PayMyBuddy.controller.SubscribeController;
import com.example.PayMyBuddy.model.dto.UserRegistrationDto;
import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.service.Interface.UserServiceInterface;
import com.example.PayMyBuddy.service.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@ContextConfiguration(classes = SecurityConfig.class)
@WebMvcTest(controllers = SubscribeController.class)
@Import(SubscribeController.class)
class SubscribeControllerTest {

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
        this.mockMvc.perform(get("/subscribe")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("subscribe")));

    }

    @Test
    void testPostNewUserSuccess() throws Exception {

        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();

        User user = new User();
        user.setActive(true);
        user.setEmail("user1@gmail.com");
        user.setPassword("user");

        when(userServiceInterface.findOne(userRegistrationDto.getEmail())).thenReturn(user);

        RequestBuilder request = post("/subscribe")
                .param("email", userRegistrationDto.getEmail())
                .param("password", userRegistrationDto.getPassword());

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(redirectedUrl("/subscribe?successRegistration"));
    }

    @Test
    void testPostNewUserIfExist() throws Exception {

        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("user1@gmail.com");
        userRegistrationDto.setPassword("user");

        User user = new User();
        user.setActive(true);
        user.setEmail("user1@gmail.com");
        user.setPassword("user");

        when(userServiceInterface.findOne(userRegistrationDto.getEmail())).thenReturn(user);

        when(userServiceInterface.existsUserByEmail(userRegistrationDto.getEmail())).thenReturn(true);

        RequestBuilder request = post("/subscribe")
                .param("email", userRegistrationDto.getEmail())
                .param("password", userRegistrationDto.getPassword());

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(redirectedUrl("/subscribe?error"));
    }

}