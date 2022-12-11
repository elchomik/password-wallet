package com.example.wallet.readmodel.readonly;

import com.example.wallet.privilleges.roles.UserRoles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class AuthenticatedUser implements UserDetails {

    private final User user;

    public AuthenticatedUser(final User user) {
        this.user = user;
    }

    public User getAuthenitactedUserData(){
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.<GrantedAuthority>singletonList(new SimpleGrantedAuthority(UserRoles.AUTHENTICATED_USER.name()));
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return user.getLogin();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
