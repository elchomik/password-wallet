package com.example.wallet.controllers;

import com.example.wallet.readmodel.readonly.UserIpAddressProjection;
import com.example.wallet.readmodel.readonly.UserVO;
import com.example.wallet.services.UserIpAddressService;
import com.example.wallet.webui.LoginUserDTO;
import com.example.wallet.webui.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping
public class LoginController {

    private final UserIpAddressService userIpAddressService;


    public LoginController(final UserIpAddressService userIpAddressService) {
        this.userIpAddressService = userIpAddressService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserIpAddressProjection> registerUser(final @RequestBody UserDTO userDTO,
                                                                final HttpServletRequest servletRequest) {
        if(Objects.isNull(userDTO)){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        final UserIpAddressProjection userWithIpAddress = userIpAddressService
                .createUserWithIpAddress(userDTO, servletRequest.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.CREATED).body(userWithIpAddress);
    }

    @GetMapping("/loginToApp")
    public ResponseEntity<UserVO> loginUser(final LoginUserDTO loginUserDTO,
                                            final HttpServletRequest request) {
        final UserVO userVO = userIpAddressService.createUserProjection(loginUserDTO,
                request.getRemoteAddr());
        return ResponseEntity.status(userVO.getHttpStatus()).body(userVO);
    }
}
