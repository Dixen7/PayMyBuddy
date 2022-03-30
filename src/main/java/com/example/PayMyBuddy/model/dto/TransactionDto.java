package com.example.PayMyBuddy.model.dto;

import com.example.PayMyBuddy.model.Account;
import com.example.PayMyBuddy.model.Type;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionDto {

    private BigDecimal amount;
    private String description;
    private Type type;
    private Account senderId;
    private Account beneficiaryId;
    private String mailBeneficiary;

}
