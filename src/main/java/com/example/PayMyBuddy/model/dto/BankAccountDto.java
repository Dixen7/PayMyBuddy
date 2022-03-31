package com.example.PayMyBuddy.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountDto {

    private String email;
    private long bankAccountId;
    private String accountNumber;
    private String iban;
    private String bic;
    private String holder;

}
