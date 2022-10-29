package com.example.wallet.readonly;

import org.springframework.security.core.userdetails.UserDetails;

public class UserProjection {

    private final UserDetails user;
    private final String generatedToken;

    public UserProjection(final UserDetails user, final String generatedToken) {
        this.user = user;
        this.generatedToken = generatedToken;
    }

    public UserDetails getUser() {
        return user;
    }

    public String getGeneratedToken() {
        return generatedToken;
    }
}
