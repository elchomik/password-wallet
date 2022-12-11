package com.example.wallet.readmodel.readonly;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVO {

    private static final int TWO_TIMES_FAILED_ATTEMPT = 2;
    private static final int THREE_TIMES_FAILED_ATTEMPT = 3;
    private static final int FOUR_TIMES_FAILED_ATTEMPT = 4;
    private static final String FIVE_SECONDS = "5s";
    private static final String TEN_SECONDS = "10s";
    private static final String TWO_MINUTES = "120s";

    private UserDetails user;
    private String generatedToken;
    private String saltForUser;

    private LocalDateTime lastUnsuccessfulLogin;
    private LocalDateTime lastSuccessfulLogin;
    private HttpStatus httpStatus;
    private long unsuccessfulAttempts;
    private String blockedEndpoint;

    private String permanentBlockedIpAddress;

    private List<IpAddress> ipAddressList;


    public UserVO() {

    }

    public UserVO(final UserDetails user, final String generatedToken, final String saltForUser) {
        this.user = user;
        this.generatedToken = generatedToken;
        this.saltForUser = saltForUser;
    }

    public UserDetails getUser() {
        return user;
    }

    public String getGeneratedToken() {
        return generatedToken;
    }

    public LocalDateTime getLastUnsuccessfulLogin() {
        return lastUnsuccessfulLogin;
    }

    public LocalDateTime getLastSuccessfulLogin() {
        return lastSuccessfulLogin;
    }

    public long getUnsuccessfulAttempts() {
        return unsuccessfulAttempts;
    }

    @JsonIgnore
    public String getSaltForUser() {
        return saltForUser;
    }

    @JsonIgnore
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getBlockedEndpoint() {
        return blockedEndpoint;
    }

    public String getPermanentBlockedIpAddress() {
        return permanentBlockedIpAddress;
    }

    public void setUnsuccessfulAttempts(long unsuccessfulAttempts) {
        this.unsuccessfulAttempts = unsuccessfulAttempts;
    }

    public void setLastUnsuccessfulLogin(LocalDateTime lastUnsuccessfulLogin) {
        this.lastUnsuccessfulLogin = lastUnsuccessfulLogin;
    }

    public void setBlockedEndpoint(final long failedAttempts) {
        if (failedAttempts == TWO_TIMES_FAILED_ATTEMPT) {
            this.blockedEndpoint = FIVE_SECONDS;
        }

        if (failedAttempts == THREE_TIMES_FAILED_ATTEMPT) {
            this.blockedEndpoint = TEN_SECONDS;
        }

        if(failedAttempts >= FOUR_TIMES_FAILED_ATTEMPT){
            this.blockedEndpoint = TWO_MINUTES;
            this.permanentBlockedIpAddress = "ADDRESS IS BLOCKED PERMANENTLY";
        }
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setIpAddressList(List<IpAddress> ipAddressList) {
        this.ipAddressList = ipAddressList;
    }

    public void setLastSuccessfulLogin(LocalDateTime lastSuccessfulLogin) {
        this.lastSuccessfulLogin = lastSuccessfulLogin;
    }


}
