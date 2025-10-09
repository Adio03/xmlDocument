package com.strata.xmlDocument.domain.model;
import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement(name = "Document", namespace = "urn:iso:std:iso:20022:tech:xsd:acmt.023.001.04")
@XmlAccessorType(XmlAccessType.FIELD)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IdentityVerificationAcmt023 {
    @XmlElement(name = "IdVrfctnReq", required = true)
    private IdVerificationRequest idVrfctnReq;

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IdVerificationRequest {
        @XmlElement(name = "Assgnmt", required = true)
        private Assignment assgnmt;

        @XmlElement(name = "Vrfctn", required = true)
        private Verification vrfctn;

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Assignment {
        @XmlElement(name = "MsgId", required = true)
        private String msgId;

        @XmlElement(name = "CreDtTm", required = true)
        private String creDtTm;

        @XmlElement(name = "Cretr", required = true)
        private PartyWrapper cretr;

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
    public static class PartyWrapper {
        @XmlElement(name = "Pty", required = true)
        private Party pty;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Party {
        @XmlElement(name = "Nm", required = true)
        private String nm;

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Assigner {
        @XmlElement(name = "Pty", required = true)
        private Party pty;

        @XmlElement(name = "Agt", required = true)
        private Agent agt;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Assignee {
        @XmlElement(name = "Agt", required = true)
        private Agent agt;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Agent {
        @XmlElement(name = "FinInstnId", required = true)
        private FinancialInstitutionId finInstnId;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
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
    public static class ClearingSystemMemberId {
        @XmlElement(name = "MmbId", required = true)
        private String mmbId;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Verification {
        @XmlElement(name = "Id", required = true)
        private String id;

        @XmlElement(name = "PtyAndAcctId", required = true)
        private PartyAndAccountId ptyAndAcctId;

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PartyAndAccountId {
        @XmlElement(name = "Pty", required = true)
        private Party pty;

        @XmlElement(name = "Acct", required = true)
        private Account acct;

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Account {
        @XmlElement(name = "Id", required = true)
        private AccountId id;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AccountId {
        @XmlElement(name = "IBAN", required = true)
        private String iban;
    }
}
