package org.example;

import java.io.Serializable;
import java.util.*;

public class Wallet implements Serializable {
    private List<org.example.Transaction> transactions;
    private Map<String, org.example.Budget> budgets;
    private Set<String> categories;

    public Wallet() {
        this.transactions = new ArrayList<>();
        this.budgets = new HashMap<>();
        this.categories = new HashSet<>();
    }

    public void addCategory(String category) {
        categories.add(category);
    }

    public void removeCategory(String category) {
        categories.remove(category);
    }

    public Set<String> getCategories() {
        return categories;
    }

    // Методы для добавления транзакций и установки бюджетов
    public void addTransaction(org.example.Transaction transaction) {
        transactions.add(transaction);
    }

    public void setBudget(String category, org.example.Budget budget) {
        budgets.put(category, budget);
    }

    // Геттеры и сеттеры
    public List<org.example.Transaction> getTransactions() {
        return transactions;
    }

    public Map<String, org.example.Budget> getBudgets() {
        return budgets;
    }
}
