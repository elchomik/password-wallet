package com.example.wallet.repositories;

import com.example.wallet.readmodel.readonly.IpAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface IpAddressRepository  extends JpaRepository<IpAddress, Integer> {

    List<IpAddress> findAllByUserId(Integer userId);

    @Modifying
    @Query( value = "update IpAddress ip set ip.unsuccessfulLoginTime =:loginTime, ip.failedLoginAttempt =:loginAttempt where ip.userId =:userId")
    void updateIpAddressAfterUnsuccessfulLogin(@Param("loginTime") final LocalDateTime loginTime, final Integer loginAttempt, final Integer userId);

    @Query(value = "select ip.successfulLoginTime from IpAddress ip where ip.userId =:userId and ip.addressIp =:remoteAddress ")
    LocalDateTime findLastSuccessfulLoginTime(@Param("userId") final Integer userId, final String remoteAddress);

    @Modifying
    @Query( value = "update IpAddress ip set ip.successfulLoginTime =:loginTime, ip.failedLoginAttempt = 0 where ip.userId =:userId")
    void updateIpAddressAfterSuccessfulLogin(@Param("loginTime") final LocalDateTime loginTime, @Param("userId") final Integer userId);

    @Query(value ="select ip.unsuccessfulLoginTime from IpAddress ip where ip.userId =:userId and ip.addressIp =:remoteAddress ")
    LocalDateTime findLastUnsuccessfulLoginTime(@Param("userId") final Integer userId, final String remoteAddress);

    @Modifying
    @Query( value = "update IpAddress ip set ip.isAccountBlocked=true, ip.unsuccessfulLoginTime =:loginTime, ip.failedLoginAttempt =:loginAttempt where ip.userId =:userId")
    void updateIpAddressAfterUnsuccessfulLoginWithBlockedAccount(@Param("loginTime") final LocalDateTime now,
                                                                 final Integer loginAttempt, final Integer userId);
}
