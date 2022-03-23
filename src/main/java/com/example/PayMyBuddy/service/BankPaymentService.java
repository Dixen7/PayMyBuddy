package com.example.PayMyBuddy.service;

import com.example.PayMyBuddy.model.Transaction;
import com.example.PayMyBuddy.service.Interface.BankPaymentInterface;
import org.springframework.stereotype.Service;

@Service
public class BankPaymentService implements BankPaymentInterface {

    @Override
    public boolean requestAuthorization(Transaction transactionPayment) {
        return true;
    }
}
