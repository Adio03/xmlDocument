package com.strata.xmlDocument.domain.model;

import jakarta.xml.bind.annotation.*;
import lombok.*;
import java.time.OffsetDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@XmlRootElement(name = "Document", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.014.001.11")
@XmlAccessorType(XmlAccessType.FIELD)
public class MandateActivationStatusPain014 {

    @XmlElement(name = "CdtrPmtActvtnReqStsRpt", required = true)
    private CreditorPaymentActivationRequestStatusReport cdtrPmtActvtnReqStsRpt;

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CreditorPaymentActivationRequestStatusReport {
        @XmlElement(name = "GrpHdr", required = true)
        private GroupHeader grpHdr;

        @XmlElement(name = "OrgnlGrpInfAndSts", required = true)
        private OriginalGroupInformationAndStatus orgnlGrpInfAndSts;

        @XmlElement(name = "OrgnlPmtInfAndSts", required = true)
        private OriginalPaymentInformationAndStatus orgnlPmtInfAndSts;
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

        @XmlElement(name = "Cdtr", required = true)
        private Creditor cdtr;

        @XmlElement(name = "CdtrAcct", required = true)
        private Account cdtrAcct;

        @XmlElement(name = "Dbtr", required = true)
        private Debtor dbtr;

        @XmlElement(name = "DbtrAcct", required = true)
        private Account dbtrAcct;

        @XmlElement(name = "FwdgAgt", required = true)
        private Agent fwdgAgt;

        @XmlElement(name = "DbtrAgt", required = true)
        private Agent dbtrAgt;

        @XmlElement(name = "CdtrAgt", required = true)
        private Agent cdtrAgt;
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
    public static class OriginalGroupInformationAndStatus {
        @XmlElement(name = "OrgnlMsgId", required = true)
        private String orgnlMsgId;

        @XmlElement(name = "OrgnlMsgNmId", required = true)
        private String orgnlMsgNmId;

        @XmlElement(name = "OrgnlCreDtTm", required = true)
        private OffsetDateTime orgnlCreDtTm;

        @XmlElement(name = "GrpSts", required = true)
        private String grpSts;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OriginalPaymentInformationAndStatus {
        @XmlElement(name = "OrgnlPmtInfId", required = true)
        private String orgnlPmtInfId;

        @XmlElement(name = "TxInfAndSts", required = true)
        private TransactionInformationAndStatus txInfAndSts;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TransactionInformationAndStatus {
        @XmlElement(name = "OrgnlEndToEndId", required = true)
        private String orgnlEndToEndId;

        @XmlElement(name = "TxSts", required = true)
        private String txSts;
    }
}
