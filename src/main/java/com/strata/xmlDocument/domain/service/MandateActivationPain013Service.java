package com.strata.xmlDocument.domain.service;

import com.strata.xmlDocument.application.input.MandateActivationPain013UseCase;
import com.strata.xmlDocument.domain.model.MandateActivationPain013;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.MandateActivationRequest;
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
public class MandateActivationPain013Service implements MandateActivationPain013UseCase {

    private static final String INSTITUTION_ID = "999058";

    @Value("${nibss.private.key.path}")
    private String privateKeyPath;

    @Value("${nibss.public.key.path}")
    private String publicKeyPath;

    @Value("${nibss.api.url.pain013}")
    private String pain013ApiUrl;

    @Override
    public String activateMandate(MandateActivationRequest request) throws Exception {
        MandateActivationPain013 pain013 = buildPain013Request(request);
        Document document = XmlDocumentConverter.marshallToDocument(pain013, MandateActivationPain013.class);

        PrivateKey privateKey = GenerateKey.loadPrivateKey(privateKeyPath);
        PublicKey publicKey = GenerateKey.loadPublicKey(publicKeyPath);

        String rootTag = "CdtrPmtActvtnReq";
        Signer.sign(document, privateKey);
        String encryptedData = Encrypter.encrypt(document, publicKey, rootTag);

        log.info("ENCRYPTED MANDATE ACTIVATION REQUEST DATA =====>>>>>> {}",
                encryptedData.substring(0, Math.min(encryptedData.length(), 100)) + "...");

        HttpSender.sendXML(encryptedData, pain013ApiUrl);

        return pain013.getCdtrPmtActvtnReq().getGrpHdr().getMsgId();
    }

    private MandateActivationPain013 buildPain013Request(MandateActivationRequest request) {
        String msgId = MessageIdGenerator.generateMessageId(INSTITUTION_ID);

        return MandateActivationPain013.builder()
                .cdtrPmtActvtnReq(MandateActivationPain013.CreditorPaymentActivationRequest.builder()
                        .grpHdr(MandateActivationPain013.GroupHeader.builder()
                                .msgId(msgId)
                                .creDtTm(OffsetDateTime.now())
                                .initgPty(MandateActivationPain013.InitiatingParty.builder()
                                        .nm(request.getInitiatingPartyName())
                                        .id(MandateActivationPain013.PartyIdentification.builder()
                                                .orgId(MandateActivationPain013.OrganisationIdentification.builder()
                                                        .othr(MandateActivationPain013.OtherIdentification.builder()
                                                                .id(request.getClientId())
                                                                .build())
                                                        .build())
                                                .build())
                                        .build())
                                .build())
                        .pmtInf(MandateActivationPain013.PaymentInformation.builder()
                                .pmtInfId(request.getPaymentInformationId())
                                .pmtMtd("TRF")
                                .reqdExctnDt(MandateActivationPain013.ExecutionDate.builder()
                                        .dtTm(OffsetDateTime.parse(request.getRequestedExecutionDateTime()))
                                        .build())
                                .dbtr(MandateActivationPain013.Debtor.builder()
                                        .nm(request.getDebtorName())
                                        .build())
                                .dbtrAcct(MandateActivationPain013.Account.builder()
                                        .id(MandateActivationPain013.AccountId.builder()
                                                .iban(request.getDebtorAccountNumber())
                                                .build())
                                        .ccy(request.getDebtorAccountCurrency())
                                        .nm(request.getDebtorAccountName())
                                        .build())
                                .dbtrAgt(MandateActivationPain013.Agent.builder()
                                        .finInstnId(MandateActivationPain013.FinancialInstitutionId.builder()
                                                .bicfi(request.getDebtorAgentBIC())
                                                .clrSysMmbId(MandateActivationPain013.ClearingSystemMemberId.builder()
                                                        .mmbId(request.getDebtorAgentMemberId())
                                                        .build())
                                                .build())
                                        .build())
                                .cdtTrfTx(MandateActivationPain013.CreditTransferTransaction.builder()
                                        .pmtId(MandateActivationPain013.PaymentIdentification.builder()
                                                .endToEndId(request.getEndToEndId())
                                                .build())
                                        .amt(MandateActivationPain013.Amount.builder()
                                                .instdAmt(MandateActivationPain013.InstructedAmount.builder()
                                                        .ccy(request.getInstructedCurrency())
                                                        .amount(request.getInstructedAmount())
                                                        .build())
                                                .build())
                                        .cdtrAgt(MandateActivationPain013.Agent.builder()
                                                .finInstnId(MandateActivationPain013.FinancialInstitutionId.builder()
                                                        .bicfi(request.getCreditorAgentBIC())
                                                        .clrSysMmbId(MandateActivationPain013.ClearingSystemMemberId.builder()
                                                                .mmbId(request.getCreditorAgentMemberId())
                                                                .build())
                                                        .build())
                                                .build())
                                        .cdtr(MandateActivationPain013.Creditor.builder()
                                                .nm(request.getCreditorName())
                                                .build())
                                        .cdtrAcct(MandateActivationPain013.Account.builder()
                                                .id(MandateActivationPain013.AccountId.builder()
                                                        .iban(request.getCreditorAccountNumber())
                                                        .build())
                                                .nm(request.getCreditorAccountName())
                                                .build())
                                        .purp(MandateActivationPain013.Purpose.builder()
                                                .prtry(request.getPaymentPurpose())
                                                .build())
                                        .build())
                                .build())
                        .splmtryData(MandateActivationPain013.SupplementaryData.builder()
                                .plcAndNm("AdditionalVerificationDetails")
                                .envlp(MandateActivationPain013.Envelope.builder()
                                        .customData(MandateActivationPain013.CustomData.builder()
                                                .debtorInfo(MandateActivationPain013.DebtorInfo.builder()
                                                        .accountDesignation(request.getDebtorAccountDesignation())
                                                        .idType(request.getDebtorIdType())
                                                        .idValue(request.getDebtorIdValue())
                                                        .accountTier(request.getDebtorAccountTier())
                                                        .build())
                                                .debtorMetadata(MandateActivationPain013.DebtorMetadata.builder()
                                                        .biometricData(request.getDebtorBiometricData())
                                                        .adrLine(request.getDebtorAddressLine())
                                                        .phneNb(request.getDebtorPhoneNumber())
                                                        .emailAdr(request.getDebtorEmailAddress())
                                                        .build())
                                                .creditorInfo(MandateActivationPain013.CreditorInfo.builder()
                                                        .accountDesignation(request.getCreditorAccountDesignation())
                                                        .idType(request.getCreditorIdType())
                                                        .idValue(request.getCreditorIdValue())
                                                        .accountTier(request.getCreditorAccountTier())
                                                        .build())
                                                .transactionInfo(MandateActivationPain013.TransactionInfo.builder()
                                                        .transactionLocation(request.getTransactionLocation())
                                                        .channelCode(request.getChannelCode())
                                                        .mandateCategory(request.getMandateCategory())
                                                        .build())
                                                .build())
                                        .build())
                                .build())
                        .build())
                .build();
    }
}
