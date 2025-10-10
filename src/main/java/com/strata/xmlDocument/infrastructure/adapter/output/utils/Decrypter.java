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

    public static void saveDecryptMessageToFile(String decryptedXml) throws Exception {

        String filePath = "C:/Users/semicolon/Downloads/xmlDocument/debug_decrypted.xml";
        log.info("Saving to file =======>>>>>> {}", filePath);
        try (FileWriter writer = new FileWriter(filePath)) {
            log.info("about to save to file ======>>>>>> {}",writer.getEncoding());
            writer.write(decryptedXml);
            writer.flush();
            log.info("completely save to file");
        }
        log.info("✅ Decrypted XML written to file (for testing only): {}", filePath);

    }


    public static String documentToString(Document doc) throws Exception {
        javax.xml.transform.TransformerFactory factory = javax.xml.transform.TransformerFactory.newInstance();
        javax.xml.transform.Transformer transformer = factory.newTransformer();
        java.io.StringWriter sw = new java.io.StringWriter();
        javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(doc);
        javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(sw);
        transformer.transform(source, result);
        return sw.toString();
    }
}
