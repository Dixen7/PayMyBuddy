package com.example.PayMyBuddy.Integration;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import com.example.PayMyBuddy.model.Account;
import com.example.PayMyBuddy.model.Transaction;
import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.repository.AccountRepository;
import com.example.PayMyBuddy.repository.TransactionRepository;
import com.example.PayMyBuddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
class TransferControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountRepository accountRepository;

    @Test
    @Transactional
    @WithMockUser(username = "christophe@gmail.com")
    void testShouldReturnDefaultMessage() throws Exception {

        User user = userRepository.findByEmail("christophe@gmail.com");
        Account account = accountRepository.findByUser(user);
        Iterable<Transaction> listTransaction = transactionRepository.findAllBySenderId(account);
        Set<User> contacts = user.getContacts();

        this.mockMvc.perform(MockMvcRequestBuilders.get("/transfer").flashAttr("user", user)
                        .flashAttr("transactions", listTransaction).flashAttr("contacts", contacts))
                .andExpect(status().isOk()).andExpect(content().string(containsString("transfer")));

    }

}