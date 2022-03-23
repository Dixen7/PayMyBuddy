package com.example.PayMyBuddy.Integration;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import javax.transaction.Transactional;

import com.example.PayMyBuddy.model.Account;
import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.model.dto.TransactionDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import com.example.PayMyBuddy.repository.AccountRepository;
import com.example.PayMyBuddy.repository.UserRepository;


@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
    @WithMockUser(username = "christophe@gmail.com")
    void testUserHome() throws Exception {
        this.mockMvc.perform(get("/home")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("home")));
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin@gmail.com")
    void testAdminHome() throws Exception {
        this.mockMvc.perform(get("/home")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("admin")));
    }

    @Test
    @WithMockUser("christophe@gmail.com")
    void testTransfer() throws Exception {

        TransactionDto transac = new TransactionDto();
        User user = userRepository.findByEmail("christophe@gmail.com");
        Account account = accountRepository.findByUser(user);

        transac.setAmount(new BigDecimal("100"));
        transac.setSenderId(account);
        transac.setDescription("payment");

        RequestBuilder request = post("/home")
                .param("description", transac.getDescription()).param("amount", transac.getAmount().toString());

        mockMvc
                .perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(redirectedUrl("/home?successPayment"));

    }

}