package com.strata.xmlDocument.domain.service;

import com.strata.xmlDocument.application.input.DirectDebitPacs003UseCase;
import com.strata.xmlDocument.domain.model.FIToFICstmrDrctDbtPacs003;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.DirectDebitRequest;
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
public class DirectDebitPacs003Service implements DirectDebitPacs003UseCase {

    private static final String INSTITUTION_ID = "999058";

    @Value("${nibss.private.key.path}")
    private String privateKeyPath;

    @Value("${nibss.public.key.path}")
    private String publicKeyPath;

    @Value("${nibss.api.url.pacs003}")
    private String pacs003ApiUrl;

    @Override
    public String initiateDirectDebit(DirectDebitRequest request) throws Exception {
        FIToFICstmrDrctDbtPacs003 pacs003 = buildPacs003Request(request);
        Document document = XmlDocumentConverter.marshallToDocument(pacs003, FIToFICstmrDrctDbtPacs003.class);

        PrivateKey privateKey = GenerateKey.loadPrivateKey(privateKeyPath);
        PublicKey publicKey = GenerateKey.loadPublicKey(publicKeyPath);

        String rootTag = "FIToFICstmrDrctDbt";
        Signer.sign(document, privateKey);
        String encryptedData = Encrypter.encrypt(document, publicKey, rootTag);

        log.info("ENCRYPTED DIRECT DEBIT REQUEST DATA =====>>>>>> {}",
                encryptedData.substring(0, Math.min(encryptedData.length(), 100)) + "...");

        HttpSender.sendXML(encryptedData, pacs003ApiUrl);

        return pacs003.getFiToFICstmrDrctDbt().getGrpHdr().getMsgId();
    }

