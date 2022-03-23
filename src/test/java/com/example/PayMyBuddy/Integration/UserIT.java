package com.example.PayMyBuddy.Integration;

import static org.assertj.core.api.Assertions.assertThat;
import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.model.dto.UserProfileDto;
import com.example.PayMyBuddy.repository.UserRepository;
import com.example.PayMyBuddy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
class UserIT {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;


    @Test
    @Transactional
    @WithMockUser(username = "christophe@gmail.com")
    void testfindUserByEmail() {
        User user = userRepository.findByEmail("christophe@gmail.com");

        assertThat(user.getFirstName()).isEqualToIgnoringCase("christophe");
    }

    @Test
    @Transactional
    @WithMockUser(username = "christophe@gmail.com")
    void testUpdateProfile() {

        UserProfileDto userDto = new UserProfileDto();
        userDto.setEmail("christophe@gmail.com");
        userDto.setLastName("balestrino");

        User userUpdate = userService.save(userDto);

        assertThat(userUpdate.getLastName()).isEqualToIgnoringCase("balestrino");
    }

}