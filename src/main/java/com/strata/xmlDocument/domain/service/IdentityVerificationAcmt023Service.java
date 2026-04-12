package com.strata.xmlDocument.domain.service;

import com.strata.xmlDocument.application.input.IdentityVerificationAcmt023UseCase;
import com.strata.xmlDocument.domain.model.IdentityVerificationAcmt023;
import com.strata.xmlDocument.domain.model.IdentityVerificationReportAcmt024;
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
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Service
@EnableAsync
@Slf4j
public class IdentityVerificationAcmt023Service implements IdentityVerificationAcmt023UseCase {
    private static final String SUCCESSFUL_VERIFICATION_NAME = "James";
    private static final String INVALID_ACCOUNT_CODE = "33";
    private static final String PROCESSING_ERROR_CODE = "96";
    private static final String SIGNATURE_ERROR_CODE = "902";

    @Value("${institution.id:999058}")
    private String institutionId;

    @Value("${creator.name:XYZ Bank}")
    private String creatorName;

    @Value("${nibss.private.key.path}")
    private String privateKeyPath;

    @Value("${nibss.public.key.path}")
    private String publicKeyPath;

    @Value("${nibss.api.url.acmt024:http://localhost:9200/api/nps/acmt024/inbound_acmt024}")
    private String acmt024ApiUrl;

