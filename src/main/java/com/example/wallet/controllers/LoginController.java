package com.example.wallet.controllers;

import com.example.wallet.readonly.User;
import com.example.wallet.readonly.UserProjection;
import com.example.wallet.services.UserService;
import com.example.wallet.webui.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping
public class LoginController {

    private final UserService userService;

    public LoginController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(final @RequestBody UserDTO userDTO) throws Exception {
        if(Objects.isNull(userDTO)){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        final User user = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/loginToApp")
    public ResponseEntity<UserProjection> loginUser(final UserDTO userDTO){
            final UserProjection userByLogin = userService.findUserByLogin(userDTO.getLogin());
            if(Objects.isNull(userByLogin)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.ok(userByLogin);
    }

}
