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
@XmlRootElement(name = "Document", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.009.001.08")
@XmlAccessorType(XmlAccessType.FIELD)
public class MandateInitiationPain009 {

    @XmlElement(name = "MndtInitnReq", required = true)
    private MandateInitiationRequest mndtInitnReq;

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MandateInitiationRequest {
        @XmlElement(name = "GrpHdr", required = true)
        private GroupHeader grpHdr;

        @XmlElement(name = "Mndt", required = true)
        private Mandate mandate;

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
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Mandate {
        @XmlElement(name = "MndtId", required = true)
        private String mndtId;

        @XmlElement(name = "Ocrncs", required = true)
        private Occurrences ocrncs;

        @XmlElement(name = "TrckgInd", required = true)
        private boolean trckgInd;

        @XmlElement(name = "ColltnAmt", required = true)
        private CollectionAmount colltnAmt;

        @XmlElement(name = "Cdtr", required = true)
        private Creditor cdtr;

        @XmlElement(name = "CdtrAcct", required = true)
        private Account cdtrAcct;

        @XmlElement(name = "CdtrAgt", required = true)
        private Agent cdtrAgt;

        @XmlElement(name = "Dbtr", required = true)
        private Debtor dbtr;

        @XmlElement(name = "DbtrAcct", required = true)
        private Account dbtrAcct;

        @XmlElement(name = "DbtrAgt", required = true)
        private Agent dbtrAgt;

        @XmlElement(name = "RfrdDoc", required = true)
        private ReferredDocument rfrdDoc;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Occurrences {
        @XmlElement(name = "SeqTp", required = true)
        private String seqTp;

        @XmlElement(name = "Frqcy", required = true)
        private Frequency frqcy;

        @XmlElement(name = "FrstColltnDt", required = true)
        private LocalDate frstColltnDt;

        @XmlElement(name = "FnlColltnDt", required = true)
        private LocalDate fnlColltnDt;
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
    public static class CollectionAmount {
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
    public static class ReferredDocument {
        @XmlElement(name = "Tp", required = true)
        private ReferredDocumentType tp;

        @XmlElement(name = "Nb", required = true)
        private String nb;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReferredDocumentType {
        @XmlElement(name = "CdOrPrtry", required = true)
        private CodeOrProprietary cdOrPrtry;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CodeOrProprietary {
        @XmlElement(name = "Cd", required = true)
        private String cd;
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

        @XmlElement(name = "DebtorMetadata", required = true)
        private DebtorMetadata debtorMetadata;

        @XmlElement(name = "CreditorInfo", required = true)
        private CreditorInfo creditorInfo;

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

        @XmlElement(name = "AdrLine", required = true)
        private String adrLine;

        @XmlElement(name = "PhneNb", required = true)
        private String phneNb;

        @XmlElement(name = "EmailAdr", required = true)
        private String emailAdr;
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
    public static class TransactionInfo {
        @XmlElement(name = "TransactionLocation", required = true)
        private String transactionLocation;

        @XmlElement(name = "ChannelCode", required = true)
        private String channelCode;

        @XmlElement(name = "MandateCategory", required = true)
        private String mandateCategory;

        @XmlElement(name = "FixedCollectionAmount", required = true)
        private boolean fixedCollectionAmount;
    }
}
