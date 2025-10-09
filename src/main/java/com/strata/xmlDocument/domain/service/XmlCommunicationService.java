package com.strata.xmlDocument.domain.service;

import com.strata.xmlDocument.infrastructure.adapter.input.VerificationRequest;
import com.strata.xmlDocument.infrastructure.adapter.output.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

import static com.strata.xmlDocument.infrastructure.adapter.output.utils.Decrypter.saveDecryptMessageToFile;

@Service
@EnableAsync
@Slf4j
public class XmlCommunicationService {


    public String decryptAndVerifyXml(String encryptedXml, PrivateKey privateKey) throws Exception {
        // Decrypt
        return Decrypter.decrypt(encryptedXml, privateKey);
        // TODO: Add signature verification if needed
    }

    @Async
    public CompletableFuture<Void> processXmlAsync(VerificationRequest request) {
        try {
            String creDtTm = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
            String xml = XmlTemplate.generateXml(
                    request.getMsgId(),
                    creDtTm,
                    request.getSendingPartyName(),
                    request.getSourceId(),
                    request.getBeneficiaryId(),
                    request.getPartyToVerifyName(),
                    request.getAccountNumber()
            );
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(new InputSource(new StringReader(xml)));
            KeyPair keyPair = KeyGenerator.generateKeyPair();
            PrivateKey privateKey = KeyGenerator.getPrivateKey(keyPair);
            PublicKey publicKey = KeyGenerator.getPublicKey(keyPair);

            Signer.sign(document,privateKey);

          String encryptedDocument =  Encrypter.encrypt(document,publicKey);
          log.info("Encrypted Document =====>>> {}", encryptedDocument);


            log.info("Document =========>>>>>>>> {}",document);

            String xmlContent = Decrypter.documentToString(document);
            log.info("Document Content:\n{}", xmlContent);


           String decryptContent = Decrypter.decrypt(xmlContent,privateKey);

            saveDecryptMessageToFile(decryptContent);


            log.info("Generated XML: {}", xml);

            // You can call RestTemplate/WebClient here if you want
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(null);
    }

    private String documentToString(Document doc) throws Exception {
        javax.xml.transform.TransformerFactory factory = javax.xml.transform.TransformerFactory.newInstance();
        javax.xml.transform.Transformer transformer = factory.newTransformer();
        java.io.StringWriter sw = new java.io.StringWriter();
        javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(doc);
        javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(sw);
        transformer.transform(source, result);
        return sw.toString();
    }
}