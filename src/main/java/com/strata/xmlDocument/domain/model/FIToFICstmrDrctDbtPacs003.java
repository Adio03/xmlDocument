package com.strata.xmlDocument.domain.model;

import jakarta.xml.bind.annotation.*;
import lombok.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@XmlRootElement(name = "Document", namespace = "urn:iso:std:iso:20022:tech:xsd:pacs.003.001.11")
@XmlAccessorType(XmlAccessType.FIELD)
public class FIToFICstmrDrctDbtPacs003 {

    @XmlElement(name = "FIToFICstmrDrctDbt", required = true)
    private DirectDebitRequest fiToFICstmrDrctDbt;

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DirectDebitRequest {
        @XmlElement(name = "GrpHdr", required = true)
        private GroupHeader grpHdr;

        @XmlElement(name = "DrctDbtTxInf", required = true)
        private DirectDebitTransactionInformation drctDbtTxInf;

        @XmlElement(name = "SplmtryData")
        private SupplementaryData splmtryData;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GroupHeader {
        @XmlElement(name = "MsgId", required = true)
        private String msgId;

        @XmlElement(name = "CreDtTm", required = true)
        private OffsetDateTime creDtTm;

        @XmlElement(name = "NbOfTxs", required = true)
        private int nbOfTxs;

        @XmlElement(name = "CtrlSum", required = true)
        private String ctrlSum;

        @XmlElement(name = "InstgAgt", required = true)
        private Agent instgAgt;

        @XmlElement(name = "InstdAgt", required = true)
        private Agent instdAgt;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DirectDebitTransactionInformation {
        @XmlElement(name = "PmtId", required = true)
        private PaymentIdentification pmtId;

        @XmlElement(name = "IntrBkSttlmAmt", required = true)
        private Amount intrBkSttlmAmt;

        @XmlElement(name = "IntrBkSttlmDt", required = true)
        private LocalDate intrBkSttlmDt;

        @XmlElement(name = "InstdAmt", required = true)
        private Amount instdAmt;

        @XmlElement(name = "DrctDbtTx", required = true)
        private DirectDebitTransactionDetails drctDbtTx;

        @XmlElement(name = "Cdtr", required = true)
        private Creditor cdtr;

        @XmlElement(name = "CdtrAcct", required = true)
        private Account cdtrAcct;

        @XmlElement(name = "CdtrAgt", required = true)
        private Agent cdtrAgt;

        @XmlElement(name = "InstgAgt", required = true)
        private Agent instgAgt;

        @XmlElement(name = "InstdAgt", required = true)
        private Agent instdAgt;

        @XmlElement(name = "Dbtr", required = true)
        private Debtor dbtr;

        @XmlElement(name = "DbtrAcct", required = true)
        private Account dbtrAcct;

        @XmlElement(name = "DbtrAgt", required = true)
        private Agent dbtrAgt;

        @XmlElement(name = "RmtInf")
        private RemittanceInformation rmtInf;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PaymentIdentification {
        @XmlElement(name = "InstrId", required = true)
        private String instrId;

        @XmlElement(name = "EndToEndId", required = true)
        private String endToEndId;

        @XmlElement(name = "TxId", required = true)
        private String txId;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Amount {
        @XmlAttribute(name = "Ccy")
        private String ccy;

        @XmlValue
        private String amount;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DirectDebitTransactionDetails {
        @XmlElement(name = "MndtRltdInf", required = true)
        private MandateRelatedInformation mndtRltdInf;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MandateRelatedInformation {
        @XmlElement(name = "MndtId", required = true)
        private String mndtId;

        @XmlElement(name = "DtOfSgntr", required = true)
        private LocalDate dtOfSgntr;

        @XmlElement(name = "FrstColltnDt", required = true)
        private LocalDate frstColltnDt;

        @XmlElement(name = "FnlColltnDt")
        private LocalDate fnlColltnDt;

        @XmlElement(name = "Frqcy", required = true)
        private Frequency frqcy;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Frequency {
        @XmlElement(name = "Tp", required = true)
        private String tp;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Creditor {
        @XmlElement(name = "Nm", required = true)
        private String nm;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Debtor {
        @XmlElement(name = "Nm", required = true)
        private String nm;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Account {
        @XmlElement(name = "Id", required = true)
        private AccountId id;

        @XmlElement(name = "Nm", required = true)
        private String nm;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AccountId {
        @XmlElement(name = "IBAN", required = true)
        private String iban;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Agent {
        @XmlElement(name = "FinInstnId", required = true)
        private FinancialInstitutionId finInstnId;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FinancialInstitutionId {
        @XmlElement(name = "BICFI")
        private String bicfi;

        @XmlElement(name = "ClrSysMmbId", required = true)
        private ClearingSystemMemberId clrSysMmbId;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ClearingSystemMemberId {
        @XmlElement(name = "MmbId", required = true)
        private String mmbId;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RemittanceInformation {
        @XmlElement(name = "Ustrd")
        private String ustrd;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SupplementaryData {
        @XmlElement(name = "PlcAndNm", required = true)
        private String plcAndNm;

        @XmlElement(name = "Envlp", required = true)
        private Envelope envlp;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Envelope {
        @XmlElement(name = "CustomData", required = true)
        private CustomData customData;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CustomData {
        @XmlElement(name = "DebtorInfo", required = true)
        private DebtorInfo debtorInfo;

        @XmlElement(name = "DebtorMetadata")
        private DebtorMetadata debtorMetadata;

        @XmlElement(name = "CreditorInfo", required = true)
        private CreditorInfo creditorInfo;

        @XmlElement(name = "CreditorMetadata")
        private CreditorMetadata creditorMetadata;

        @XmlElement(name = "TransactionInfo", required = true)
        private TransactionInfo transactionInfo;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DebtorInfo {
        @XmlElement(name = "AccountDesignation", required = true)
        private String accountDesignation;

        @XmlElement(name = "IdType", required = true)
        private String idType;

        @XmlElement(name = "IdValue", required = true)
        private String idValue;

        @XmlElement(name = "AccountTier", required = true)
        private String accountTier;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DebtorMetadata {
        @XmlElement(name = "BiometricData")
        private String biometricData;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CreditorInfo {
        @XmlElement(name = "AccountDesignation", required = true)
        private String accountDesignation;

        @XmlElement(name = "IdType", required = true)
        private String idType;

        @XmlElement(name = "IdValue", required = true)
        private String idValue;

        @XmlElement(name = "AccountTier", required = true)
        private String accountTier;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CreditorMetadata {
        // Additional metadata if needed
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TransactionInfo {
        @XmlElement(name = "TransactionLocation", required = true)
        private String transactionLocation;

        @XmlElement(name = "NameEnquiryMsgId", required = true)
        private String nameEnquiryMsgId;

        @XmlElement(name = "ChannelCode", required = true)
        private String channelCode;

        @XmlElement(name = "RiskRating")
        private String riskRating;

        @XmlElement(name = "FixedCollectionAmount", required = true)
        private boolean fixedCollectionAmount;
    }
}
