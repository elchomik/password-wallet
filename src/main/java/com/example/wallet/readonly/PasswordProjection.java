package com.example.wallet.readonly;

public class PasswordProjection {

    private final Integer passwordId;
    private final String password;

    public PasswordProjection(final Integer passwordId, final String password) {
        this.passwordId = passwordId;
        this.password = password;
    }

    public Integer getPasswordId() {
        return passwordId;
    }

    public String getPassword() {
        return password;
    }
}
