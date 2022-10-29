package com.example.wallet.privilleges.roles;

public enum UserRoles {
    AUTHENTICATED_USER("AUTHENTICATED_USER"),
    UNAUTHENTICATED_USER("UNAUTHENTICATED_USER");

    final String fieldName;

    UserRoles(final String fieldName) {
        this.fieldName=fieldName;
    }
}
