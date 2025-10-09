package com.strata.xmlDocument.domain.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

@XmlRootElement(name = "Document", namespace = "urn:iso:std:iso:20022:tech:xsd:pacs.002.001.12")
@XmlAccessorType(XmlAccessType.FIELD)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentApprovalPacs002 {
    @XmlElement(name = "FIToFIPmtStsRpt", required = true)
    private PaymentStatusReport fiToFiPmtStsRpt;



    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PaymentStatusReport {

        @XmlElement(name = "GrpHdr", required = true)
        private GroupHeader grpHdr;

        @XmlElement(name = "OrgnlGrpInfAndSts", required = true)
        private OriginalGroupInfoAndStatus orgnlGrpInfAndSts;

        @XmlElement(name = "TxInfAndSts", required = true)
        private TransactionInfoAndStatus txInfAndSts;

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
        private String creDtTm;

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
    public static class OriginalGroupInfoAndStatus {

        @XmlElement(name = "OrgnlMsgId", required = true)
        private String orgnlMsgId;

        @XmlElement(name = "OrgnlMsgNmId", required = true)
        private String orgnlMsgNmId;

        @XmlElement(name = "OrgnlCreDtTm", required = true)
        private String orgnlCreDtTm;

        @XmlElement(name = "GrpSts", required = true)
        private String grpSts;


    }
    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TransactionInfoAndStatus {

        @XmlElement(name = "InstgAgt", required = true)
        private Agent instgAgt;

        @XmlElement(name = "InstdAgt", required = true)
        private Agent instdAgt;

        @XmlElement(name = "OrgnlTxRef", required = true)
        private OriginalTransactionReference orgnlTxRef;

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OriginalTransactionReference {

        @XmlElement(name = "IntrBkSttlmDt", required = true)
        private String intrBkSttlmDt;

    }
}
