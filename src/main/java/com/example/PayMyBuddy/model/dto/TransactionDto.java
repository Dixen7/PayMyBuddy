package com.example.PayMyBuddy.model.dto;

import com.example.PayMyBuddy.model.Account;
import com.example.PayMyBuddy.model.Type;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDto {

    private BigDecimal amount;
    private String description;
    private Type type;
    private Account senderId;
    private Account beneficiaryId;
    private String mailBeneficiary;

}
