package com.example.wallet.readmodel.readonly;

import com.example.wallet.readmodel.readonly.IpAddress;
import com.example.wallet.readmodel.readonly.User;

public class UserIpAddressProjection {

    private User createdUser;
    private IpAddress ipAddress;

    public UserIpAddressProjection(User createdUser, IpAddress ipAddress) {
        this.createdUser = createdUser;
        this.ipAddress = ipAddress;
    }

    public User getCreatedUser() {
        return createdUser;
    }

    public IpAddress getIpAddress() {
        return ipAddress;
    }
}
