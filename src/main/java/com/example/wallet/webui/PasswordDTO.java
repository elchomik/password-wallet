package com.example.wallet.webui;

import java.io.Serializable;

public class PasswordDTO implements Serializable {

    private final String password;
    private final String webAddress;
    private final String description;
    private final String hashAlgorithm;

    public PasswordDTO(final String password, final String webAddress,
                       final String description, final String hashAlgorithm) {
        this.webAddress = webAddress;
        this.description = description;
        this.password=password;
        this.hashAlgorithm= hashAlgorithm;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public String getDescription() {
        return description;
    }

    public String getPassword() {
        return password;
    }

    public String getHashAlgorithm() {
        return hashAlgorithm;
    }
}
