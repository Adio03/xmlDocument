package com.strata.xmlDocument.infrastructure.adapter.output.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
@Slf4j
public class GenerateKey {
    public static void main(String[] args) {
        String privateKeyName = "banks_private.pem";
        String publicKeyName = "banks_public.pem";
        Map<String, Object> keys = generateKeyPair(privateKeyName, publicKeyName);

        try (FileOutputStream fos = new FileOutputStream(privateKeyName)) {
            fos.write(((String) keys.get(privateKeyName)).getBytes());
            System.out.println("Private key saved to " + privateKeyName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileOutputStream fos = new FileOutputStream(publicKeyName)) {
            fos.write(((String) keys.get(publicKeyName)).getBytes());
            System.out.println("Public key saved to " + publicKeyName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Object> generateKeyPair(String privateKeyName, String publicKeyName) {
        try {
            Map<String, Object> result = new LinkedHashMap<>();
            KeyPairGenerator pairGenerator = KeyPairGenerator.getInstance("RSA");
            pairGenerator.initialize(2048);
            KeyPair keyPair = pairGenerator.generateKeyPair();
            result.put(privateKeyName, convertKeyToPEM(keyPair.getPrivate()));
            result.put(publicKeyName, convertKeyToPEM(keyPair.getPublic()));
            return result;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static PrivateKey loadPrivateKey(String filePath) throws Exception {
        String key = new String(Files.readAllBytes(Paths.get(filePath)))
                .replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s+", "");

        byte[] decoded = Base64.getDecoder().decode(key);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }


    public static PublicKey loadPublicKey(String filePath) throws Exception {
        String key = new String(Files.readAllBytes(Paths.get(filePath)))
                .replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s+", "");

        byte[] decoded = Base64.getDecoder().decode(key);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    private static String convertKeyToPEM(Key key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");

        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        String header;
        String footer;

        if (key instanceof PublicKey) {
            header = "-----BEGIN PUBLIC KEY-----";
            footer = "-----END PUBLIC KEY-----";
        } else if (key instanceof PrivateKey) {
            header = "-----BEGIN PRIVATE KEY-----";
            footer = "-----END PRIVATE KEY-----";
        } else {
            throw new IllegalArgumentException("Unsupported key type: " + key.getClass().getName());
        }
        StringBuilder wrappedKey = new StringBuilder();
        for (int i = 0; i < encodedKey.length(); i += 64) {
            int end = Math.min(i + 64, encodedKey.length());
            wrappedKey.append(encodedKey, i, end).append("\n");
        }

        return header + "\n" + wrappedKey + footer;
    }
}
