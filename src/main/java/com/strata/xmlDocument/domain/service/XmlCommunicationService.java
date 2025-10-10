package com.strata.xmlDocument.domain.service;

import com.strata.xmlDocument.application.input.IdentityVerificationAcmt023UseCase;
import com.strata.xmlDocument.domain.model.IdentityVerificationAcmt023;
import com.strata.xmlDocument.infrastructure.adapter.input.VerificationRequest;
import com.strata.xmlDocument.infrastructure.adapter.output.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import org.w3c.dom.Document;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@EnableAsync
@Slf4j
public class XmlCommunicationService implements IdentityVerificationAcmt023UseCase {


    private static final String INSTITUTION_ID = "999058";
    private static final String CREATOR_NAME  = "NIBSS";
    private static final String PRIVATE_KEY_PATH = "C:\\Users\\semicolon\\Downloads\\xmlDocument\\xmlDocument\\kolomoni_private.pem";
    private static final String PUBLIC_KEY_PATH = "C:\\Users\\semicolon\\Downloads\\xmlDocument\\xmlDocument\\kolomoni_public.pem";
    private static final String API_URL = "https://api.example.com/verify";
    private final LocalDateTime localDateTime = LocalDateTime.now();





//    public String decryptAndVerifyXml(String encryptedXml, PrivateKey privateKey) throws Exception {
//        // Decrypt
//        return Decrypter.decrypt(encryptedXml, privateKey);
//        // TODO: Add signature verification if needed
//    }
//
//    @Async
//    public CompletableFuture<Void> processXmlAsync(VerificationRequest request) {
//        try {
//            String creDtTm = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
//            String xml = XmlTemplate.generateXml(
//                    request.getMsgId(),
//                    creDtTm,
//                    request.getSendingPartyName(),
//                    request.getSourceId(),
//                    request.getBeneficiaryId(),
//                    request.getPartyToVerifyName(),
//                    request.getAccountNumber()
//            );
//            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//            dbf.setNamespaceAware(true);
//            DocumentBuilder db = dbf.newDocumentBuilder();
//            Document document = db.parse(new InputSource(new StringReader(xml)));
//            KeyPair keyPair = KeyGenerator.generateKeyPair();
//            PrivateKey privateKey = KeyGenerator.getPrivateKey(keyPair);
//            PublicKey publicKey = KeyGenerator.getPublicKey(keyPair);
//
//            Signer.sign(document,privateKey);
//
//          String encryptedDocument =  Encrypter.encrypt(document,publicKey);
//          log.info("Encrypted Document =====>>> {}", encryptedDocument);
//
//
//            log.info("Document =========>>>>>>>> {}",document);
//
//            String xmlContent = Decrypter.documentToString(document);
//            log.info("Document Content:\n{}", xmlContent);
//
//
//           String decryptContent = Decrypter.decrypt(xmlContent,privateKey);
//
//            saveDecryptMessageToFile(decryptContent);
//
//
//            log.info("Generated XML: {}", xml);
//
//            // You can call RestTemplate/WebClient here if you want
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return CompletableFuture.completedFuture(null);
//    }
//
//    private String documentToString(Document doc) throws Exception {
//        javax.xml.transform.TransformerFactory factory = javax.xml.transform.TransformerFactory.newInstance();
//        javax.xml.transform.Transformer transformer = factory.newTransformer();
//        java.io.StringWriter sw = new java.io.StringWriter();
//        javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(doc);
//        javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(sw);
//        transformer.transform(source, result);
//        return sw.toString();
//    }

