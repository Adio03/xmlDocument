package com.strata.xmlDocument.infrastructure.adapter.output.utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.xml.security.encryption.EncryptedData;
import org.apache.xml.security.encryption.EncryptedKey;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.keys.KeyInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.security.PublicKey;
@Slf4j
public class Encrypter {
    static {

        org.apache.xml.security.Init.init();
    }

    private Encrypter() {

    }

    public static String encrypt(Document document, PublicKey pubKey) throws Exception {

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey sessionKey = keyGen.generateKey();


        XMLCipher keyCipher = XMLCipher.getInstance(XMLCipher.RSA_OAEP);
        keyCipher.init(XMLCipher.WRAP_MODE, pubKey);
        EncryptedKey encryptedKey = keyCipher.encryptKey(document, sessionKey);

        XMLCipher xmlCipher = XMLCipher.getInstance(XMLCipher.AES_256_GCM);
        xmlCipher.init(XMLCipher.ENCRYPT_MODE, sessionKey);


        EncryptedData encryptedData = xmlCipher.getEncryptedData();
        KeyInfo keyInfo = new KeyInfo(document);
        keyInfo.add(encryptedKey);
        encryptedData.setKeyInfo(keyInfo);

        Element elementToEncrypt = (Element) document.getElementsByTagName("IdVrfctnReq").item(0);


        if (elementToEncrypt == null) {
            throw new IllegalStateException("Element 'IdVrfctnReq' not found in document!");
        }

        log.info("Encrypting element: {}", elementToEncrypt.getTagName());

        xmlCipher.doFinal(document, elementToEncrypt, true);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));

        return writer.toString();
    }
}
