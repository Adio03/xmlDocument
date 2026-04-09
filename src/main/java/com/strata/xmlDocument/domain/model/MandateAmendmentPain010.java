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
@XmlRootElement(name = "Document", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.010.001.08")
@XmlAccessorType(XmlAccessType.FIELD)
public class MandateAmendmentPain010 {

    @XmlElement(name = "MndtAmdmntReq", required = true)
    private MandateAmendmentRequest mndtAmdmntReq;

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MandateAmendmentRequest {
        @XmlElement(name = "GrpHdr", required = true)
        private GroupHeader grpHdr;

        @XmlElement(name = "UndrlygAmdmntDtls", required = true)
        private UnderlyingAmendmentDetails undrlygAmdmntDtls;
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
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UnderlyingAmendmentDetails {
        @XmlElement(name = "OrgnlMsgInf", required = true)
        private OriginalMessageInformation orgnlMsgInf;

        @XmlElement(name = "AmdmntRsn", required = true)
        private AmendmentReason amdmntRsn;

        @XmlElement(name = "Mndt", required = true)
        private Mandate mandate;

        @XmlElement(name = "OrgnlMndt", required = true)
        private OriginalMandate orgnlMndt;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OriginalMessageInformation {
        @XmlElement(name = "MsgId", required = true)
        private String msgId;

        @XmlElement(name = "MsgNmId", required = true)
        private String msgNmId;

        @XmlElement(name = "CreDtTm", required = true)
        private OffsetDateTime creDtTm;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AmendmentReason {
        @XmlElement(name = "Rsn", required = true)
        private Reason rsn;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Reason {
        @XmlElement(name = "Cd", required = true)
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
    public static class Mandate {
        @XmlElement(name = "MndtId", required = true)
        private String mndtId;

        @XmlElement(name = "Ocrncs", required = true)
        private Occurrences ocrncs;

        @XmlElement(name = "TrckgInd", required = true)
        private boolean trckgInd;

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
    public static class OriginalMandate {
        @XmlElement(name = "OrgnlMndtId", required = true)
        private String orgnlMndtId;
    }
}
