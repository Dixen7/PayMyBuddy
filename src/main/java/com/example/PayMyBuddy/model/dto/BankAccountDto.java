package com.example.PayMyBuddy.model.dto;

import lombok.Data;

@Data
public class BankAccountDto {

    private String email;
    private long bankAccountId;
    private String accountNumber;
    private String iban;
    private String bic;
    private String holder;

}
