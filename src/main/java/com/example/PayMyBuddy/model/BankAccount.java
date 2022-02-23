package com.example.PayMyBuddy.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "bank_account")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long bankAccountId;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "iban")
    private String iban;

    @Column(name = "bic")
    private String bic;

    @Column(name = "holder")
    private String holder;

}