    @Override
    public String identityVerificationOutBoundAcmt023(VerificationRequest verificationRequest) throws Exception {
        IdentityVerificationAcmt023.Assignment assignment = IdentityVerificationAcmt023.Assignment.builder()
                .msgId(MessageIdGenerator.generateMessageId(institutionId))
                .creDtTm(OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .cretr(IdentityVerificationAcmt023.PartyWrapper.builder()
                        .pty(IdentityVerificationAcmt023.Party.builder().nm(creatorName).build())
                        .build())
                .assgnr(IdentityVerificationAcmt023.Assigner.builder()
                        .pty(IdentityVerificationAcmt023.Party.builder().nm(creatorName).build())
                        .agt(getAgent(institutionId))
                        .build())
                .assgne(IdentityVerificationAcmt023.Assignee.builder()
                        .agt(getAgent(verificationRequest.getBeneficiaryId()))
                        .build())
                .build();

        IdentityVerificationAcmt023.Verification verification = IdentityVerificationAcmt023.Verification.builder()
                .id(assignment.getMsgId())
                .ptyAndAcctId(IdentityVerificationAcmt023.PartyAndAccountId.builder()
                        .pty(IdentityVerificationAcmt023.Party.builder().nm(verificationRequest.getPartyToVerifyName()).build())
                        .acct(IdentityVerificationAcmt023.Account.builder()
                                .id(IdentityVerificationAcmt023.AccountId.builder().iban(verificationRequest.getAccountNumber()).build())
                                .build())
                        .build())
                .build();

        IdentityVerificationAcmt023 acmt023 = IdentityVerificationAcmt023.builder()
                .idVrfctnReq(IdentityVerificationAcmt023.IdVerificationRequest.builder()
                        .assgnmt(assignment)
                        .vrfctn(verification)
                        .build())
                .build();

        Document doc = XmlDocumentConverter.marshallToDocument(acmt023, IdentityVerificationAcmt023.class);
        return completeDocumentSetup(doc, assignment.getMsgId(), "IdVrfctnReq");
    }

    @Override
    public void identityVerificationInBoundAcmt023(String encryptedData) throws Exception {
        PrivateKey privateKey = GenerateKey.loadPrivateKey(privateKeyPath);
        PublicKey publicKey = GenerateKey.loadPublicKey(publicKeyPath);
        
        Document decryptedDoc = Decrypter.decrypt(encryptedData, privateKey);
        IdentityVerificationAcmt023 acmt023 = XmlDocumentConverter.unmarshallFromDocument(decryptedDoc, IdentityVerificationAcmt023.class);
        boolean isValid = Signer.validateXmlSignature(decryptedDoc, publicKey);
        log.info("Received ACMT.023: {}", acmt023);
        
        FileSaver.saveDecryptMessageToFile(XmlDocumentConverter.documentToString(decryptedDoc), MessageTypes.ACMT_023.name());

        if (!isValid) {
            log.error("Invalid signature in inbound ACMT.023 message");
            sendAcmt024Response(acmt023, false, SIGNATURE_ERROR_CODE, "Invalid signature in ACMT.023 request", null);
            return;
        }

        try {
            String accountNumber = acmt023.getIdVrfctnReq().getVrfctn().getPtyAndAcctId().getAcct().getId().getIban();

            if (!isValidAccountNumber(accountNumber)) {
                sendAcmt024Response(acmt023, false, INVALID_ACCOUNT_CODE, "Invalid account number", null);
                return;
            }

            sendAcmt024Response(acmt023, true, null, null, SUCCESSFUL_VERIFICATION_NAME);
        } catch (Exception exception) {
            log.error("Failed to process inbound ACMT.023", exception);
            sendAcmt024Response(acmt023, false, PROCESSING_ERROR_CODE, buildFailureReason(exception), null);
        }
    }

    private void sendAcmt024Response(IdentityVerificationAcmt023 acmt023,
                                     boolean verified,
                                     String reasonCode,
                                     String reasonDescription,
                                     String verifiedName) throws Exception {
        IdentityVerificationAcmt023.Assignment origAssgnmt = acmt023.getIdVrfctnReq().getAssgnmt();
        String originalAccountNumber = acmt023.getIdVrfctnReq().getVrfctn().getPtyAndAcctId().getAcct().getId().getIban();
        
        IdentityVerificationReportAcmt024.IdVerificationReport report = IdentityVerificationReportAcmt024.IdVerificationReport.builder()
                .assgnmt(IdentityVerificationReportAcmt024.Assignment.builder()
                        .msgId(MessageIdGenerator.generateMessageId(institutionId))
                        .creDtTm(OffsetDateTime.now())
                        .assgnr(IdentityVerificationReportAcmt024.Assigner.builder()
                                .agt(getAcmt024Agent(institutionId))
                                .build())
                        .assgne(IdentityVerificationReportAcmt024.Assignee.builder()
                                .pty(IdentityVerificationReportAcmt024.Party.builder().nm(origAssgnmt.getAssgnr().getPty().getNm()).build())
                                .agt(getAcmt024Agent(origAssgnmt.getAssgnr().getAgt().getFinInstnId().getClrSysMmbId().getMmbId()))
                                .build())
                        .build())
                .orgnlAssgnmt(IdentityVerificationReportAcmt024.OriginalAssignment.builder()
                        .msgId(origAssgnmt.getMsgId())
                        .creDtTm(OffsetDateTime.parse(origAssgnmt.getCreDtTm()))
                        .build())
                .rpt(IdentityVerificationReportAcmt024.Report.builder()
                        .orgnlId(origAssgnmt.getMsgId())
                        .vrfctn(verified)
                        .rsn(verified ? null : IdentityVerificationReportAcmt024.Reason.builder()
                                .cd(defaultIfBlank(reasonCode, PROCESSING_ERROR_CODE))
                                .prtry(defaultIfBlank(reasonDescription, "Verification failed"))
                                .build())
                        .orgnlPtyAndAcctId(IdentityVerificationReportAcmt024.OriginalPartyAndAccountId.builder()
                                .acct(IdentityVerificationReportAcmt024.Account.builder()
                                        .id(IdentityVerificationReportAcmt024.AccountId.builder()
                                                .iban(originalAccountNumber)
                                                .build())
                                        .build())
                                .build())
                        .updtdPtyAndAcctId(verified ? IdentityVerificationReportAcmt024.UpdatedPartyAndAccountId.builder()
                                .pty(IdentityVerificationReportAcmt024.Party.builder().nm(defaultIfBlank(verifiedName, SUCCESSFUL_VERIFICATION_NAME)).build())
                                .build() : null)
                        .build())
                .splmtryData(IdentityVerificationReportAcmt024.SupplementaryData.builder()
                        .plcAndNm("AdditionalVerificationDetails")
                        .envlp(IdentityVerificationReportAcmt024.Envelope.builder()
                                .customData(IdentityVerificationReportAcmt024.CustomData.builder()
                                        .creditorInfo(IdentityVerificationReportAcmt024.CreditorInfo.builder()
                                                .accountDesignation("1")
                                                .idType("BVN")
                                                .idValue("2211232346")
                                                .accountTier("1")
                                                .build())
                                        .transactionInfo(IdentityVerificationReportAcmt024.TransactionInfo.builder()
                                                .riskRating("R000000000000000000B9")
                                                .build())
                                        .build())
                                .build())
                        .build())
                .build();

        IdentityVerificationReportAcmt024 acmt024 = IdentityVerificationReportAcmt024.builder()
                .idVrfctnRpt(report)
                .build();

        Document doc = XmlDocumentConverter.marshallToDocument(acmt024, IdentityVerificationReportAcmt024.class);
        completeDocumentSetup(doc, report.getAssgnmt().getMsgId(), "IdVrfctnRpt");
    }

    private String completeDocumentSetup(Document doc, String msgId, String rootTag) throws Exception {
        PrivateKey privateKey = GenerateKey.loadPrivateKey(privateKeyPath);
        PublicKey publicKey = GenerateKey.loadPublicKey(publicKeyPath);

        Signer.sign(doc, privateKey);
        String encryptData = Encrypter.encrypt(doc, publicKey, rootTag);
        
        log.info("Sending encrypted {} with MsgId: {}", rootTag, msgId);
        HttpSender.sendXML(encryptData, acmt024ApiUrl);
        return msgId;
    }

    private IdentityVerificationAcmt023.Agent getAgent(String mbId) {
        return IdentityVerificationAcmt023.Agent.builder()
                .finInstnId(IdentityVerificationAcmt023.FinancialInstitutionId.builder()
                        .bicfi(mbId)
                        .clrSysMmbId(IdentityVerificationAcmt023.ClearingSystemMemberId.builder().mmbId(mbId).build())
                        .build())
                .build();
    }

    private IdentityVerificationReportAcmt024.Agent getAcmt024Agent(String mbId) {
        return IdentityVerificationReportAcmt024.Agent.builder()
                .finInstnId(IdentityVerificationReportAcmt024.FinancialInstitutionId.builder()
                        .bicfi(mbId)
                        .clrSysMmbId(IdentityVerificationReportAcmt024.ClearingSystemMemberId.builder().mmbId(mbId).build())
                        .build())
                .build();
    }

    private boolean isValidAccountNumber(String accountNumber) {
        return accountNumber != null && accountNumber.matches("\\d{10}");
    }

    private String buildFailureReason(Exception exception) {
        String message = exception.getMessage();
        if (message == null || message.isBlank()) {
            return "Verification processing failed";
        }
        return message;
    }

    private String defaultIfBlank(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
