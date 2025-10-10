package com.strata.xmlDocument.domain.service;

import com.strata.xmlDocument.application.input.IdentityVerificationReportAcmt024UseCase;
import com.strata.xmlDocument.infrastructure.adapter.output.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.w3c.dom.Document;

import java.security.PrivateKey;
import java.security.PublicKey;

@Setter
@RequiredArgsConstructor
public class IdentityVerificationReportAcmt024Service implements IdentityVerificationReportAcmt024UseCase {

    private static final String INSTITUTION_ID = "999058";
    private static final String CREATOR_NAME  = "NIBSS";
    private static final String PRIVATE_KEY_PATH = "C:\\Users\\semicolon\\Downloads\\xmlDocument\\xmlDocument\\banks_private.pem";
    private static final String PUBLIC_KEY_PATH = "C:\\Users\\semicolon\\Downloads\\xmlDocument\\xmlDocument\\banks_public.pem";
    private static final String API_URL = "https://api.example.com/verify";


    @Override
    public void recieveAcmt024CallBack(String encryptData) throws Exception {
        PrivateKey privateKey = GenerateKey.loadPrivateKey(PRIVATE_KEY_PATH);
        PublicKey publicKey = GenerateKey.loadPublicKey(PUBLIC_KEY_PATH);

        Document decryptedDocument = Decrypter.decrypt(encryptData,privateKey);
        Signer.validateXmlSignature(decryptedDocument,publicKey);
        String decryptedStringValue = XmlDocumentConverter.documentToString(decryptedDocument);

        FileSaver.saveDecryptMessageToFile(decryptedStringValue);

    }
}
