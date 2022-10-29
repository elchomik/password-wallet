package com.example.wallet.cryptography;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HMACAlgorithm {
    private static final String HMAC_SHA512 = "HmacSHA512";

    public static String calculateHMAC(final String text, final String key){
        Mac sha512Hmac;
        String result="";
        try{
        final byte[] byteKey = key.getBytes(StandardCharsets.UTF_8);
        sha512Hmac = Mac.getInstance(HMAC_SHA512);
        final SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA512);
        sha512Hmac.init(keySpec);
        final byte[] macData = sha512Hmac.doFinal(text.getBytes(StandardCharsets.UTF_8));
        result= Base64.getEncoder().encodeToString(macData);
        }catch (InvalidKeyException | NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return result;
    }
}
