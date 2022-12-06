package com.example.wallet.readonly;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "appUser")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String login;
    private String passwordHash;
    private String salt;
    private boolean isPasswordKeptAsHash;

    @Column
    private boolean isLoginSuccessful;

    @Column
    private LocalDateTime loginTime;
    @Column
    private String ipAddress;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId",insertable = false,updatable = false)
    private List<Password> passwords;
    public User(final String login, final String passwordHash,
                final String salt, final boolean isPasswordKeptAsHash) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.isPasswordKeptAsHash = isPasswordKeptAsHash;
    }

    public User() {}

    public User(final Integer userId, final String login, final String passwordHash,final  String salt,
                final boolean isPasswordKeptAsHash) {
        this.userId = userId;
        this.login = login;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.isPasswordKeptAsHash = isPasswordKeptAsHash;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getLogin() {
        return login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getSalt() {
        return salt;
    }

    public boolean isPasswordKeptAsHash() {
        return isPasswordKeptAsHash;
    }

    public boolean isLoginSuccessful() {
        return isLoginSuccessful;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setLoginSuccessful(boolean loginSuccessful) {
        isLoginSuccessful = loginSuccessful;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public void setPasswordHash(final String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
