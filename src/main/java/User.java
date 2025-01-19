package org.example;

import java.io.Serializable;

public class User implements Serializable {
    private String login;
    private String password;
    private Wallet wallet;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.wallet = new Wallet();
    }

    // Геттеры и сеттеры
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Wallet getWallet() {
        return wallet;
    }
}
