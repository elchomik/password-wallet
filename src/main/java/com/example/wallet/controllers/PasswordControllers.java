package com.example.wallet.controllers;

import com.example.wallet.privilleges.roles.IsAuthenticatedUser;
import com.example.wallet.readonly.*;
import com.example.wallet.services.PasswordService;
import com.example.wallet.webui.PasswordDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/passwords")
public class PasswordControllers {

    private final PasswordService passwordService;

    public PasswordControllers(final PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PostMapping
    @IsAuthenticatedUser
    public ResponseEntity<Password> createPassword(final @RequestBody PasswordDTO passwordDTO,
                                                   final Authentication authentication) throws Exception {
        final UserProjection userProjection = (UserProjection) authentication.getPrincipal();
        final Password passwordInWallet = passwordService.createPasswordInWallet(passwordDTO, userProjection.getUser());
        if(Objects.isNull(passwordInWallet.getId())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
       return ResponseEntity.status(HttpStatus.CREATED).body(passwordInWallet);
    }

    @GetMapping("/allPasswords")
    @IsAuthenticatedUser
    public ResponseEntity<List<String>> getAllPasswordsForCurrentUser(final boolean shouldDecryptPassword,
                                                                        final Authentication authentication) throws Exception {
        final UserProjection userProjection = (UserProjection) authentication.getPrincipal();
        final AuthenticatedUser authenticatedUser = (AuthenticatedUser) userProjection.getUser();
        final Integer userId = authenticatedUser.getAuthenitactedUserData().getUserId();
        final List<String> allPasswords = passwordService.getAllPasswords(userId, shouldDecryptPassword).stream()
                .map(PasswordProjection::getPassword).collect(Collectors.toList());

        if(allPasswords.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(allPasswords);
    }

}
