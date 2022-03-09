package com.example.PayMyBuddy.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private long transactionId;

    private BigDecimal amount;

    @Column(name = "date_transaction")
    private LocalDate dateTransaction;

    private BigDecimal fee;

    @Size(max = 100, message = "Message must be less than 100 characters")
    private String description;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    @JoinColumn(name = "account_sender_id", referencedColumnName = "user_id")
    private Account senderId;

    @ManyToOne
    @JoinColumn(name = "account_beneficiary_id", referencedColumnName = "user_id")
    private Account beneficiaryId;

};