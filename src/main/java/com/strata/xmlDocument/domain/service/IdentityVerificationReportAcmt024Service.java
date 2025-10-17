package com.strata.xmlDocument.domain.service;

import com.strata.xmlDocument.application.input.IdentityVerificationReportAcmt024UseCase;
import com.strata.xmlDocument.domain.model.types.MessageTypes;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.VerificationRequest;
import com.strata.xmlDocument.infrastructure.adapter.output.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import java.security.PrivateKey;
import java.security.PublicKey;

@Slf4j
@Setter
@RequiredArgsConstructor
@Service
public class IdentityVerificationReportAcmt024Service implements IdentityVerificationReportAcmt024UseCase {


    @Value("${institution.id}")
    private String institutionId;
    @Value("$creator.name")
    private String creatorName;

    @Value("${nibss.private.key.path}")
    private String privateKeyPath;

    @Value("${nibss.public.key.path}")
    private String publicKeyPath;


    @Override
    public void identityVerificationReportInboundAcmt024(String encryptData) throws Exception {
        PrivateKey privateKey = GenerateKey.loadPrivateKey(privateKeyPath);
        PublicKey publicKey = GenerateKey.loadPublicKey(publicKeyPath);
        Document decryptedDocument = Decrypter.decrypt(encryptData,privateKey);
        Signer.validateXmlSignature(decryptedDocument,publicKey);
        String decryptedStringValue = XmlDocumentConverter.documentToString(decryptedDocument);
        log.info("DECRYPTED  ========>>>>>>> {}", decryptedStringValue);
        String messageType = MessageTypes.ACMT_024.name();
        FileSaver.saveDecryptMessageToFile(decryptedStringValue,messageType);
    }

    @Override
    public String identityVerificationReportOutboundAcmt024(VerificationRequest verificationRequest) {
        return "";
    }
}
