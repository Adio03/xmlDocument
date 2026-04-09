package com.strata.xmlDocument.domain.service;

import com.strata.xmlDocument.application.input.MandateCancellationPain011UseCase;
import com.strata.xmlDocument.domain.model.MandateCancellationPain011;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.MandateCancellationRequest;
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
public class MandateCancellationPain011Service implements MandateCancellationPain011UseCase {

    private static final String INSTITUTION_ID = "999058";

    @Value("${nibss.private.key.path}")
    private String privateKeyPath;

    @Value("${nibss.public.key.path}")
    private String publicKeyPath;

    @Value("${nibss.api.url.pain011}")
    private String pain011ApiUrl;

    @Override
    public String cancelMandate(MandateCancellationRequest request) throws Exception {
        MandateCancellationPain011 pain011 = buildPain011Request(request);
        Document document = XmlDocumentConverter.marshallToDocument(pain011, MandateCancellationPain011.class);

        PrivateKey privateKey = GenerateKey.loadPrivateKey(privateKeyPath);
        PublicKey publicKey = GenerateKey.loadPublicKey(publicKeyPath);

        String rootTag = "MndtCxlReq";
        Signer.sign(document, privateKey);
        String encryptedData = Encrypter.encrypt(document, publicKey, rootTag);

        log.info("ENCRYPTED MANDATE CANCELLATION REQUEST DATA =====>>>>>> {}",
                encryptedData.substring(0, Math.min(encryptedData.length(), 100)) + "...");

        HttpSender.sendXML(encryptedData, pain011ApiUrl);

        return pain011.getMndtCxlReq().getGrpHdr().getMsgId();
    }

    private MandateCancellationPain011 buildPain011Request(MandateCancellationRequest request) {
        String msgId = MessageIdGenerator.generateMessageId(INSTITUTION_ID);

        return MandateCancellationPain011.builder()
                .mndtCxlReq(MandateCancellationPain011.MandateCancellationRequest.builder()
                        .grpHdr(MandateCancellationPain011.GroupHeader.builder()
                                .msgId(msgId)
                                .creDtTm(OffsetDateTime.now())
                                .build())
                        .undrlygCxlDtls(MandateCancellationPain011.UnderlyingCancellationDetails.builder()
                                .orgnlMsgInf(MandateCancellationPain011.OriginalMessageInformation.builder()
                                        .msgId(request.getOriginalMessageId())
                                        .msgNmId("pain.009.001.08")
                                        .creDtTm(OffsetDateTime.parse(request.getOriginalCreationDateTime()))
                                        .build())
                                .cxlRsn(MandateCancellationPain011.CancellationReason.builder()
                                        .rsn(MandateCancellationPain011.Reason.builder()
                                                .cd(request.getCancellationReasonCode())
                                                .prtry(request.getCancellationReasonDescription())
                                                .build())
                                        .build())
                                .orgnlMndt(MandateCancellationPain011.OriginalMandateWrapper.builder()
                                        .orgnlMndtId(request.getOriginalMandateId())
                                        .orgnlMndt(MandateCancellationPain011.OriginalMandateDetails.builder()
                                                .ocrncs(MandateCancellationPain011.Occurrences.builder()
                                                        .seqTp(request.getSequenceType())
                                                        .frqcy(MandateCancellationPain011.Frequency.builder()
                                                                .tp(request.getFrequencyType())
                                                                .build())
                                                        .frstColltnDt(LocalDate.parse(request.getFirstCollectionDate()))
                                                        .fnlColltnDt(LocalDate.parse(request.getFinalCollectionDate()))
                                                        .build())
                                                .trckgInd(request.isTrackingIndicator())
                                                .cdtr(MandateCancellationPain011.Creditor.builder()
                                                        .nm(request.getCreditorName())
                                                        .build())
                                                .cdtrAcct(MandateCancellationPain011.Account.builder()
                                                        .id(MandateCancellationPain011.AccountId.builder()
                                                                .iban(request.getCreditorAccountNumber())
                                                                .build())
                                                        .nm(request.getCreditorAccountName())
                                                        .build())
                                                .cdtrAgt(MandateCancellationPain011.Agent.builder()
                                                        .finInstnId(MandateCancellationPain011.FinancialInstitutionId.builder()
                                                                .bicfi(request.getCreditorAgentBIC())
                                                                .clrSysMmbId(MandateCancellationPain011.ClearingSystemMemberId.builder()
                                                                        .mmbId(request.getCreditorAgentMemberId())
                                                                        .build())
                                                                .build())
                                                        .build())
                                                .dbtr(MandateCancellationPain011.Debtor.builder()
                                                        .nm(request.getDebtorName())
                                                        .build())
                                                .dbtrAcct(MandateCancellationPain011.Account.builder()
                                                        .id(MandateCancellationPain011.AccountId.builder()
                                                                .iban(request.getDebtorAccountNumber())
                                                                .build())
                                                        .nm(request.getDebtorAccountName())
                                                        .build())
                                                .dbtrAgt(MandateCancellationPain011.Agent.builder()
                                                        .finInstnId(MandateCancellationPain011.FinancialInstitutionId.builder()
                                                                .bicfi(request.getDebtorAgentBIC())
                                                                .clrSysMmbId(MandateCancellationPain011.ClearingSystemMemberId.builder()
                                                                        .mmbId(request.getDebtorAgentMemberId())
                                                                        .build())
                                                                .build())
                                                        .build())
                                                .build())
                                        .build())
                                .build())
                        .build())
                .build();
    }
}
