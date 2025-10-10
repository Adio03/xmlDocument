package com.strata.xmlDocument.infrastructure.adapter.output.utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.xml.security.encryption.XMLCipher;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
@Slf4j
public class Decrypter {
    static {
        org.apache.xml.security.Init.init();
    }

    private Decrypter() {
    }

    public static Document decrypt(String encryptedXml, PrivateKey privateKey) throws Exception {
        log.info("Starting XML decryption");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        Document document = documentBuilder.parse(
                new ByteArrayInputStream(encryptedXml.getBytes(StandardCharsets.UTF_8))
        );

        Element encryptedDataElement = (Element) document.getElementsByTagNameNS(
                "http://www.w3.org/2001/04/xmlenc#",
                "EncryptedData"
        ).item(0);

        if (encryptedDataElement == null) {
            log.warn("No EncryptedData element found - returning original document");
            return document;
        }

        log.info("Found EncryptedData element, proceeding with decryption");

        XMLCipher xmlCipher = XMLCipher.getInstance();
        xmlCipher.init(XMLCipher.DECRYPT_MODE, null);
        xmlCipher.setKEK(privateKey);

        xmlCipher.doFinal(document, encryptedDataElement);

        return document;
    }



}
