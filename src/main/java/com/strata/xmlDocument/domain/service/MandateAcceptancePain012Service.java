package com.strata.xmlDocument.domain.service;

import com.strata.xmlDocument.application.input.MandateAcceptancePain012UseCase;
import com.strata.xmlDocument.domain.model.MandateAcceptancePain012;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.MandateAcceptanceRequest;
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
public class MandateAcceptancePain012Service implements MandateAcceptancePain012UseCase {

    private static final String INSTITUTION_ID = "999058";

    @Value("${nibss.private.key.path}")
    private String privateKeyPath;

    @Value("${nibss.public.key.path}")
    private String publicKeyPath;

    @Value("${nibss.api.url.pain012}")
    private String pain012ApiUrl;

    @Override
    public String reportAcceptance(MandateAcceptanceRequest request) throws Exception {
        MandateAcceptancePain012 pain012 = buildPain012Request(request);
        Document document = XmlDocumentConverter.marshallToDocument(pain012, MandateAcceptancePain012.class);

        PrivateKey privateKey = GenerateKey.loadPrivateKey(privateKeyPath);
        PublicKey publicKey = GenerateKey.loadPublicKey(publicKeyPath);

        String rootTag = "MndtAccptncRpt";
        Signer.sign(document, privateKey);
        String encryptedData = Encrypter.encrypt(document, publicKey, rootTag);

        log.info("ENCRYPTED MANDATE ACCEPTANCE REPORT DATA =====>>>>>> {}",
                encryptedData.substring(0, Math.min(encryptedData.length(), 100)) + "...");

        HttpSender.sendXML(encryptedData, pain012ApiUrl);

        return pain012.getMndtAccptncRpt().getGrpHdr().getMsgId();
    }

    private MandateAcceptancePain012 buildPain012Request(MandateAcceptanceRequest request) {
        String msgId = MessageIdGenerator.generateMessageId(INSTITUTION_ID);

        return MandateAcceptancePain012.builder()
                .mndtAccptncRpt(MandateAcceptancePain012.MandateAcceptanceReport.builder()
                        .grpHdr(MandateAcceptancePain012.GroupHeader.builder()
                                .msgId(msgId)
                                .creDtTm(OffsetDateTime.now())
                                .build())
                        .undrlygAccptncDtls(MandateAcceptancePain012.UnderlyingAcceptanceDetails.builder()
                                .orgnlMsgInf(MandateAcceptancePain012.OriginalMessageInformation.builder()
                                        .msgId(request.getOriginalMessageId())
                                        .msgNmId("pain.009.001.08")
                                        .creDtTm(OffsetDateTime.parse(request.getOriginalCreationDateTime()))
                                        .build())
                                .accptncRslt(MandateAcceptancePain012.AcceptanceResult.builder()
                                        .accptd(request.isAccepted())
                                        .build())
                                .orgnlMndt(MandateAcceptancePain012.OriginalMandateWrapper.builder()
                                        .orgnlMndtId(request.getOriginalMandateId())
                                        .orgnlMndt(MandateAcceptancePain012.OriginalMandateDetails.builder()
                                                .ocrncs(MandateAcceptancePain012.Occurrences.builder()
                                                        .seqTp(request.getSequenceType())
                                                        .frqcy(MandateAcceptancePain012.Frequency.builder()
                                                                .tp(request.getFrequencyType())
                                                                .build())
                                                        .frstColltnDt(LocalDate.parse(request.getFirstCollectionDate()))
                                                        .fnlColltnDt(LocalDate.parse(request.getFinalCollectionDate()))
                                                        .build())
                                                .trckgInd(request.isTrackingIndicator())
                                                .cdtr(MandateAcceptancePain012.Creditor.builder()
                                                        .nm(request.getCreditorName())
                                                        .build())
                                                .cdtrAcct(MandateAcceptancePain012.Account.builder()
                                                        .id(MandateAcceptancePain012.AccountId.builder()
                                                                .iban(request.getCreditorAccountNumber())
                                                                .build())
                                                        .nm(request.getCreditorAccountName())
                                                        .build())
                                                .cdtrAgt(MandateAcceptancePain012.Agent.builder()
                                                        .finInstnId(MandateAcceptancePain012.FinancialInstitutionId.builder()
                                                                .bicfi(request.getCreditorAgentBIC())
                                                                .clrSysMmbId(MandateAcceptancePain012.ClearingSystemMemberId.builder()
                                                                        .mmbId(request.getCreditorAgentMemberId())
                                                                        .build())
                                                                .build())
                                                        .build())
                                                .dbtr(MandateAcceptancePain012.Debtor.builder()
                                                        .nm(request.getDebtorName())
                                                        .build())
                                                .dbtrAcct(MandateAcceptancePain012.Account.builder()
                                                        .id(MandateAcceptancePain012.AccountId.builder()
                                                                .iban(request.getDebtorAccountNumber())
                                                                .build())
                                                        .nm(request.getDebtorAccountName())
                                                        .build())
                                                .dbtrAgt(MandateAcceptancePain012.Agent.builder()
                                                        .finInstnId(MandateAcceptancePain012.FinancialInstitutionId.builder()
                                                                .bicfi(request.getDebtorAgentBIC())
                                                                .clrSysMmbId(MandateAcceptancePain012.ClearingSystemMemberId.builder()
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
