package com.example.wallet.cryptography;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptionAndDecryptionPasswords {
    private static final String MD5= "MD5";
    private static final String AES = "AES";

    public static String enrypt(final String data, final Key key, final String algorithm) throws Exception{
        final Cipher cipher = Cipher.getInstance(algorithm);
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        final IvParameterSpec ivspec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE,key,ivspec);
        final byte[] encryptedValue= cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedValue);
    }

    public static String decrypt(final String encryptedData, final Key key, final String algorithm) throws Exception{
        final Cipher cipher = Cipher.getInstance(algorithm);
        final byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        final IvParameterSpec ivspec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key,ivspec);
        final byte[] decodedValue = Base64.getDecoder().decode(encryptedData);
        final byte[] decprytedValue = cipher.doFinal(decodedValue);
        return new String(decprytedValue);
    }

    private static byte[] calculateMD5(final String text) {
        try{
            final MessageDigest md = MessageDigest.getInstance(MD5);
            final byte[] messageDigest = md.digest(text.getBytes());
            return messageDigest;
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }

    public static Key generateKey(final String password) throws Exception {
        return new SecretKeySpec(calculateMD5(password),AES);
    }
}
