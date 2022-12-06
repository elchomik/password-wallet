package com.example.wallet.controllers;

import com.example.wallet.readonly.LoginData;
import com.example.wallet.readonly.UnAuthenticatedUser;
import com.example.wallet.readonly.User;
import com.example.wallet.readonly.UserProjection;
import com.example.wallet.services.UserService;
import com.example.wallet.services.ValidateLoginService;
import com.example.wallet.webui.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping
public class LoginController {

    private final UserService userService;
    private final ValidateLoginService validateLoginService;

    public LoginController(final UserService userService, final ValidateLoginService validateLoginService) {
        this.userService = userService;
        this.validateLoginService =validateLoginService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(final @RequestBody UserDTO userDTO,
                                             final HttpServletRequest servletRequest) throws Exception {
        if(Objects.isNull(userDTO)){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        final User user = userService.createUser(userDTO, servletRequest.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/loginToApp")
    public ResponseEntity<UserProjection> loginUser(final UserDTO userDTO,
                                                    final HttpServletRequest request){
            final UserProjection userByLogin = userService.findUserByLogin(userDTO.getLogin(),
                    request.getRemoteAddr());
            if(userByLogin.getGeneratedToken().isEmpty()) {
                final UserProjection userProjection = new UserProjection(null,"");
                userProjection.setLastUnsuccessfulLogin(LocalDateTime.now());
                final LoginData loginData = new LoginData(request.getRemoteAddr(),userDTO.getLogin());
                loginData.setLastUnsuccesfulLogin(LocalDateTime.now());
                validateLoginService.addLoginAttemptionToList(loginData);
                long unsuccessfulLoginTime = validateLoginService.countUnsuccessfulLogin(loginData);
                userProjection.setUnsuccessfulAttempts(unsuccessfulLoginTime);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userProjection);
            }
            validateLoginService.cleanLoginAttemptionIfExists(userDTO.getLogin());
            return ResponseEntity.ok(userByLogin);
    }

}
