package com.strata.xmlDocument.domain.service;

import com.strata.xmlDocument.application.input.IdentityVerificationAcmt023UseCase;
import com.strata.xmlDocument.domain.model.IdentityVerificationAcmt023;
import com.strata.xmlDocument.domain.model.types.MessageTypes;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.VerificationRequest;
import com.strata.xmlDocument.infrastructure.adapter.output.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
public class IdentityVerificationAcmt023Service implements IdentityVerificationAcmt023UseCase {

    @Value("${institution.id}")
    private String institutionId;

    @Value("${creator.name}")
    private String creatorName;

//    @Value("${nibss.private.key.path}")
    private final String privateKeyPath = "C:\\Users\\semicolon\\IdeaProjects\\xmlDocument\\banks_private.pem";

//    @Value("${nibss.public.key.path}")
    private final String publicKeyPath = "C:\\Users\\semicolon\\IdeaProjects\\xmlDocument\\banks_public.pem";

    @Value("${nibss.api.url}")
    private String apiUrl;


    @Override
    public String identityVerificationOutBoundAcmt023(VerificationRequest verificationRequest) throws Exception {
        IdentityVerificationAcmt023.Assignment assignment = new IdentityVerificationAcmt023.Assignment();

        IdentityVerificationAcmt023.Assigner assigner = new IdentityVerificationAcmt023.Assigner();
        IdentityVerificationAcmt023.Agent assignerAgent = getAgent();
        assigner.setPty(IdentityVerificationAcmt023.Party.builder().nm(creatorName).build());
        assigner.setAgt(assignerAgent);

        IdentityVerificationAcmt023.Assignee assignee = getAssignee(verificationRequest);

        // Creator
        IdentityVerificationAcmt023.PartyWrapper creator =
                IdentityVerificationAcmt023.PartyWrapper.builder()
                        .pty(IdentityVerificationAcmt023.Party.builder()
                                .nm(creatorName)
                                .build())
                        .build();

        // Fill Assignment
        assignment.setMsgId(MessageIdGenerator.generateMessageId(institutionId));
        assignment.setCreDtTm(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        assignment.setCretr(creator);
        assignment.setAssgnr(assigner);
        assignment.setAssgne(assignee);

        IdentityVerificationAcmt023.IdVerificationRequest idVerificationRequest = getIdVerificationRequest(verificationRequest, assignment);


        IdentityVerificationAcmt023 identityVerificationAcmt023 = new IdentityVerificationAcmt023();
        identityVerificationAcmt023.setIdVrfctnReq(idVerificationRequest);

        Document createXmlDocument = XmlDocumentConverter.marshallToDocument(
                identityVerificationAcmt023, IdentityVerificationAcmt023.class

        );

        return completeDocumentSetup(createXmlDocument, identityVerificationAcmt023);
    }

    private String completeDocumentSetup(Document createXmlDocument, IdentityVerificationAcmt023 identityVerificationAcmt023) throws Exception {
        PrivateKey privateKey = GenerateKey.loadPrivateKey(privateKeyPath);
        PublicKey publicKey = GenerateKey.loadPublicKey(publicKeyPath);
        String rootTag = "IdVrfctnReq";

        Signer.sign(createXmlDocument, privateKey);
        String encryptData = Encrypter.encrypt(createXmlDocument, publicKey,rootTag);
        log.info("DATA =====>>>>>> {}",encryptData);
        String url ="http://localhost:9200/api/nps/acmt023/inbound_acmt023";
        HttpSender.sendXML(encryptData, url);

        return identityVerificationAcmt023.getIdVrfctnReq().getAssgnmt().getMsgId();
    }

    private  IdentityVerificationAcmt023.Agent getAgent() {
        IdentityVerificationAcmt023.Agent assignerAgent = new IdentityVerificationAcmt023.Agent();
        IdentityVerificationAcmt023.FinancialInstitutionId assignerFinId = new IdentityVerificationAcmt023.FinancialInstitutionId();
        IdentityVerificationAcmt023.ClearingSystemMemberId assignerClrId = new IdentityVerificationAcmt023.ClearingSystemMemberId();

        assignerClrId.setMmbId(institutionId);
        assignerFinId.setClrSysMmbId(assignerClrId);
        assignerFinId.setBicfi(institutionId);
        assignerAgent.setFinInstnId(assignerFinId);
        return assignerAgent;
    }

    private  IdentityVerificationAcmt023.Assignee getAssignee(VerificationRequest verificationRequest) {
        IdentityVerificationAcmt023.Assignee assignee = new IdentityVerificationAcmt023.Assignee();
        IdentityVerificationAcmt023.Agent assigneeAgent = new IdentityVerificationAcmt023.Agent();
        IdentityVerificationAcmt023.FinancialInstitutionId assigneeFinId = new IdentityVerificationAcmt023.FinancialInstitutionId();
        IdentityVerificationAcmt023.ClearingSystemMemberId assigneeClrId = new IdentityVerificationAcmt023.ClearingSystemMemberId();

        assigneeClrId.setMmbId(verificationRequest.getBeneficiaryId());
        assigneeFinId.setClrSysMmbId(assigneeClrId);
        assigneeFinId.setBicfi(verificationRequest.getBeneficiaryId());
        assigneeAgent.setFinInstnId(assigneeFinId);
        assignee.setAgt(assigneeAgent);
        return assignee;
    }

    private  IdentityVerificationAcmt023.IdVerificationRequest getIdVerificationRequest(VerificationRequest verificationRequest, IdentityVerificationAcmt023.Assignment assignment) {
        IdentityVerificationAcmt023.Verification verification = new IdentityVerificationAcmt023.Verification();
        verification.setId(assignment.getMsgId());

        IdentityVerificationAcmt023.PartyAndAccountId ptyAndAcctId = getPartyAndAccountId(verificationRequest);
        verification.setPtyAndAcctId(ptyAndAcctId);

        IdentityVerificationAcmt023.IdVerificationRequest idVerificationRequest =
                new IdentityVerificationAcmt023.IdVerificationRequest();
        idVerificationRequest.setAssgnmt(assignment);
        idVerificationRequest.setVrfctn(verification);
        return idVerificationRequest;
    }

    private IdentityVerificationAcmt023.PartyAndAccountId getPartyAndAccountId(VerificationRequest verificationRequest) {
        IdentityVerificationAcmt023.PartyAndAccountId ptyAndAcctId = new IdentityVerificationAcmt023.PartyAndAccountId();
        IdentityVerificationAcmt023.Party verifiedParty = new IdentityVerificationAcmt023.Party();
        verifiedParty.setNm(verificationRequest.getPartyToVerifyName());

        IdentityVerificationAcmt023.Account account = new IdentityVerificationAcmt023.Account();
        IdentityVerificationAcmt023.AccountId accountId = new IdentityVerificationAcmt023.AccountId();
        accountId.setIban(verificationRequest.getAccountNumber());
        account.setId(accountId);

        ptyAndAcctId.setPty(verifiedParty);
        ptyAndAcctId.setAcct(account);
        return ptyAndAcctId;
    }

    @Override
    public void identityVerificationInBoundAcmt023(String encryptedData) throws Exception {
        PrivateKey privateKey = GenerateKey.loadPrivateKey(privateKeyPath);
        PublicKey publicKey = GenerateKey.loadPublicKey(publicKeyPath);
        Document decryptedDocument = Decrypter.decrypt(encryptedData,privateKey);

       boolean isValid = Signer.validateXmlSignature(decryptedDocument,publicKey);
        if(!isValid){
            log.error("Invalid Signature ===================>>>>>>>>>>>>>");
        }
        String decryptedStringValue = XmlDocumentConverter.documentToString(decryptedDocument);
        log.info("DECRYPTED  ========>>>>>>> {}", decryptedStringValue);
        String messageType = MessageTypes.ACMT_023.name();
        FileSaver.saveDecryptMessageToFile(decryptedStringValue,messageType);
    }

}