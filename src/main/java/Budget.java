package org.example;


import java.io.Serializable;

public class Budget implements Serializable {
    private double limit;

    public Budget(double limit) {
        this.limit = limit;
    }

    // Геттеры и сеттеры
    public double getLimit() {
        return limit;
    }
}
