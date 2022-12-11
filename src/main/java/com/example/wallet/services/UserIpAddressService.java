package com.example.wallet.services;

import com.example.wallet.readmodel.readonly.*;
import com.example.wallet.webui.LoginUserDTO;
import com.example.wallet.webui.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class UserIpAddressService {

    private static final int OVER_LIMIT_FAILED_ATTEMPTS = 4;

    private final UserService userService;
    private final IpAddressService ipAddressService;
    private final ValidateLoginService validateLoginService;

    public UserIpAddressService(final UserService userService, final IpAddressService ipAddressService,
                                final ValidateLoginService validateLoginService) {
        this.userService = userService;
        this.ipAddressService = ipAddressService;
        this.validateLoginService = validateLoginService;
    }

    public UserIpAddressProjection createUserWithIpAddress(final UserDTO userDTO, final String remoteAddress){
        final User user = userService.createUser(userDTO);
        final IpAddress ipAddressForRegisterUser = ipAddressService.saveIpAddressForUser(user.getUserId(), remoteAddress);
         return new UserIpAddressProjection(user, ipAddressForRegisterUser);
    }

    public UserVO createUserProjection(final LoginUserDTO loginUserDTO, final String remoteAddr) {
        final UserVO userByLogin = userService.findUserByLogin(loginUserDTO.login());
        final String hashPasswordFromDB = userByLogin.getUser().getPassword();
        final String hashPassword = userService.verifyPasswordHash(loginUserDTO.hashAlgorithm(), loginUserDTO.password(),
                userByLogin.getSaltForUser());

        if(userByLogin.getGeneratedToken().isEmpty() || !hashPassword.equals(hashPasswordFromDB)) {
            return setUnsuccessfulUserWithDetails(remoteAddr, loginUserDTO.login(), userByLogin);
        }

        return setSuccessfulUserWithDetails(userByLogin, loginUserDTO.login(), remoteAddr);
    }

    private UserVO setUnsuccessfulUserWithDetails(final String remoteAddress, final String login,
                                                  final UserVO userByLogin) {
        final UserVO userVO = new UserVO();
        final AuthenticatedUser authenticatedUser = (AuthenticatedUser) userByLogin.getUser();
        final Integer userId = authenticatedUser.getAuthenitactedUserData().getUserId();

        final List<IpAddress> ipAddressesForUser = ipAddressService.
                findIpAddressesForUser(userId);


        final LoginData loginData = createLoginData(remoteAddress, login);

        validateLoginService.addLoginAttemptionToList(loginData);
        final long unsuccessfulLoginTime = validateLoginService.countUnsuccessfulLogin(loginData);

        final LocalDateTime lastSuccessfulLoginTime = ipAddressService.findLastSuccessfulLoginTime(userId, remoteAddress);
        userVO.setLastSuccessfulLogin(lastSuccessfulLoginTime);
        userVO.setLastUnsuccessfulLogin(LocalDateTime.now());
        userVO.setUnsuccessfulAttempts(unsuccessfulLoginTime);
        userVO.setIpAddressList(ipAddressesForUser);
        userVO.setBlockedEndpoint(unsuccessfulLoginTime);
        ipAddressService.updateDataAfterUnsuccessfulLogin(userId, (int) unsuccessfulLoginTime);

        if(Objects.nonNull(userVO.getPermanentBlockedIpAddress())) {
            return createForbiddenUser();
        }

        userVO.setHttpStatus(HttpStatus.NOT_FOUND);

        return userVO;
    }

    private UserVO setSuccessfulUserWithDetails(final UserVO userByLogin, final String login, final String remoteAddress) {
        final AuthenticatedUser authenticatedUser = (AuthenticatedUser) userByLogin.getUser();
        final Integer userId = authenticatedUser.getAuthenitactedUserData().getUserId();
        final LocalDateTime lastUnSuccessfulLoginDate = ipAddressService.findLastUnSuccessfulLoginDate(userId, remoteAddress);

        validateLoginService.cleanLoginAttemptionIfExists(login);
        userByLogin.setHttpStatus(HttpStatus.OK);
        userByLogin.setLastUnsuccessfulLogin(lastUnSuccessfulLoginDate);
        userByLogin.setLastSuccessfulLogin(LocalDateTime.now());
        ipAddressService.updateDataAfterSuccessfulLogin(userId);
        return userByLogin;
    }

    private LoginData createLoginData(final String remoteAddress, final String login){
        final LoginData loginData = new LoginData(remoteAddress,login);
        loginData.setLastUnsuccesfulLogin(LocalDateTime.now());
        return loginData;
    }

    private UserVO createForbiddenUser(){
        final UserVO forbiddenAccountVO = new UserVO();
        forbiddenAccountVO.setHttpStatus(HttpStatus.FORBIDDEN);
        forbiddenAccountVO.setBlockedEndpoint(OVER_LIMIT_FAILED_ATTEMPTS);
        return forbiddenAccountVO;
    }

}
