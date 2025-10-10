package com.strata.xmlDocument.domain.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@XmlRootElement(name = "Document", namespace = "urn:iso:std:iso:20022:tech:xsd:pacs.028.001.06")
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentStatusPacs028 {

    @XmlElement(name = "FIToFIPmtStsReq", required = true)
    private FIToFIPmtStsReq fiToFIPmtStsReq;


    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FIToFIPmtStsReq {

        @XmlElement(name = "GrpHdr", required = true)
        private GroupHeader grpHdr;

        @XmlElement(name = "OrgnlGrpInf", required = true)
        private OriginalGroupInformation orgnlGrpInf;

        @XmlElement(name = "TxInf", required = true)
        private TransactionInformation txInf;

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

        @XmlElement(name = "InstgAgt", required = true)
        private Agent instgAgt;

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OriginalGroupInformation {

        @XmlElement(name = "OrgnlMsgId", required = true)
        private String orgnlMsgId;

        @XmlElement(name = "OrgnlMsgNmId", required = true)
        private String orgnlMsgNmId;

        @XmlElement(name = "OrgnlCreDtTm", required = true)
        private OffsetDateTime orgnlCreDtTm;

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TransactionInformation {

        @XmlElement(name = "StsReqId", required = true)
        private String stsReqId;

        @XmlElement(name = "OrgnlTxId", required = true)
        private String orgnlTxId;

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
    public static class Agent {

        @XmlElement(name = "FinInstnId", required = true)
        private FinancialInstitutionIdentification finInstnId;

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FinancialInstitutionIdentification {

        @XmlElement(name = "BICFI")
        private String bicfi;

        @XmlElement(name = "ClrSysMmbId", required = true)
        private ClearingSystemMemberIdentification clrSysMmbId;

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ClearingSystemMemberIdentification {

        @XmlElement(name = "MmbId", required = true)
        private String mmbId;

    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OriginalTransactionReference {

        @XmlElement(name = "IntrBkSttlmDt", required = true)
        private LocalDate intrBkSttlmDt;


    }
}
