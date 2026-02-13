package com.strata.xmlDocument.infrastructure.adapter.output.utils;

import lombok.extern.slf4j.Slf4j;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class FileSaver {
    public static void saveDecryptMessageToFile(String decryptedXml, String messageType) throws Exception {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = messageType + "_debug_decrypted_" + timestamp + ".xml";
        String filePath = "C:/Users/semicolon/Downloads/callbackresponses/" + fileName;

        // Pretty print using Transformer (without reparsing document)
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        transformer.transform(
                new StreamSource(new StringReader(decryptedXml)),
                new StreamResult(new File(filePath))
        );

        System.out.println("✅ Saved formatted decrypted XML to: " + filePath);
    }

}
