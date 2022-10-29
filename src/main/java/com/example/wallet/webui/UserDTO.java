package com.example.wallet.webui;

import java.io.Serializable;

public class UserDTO implements Serializable {

    private final String login;
    private final String password;
    private final boolean isPasswordKeptAsHash;
    private final String hashAlgorithm;

    public UserDTO(final String login, final String password, final boolean isPasswordKeptAsHash,
                   final String hashAlgorithm) {
        this.login = login;
        this.password = password;
        this.isPasswordKeptAsHash = isPasswordKeptAsHash;
        this.hashAlgorithm = hashAlgorithm;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean isPasswordKeptAsHash() {
        return isPasswordKeptAsHash;
    }

    public String getHashAlgorithm() {
        return hashAlgorithm;
    }
}
