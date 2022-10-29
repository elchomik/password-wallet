package com.example.wallet.webui;

import java.io.Serializable;

public class UpdateMasterPasswordDTO implements Serializable {

    private final String password;
    private final String hashAlgorithm;

    public UpdateMasterPasswordDTO(final String password, final String hashAlgorithm) {
        this.password = password;
        this.hashAlgorithm = hashAlgorithm;
    }


    public String getHashAlgorithm() {
        return hashAlgorithm;
    }

    public String getPassword() {
        return password;
    }
}
