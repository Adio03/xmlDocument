package com.strata.xmlDocument.infrastructure.adapter.output.utils;
import org.apache.xml.security.encryption.EncryptedData;
import org.apache.xml.security.encryption.EncryptedKey;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.keys.KeyInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.PublicKey;
public class Encrypter {
    static {
        // Initialize the XML Security library
        org.apache.xml.security.Init.init();
    }

    private Encrypter() {
        // Private constructor to prevent instantiation of this utility class.
    }

    public static String encrypt(Document doc, PublicKey pubKey) throws Exception {

        // 1. Generate AES-256 session key
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey sessionKey = keyGen.generateKey();

        // 2. Wrap session key with RSA public key (RSA-OAEP)
        XMLCipher keyCipher = XMLCipher.getInstance(XMLCipher.RSA_OAEP);
        keyCipher.init(XMLCipher.WRAP_MODE, pubKey);
        EncryptedKey encryptedKey = keyCipher.encryptKey(doc, sessionKey);

        // 3. Encrypt sensitive XML data using AES-256-GCM
        XMLCipher xmlCipher = XMLCipher.getInstance(XMLCipher.AES_256_GCM);
        xmlCipher.init(XMLCipher.ENCRYPT_MODE, sessionKey);

        // 4. Attach encrypted session key info inside <EncryptedData>
        EncryptedData encryptedData = xmlCipher.getEncryptedData();
        KeyInfo keyInfo = new KeyInfo(doc);
        keyInfo.add(encryptedKey);
        encryptedData.setKeyInfo(keyInfo);

        // 5. Locate the XML element to encrypt (example: <SensitiveData>)
        Element sensitiveData = (Element) doc.getElementsByTagName("SensitiveData").item(0);

        // 6. Perform encryption and replace the element with <EncryptedData>
        xmlCipher.doFinal(doc, sensitiveData, true);
        return xmlCipher.toString();
    }
}