    @Override
    public String identityVerification(VerificationRequest verificationRequest) throws Exception {
        IdentityVerificationAcmt023 identityVerificationAcmt023 = IdentityVerificationAcmt023.builder().build();
        IdentityVerificationAcmt023.IdVerificationRequest  idVerificationRequest= IdentityVerificationAcmt023.IdVerificationRequest.builder().build();


        IdentityVerificationAcmt023.Assignment assignment = IdentityVerificationAcmt023.Assignment.builder().build();

        //ASSIGNER
        IdentityVerificationAcmt023.Assigner assigner = IdentityVerificationAcmt023.Assigner.builder().build();

        //AGENT
        IdentityVerificationAcmt023.Agent agent = IdentityVerificationAcmt023.Agent.builder().build();
        // FINANCIAL INSTITUTION
        IdentityVerificationAcmt023.FinancialInstitutionId financialInstitutionId = IdentityVerificationAcmt023.FinancialInstitutionId.builder().build();
        financialInstitutionId.setBicfi(INSTITUTION_ID);
        // CLEARING SYSTEM MEMBER ID
        IdentityVerificationAcmt023.ClearingSystemMemberId clearingSystemMemberId = IdentityVerificationAcmt023.ClearingSystemMemberId.builder().build();
        clearingSystemMemberId.setMmbId(INSTITUTION_ID);
        financialInstitutionId.setClrSysMmbId(clearingSystemMemberId);


        IdentityVerificationAcmt023.Party creatorName = IdentityVerificationAcmt023.Party.builder().nm(CREATOR_NAME).build();
        assigner.setPty(IdentityVerificationAcmt023.Party.builder().nm(CREATOR_NAME).build());
        assigner.setAgt(agent);

        IdentityVerificationAcmt023.PartyWrapper creatorInformation = IdentityVerificationAcmt023.PartyWrapper.builder().pty(creatorName).build();

        assignment.setMsgId(MessageIdGenerator.generateMessageId(INSTITUTION_ID));
        assignment.setCreDtTm(localDateTime.format(DateTimeFormatter.ISO_DATE_TIME));
        assignment.setCretr(creatorInformation);
        assignment.setAssgnr(assigner);
    // ASSIGNER
        //ASSIGNEE
  IdentityVerificationAcmt023.Assignee assignee = IdentityVerificationAcmt023.Assignee.builder().build();
  IdentityVerificationAcmt023.Agent assigneeAgent= IdentityVerificationAcmt023.Agent.builder().build();
        IdentityVerificationAcmt023.FinancialInstitutionId assigneeFinancialInstitutionId = IdentityVerificationAcmt023.FinancialInstitutionId.builder().build();
        assigneeFinancialInstitutionId.setBicfi(verificationRequest.getBeneficiaryId());

        IdentityVerificationAcmt023.ClearingSystemMemberId assigneeClearingSystemMemberId = IdentityVerificationAcmt023.ClearingSystemMemberId.builder().build();
        assigneeClearingSystemMemberId.setMmbId(verificationRequest.getBeneficiaryId());
        financialInstitutionId.setClrSysMmbId(assigneeClearingSystemMemberId);

        assigneeAgent.setFinInstnId(assigneeFinancialInstitutionId);

        assignee.setAgt(assigneeAgent);
  // ASSIGNEE

  // VERIFICATION

IdentityVerificationAcmt023.Verification verification = IdentityVerificationAcmt023.Verification.builder().build();
verification.setId(assignment.getMsgId());

IdentityVerificationAcmt023.PartyAndAccountId partyAndAccountId = IdentityVerificationAcmt023.PartyAndAccountId.builder().build();
IdentityVerificationAcmt023.Party verifiedParty = IdentityVerificationAcmt023.Party.builder().build();
verifiedParty.setNm(verificationRequest.getPartyToVerifyName());

IdentityVerificationAcmt023.Account account = IdentityVerificationAcmt023.Account.builder().build();
IdentityVerificationAcmt023.AccountId accountId = IdentityVerificationAcmt023.AccountId.builder().build();
accountId.setIban(verificationRequest.getAccountNumber());
account.setId(accountId);

verification.setPtyAndAcctId(partyAndAccountId);

identityVerificationAcmt023.setIdVrfctnReq(idVerificationRequest);

Document createXmlDocument = XmlDocumentConverter.marshallToDocument(identityVerificationAcmt023, IdentityVerificationAcmt023.class);
PrivateKey privateKey = GenerateKey.loadPrivateKey(PRIVATE_KEY_PATH);
PublicKey publicKey = GenerateKey.loadPublicKey(PUBLIC_KEY_PATH);

Signer.sign(createXmlDocument,privateKey);

Encrypter.encrypt(createXmlDocument,publicKey);

String encryptDocument = XmlDocumentConverter.documentToString(createXmlDocument);












        return encryptDocument;
    }
}