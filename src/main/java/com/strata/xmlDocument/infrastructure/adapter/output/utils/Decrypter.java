package com.strata.xmlDocument.infrastructure.adapter.output.utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.xml.security.encryption.XMLCipher;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.security.PrivateKey;
@Slf4j
public class Decrypter {
    static {
        org.apache.xml.security.Init.init();
    }

    private Decrypter() {
    }

    public static String decrypt(String encryptedXml, PrivateKey privateKey) throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        Document document = documentBuilder.parse(new ByteArrayInputStream(encryptedXml.getBytes()));

        // STEP 2: Locate the <EncryptedData> element in the XML
        Element encryptedDataElement = (Element) document.getElementsByTagNameNS(
                "http://www.w3.org/2001/04/xmlenc#", "EncryptedData").item(0);

        if (encryptedDataElement == null) {
            return encryptedXml;
        }
        XMLCipher xmlCipher = XMLCipher.getInstance();
        xmlCipher.init(XMLCipher.DECRYPT_MODE, null);
        xmlCipher.setKEK(privateKey);

        // STEP 4: Perform decryption and replace <EncryptedData> with original content
        xmlCipher.doFinal(document, encryptedDataElement);

        // STEP 5: Convert decrypted Document back to string and return

        return documentToString(document);
    }

    public static void saveDecryptMessageToFile(String decryptedXml) throws Exception {

        String filePath = "C:/Users/semicolon/Downloads/xmlDocument/debug_decrypted.xml";

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(decryptedXml);
            writer.flush();
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
