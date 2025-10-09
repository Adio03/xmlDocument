package com.strata.xmlDocument.domain.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.time.LocalDateTime;

@XmlRootElement(name = "Document", namespace = "urn:iso:std:iso:20022:tech:xsd:acmt.024.001.04")
@XmlAccessorType(XmlAccessType.FIELD)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IdentityVerificationReportAcmt024 {
    @XmlElement(name = "IdVrfctnRpt", required = true)
    private IdVerificationReport idVerificationReport;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class IdVerificationReport {

        @XmlElement(name = "Assgnmt", required = true)
        private Assignment assignment;

        @XmlElement(name = "Rpt", required = true)
        private Report report;

    }
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Assignment {

        @XmlElement(name = "MsgId", required = true)
        private String msgId;

        @XmlElement(name = "CreDtTm", required = true)
        private LocalDateTime creationDateTime;

        @XmlElement(name = "Assgnr", required = true)
        private Party agent;

        @XmlElement(name = "Assgne", required = true)
        private Party assignee;

    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Party {
        @XmlElement(name = "Agt")
        private Agent agent;

    }
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Agent {
        @XmlElement(name = "FinInstnId")
        private FinancialInstitutionId finInstnId;

    }
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class FinancialInstitutionId {
        @XmlElement(name = "ClrSysMmbId")
        private MemberId clrSysMmbId;
    }
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class MemberId {
        @XmlElement(name = "MmbId")
        private String mmbId;

    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Report {
        @XmlElement(name = "Id", required = true)
        private String id;

        @XmlElement(name = "Rspn", required = true)
        private Response response;

    }
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Response {
        @XmlElement(name = "RspnCd")
        private String responseCode;

        @XmlElement(name = "AddtlInf")
        private String additionalInfo;

    }


}
