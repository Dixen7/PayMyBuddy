package com.example.PayMyBuddy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "bank_account")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_account_id")
    private long bankAccountId;

    @Size(max = 11, message = "Account number must be less than 11 characters")
    @Column(name = "account_number")
    private String accountNumber;

    @Size(max = 34, message = "IBAN must be less than 34 characters")
    @Column(name = "iban")
    private String iban;

    @Size(max = 11, message = "Bic must be less than 100 characters")
    @Column(name = "bic")
    private String bic;

    @Size(max = 20, message = "Holder must be less than 10 characters")
    @Column(name = "holder")
    private String holder;

}
