package com.strata.xmlDocument.infrastructure.adapter.input.controller;

import com.strata.xmlDocument.application.input.IdentityVerificationAcmt023UseCase;
import com.strata.xmlDocument.domain.service.XmlCommunicationService;
import com.strata.xmlDocument.infrastructure.adapter.input.VerificationRequest;
import com.strata.xmlDocument.infrastructure.adapter.output.utils.KeyGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

@RestController
@RequestMapping("/api/xml")
@RequiredArgsConstructor
public class XmlCommunicationController {

    private final IdentityVerificationAcmt023UseCase  identityVerificationAcmt023UseCase;


    @PostMapping("/send")
    public ResponseEntity<String> sendXmlRequest(@RequestBody VerificationRequest request) throws Exception {
        String response = identityVerificationAcmt023UseCase.identityVerification(request);
        return ResponseEntity.accepted().body(response);
    }


//    @PostMapping("/receive")
//    public ResponseEntity<String> receiveXml(@RequestBody String encryptedXml) throws Exception {
//        KeyPair keyPair = KeyGenerator.generateKeyPair();
//        PrivateKey privateKey = KeyGenerator.getPrivateKey(keyPair);
//
//        String decryptedXml = service.decryptAndVerifyXml(encryptedXml, privateKey);
//
//        String filePath = "C:/Users/semicolon/Downloads/xmlDocument/received_decrypted.xml";
//        try (java.io.FileWriter writer = new java.io.FileWriter(filePath)) {
//            writer.write(decryptedXml);
//        }
//
//        return ResponseEntity.ok("XML received, decrypted and saved to file: " + filePath);
//    }
}