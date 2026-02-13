package com.strata.xmlDocument.infrastructure.adapter.output.utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.xml.security.encryption.EncryptedKey;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.crypto.SecretKey;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.PrivateKey;
@Slf4j
public class Decrypter {
    static {
        org.apache.xml.security.Init.init();
    }

    private Decrypter() {
    }

    public static Document decrypts(String encryptedXml, PrivateKey privateKey) throws Exception {
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

    public static Document decrypt(String encryptedXml, PrivateKey privateKey) throws Exception {
        org.apache.xml.security.Init.init();
        String cleanXml = encryptedXml.trim();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);

        try {
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            dbf.setXIncludeAware(false);
            dbf.setExpandEntityReferences(false);
        } catch (Exception e) {
            System.err.println("Warning: Could not set all security features: " + e.getMessage());
        }

        DocumentBuilder db = dbf.newDocumentBuilder();

        Document document;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(cleanXml.getBytes(StandardCharsets.UTF_8))) {
            document = db.parse(bais);
        } catch (org.xml.sax.SAXParseException e) {
            throw new IllegalArgumentException(
                    "Failed to parse XML at line " + e.getLineNumber() + ", column " + e.getColumnNumber() +
                            ": " + e.getMessage() + ". Please check the XML format.", e
            );
        }

        Element encryptedKeyElement = (Element) document.getElementsByTagNameNS(
                "http://www.w3.org/2001/04/xmlenc#", "EncryptedKey"
        ).item(0);

        if (encryptedKeyElement == null) {
            throw new IllegalStateException("No EncryptedKey element found in the XML.");
        }

        XMLCipher keyCipher = XMLCipher.getInstance();
        keyCipher.init(XMLCipher.UNWRAP_MODE, privateKey);
        EncryptedKey encryptedKey = keyCipher.loadEncryptedKey(document, encryptedKeyElement);
        SecretKey secretKey = (SecretKey) keyCipher.decryptKey(encryptedKey, XMLCipher.AES_256);

        Element encryptedDataElement = (Element) document.getElementsByTagNameNS(
                "http://www.w3.org/2001/04/xmlenc#", "EncryptedData"
        ).item(0);

        if (encryptedDataElement == null) {
            throw new IllegalStateException("No EncryptedData element found in the XML.");
        }

        XMLCipher dataCipher = XMLCipher.getInstance();
        dataCipher.init(XMLCipher.DECRYPT_MODE, secretKey);
        dataCipher.doFinal(document, encryptedDataElement);

        return document;
    }

    public static String decryptToString(String encryptedXml, PrivateKey privateKey) throws Exception {
        Document decryptedDoc = decrypt(encryptedXml, privateKey);
        return documentToString(decryptedDoc, true);
    }

    public static String documentToString(Document document, boolean includeXmlDeclaration) throws Exception {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();

        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, includeXmlDeclaration ? "no" : "yes");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));

        return writer.toString();
    }

    private static String cleanInput(String xml) {
        if (xml == null || xml.isEmpty()) {
            throw new IllegalArgumentException("XML input cannot be null or empty");
        }

        String cleaned = xml.trim();

        // Remove UTF-8 BOM if present (EF BB BF)
        if (cleaned.startsWith("\uFEFF")) {
            cleaned = cleaned.substring(1);
        }

        while (!cleaned.isEmpty() && cleaned.charAt(0) < 0x20 && cleaned.charAt(0) != '\n' && cleaned.charAt(0) != '\r' && cleaned.charAt(0) != '\t') {
            cleaned = cleaned.substring(1);
        }

        // Final trim after BOM removal
        cleaned = cleaned.trim();

        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException("XML input is empty after cleaning");
        }

        return cleaned;
    }





}
