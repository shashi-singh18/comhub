package com.shashi.comhub.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

public class RsaKeyGenerator {

    public static void main(String[] args) throws Exception {

        KeyPairGenerator keyPairGenerator =
                KeyPairGenerator.getInstance("RSA");

        keyPairGenerator.initialize(2048);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        String privateKey = Base64.getEncoder()
                .encodeToString(keyPair.getPrivate().getEncoded());

        String publicKey = Base64.getEncoder()
                .encodeToString(keyPair.getPublic().getEncoded());

        System.out.println("PRIVATE KEY:");
        System.out.println(privateKey);

        System.out.println("\nPUBLIC KEY:");
        System.out.println(publicKey);
    }
}