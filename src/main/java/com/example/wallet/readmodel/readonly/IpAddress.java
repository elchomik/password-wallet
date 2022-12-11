package com.example.wallet.readmodel.readonly;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ipAddress")
public class IpAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ipAddressId;

    @Column
    private String addressIp;

    @Column
    private LocalDateTime successfulLoginTime;

    @Column
    private LocalDateTime unsuccessfulLoginTime;

    @Column
    private Integer failedLoginAttempt;

    @Column
    private boolean isAccountBlocked;

    @Column
    private Integer userId;

    public IpAddress() {
    }

    public IpAddress(final String addressIp, final LocalDateTime successfulLoginTime,
                     final boolean isAccountBlocked, final Integer userId) {
        this.addressIp = addressIp;
        this.successfulLoginTime = successfulLoginTime;
        this.isAccountBlocked = isAccountBlocked;
        this.userId = userId;
    }

    public Integer getIpAddressId() {
        return ipAddressId;
    }

    public String getAddressIp() {
        return addressIp;
    }

    public LocalDateTime getSuccessfulLoginTime() {
        return successfulLoginTime;
    }

    public LocalDateTime getUnsuccessfulLoginTime() {
        return unsuccessfulLoginTime;
    }

    public Integer getFailedLoginAttempt() {
        return failedLoginAttempt;
    }

    public boolean isAccountBlocked() {
        return isAccountBlocked;
    }

    public Integer getUserId() {
        return userId;
    }

}
