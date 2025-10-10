package com.strata.xmlDocument.domain.service;

import com.strata.xmlDocument.application.input.IdentityVerificationAcmt023UseCase;
import com.strata.xmlDocument.domain.model.IdentityVerificationAcmt023;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.VerificationRequest;
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
public class IdentityVerificationAcmt023Service implements IdentityVerificationAcmt023UseCase {
    private static final String INSTITUTION_ID = "999058";
    private static final String CREATOR_NAME  = "NIBSS";
    private static final String PRIVATE_KEY_PATH = "C:\\Users\\semicolon\\Downloads\\xmlDocument\\xmlDocument\\banks_private.pem";
    private static final String PUBLIC_KEY_PATH = "C:\\Users\\semicolon\\Downloads\\xmlDocument\\xmlDocument\\banks_public.pem";
    private static final String API_URL = "https://api.example.com/verify";


    @Override
    public String identityVerification(VerificationRequest verificationRequest) throws Exception {
        IdentityVerificationAcmt023.Assignment assignment = new IdentityVerificationAcmt023.Assignment();

        IdentityVerificationAcmt023.Assigner assigner = new IdentityVerificationAcmt023.Assigner();
        IdentityVerificationAcmt023.Agent assignerAgent = getAgent();
        assigner.setPty(IdentityVerificationAcmt023.Party.builder().nm(CREATOR_NAME).build());
        assigner.setAgt(assignerAgent);

        IdentityVerificationAcmt023.Assignee assignee = getAssignee(verificationRequest);

        // Creator
        IdentityVerificationAcmt023.PartyWrapper creator =
                IdentityVerificationAcmt023.PartyWrapper.builder()
                        .pty(IdentityVerificationAcmt023.Party.builder()
                                .nm(CREATOR_NAME)
                                .build())
                        .build();

        // Fill Assignment
        assignment.setMsgId(MessageIdGenerator.generateMessageId(INSTITUTION_ID));
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
        PrivateKey privateKey = GenerateKey.loadPrivateKey(PRIVATE_KEY_PATH);
        PublicKey publicKey = GenerateKey.loadPublicKey(PUBLIC_KEY_PATH);

        Signer.sign(createXmlDocument, privateKey);
       String encryptData = Encrypter.encrypt(createXmlDocument, publicKey);
        HttpSender.sendXML(encryptData,API_URL);

        return identityVerificationAcmt023.getIdVrfctnReq().getAssgnmt().getMsgId();
    }

    private static IdentityVerificationAcmt023.Agent getAgent() {
        IdentityVerificationAcmt023.Agent assignerAgent = new IdentityVerificationAcmt023.Agent();
        IdentityVerificationAcmt023.FinancialInstitutionId assignerFinId = new IdentityVerificationAcmt023.FinancialInstitutionId();
        IdentityVerificationAcmt023.ClearingSystemMemberId assignerClrId = new IdentityVerificationAcmt023.ClearingSystemMemberId();

        assignerClrId.setMmbId(INSTITUTION_ID);
        assignerFinId.setClrSysMmbId(assignerClrId);
        assignerFinId.setBicfi(INSTITUTION_ID);
        assignerAgent.setFinInstnId(assignerFinId);
        return assignerAgent;
    }

    private static IdentityVerificationAcmt023.Assignee getAssignee(VerificationRequest verificationRequest) {
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

    private static IdentityVerificationAcmt023.IdVerificationRequest getIdVerificationRequest(VerificationRequest verificationRequest, IdentityVerificationAcmt023.Assignment assignment) {
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

    private static IdentityVerificationAcmt023.PartyAndAccountId getPartyAndAccountId(VerificationRequest verificationRequest) {
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

}