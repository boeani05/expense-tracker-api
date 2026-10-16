package com.boeani.expenseTrackerApi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String email;

    public User(String email) {
        this.email = email;
    }

    protected User() {
    }

    public Long getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }
}
