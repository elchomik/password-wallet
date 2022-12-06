package com.example.wallet.readonly;

import java.time.LocalDateTime;
import java.util.Objects;

public class LoginData {

    private final String remoteAddress;
    private final String userName;

    private LocalDateTime lastSuccessfulLogin;
    private  LocalDateTime lastUnsuccesfulLogin;

    public LoginData(final String remoteAddress,  final String userName) {
        this.remoteAddress = remoteAddress;
        this.userName = userName;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public LocalDateTime getLastSuccessfulLogin() {
        return lastSuccessfulLogin;
    }

    public LocalDateTime getLastUnsuccesfulLogin() {
        return lastUnsuccesfulLogin;
    }

    public String getUserName() {
        return userName;
    }

    public void setLastUnsuccesfulLogin(LocalDateTime lastUnsuccesfulLogin) {
        this.lastUnsuccesfulLogin = lastUnsuccesfulLogin;
    }

    public void setLastSuccessfulLogin(LocalDateTime lastSuccessfulLogin) {
        this.lastSuccessfulLogin = lastSuccessfulLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginData loginData = (LoginData) o;
        return Objects.equals(remoteAddress, loginData.remoteAddress) && Objects.equals(lastSuccessfulLogin, loginData.lastSuccessfulLogin) && Objects.equals(lastUnsuccesfulLogin, loginData.lastUnsuccesfulLogin) && Objects.equals(userName, loginData.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(remoteAddress, lastSuccessfulLogin, lastUnsuccesfulLogin, userName);
    }
}
