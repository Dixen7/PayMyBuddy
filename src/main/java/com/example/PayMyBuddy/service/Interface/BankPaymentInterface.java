package com.example.PayMyBuddy.service.Interface;

import com.example.PayMyBuddy.model.Transaction;

public interface BankPaymentInterface {

    boolean requestAuthorization(Transaction transactionPayment);

}
