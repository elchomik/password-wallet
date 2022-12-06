package com.example.wallet.services;

import com.example.wallet.cryptography.SHA512Algorithm;
import com.example.wallet.readonly.AuthenticatedUser;
import com.example.wallet.readonly.UnAuthenticatedUser;
import com.example.wallet.readonly.User;
import com.example.wallet.readonly.UserProjection;
import com.example.wallet.repositories.UserRepository;
import com.example.wallet.webui.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final String LOGIN = "test";
    private static final String PASSWORD = "test_password";
    private static final boolean IS_PASSWORD_KEPT_AS_HASH = true;
    private static final String HASH_ALGORITHM = "SHA-512";
    private static final String pepper = "abcd1243";
    private static final String IP_ADDRESS = "0.0.0.1";

    public static String USER_PASSWORD;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveUserWithPasswordHashInDatabase(){
        //GIVEN
        final UserDTO testUser = new UserDTO(LOGIN, PASSWORD, IS_PASSWORD_KEPT_AS_HASH, HASH_ALGORITHM);

        final String salt = generateRandomSaltForNewRegisterUser();
        final String passwordToHash = salt + pepper + USER_PASSWORD;
        final String hashPassword = SHA512Algorithm.calculateSHA512(passwordToHash);
        final User userToSave = new User(testUser.getLogin(), hashPassword,salt,IS_PASSWORD_KEPT_AS_HASH);
        final User savedUser = new User(1,userToSave.getLogin(),
                userToSave.getPasswordHash(), userToSave.getSalt(),userToSave.isPasswordKeptAsHash());

        //WHEN
        lenient().when(userService.createUser(testUser, IP_ADDRESS)).thenReturn(userToSave);
        lenient().when(userRepository.save(userToSave)).thenReturn(savedUser);
        Mockito.inOrder(userService, userRepository);
        assertEquals(1, savedUser.getUserId());
    }

    @Test
    void shouldFindUserByLogin(){
        //GIVEN
        final UserDTO testUser = new UserDTO(LOGIN, PASSWORD, IS_PASSWORD_KEPT_AS_HASH, HASH_ALGORITHM);

        final String salt = generateRandomSaltForNewRegisterUser();
        final String passwordToHash = salt + pepper + USER_PASSWORD;
        final String hashPassword = SHA512Algorithm.calculateSHA512(passwordToHash);
        final User userToSave = new User(testUser.getLogin(), hashPassword,salt,IS_PASSWORD_KEPT_AS_HASH);
        final User savedUser = new User(1,userToSave.getLogin(),
                userToSave.getPasswordHash(), userToSave.getSalt(),userToSave.isPasswordKeptAsHash());
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(savedUser);

        lenient().when(userService.findUserByLogin(LOGIN, IP_ADDRESS))
                .thenReturn(new UserProjection(authenticatedUser, anyString()));
        lenient().when(userRepository.findUserByLogin(LOGIN)).thenReturn(savedUser);
        Mockito.inOrder(userService, userRepository);

        assertEquals(LOGIN, savedUser.getLogin());

    }

    @Test
    void shouldNotFindUserByLogin() {
        //Given
        final UnAuthenticatedUser unAuthenticatedUser = new UnAuthenticatedUser();
        lenient().when(userService.findUserByLogin(LOGIN,""))
                .thenReturn(new UserProjection(unAuthenticatedUser, ""));
        assertNull(unAuthenticatedUser.getUsername());

    }

    private String generateRandomSaltForNewRegisterUser(){
        final int lefLimit=48; // numeral '0'
        final int rightLimit=122; //letter 'z'
        final Random randomInteger= new Random();
        final int saltLength = randomInteger.nextInt(10);
        final Random randomString= new Random();
        return randomString.ints(lefLimit, rightLimit+1)
                .filter(i->(i<=57 || i>=65) && (i<=90 || i>=97))
                .limit(saltLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
