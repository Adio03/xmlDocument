package com.strata.xmlDocument.domain.model;

import jakarta.xml.bind.annotation.*;
import lombok.*;
import java.time.OffsetDateTime;

@XmlRootElement(name = "Document", namespace = "urn:iso:std:iso:20022:tech:xsd:acmt.024.001.04")
@XmlType(name = "Document", namespace = "urn:iso:std:iso:20022:tech:xsd:acmt.024.001.04")
@XmlAccessorType(XmlAccessType.FIELD)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class IdentityVerificationReportAcmt024 {

    @XmlElement(name = "IdVrfctnRpt", required = true)
    private IdVerificationReport idVrfctnRpt;

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class IdVerificationReport {
        @XmlElement(name = "Assgnmt", required = true)
        private Assignment assgnmt;

        @XmlElement(name = "OrgnlAssgnmt")
        private OriginalAssignment orgnlAssgnmt;

        @XmlElement(name = "Rpt", required = true)
        private Report rpt;

        @XmlElement(name = "SplmtryData")
        private SupplementaryData splmtryData;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Assignment {
        @XmlElement(name = "MsgId", required = true)
        private String msgId;

        @XmlElement(name = "CreDtTm", required = true)
        private OffsetDateTime creDtTm;

        @XmlElement(name = "Assgnr", required = true)
        private Assigner assgnr;

        @XmlElement(name = "Assgne", required = true)
        private Assignee assgne;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Assigner {
        @XmlElement(name = "Agt", required = true)
        private Agent agt;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Assignee {
        @XmlElement(name = "Pty")
        private Party pty;

        @XmlElement(name = "Agt", required = true)
        private Agent agt;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Party {
        @XmlElement(name = "Nm", required = true)
        private String nm;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
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
    @ToString
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
    @ToString
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
    @ToString
    public static class OriginalAssignment {
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
    @ToString
    public static class Report {
        @XmlElement(name = "OrgnlId", required = true)
        private String orgnlId;

        @XmlElement(name = "Vrfctn", required = true)
        private boolean vrfctn;

        @XmlElement(name = "Rsn")
        private Reason rsn;

        @XmlElement(name = "OrgnlPtyAndAcctId")
        private OriginalPartyAndAccountId orgnlPtyAndAcctId;

        @XmlElement(name = "UpdtdPtyAndAcctId")
        private UpdatedPartyAndAccountId updtdPtyAndAcctId;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class OriginalPartyAndAccountId {
        @XmlElement(name = "Acct", required = true)
        private Account acct;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class UpdatedPartyAndAccountId {
        @XmlElement(name = "Pty", required = true)
        private Party pty;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Reason {
        @XmlElement(name = "Cd")
        private String cd;

        @XmlElement(name = "Prtry")
        private String prtry;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Account {
        @XmlElement(name = "Id", required = true)
        private AccountId id;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
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
    @ToString
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
    @ToString
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
    @ToString
    public static class CustomData {
        @XmlElement(name = "CreditorInfo")
        private CreditorInfo creditorInfo;

        @XmlElement(name = "TransactionInfo")
        private TransactionInfo transactionInfo;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class CreditorInfo {
        @XmlElement(name = "AccountDesignation")
        private String accountDesignation;

        @XmlElement(name = "IdType")
        private String idType;

        @XmlElement(name = "IdValue")
        private String idValue;

        @XmlElement(name = "AccountTier")
        private String accountTier;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class TransactionInfo {
        @XmlElement(name = "RiskRating")
        private String riskRating;
    }
}
