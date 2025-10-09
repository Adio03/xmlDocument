package com.strata.xmlDocument.infrastructure.adapter.output.utils;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;


public class KeyGenerator {

    public static KeyPair generateKeyPair() throws Exception {
        java.security.KeyPairGenerator keyGen = java.security.KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }


    public static PublicKey getPublicKey(KeyPair keyPair) {
        return keyPair.getPublic();
    }

    public static PrivateKey getPrivateKey(KeyPair keyPair) {
        return keyPair.getPrivate();
    }
}
