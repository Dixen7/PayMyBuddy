package com.example.PayMyBuddy.model;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    public Role(String name) {
        super();
        this.name = name;
    }

    public Role() {
        super();
    }
}
