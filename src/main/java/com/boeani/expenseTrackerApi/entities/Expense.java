package com.boeani.expenseTrackerApi.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @Enumerated(value = EnumType.STRING)
    private Category category;

    private String description;

    @Column(name = "expense_date")
    private LocalDate expenseDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Expense(BigDecimal amount, Category category, String description, LocalDate expenseDate, User user) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.expenseDate = expenseDate;
        this.user = user;
    }

    protected Expense() {
    }

    public Long getId() {
        return this.id;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Category getCategory() {
        return this.category;
    }

    public String getDescription() {
        return this.description;
    }

    public User getUser() {
        return this.user;
    }

    public LocalDate getExpenseDate() {
        return this.expenseDate;
    }
}
