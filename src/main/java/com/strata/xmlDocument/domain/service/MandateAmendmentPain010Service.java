package com.strata.xmlDocument.domain.service;

import com.strata.xmlDocument.application.input.MandateAmendmentPain010UseCase;
import com.strata.xmlDocument.domain.model.MandateAmendmentPain010;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.MandateAmendmentRequest;
import com.strata.xmlDocument.infrastructure.adapter.output.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Service
@Slf4j
public class MandateAmendmentPain010Service implements MandateAmendmentPain010UseCase {

    private static final String INSTITUTION_ID = "999058";

    @Value("${nibss.private.key.path}")
    private String privateKeyPath;

    @Value("${nibss.public.key.path}")
    private String publicKeyPath;

    @Value("${nibss.api.url.pain010}")
    private String pain010ApiUrl;

    @Override
    public String amendMandate(MandateAmendmentRequest request) throws Exception {
        MandateAmendmentPain010 pain010 = buildPain010Request(request);
        Document document = XmlDocumentConverter.marshallToDocument(pain010, MandateAmendmentPain010.class);

        PrivateKey privateKey = GenerateKey.loadPrivateKey(privateKeyPath);
        PublicKey publicKey = GenerateKey.loadPublicKey(publicKeyPath);

        String rootTag = "MndtAmdmntReq";
        Signer.sign(document, privateKey);
        String encryptedData = Encrypter.encrypt(document, publicKey, rootTag);

        log.info("ENCRYPTED MANDATE AMENDMENT REQUEST DATA =====>>>>>> {}",
                encryptedData.substring(0, Math.min(encryptedData.length(), 100)) + "...");

        HttpSender.sendXML(encryptedData, pain010ApiUrl);

        return pain010.getMndtAmdmntReq().getGrpHdr().getMsgId();
    }

    private MandateAmendmentPain010 buildPain010Request(MandateAmendmentRequest request) {
        String msgId = MessageIdGenerator.generateMessageId(INSTITUTION_ID);

        return MandateAmendmentPain010.builder()
                .mndtAmdmntReq(MandateAmendmentPain010.MandateAmendmentRequest.builder()
                        .grpHdr(MandateAmendmentPain010.GroupHeader.builder()
                                .msgId(msgId)
                                .creDtTm(OffsetDateTime.now())
                                .initgPty(MandateAmendmentPain010.InitiatingParty.builder()
                                        .nm(request.getInitiatingPartyName())
                                        .build())
                                .build())
                        .undrlygAmdmntDtls(MandateAmendmentPain010.UnderlyingAmendmentDetails.builder()
                                .orgnlMsgInf(MandateAmendmentPain010.OriginalMessageInformation.builder()
                                        .msgId(request.getOriginalMessageId())
                                        .msgNmId("pain.009.001.08")
                                        .creDtTm(OffsetDateTime.parse(request.getOriginalCreationDateTime()))
                                        .build())
                                .amdmntRsn(MandateAmendmentPain010.AmendmentReason.builder()
                                        .rsn(MandateAmendmentPain010.Reason.builder()
                                                .cd(request.getAmendmentReasonCode())
                                                .prtry(request.getAmendmentReasonDescription())
                                                .build())
                                        .build())
                                .mandate(MandateAmendmentPain010.Mandate.builder()
                                        .mndtId(request.getMandateId())
                                        .ocrncs(MandateAmendmentPain010.Occurrences.builder()
                                                .seqTp(request.getSequenceType())
                                                .frqcy(MandateAmendmentPain010.Frequency.builder()
                                                        .tp(request.getFrequencyType())
                                                        .build())
                                                .frstColltnDt(LocalDate.parse(request.getFirstCollectionDate()))
                                                .fnlColltnDt(LocalDate.parse(request.getFinalCollectionDate()))
                                                .build())
                                        .trckgInd(request.isTrackingIndicator())
                                        .cdtr(MandateAmendmentPain010.Creditor.builder()
                                                .nm(request.getCreditorName())
                                                .build())
                                        .cdtrAcct(MandateAmendmentPain010.Account.builder()
                                                .id(MandateAmendmentPain010.AccountId.builder()
                                                        .iban(request.getCreditorAccountNumber())
                                                        .build())
                                                .nm(request.getCreditorAccountName())
                                                .build())
                                        .cdtrAgt(MandateAmendmentPain010.Agent.builder()
                                                .finInstnId(MandateAmendmentPain010.FinancialInstitutionId.builder()
                                                        .bicfi(request.getCreditorAgentBIC())
                                                        .clrSysMmbId(MandateAmendmentPain010.ClearingSystemMemberId.builder()
                                                                .mmbId(request.getCreditorAgentMemberId())
                                                                .build())
                                                        .build())
                                                .build())
                                        .dbtr(MandateAmendmentPain010.Debtor.builder()
                                                .nm(request.getDebtorName())
                                                .build())
                                        .dbtrAcct(MandateAmendmentPain010.Account.builder()
                                                .id(MandateAmendmentPain010.AccountId.builder()
                                                        .iban(request.getDebtorAccountNumber())
                                                        .build())
                                                .nm(request.getDebtorAccountName())
                                                .build())
                                        .dbtrAgt(MandateAmendmentPain010.Agent.builder()
                                                .finInstnId(MandateAmendmentPain010.FinancialInstitutionId.builder()
                                                        .bicfi(request.getDebtorAgentBIC())
                                                        .clrSysMmbId(MandateAmendmentPain010.ClearingSystemMemberId.builder()
                                                                .mmbId(request.getDebtorAgentMemberId())
                                                                .build())
                                                        .build())
                                                .build())
                                        .build())
                                .orgnlMndt(MandateAmendmentPain010.OriginalMandate.builder()
                                        .orgnlMndtId(request.getOriginalMandateId())
                                        .build())
                                .build())
                        .build())
                .build();
    }
}
