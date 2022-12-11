package com.example.wallet.webui;

import java.util.Objects;

public final class LoginUserDTO {
    private final String login;
    private final String password;
    private final String hashAlgorithm;

    public LoginUserDTO(String login, String password, String hashAlgorithm) {
        this.login = login;
        this.password = password;
        this.hashAlgorithm = hashAlgorithm;
    }

    public String login() {
        return login;
    }

    public String password() {
        return password;
    }

    public String hashAlgorithm() {
        return hashAlgorithm;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (LoginUserDTO) obj;
        return Objects.equals(this.login, that.login) &&
                Objects.equals(this.password, that.password) &&
                Objects.equals(this.hashAlgorithm, that.hashAlgorithm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, hashAlgorithm);
    }

    @Override
    public String toString() {
        return "LoginUserDTO[" +
                "login=" + login + ", " +
                "password=" + password + ", " +
                "hashAlgorithm=" + hashAlgorithm + ']';
    }


}
