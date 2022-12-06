package com.example.wallet.services;

import com.example.wallet.readonly.LoginData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ValidateLoginService {

    private final List<LoginData> loginDataList;

    public ValidateLoginService() {
        this.loginDataList = new ArrayList<>();
    }

    public long countUnsuccessfulLogin(final LoginData loginData) {
        return loginDataList.stream()
                .filter(loginData1 -> loginData1.getUserName().equals(loginData.getUserName()))
                .map(LoginData::getLastUnsuccesfulLogin).count();
    }

    public void cleanLoginAttemptionIfExists(final String login) {
        loginDataList.removeIf(loginData -> loginData.getUserName().equals(login));
    }

    public void addLoginAttemptionToList(final LoginData loginData){
        loginDataList.add(loginData);
    }

}
