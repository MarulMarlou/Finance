package org.example;

import java.io.Serializable;

public class Transaction implements Serializable {
    private double amount;
    private String category;
    private boolean isIncome;

    public Transaction(double amount, String category, boolean isIncome) {
        this.amount = amount;
        this.category = category;
        this.isIncome = isIncome;
    }

    // Геттеры и сеттеры
    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public boolean isIncome() {
        return isIncome;
    }
}
