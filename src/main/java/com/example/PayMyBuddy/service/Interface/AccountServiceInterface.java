package com.example.PayMyBuddy.service.Interface;

import com.example.PayMyBuddy.model.Account;
import com.example.PayMyBuddy.model.User;

public interface AccountServiceInterface {

    Account findByUserAccountId(User user);
    String save(User user);
}
