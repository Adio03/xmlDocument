package com.strata.xmlDocument.domain.model;

import jakarta.xml.bind.annotation.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@XmlRootElement(name = "Document", namespace = "urn:iso:std:iso:20022:tech:xsd:pacs.008.001.12")
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentRequestPacs008 {
    @XmlElement(name = "FIToFICstmrCdtTrf", required = true)
    private FIToFICstmrCdtTrf ficoCustomerCreditTransfer;


    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FIToFICstmrCdtTrf {
        @XmlElement(name = "GrpHdr", required = true)
        private GroupHeader grpHdr;

        @XmlElement(name = "CdtTrfTxInf", required = true)
        private CreditTransferTransactionInformation cdtTrfTxInf;

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
        private LocalDateTime creDtTm;

        @XmlElement(name = "BtchBookg", required = true)
        private boolean btchBookg;

        @XmlElement(name = "NbOfTxs", required = true)
        private int nbOfTxs;

        @XmlElement(name = "SttlmInf", required = true)
        private SettlementInformation sttlmInf;

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
    public static class SettlementInformation {
        @XmlElement(name = "SttlmMtd", required = true)
        private String sttlmMtd;

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

    // Financial Institution
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
        private MemberId clrSysMmbId;

    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MemberId {
        @XmlElement(name = "MmbId", required = true)
        private String mmbId;

    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CreditTransferTransactionInformation {

        @XmlElement(name = "PmtId", required = true)
        private PaymentId pmtId;

        @XmlElement(name = "PmtTpInf", required = true)
        private PaymentTypeInformation pmtTpInf;

        @XmlElement(name = "IntrBkSttlmAmt", required = true)
        private Amount intrBkSttlmAmt;

        @XmlElement(name = "IntrBkSttlmDt", required = true)
        private LocalDate intrBkSttlmDt;

        @XmlElement(name = "ChrgBr", required = true)
        private String chrgBr;

        @XmlElement(name = "InstgAgt", required = true)
        private Agent instgAgt;

        @XmlElement(name = "InstdAgt", required = true)
        private Agent instdAgt;

        @XmlElement(name = "Dbtr", required = true)
        private Party dbtr;

        @XmlElement(name = "DbtrAcct", required = true)
        private Account dbtrAcct;

        @XmlElement(name = "DbtrAgt", required = true)
        private Agent dbtrAgt;

        @XmlElement(name = "CdtrAgt", required = true)
        private Agent cdtrAgt;

        @XmlElement(name = "Cdtr", required = true)
        private Party cdtr;

        @XmlElement(name = "CdtrAcct", required = true)
        private Account cdtrAcct;

        @XmlElement(name = "InstrForNxtAgt")
        private List<InstructionForNextAgent> instrForNxtAgt;

        @XmlElement(name = "RmtInf")
        private RemittanceInformation rmtInf;

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PaymentId {
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
    public static class PaymentTypeInformation {
        @XmlElement(name = "ClrChanl", required = true)
        private String clrChanl;
        @XmlElement(name = "SvcLvl", required = true)
        private Proprietary svcLvl;
        @XmlElement(name = "LclInstrm", required = true)
        private Proprietary lclInstrm;
        @XmlElement(name = "CtgyPurp", required = true)
        private Proprietary ctgyPurp;

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Proprietary {
        @XmlElement(name = "Prtry", required = true)
        private String prtry;

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Amount {
        @XmlAttribute(name = "Ccy")
        private String currency;
        @XmlValue
        private BigDecimal value;

    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Party {
        @XmlElement(name = "Nm", required = true)
        private String name;
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
        private String name;

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
    public static class InstructionForNextAgent {
        @XmlElement(name = "InstrInf")
        private String instrInf;
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
        private Object debtorMetadata;

        @XmlElement(name = "CreditorInfo", required = true)
        private CreditorInfo creditorInfo;

        @XmlElement(name = "CreditorMetadata")
        private Object creditorMetadata;

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
        @XmlElement(name = "NameEnquiryMsgId", required = true)
        private String nameEnquiryMsgId;
        @XmlElement(name = "ChannelCode", required = true)
        private String channelCode;
        @XmlElement(name = "RiskRating", required = true)
        private String riskRating;

    }
}
