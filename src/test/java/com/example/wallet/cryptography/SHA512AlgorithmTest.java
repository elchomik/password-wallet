package com.example.wallet.cryptography;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

public class SHA512AlgorithmTest {

    private static final String SHA512 = "SHA-512";
    private static final String ALGORITHM = "ALGORITHM";


    @ParameterizedTest
    @ValueSource(strings = {"password"})
    void shouldCalculateProperLengthSHA512ForGivenPassword(final String password) throws NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance(SHA512);

        //calculate message digest of the input string - returns byte array
        final byte[] messageDigest = md.digest(password.getBytes());

        //convert byte array into signum representation
        final BigInteger signumRepresentation = new BigInteger(1, messageDigest);

        //convert message digest into hex value
        StringBuilder hashText= new StringBuilder(signumRepresentation.toString(16));

        //Add preceding 0s to make it 32bits
        while(hashText.length()<32){
            hashText.insert(0, "0");
        }
        assertEquals(128, hashText.length());
    }

    @ParameterizedTest
    @ValueSource(strings = {"password"})
    void shouldThrowNoSuchAlgorithmExceptionForAlgorithm(final String password){
       assertThrows(NoSuchAlgorithmException.class, () -> {
            final MessageDigest md = MessageDigest.getInstance(ALGORITHM);

            //calculate message digest of the input string - returns byte array
            final byte[] messageDigest = md.digest(password.getBytes());

            //convert byte array into signum representation
            final BigInteger signumRepresentation = new BigInteger(1, messageDigest);

            //convert message digest into hex value
            StringBuilder hashText= new StringBuilder(signumRepresentation.toString(16));

            //Add preceding 0s to make it 32bits
            while(hashText.length()<32){
                hashText.insert(0, "0");
            }
        });

    }

}
