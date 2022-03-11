package com.example.PayMyBuddy.Controller;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;

import com.example.PayMyBuddy.configuration.SecurityConfig;
import com.example.PayMyBuddy.controller.HomeController;
import com.example.PayMyBuddy.model.Account;
import com.example.PayMyBuddy.model.Role;
import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.model.dto.TransactionDto;
import com.example.PayMyBuddy.service.Interface.AccountServiceInterface;
import com.example.PayMyBuddy.service.Interface.BankAccountServiceInterface;
import com.example.PayMyBuddy.service.Interface.TransactionServiceInterface;
import com.example.PayMyBuddy.service.Interface.UserServiceInterface;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import lombok.extern.slf4j.Slf4j;


//TODO
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SecurityConfig.class)
@WebMvcTest(controllers = HomeController.class)
@Import(HomeController.class)
@Slf4j
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    @MockBean
    UserServiceInterface userServiceI;

    @MockBean
    TransactionServiceInterface transactionServiceI;

    @MockBean
    BankAccountServiceInterface bankAccountServiceI;

    @MockBean
    AccountServiceInterface accountServiceI;
    Account account = new Account();
    User user = new User();

    @Before
    public void setup(){
        mockMvc= MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity()).build();

    }

    @Test
    @WithMockUser("user@gmail.com")
    void testShouldReturnMessageHome() throws Exception {
        //ARRANGE
        Collection<Role> roles = new HashSet<Role>();
        roles.add(new Role("ROLE_USER"));
        user.setEmail("user@gmail.com");
        user.setPassword("user");
        user.setRoles(roles);
        account.setBalance(new BigDecimal("0"));
        account.setUser(user);
        when(userServiceI.findOne("user@gmail.com")).thenReturn(user);
        when(accountServiceI.findByUserAccountId(user)).thenReturn(account);

        //ACT AND ASSERT
        this.mockMvc.perform(MockMvcRequestBuilders.get("/home").flashAttr("account", account)).andExpect(status().isOk())
                .andExpect(content().string(containsString("home")));

    }

    @Test
    void testHomeNotAutorize() throws Exception {
        //ACT AND ASSERT
        this.mockMvc.perform(MockMvcRequestBuilders.get("/home").flashAttr("account", account)).andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser("admin@gmail.com")
    void testHomeAdmin() throws Exception {
        //ARRANGE
        Collection<Role> roles = new HashSet<Role>();
        roles.add(new Role("ROLE_ADMIN"));
        user.setEmail("admin@gmail.com");
        user.setPassword("admin");
        user.setRoles(roles);
        account.setBalance(new BigDecimal("0"));
        account.setUser(user);
        when(userServiceI.findOne("admin@gmail.com")).thenReturn(user);
        when(accountServiceI.findByUserAccountId(user)).thenReturn(account);

        //ACT AND ASSERT
        this.mockMvc.perform(MockMvcRequestBuilders.get("/home").flashAttr("account", account)).andExpect(status().isOk())
                .andExpect(content().string(containsString("admin")));

    }

    @Test
    @WithMockUser("user@gmail.com")
    void testPostNewBankDetails() throws Exception {
        // ACT
        mockMvc
                .perform(post("/home/addBankAccount"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(redirectedUrl("/home?successAddBankAccount"));

    }

    @Test
    @WithMockUser("user@gmail.com")
    void testTransfer() throws Exception {
        // ARRANGE
        TransactionDto transac = new TransactionDto();
        User user =new User();
        user.setEmail("user@gmail.com");
        Account account = new Account();
        account.setBalance(new BigDecimal("200"));
        account.setUser(user);
        when(userServiceI.findOne("user@gmail.com")).thenReturn(user);
        when(accountServiceI.findByUserAccountId(user)).thenReturn(account);
        when(transactionServiceI.save(transac)).thenReturn("success");

        transac.setAmount(new BigDecimal("100"));
        transac.setSenderId(account);
        transac.setDescription("payment");

        RequestBuilder request = post("/home")
                .param("description", transac.getDescription()).param("amount", transac.getAmount().toString());
        //ACT AND ASSERT
        mockMvc
                .perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(redirectedUrl("/home?successPayment"));

    }

}
