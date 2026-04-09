package com.strata.xmlDocument.domain.service;

import com.strata.xmlDocument.application.input.MandateInitiationPain009UseCase;
import com.strata.xmlDocument.domain.model.MandateInitiationPain009;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.MandateInitiationRequest;
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
public class MandateInitiationPain009Service implements MandateInitiationPain009UseCase {

    private static final String INSTITUTION_ID = "999058";

    @Value("${nibss.private.key.path}")
    private String privateKeyPath;

    @Value("${nibss.public.key.path}")
    private String publicKeyPath;

    @Value("${nibss.api.url.pain009}")
    private String pain009ApiUrl;

    @Override
    public String initiateMandate(MandateInitiationRequest request) throws Exception {
        MandateInitiationPain009 pain009 = buildPain009Request(request);
        Document document = XmlDocumentConverter.marshallToDocument(pain009, MandateInitiationPain009.class);

        PrivateKey privateKey = GenerateKey.loadPrivateKey(privateKeyPath);
        PublicKey publicKey = GenerateKey.loadPublicKey(publicKeyPath);

        String rootTag = "MndtInitnReq";
        Signer.sign(document, privateKey);
        String encryptedData = Encrypter.encrypt(document, publicKey, rootTag);

        log.info("ENCRYPTED MANDATE INITIATION REQUEST DATA =====>>>>>> {}",
                encryptedData.substring(0, Math.min(encryptedData.length(), 100)) + "...");

        HttpSender.sendXML(encryptedData, pain009ApiUrl);

        return pain009.getMndtInitnReq().getGrpHdr().getMsgId();
    }

    private MandateInitiationPain009 buildPain009Request(MandateInitiationRequest request) {
        String msgId = MessageIdGenerator.generateMessageId(INSTITUTION_ID);

        return MandateInitiationPain009.builder()
                .mndtInitnReq(MandateInitiationPain009.MandateInitiationRequest.builder()
                        .grpHdr(MandateInitiationPain009.GroupHeader.builder()
                                .msgId(msgId)
                                .creDtTm(OffsetDateTime.now())
                                .build())
                        .mandate(MandateInitiationPain009.Mandate.builder()
                                .mndtId(request.getMandateId())
                                .ocrncs(MandateInitiationPain009.Occurrences.builder()
                                        .seqTp(request.getSequenceType())
                                        .frqcy(MandateInitiationPain009.Frequency.builder()
                                                .tp(request.getFrequencyType())
                                                .build())
                                        .frstColltnDt(LocalDate.parse(request.getFirstCollectionDate()))
                                        .fnlColltnDt(LocalDate.parse(request.getFinalCollectionDate()))
                                        .build())
                                .trckgInd(request.isTrackingIndicator())
                                .colltnAmt(MandateInitiationPain009.CollectionAmount.builder()
                                        .ccy(request.getCollectionCurrency())
                                        .amount(request.getCollectionAmount())
                                        .build())
                                .cdtr(MandateInitiationPain009.Creditor.builder()
                                        .nm(request.getCreditorName())
                                        .build())
                                .cdtrAcct(MandateInitiationPain009.Account.builder()
                                        .id(MandateInitiationPain009.AccountId.builder()
                                                .iban(request.getCreditorAccountNumber())
                                                .build())
                                        .nm(request.getCreditorAccountName())
                                        .build())
                                .cdtrAgt(MandateInitiationPain009.Agent.builder()
                                        .finInstnId(MandateInitiationPain009.FinancialInstitutionId.builder()
                                                .bicfi(request.getCreditorAgentBIC())
                                                .clrSysMmbId(MandateInitiationPain009.ClearingSystemMemberId.builder()
                                                        .mmbId(request.getCreditorAgentMemberId())
                                                        .build())
                                                .build())
                                        .build())
                                .dbtr(MandateInitiationPain009.Debtor.builder()
                                        .nm(request.getDebtorName())
                                        .build())
                                .dbtrAcct(MandateInitiationPain009.Account.builder()
                                        .id(MandateInitiationPain009.AccountId.builder()
                                                .iban(request.getDebtorAccountNumber())
                                                .build())
                                        .nm(request.getDebtorAccountName())
                                        .build())
                                .dbtrAgt(MandateInitiationPain009.Agent.builder()
                                        .finInstnId(MandateInitiationPain009.FinancialInstitutionId.builder()
                                                .bicfi(request.getDebtorAgentBIC())
                                                .clrSysMmbId(MandateInitiationPain009.ClearingSystemMemberId.builder()
                                                        .mmbId(request.getDebtorAgentMemberId())
                                                        .build())
                                                .build())
                                        .build())
                                .rfrdDoc(MandateInitiationPain009.ReferredDocument.builder()
                                        .tp(MandateInitiationPain009.ReferredDocumentType.builder()
                                                .cdOrPrtry(MandateInitiationPain009.CodeOrProprietary.builder()
                                                        .cd(request.getDocumentTypeCode())
                                                        .build())
                                                .build())
                                        .nb(request.getDocumentNumber())
                                        .build())
                                .build())
                        .splmtryData(MandateInitiationPain009.SupplementaryData.builder()
                                .plcAndNm("AdditionalVerificationDetails")
                                .envlp(MandateInitiationPain009.Envelope.builder()
                                        .customData(MandateInitiationPain009.CustomData.builder()
                                                .debtorInfo(MandateInitiationPain009.DebtorInfo.builder()
                                                        .accountDesignation(request.getDebtorAccountDesignation())
                                                        .idType(request.getDebtorIdType())
                                                        .idValue(request.getDebtorIdValue())
                                                        .accountTier(request.getDebtorAccountTier())
                                                        .build())
                                                .debtorMetadata(MandateInitiationPain009.DebtorMetadata.builder()
                                                        .biometricData(request.getDebtorBiometricData())
                                                        .adrLine(request.getDebtorAddressLine())
                                                        .phneNb(request.getDebtorPhoneNumber())
                                                        .emailAdr(request.getDebtorEmailAddress())
                                                        .build())
                                                .creditorInfo(MandateInitiationPain009.CreditorInfo.builder()
                                                        .accountDesignation(request.getCreditorAccountDesignation())
                                                        .idType(request.getCreditorIdType())
                                                        .idValue(request.getCreditorIdValue())
                                                        .accountTier(request.getCreditorAccountTier())
                                                        .build())
                                                .transactionInfo(MandateInitiationPain009.TransactionInfo.builder()
                                                        .transactionLocation(request.getTransactionLocation())
                                                        .channelCode(request.getChannelCode())
                                                        .mandateCategory(request.getMandateCategory())
                                                        .fixedCollectionAmount(request.isFixedCollectionAmount())
                                                        .build())
                                                .build())
                                        .build())
                                .build())
                        .build())
                .build();
    }
}
