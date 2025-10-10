package com.strata.xmlDocument.infrastructure.adapter.output.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
@Slf4j
public class FileSaver {
    public static void saveDecryptMessageToFile(String decryptedXml) throws Exception {

        String filePath = "C:/Users/semicolon/Downloads/callbackresponses/debug_decrypted.xml";
        log.info("Saving to file =======>>>>>> {}", filePath);
        try (FileWriter writer = new FileWriter(filePath)) {
            log.info("about to save to file ======>>>>>> {}", writer.getEncoding());
            writer.write(decryptedXml);
            writer.flush();
            log.info("completely save to file");
        }
        log.info(" Decrypted XML written to file (for testing only): {}", filePath);

    }
}
