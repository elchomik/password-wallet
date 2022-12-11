package com.example.wallet.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.wallet.cryptography.HMACAlgorithm;
import com.example.wallet.cryptography.SHA512Algorithm;
import com.example.wallet.readmodel.readonly.AuthenticatedUser;
import com.example.wallet.readmodel.readonly.UnAuthenticatedUser;
import com.example.wallet.readmodel.readonly.User;
import com.example.wallet.readmodel.readonly.UserVO;
import com.example.wallet.repositories.UserRepository;
import com.example.wallet.webui.UpdateMasterPasswordDTO;
import com.example.wallet.webui.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements UserDetailsService {

    private static final String SHA512="SHA-512";
    private static final String HMAC_SHA512 = "HmacSHA512";

    private final UserRepository userRepository;
    private final long expirationTime;
    private final String secret;
    private final String pepper;

    public static String userPassword;
    public UserService(final UserRepository userRepository,
                       final @Value("${jwt.expirationTime}") long expirationTime,
                       final @Value("${jwt.secret}") String secret,
                       final @Value("${passwords.pepper}") String pepper) {
        this.userRepository = userRepository;
        this.expirationTime = expirationTime;
        this.secret = secret;
        this.pepper = pepper;
    }

    public User createUser(final UserDTO userDTO) {
        final String salt = generateRandomSaltForNewRegisterUser();
        userPassword = userDTO.getPassword();
        final String passwordToHash= salt + pepper + userDTO.getPassword();

        if(userDTO.isPasswordKeptAsHash()){
            return saveUserHashPassword(userDTO,userDTO.getHashAlgorithm(),salt,passwordToHash);
        }

        final User userToSave = getUserToSave(userDTO.getLogin(),userDTO.getPassword(),salt,false);
        return userRepository.save(userToSave);
    }


    private User saveUserHashPassword(final UserDTO userDTO, final String hashAlgorithm,final String salt,
                                      final String passwordToHash){
        final String hashPassword = getHashPassword(hashAlgorithm, passwordToHash);
        final User userToSave = getUserToSave(userDTO.getLogin(),hashPassword,salt,true);
        return userRepository.save(userToSave);
    }

    private String getHashPassword(final String hashAlgortihm, final String passwordToHash) {
        if(hashAlgortihm.equals(SHA512)){
            return SHA512Algorithm.calculateSHA512(passwordToHash);
        }
        return HMACAlgorithm.calculateHMAC(passwordToHash, userPassword);
    }

    public String verifyPasswordHash(final String hashAlgorithm, final String password, final String salt){
        final String passwordToHash = salt + pepper +password;
        userPassword = password;
        return getHashPassword(hashAlgorithm, passwordToHash);
    }

    private User getUserToSave(final String login, final String password, final String salt,
                               final boolean isPasswordKeptAsHash){
        return new User(login, password, salt,isPasswordKeptAsHash);
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

    public User updatedPassword(AuthenticatedUser authenticatedUser, final UpdateMasterPasswordDTO updateMasterPasswordDTO) {
        final Integer userId = authenticatedUser.getAuthenitactedUserData().getUserId();
        final String login = authenticatedUser.getUsername();
        final String salt = generateRandomSaltForNewRegisterUser();
        userPassword = updateMasterPasswordDTO.getPassword();
        final String passwordToHash = salt+ pepper+userPassword;
        final boolean passwordKeptAsHash = authenticatedUser.getAuthenitactedUserData().isPasswordKeptAsHash();
        if(passwordKeptAsHash && updateMasterPasswordDTO.getHashAlgorithm().equals(SHA512)){
            final String hashPassword = SHA512Algorithm.calculateSHA512(passwordToHash);
            return userRepository.save(new User(userId, login, hashPassword, salt, true));
        }
        else if(passwordKeptAsHash && updateMasterPasswordDTO.getHashAlgorithm().equals(HMAC_SHA512)){
            final String hashPassword = HMACAlgorithm.calculateHMAC(passwordToHash,userPassword);
            return userRepository.save(new User(userId, login, hashPassword, salt, true));
        }
        return userRepository.save(new User(userId, login,userPassword, salt, false));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       final User user = userRepository.findUserByLogin(username);
       if(Objects.isNull(user)){
          return new UnAuthenticatedUser();
       }
       return new AuthenticatedUser(user);
    }

    public UserVO findUserByLogin(final String login){
        final UserDetails userDetails = loadUserByUsername(login);
        if(!userDetails.isEnabled()){
            return new UserVO(userDetails, "", "");
        }
        if (!(userDetails instanceof final AuthenticatedUser authenticatedUser)) throw new AssertionError();
        final String saltForUser = authenticatedUser.getAuthenitactedUserData().getSalt();
        final String userToken = getUserToken(authenticatedUser);
        return new UserVO(authenticatedUser, userToken, saltForUser);
    }


    private String getUserToken(final AuthenticatedUser authenticatedUser){
        final String login = authenticatedUser.getAuthenitactedUserData().getLogin();
        return JWT.create()
                .withSubject(login)
                .withExpiresAt(new Date(System.currentTimeMillis()+ expirationTime))
                .sign(Algorithm.HMAC512(secret));
    }
}
