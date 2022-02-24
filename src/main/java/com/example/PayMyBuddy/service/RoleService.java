package com.example.PayMyBuddy.service;

import com.example.PayMyBuddy.repository.RoleRepository;
import com.example.PayMyBuddy.service.Interface.RoleServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService implements RoleServiceInterface {

    @Autowired
    RoleRepository roleRepository;

}
