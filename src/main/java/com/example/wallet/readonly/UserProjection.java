package com.example.wallet.readonly;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProjection {

    private final UserDetails user;
    private final String generatedToken;

    private LocalDateTime lastUnsuccessfulLogin;
    private long unsuccessfulAttempts;


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

    public LocalDateTime getLastSuccessfulLogin() {
        if(Objects.nonNull(user)) {
            final AuthenticatedUser authenticatedUser = (AuthenticatedUser) user;
            return ((AuthenticatedUser) user).getAuthenitactedUserData().getLoginTime();
        }
        return null;
    }

    public LocalDateTime getLastUnsuccessfulLogin() {
        return lastUnsuccessfulLogin;
    }

    public long getUnsuccessfulAttempts() {
        return unsuccessfulAttempts;
    }

    public void setUnsuccessfulAttempts(long unsuccessfulAttempts) {
        this.unsuccessfulAttempts = unsuccessfulAttempts;
    }

    public void setLastUnsuccessfulLogin(LocalDateTime lastUnsuccessfulLogin) {
        this.lastUnsuccessfulLogin = lastUnsuccessfulLogin;
    }
}
