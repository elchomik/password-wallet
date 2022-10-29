package com.example.wallet.cryptography;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA512Algorithm {

    private static final String SHA512="SHA-512";

    public static String calculateSHA512(final String password){
        try{
            //get an Instance of SHA-512
            final MessageDigest md = MessageDigest.getInstance(SHA512);

            //calculate message digest of the input string - returns byte array
            final byte[] messageDigest = md.digest(password.getBytes());

            //convert byte array into signum representation
            final BigInteger signumRepresentation = new BigInteger(1, messageDigest);

            //convert message digest into hex value
            String hashText= signumRepresentation.toString(16);

            //Add preceding 0s to make it 32bits
            while(hashText.length()<32){
                hashText= "0"+hashText;
            }

            return hashText;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
