package com.strata.xmlDocument.domain.service;

import com.strata.xmlDocument.application.input.MandateActivationStatusPain014UseCase;
import com.strata.xmlDocument.domain.model.MandateActivationStatusPain014;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.MandateActivationStatusRequest;
import com.strata.xmlDocument.infrastructure.adapter.output.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.OffsetDateTime;

@Service
@Slf4j
public class MandateActivationStatusPain014Service implements MandateActivationStatusPain014UseCase {

    private static final String INSTITUTION_ID = "999058";

    @Value("${nibss.private.key.path}")
    private String privateKeyPath;

    @Value("${nibss.public.key.path}")
    private String publicKeyPath;

    @Value("${nibss.api.url.pain014}")
    private String pain014ApiUrl;

    @Override
    public String reportActivationStatus(MandateActivationStatusRequest request) throws Exception {
        MandateActivationStatusPain014 pain014 = buildPain014Request(request);
        Document document = XmlDocumentConverter.marshallToDocument(pain014, MandateActivationStatusPain014.class);

        PrivateKey privateKey = GenerateKey.loadPrivateKey(privateKeyPath);
        PublicKey publicKey = GenerateKey.loadPublicKey(publicKeyPath);

        String rootTag = "CdtrPmtActvtnReqStsRpt";
        Signer.sign(document, privateKey);
        String encryptedData = Encrypter.encrypt(document, publicKey, rootTag);

        log.info("ENCRYPTED MANDATE ACTIVATION STATUS REPORT DATA =====>>>>>> {}",
                encryptedData.substring(0, Math.min(encryptedData.length(), 100)) + "...");

        HttpSender.sendXML(encryptedData, pain014ApiUrl);

        return pain014.getCdtrPmtActvtnReqStsRpt().getGrpHdr().getMsgId();
    }

    private MandateActivationStatusPain014 buildPain014Request(MandateActivationStatusRequest request) {
        String msgId = MessageIdGenerator.generateMessageId(INSTITUTION_ID);

        return MandateActivationStatusPain014.builder()
                .cdtrPmtActvtnReqStsRpt(MandateActivationStatusPain014.CreditorPaymentActivationRequestStatusReport.builder()
                        .grpHdr(MandateActivationStatusPain014.GroupHeader.builder()
                                .msgId(msgId)
                                .creDtTm(OffsetDateTime.now())
                                .initgPty(MandateActivationStatusPain014.InitiatingParty.builder()
                                        .nm(request.getInitiatingPartyName())
                                        .build())
                                .cdtr(MandateActivationStatusPain014.Creditor.builder()
                                        .nm(request.getCreditorName())
                                        .build())
                                .cdtrAcct(MandateActivationStatusPain014.Account.builder()
                                        .id(MandateActivationStatusPain014.AccountId.builder()
                                                .iban(request.getCreditorAccountNumber())
                                                .build())
                                        .nm(request.getCreditorAccountName())
                                        .build())
                                .dbtr(MandateActivationStatusPain014.Debtor.builder()
                                        .nm(request.getDebtorName())
                                        .build())
                                .dbtrAcct(MandateActivationStatusPain014.Account.builder()
                                        .id(MandateActivationStatusPain014.AccountId.builder()
                                                .iban(request.getDebtorAccountNumber())
                                                .build())
                                        .nm(request.getDebtorAccountName())
                                        .build())
                                .fwdgAgt(MandateActivationStatusPain014.Agent.builder()
                                        .finInstnId(MandateActivationStatusPain014.FinancialInstitutionId.builder()
                                                .bicfi(request.getForwardingAgentBIC())
                                                .clrSysMmbId(MandateActivationStatusPain014.ClearingSystemMemberId.builder()
                                                        .mmbId(request.getForwardingAgentMemberId())
                                                        .build())
                                                .build())
                                        .build())
                                .dbtrAgt(MandateActivationStatusPain014.Agent.builder()
                                        .finInstnId(MandateActivationStatusPain014.FinancialInstitutionId.builder()
                                                .bicfi(request.getDebtorAgentBIC())
                                                .clrSysMmbId(MandateActivationStatusPain014.ClearingSystemMemberId.builder()
                                                        .mmbId(request.getDebtorAgentMemberId())
                                                        .build())
                                                .build())
                                        .build())
                                .cdtrAgt(MandateActivationStatusPain014.Agent.builder()
                                        .finInstnId(MandateActivationStatusPain014.FinancialInstitutionId.builder()
                                                .bicfi(request.getCreditorAgentBIC())
                                                .clrSysMmbId(MandateActivationStatusPain014.ClearingSystemMemberId.builder()
                                                        .mmbId(request.getCreditorAgentMemberId())
                                                        .build())
                                                .build())
                                        .build())
                                .build())
                        .orgnlGrpInfAndSts(MandateActivationStatusPain014.OriginalGroupInformationAndStatus.builder()
                                .orgnlMsgId(request.getOriginalMessageId())
                                .orgnlMsgNmId("pain.013.001.11")
                                .orgnlCreDtTm(OffsetDateTime.parse(request.getOriginalCreationDateTime()))
                                .grpSts(request.getGroupStatus())
                                .build())
                        .orgnlPmtInfAndSts(MandateActivationStatusPain014.OriginalPaymentInformationAndStatus.builder()
                                .orgnlPmtInfId(request.getOriginalPaymentInformationId())
                                .txInfAndSts(MandateActivationStatusPain014.TransactionInformationAndStatus.builder()
                                        .orgnlEndToEndId(request.getOriginalEndToEndId())
                                        .txSts(request.getTransactionStatus())
                                        .build())
                                .build())
                        .build())
                .build();
    }
}
