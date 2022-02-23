package com.example.PayMyBuddy.model;

import com.sun.istack.NotNull;
import lombok.Data;

import java.util.Collection;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true)
    @NotNull
    @Size(max = 50, message = "Email must be less than 50 characters")
    private String email;

    @NotNull
    private String password;

    @Column(name = "last_name")
    @Size(max = 25, message = "Last name must be less than 25 characters")
    private String lastName;

    @Column(name = "first_name")
    @Size(max = 25, message = "First name must be less than 25 characters")
    private String firstName;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<Role> roles;

    @OneToOne
    private BankAccount bankAccount;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<User> contacts;
    private boolean active = true;

}