    private FIToFICstmrDrctDbtPacs003 buildPacs003Request(DirectDebitRequest request) {
        String msgId = MessageIdGenerator.generateMessageId(INSTITUTION_ID);

        return FIToFICstmrDrctDbtPacs003.builder()
                .fiToFICstmrDrctDbt(FIToFICstmrDrctDbtPacs003.DirectDebitRequest.builder()
                        .grpHdr(FIToFICstmrDrctDbtPacs003.GroupHeader.builder()
                                .msgId(msgId)
                                .creDtTm(OffsetDateTime.now())
                                .nbOfTxs(1)
                                .ctrlSum(request.getControlSum())
                                .instgAgt(FIToFICstmrDrctDbtPacs003.Agent.builder()
                                        .finInstnId(FIToFICstmrDrctDbtPacs003.FinancialInstitutionId.builder()
                                                .clrSysMmbId(FIToFICstmrDrctDbtPacs003.ClearingSystemMemberId.builder()
                                                        .mmbId(request.getCreditorBankMemberId())
                                                        .build())
                                                .build())
                                        .build())
                                .instdAgt(FIToFICstmrDrctDbtPacs003.Agent.builder()
                                        .finInstnId(FIToFICstmrDrctDbtPacs003.FinancialInstitutionId.builder()
                                                .clrSysMmbId(FIToFICstmrDrctDbtPacs003.ClearingSystemMemberId.builder()
                                                        .mmbId(request.getDebtorBankMemberId())
                                                        .build())
                                                .build())
                                        .build())
                                .build())
                        .drctDbtTxInf(FIToFICstmrDrctDbtPacs003.DirectDebitTransactionInformation.builder()
                                .pmtId(FIToFICstmrDrctDbtPacs003.PaymentIdentification.builder()
                                        .instrId(request.getInstructionId())
                                        .endToEndId(request.getEndToEndId())
                                        .txId(request.getTransactionId())
                                        .build())
                                .intrBkSttlmAmt(FIToFICstmrDrctDbtPacs003.Amount.builder()
                                        .ccy(request.getInterbankSettlementCurrency())
                                        .amount(request.getInterbankSettlementAmount())
                                        .build())
                                .intrBkSttlmDt(LocalDate.parse(request.getInterbankSettlementDate()))
                                .instdAmt(FIToFICstmrDrctDbtPacs003.Amount.builder()
                                        .ccy(request.getInstructedCurrency())
                                        .amount(request.getInstructedAmount())
                                        .build())
                                .drctDbtTx(FIToFICstmrDrctDbtPacs003.DirectDebitTransactionDetails.builder()
                                        .mndtRltdInf(FIToFICstmrDrctDbtPacs003.MandateRelatedInformation.builder()
                                                .mndtId(request.getMandateId())
                                                .dtOfSgntr(LocalDate.parse(request.getDateOfSignature()))
                                                .frstColltnDt(LocalDate.parse(request.getFirstCollectionDate()))
                                                .fnlColltnDt(request.getFinalCollectionDate() != null ? LocalDate.parse(request.getFinalCollectionDate()) : null)
                                                .frqcy(FIToFICstmrDrctDbtPacs003.Frequency.builder()
                                                        .tp(request.getFrequencyType())
                                                        .build())
                                                .build())
                                        .build())
                                .cdtr(FIToFICstmrDrctDbtPacs003.Creditor.builder()
                                        .nm(request.getCreditorName())
                                        .build())
                                .cdtrAcct(FIToFICstmrDrctDbtPacs003.Account.builder()
                                        .id(FIToFICstmrDrctDbtPacs003.AccountId.builder()
                                                .iban(request.getCreditorAccountNumber())
                                                .build())
                                        .nm(request.getCreditorAccountName())
                                        .build())
                                .cdtrAgt(FIToFICstmrDrctDbtPacs003.Agent.builder()
                                        .finInstnId(FIToFICstmrDrctDbtPacs003.FinancialInstitutionId.builder()
                                                .bicfi(request.getCreditorBankBIC())
                                                .clrSysMmbId(FIToFICstmrDrctDbtPacs003.ClearingSystemMemberId.builder()
                                                        .mmbId(request.getCreditorBankMemberId())
                                                        .build())
                                                .build())
                                        .build())
                                .instgAgt(FIToFICstmrDrctDbtPacs003.Agent.builder()
                                        .finInstnId(FIToFICstmrDrctDbtPacs003.FinancialInstitutionId.builder()
                                                .bicfi(request.getInstructingBankBIC())
                                                .clrSysMmbId(FIToFICstmrDrctDbtPacs003.ClearingSystemMemberId.builder()
                                                        .mmbId(request.getInstructingBankMemberId())
                                                        .build())
                                                .build())
                                        .build())
                                .instdAgt(FIToFICstmrDrctDbtPacs003.Agent.builder()
                                        .finInstnId(FIToFICstmrDrctDbtPacs003.FinancialInstitutionId.builder()
                                                .bicfi(request.getInstructedBankBIC())
                                                .clrSysMmbId(FIToFICstmrDrctDbtPacs003.ClearingSystemMemberId.builder()
                                                        .mmbId(request.getInstructedBankMemberId())
                                                        .build())
                                                .build())
                                        .build())
                                .dbtr(FIToFICstmrDrctDbtPacs003.Debtor.builder()
                                        .nm(request.getDebtorName())
                                        .build())
                                .dbtrAcct(FIToFICstmrDrctDbtPacs003.Account.builder()
                                        .id(FIToFICstmrDrctDbtPacs003.AccountId.builder()
                                                .iban(request.getDebtorAccountNumber())
                                                .build())
                                        .nm(request.getDebtorAccountName())
                                        .build())
                                .dbtrAgt(FIToFICstmrDrctDbtPacs003.Agent.builder()
                                        .finInstnId(FIToFICstmrDrctDbtPacs003.FinancialInstitutionId.builder()
                                                .bicfi(request.getDebtorBankBIC())
                                                .clrSysMmbId(FIToFICstmrDrctDbtPacs003.ClearingSystemMemberId.builder()
                                                        .mmbId(request.getDebtorBankMemberId())
                                                        .build())
                                                .build())
                                        .build())
                                .rmtInf(FIToFICstmrDrctDbtPacs003.RemittanceInformation.builder()
                                        .ustrd(request.getNarration())
                                        .build())
                                .build())
                        .splmtryData(FIToFICstmrDrctDbtPacs003.SupplementaryData.builder()
                                .plcAndNm("AdditionalVerificationDetails")
                                .envlp(FIToFICstmrDrctDbtPacs003.Envelope.builder()
                                        .customData(FIToFICstmrDrctDbtPacs003.CustomData.builder()
                                                .debtorInfo(FIToFICstmrDrctDbtPacs003.DebtorInfo.builder()
                                                        .accountDesignation(request.getDebtorAccountDesignation())
                                                        .idType(request.getDebtorIdType())
                                                        .idValue(request.getDebtorIdValue())
                                                        .accountTier(request.getDebtorAccountTier())
                                                        .build())
                                                .debtorMetadata(FIToFICstmrDrctDbtPacs003.DebtorMetadata.builder()
                                                        .biometricData(request.getDebtorBiometricData())
                                                        .build())
                                                .creditorInfo(FIToFICstmrDrctDbtPacs003.CreditorInfo.builder()
                                                        .accountDesignation(request.getCreditorAccountDesignation())
                                                        .idType(request.getCreditorIdType())
                                                        .idValue(request.getCreditorIdValue())
                                                        .accountTier(request.getCreditorAccountTier())
                                                        .build())
                                                .transactionInfo(FIToFICstmrDrctDbtPacs003.TransactionInfo.builder()
                                                        .transactionLocation(request.getTransactionLocation())
                                                        .nameEnquiryMsgId(request.getNameEnquiryMsgId())
                                                        .channelCode(request.getChannelCode())
                                                        .fixedCollectionAmount(request.isFixedCollectionAmount())
                                                        .build())
                                                .build())
                                        .build())
                                .build())
                        .build())
                .build();
    }
}
