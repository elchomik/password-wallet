package com.example.wallet.services;

import com.example.wallet.readmodel.readonly.IpAddress;
import com.example.wallet.repositories.IpAddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IpAddressService {

    private static final int FAILED_ATTEMPT = 4;
    private final IpAddressRepository ipAddressRepository;

    public IpAddressService(IpAddressRepository ipAddressRepository) {
        this.ipAddressRepository = ipAddressRepository;
    }

    public IpAddress saveIpAddressForUser(final Integer userId, final String remoteAddr) {
        return ipAddressRepository.save(new IpAddress(remoteAddr, LocalDateTime.now(),false, userId));
    }

    @Transactional
    public void updateDataAfterUnsuccessfulLogin(final Integer userId, final Integer loginAttempt) {
        if (loginAttempt >= FAILED_ATTEMPT) {
            ipAddressRepository.updateIpAddressAfterUnsuccessfulLogin(LocalDateTime.now(), loginAttempt, userId);
        }
        ipAddressRepository.updateIpAddressAfterUnsuccessfulLoginWithBlockedAccount(LocalDateTime.now(), loginAttempt, userId);
    }

    public List<IpAddress> findIpAddressesForUser(final Integer userId) {
        return ipAddressRepository.findAllByUserId(userId);
    }

    public LocalDateTime findLastUnSuccessfulLoginDate(final Integer userId, final String remoteAddress) {
        return ipAddressRepository.findLastUnsuccessfulLoginTime(userId, remoteAddress);
    }

    @Transactional
    public void updateDataAfterSuccessfulLogin(final Integer userId) {
        ipAddressRepository.updateIpAddressAfterSuccessfulLogin(LocalDateTime.now(), userId);
    }

    public LocalDateTime findLastSuccessfulLoginTime(final Integer userId, final String remoteAddress) {
        return ipAddressRepository.findLastSuccessfulLoginTime(userId, remoteAddress);
    }
}
