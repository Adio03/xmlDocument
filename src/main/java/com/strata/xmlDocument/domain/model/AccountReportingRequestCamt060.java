package com.strata.xmlDocument.domain.model;

import jakarta.xml.bind.annotation.*;
import lombok.*;
import java.time.OffsetDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@XmlRootElement(name = "Document", namespace = "urn:iso:std:iso:20022:tech:xsd:camt.060.001.07")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountReportingRequestCamt060 {

    @XmlElement(name = "AcctRptgReq", required = true)
    private AccountReportingRequest acctRptgReq;

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AccountReportingRequest {
        @XmlElement(name = "GrpHdr", required = true)
        private GroupHeader grpHdr;

        @XmlElement(name = "RptgReq", required = true)
        private ReportingRequest rptgReq;

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

        @XmlElement(name = "MsgSndr", required = true)
        private MessageSender msgSndr;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MessageSender {
        @XmlElement(name = "Agt", required = true)
        private Agent agt;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReportingRequest {
        @XmlElement(name = "Id", required = true)
        private String id;

        @XmlElement(name = "ReqdMsgNmId", required = true)
        private String reqdMsgNmId;

        @XmlElement(name = "Acct", required = true)
        private Account acct;

        @XmlElement(name = "AcctOwnr", required = true)
        private AccountOwner acctOwnr;

        @XmlElement(name = "AcctSvcr", required = true)
        private AccountServicer acctSvcr;

        @XmlElement(name = "RptgPrd", required = true)
        private ReportingPeriod rptgPrd;
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

        @XmlElement(name = "Ccy", required = true)
        private String ccy;
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
    public static class AccountOwner {
        @XmlElement(name = "Agt", required = true)
        private Agent agt;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AccountServicer {
        @XmlElement(name = "FinInstnId", required = true)
        private FinancialInstitutionId finInstnId;
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
    public static class ReportingPeriod {
        @XmlElement(name = "FrToDt", required = true)
        private FromToDate frToDt;

        @XmlElement(name = "Tp", required = true)
        private String tp;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FromToDate {
        @XmlElement(name = "FrDt", required = true)
        private String frDt;

        @XmlElement(name = "ToDt", required = true)
        private String toDt;
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

        @XmlElement(name = "FixedCollectionAmount", required = true)
        private boolean fixedCollectionAmount;

        @XmlElement(name = "MandateCode")
        private String mandateCode;
    }
}
