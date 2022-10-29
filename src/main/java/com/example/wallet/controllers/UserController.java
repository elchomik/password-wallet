package com.example.wallet.controllers;

import com.example.wallet.privilleges.roles.IsAuthenticatedUser;
import com.example.wallet.readonly.AuthenticatedUser;
import com.example.wallet.readonly.PasswordProjection;
import com.example.wallet.readonly.User;
import com.example.wallet.readonly.UserProjection;
import com.example.wallet.services.PasswordService;
import com.example.wallet.services.UserService;
import com.example.wallet.webui.UpdateMasterPasswordDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PasswordService passwordService;

    public UserController(final UserService userService, final PasswordService passwordService) {
        this.userService = userService;
        this.passwordService = passwordService;
    }

    @PutMapping("/changePassword")
    @IsAuthenticatedUser
    public ResponseEntity<User> changePassword(final @RequestBody UpdateMasterPasswordDTO updateMasterPasswordDTO,
                                               final Authentication authentication) throws Exception {
        final UserProjection userProjection = (UserProjection) authentication.getPrincipal();
        final AuthenticatedUser authenticatedUser = (AuthenticatedUser) userProjection.getUser();
        List<PasswordProjection> allPasswords = passwordService.getAllPasswords(authenticatedUser.getAuthenitactedUserData().getUserId(), true);
        final User user = userService.updatedPassword(authenticatedUser, updateMasterPasswordDTO);
        if(Objects.isNull(user.getUserId())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        HttpStatus httpStatusResponseEntity = passwordService.updatePasswords(user, allPasswords);
        return ResponseEntity.status(httpStatusResponseEntity).body(user);
    }
}
