package com.strata.xmlDocument.infrastructure.adapter.output.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

@Slf4j
public class KeyGenerator {

    public static KeyPair generateKeyPair() throws Exception {
        java.security.KeyPairGenerator keyGen = java.security.KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }


    public static PublicKey getPublicKey(KeyPair keyPair) {
        log.info("Public key =====>>>>>  {}",keyPair.getPublic());
        return keyPair.getPublic();
    }

    public static PrivateKey getPrivateKey(KeyPair keyPair) {
        log.info("Private key  =======>>>>  {}",keyPair.getPrivate());
        return keyPair.getPrivate();
    }
}
