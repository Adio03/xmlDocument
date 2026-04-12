package com.strata.xmlDocument.domain.model;

import jakarta.xml.bind.annotation.*;
import lombok.*;
import java.time.OffsetDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@XmlRootElement(name = "Document", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.013.001.11")
@XmlAccessorType(XmlAccessType.FIELD)
public class MandateActivationPain013 {

    @XmlElement(name = "CdtrPmtActvtnReq", required = true)
    private CreditorPaymentActivationRequest cdtrPmtActvtnReq;

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CreditorPaymentActivationRequest {
        @XmlElement(name = "GrpHdr", required = true)
        private GroupHeader grpHdr;

        @XmlElement(name = "PmtInf", required = true)
        private PaymentInformation pmtInf;

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

        @XmlElement(name = "InitgPty", required = true)
        private InitiatingParty initgPty;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class InitiatingParty {
        @XmlElement(name = "Nm", required = true)
        private String nm;

        @XmlElement(name = "Id", required = true)
        private PartyIdentification id;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PartyIdentification {
        @XmlElement(name = "OrgId", required = true)
        private OrganisationIdentification orgId;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrganisationIdentification {
        @XmlElement(name = "Othr", required = true)
        private OtherIdentification othr;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OtherIdentification {
        @XmlElement(name = "Id", required = true)
        private String id;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PaymentInformation {
        @XmlElement(name = "PmtInfId", required = true)
        private String pmtInfId;

        @XmlElement(name = "PmtMtd", required = true)
        private String pmtMtd;

        @XmlElement(name = "ReqdExctnDt", required = true)
        private ExecutionDate reqdExctnDt;

        @XmlElement(name = "Dbtr", required = true)
        private Debtor dbtr;

        @XmlElement(name = "DbtrAcct", required = true)
        private Account dbtrAcct;

        @XmlElement(name = "DbtrAgt", required = true)
        private Agent dbtrAgt;

        @XmlElement(name = "CdtTrfTx", required = true)
        private CreditTransferTransaction cdtTrfTx;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ExecutionDate {
        @XmlElement(name = "DtTm", required = true)
        private OffsetDateTime dtTm;
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

        @XmlElement(name = "Ccy")
        private String ccy;

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

        @XmlElement(name = "Othr")
        private OtherIdentification othr;
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
    public static class CreditTransferTransaction {
        @XmlElement(name = "PmtId", required = true)
        private PaymentIdentification pmtId;

        @XmlElement(name = "Amt", required = true)
        private Amount amt;

        @XmlElement(name = "CdtrAgt", required = true)
        private Agent cdtrAgt;

        @XmlElement(name = "Cdtr", required = true)
        private Creditor cdtr;

        @XmlElement(name = "CdtrAcct", required = true)
        private Account cdtrAcct;

        @XmlElement(name = "Purp")
        private Purpose purp;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PaymentIdentification {
        @XmlElement(name = "EndToEndId", required = true)
        private String endToEndId;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Amount {
        @XmlElement(name = "InstdAmt", required = true)
        private InstructedAmount instdAmt;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class InstructedAmount {
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
    public static class Purpose {
        @XmlElement(name = "Prtry")
        private String prtry;
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

        @XmlElement(name = "AdrLine")
        private String adrLine;

        @XmlElement(name = "PhneNb")
        private String phneNb;

        @XmlElement(name = "EmailAdr")
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
    }
}
