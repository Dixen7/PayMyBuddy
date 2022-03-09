package com.example.PayMyBuddy.service;

import com.example.PayMyBuddy.model.Transaction;
import com.example.PayMyBuddy.service.Interface.BankPaymentInterface;

public class BankPaymentService implements BankPaymentInterface {

    @Override
    public boolean requestAuthorization(Transaction transactionPayment) {
        // now return always true, here we send transfer betwenn bank account sender and receiver, and admin bank for the fee
        return true;
    }

}
