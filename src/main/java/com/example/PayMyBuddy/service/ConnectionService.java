package com.example.PayMyBuddy.service;

import com.example.PayMyBuddy.model.Dto.UserConnectionDto;
import com.example.PayMyBuddy.model.User;
import com.example.PayMyBuddy.repository.UserRepository;
import com.example.PayMyBuddy.service.Interface.ConnectionServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ConnectionService implements ConnectionServiceInterface {

    @Autowired
    UserRepository userRepository;

    /**
     * To add contact to userBuddy account
     */
    @Override
    public void add(UserConnectionDto userConnectionDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByEmail(username);

        User userToUpdate = userRepository.getOne(user.getId());
        Set<User> contacts = userToUpdate.getContacts();

        User userConnect = userRepository.findByEmail(userConnectionDto.getEmail());
        contacts.add(userConnect);
        userToUpdate.setContacts(contacts);
        userRepository.save(userToUpdate);
    }
}
