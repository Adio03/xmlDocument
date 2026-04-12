package com.strata.xmlDocument.domain.model;

import jakarta.xml.bind.annotation.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@XmlRootElement(name = "Document", namespace = "urn:iso:std:iso:20022:tech:xsd:camt.052.001.12")
@XmlAccessorType(XmlAccessType.FIELD)
public class BankToCustomerAccountReportCamt052 {

    @XmlElement(name = "BkToCstmrAcctRpt", required = true)
    private BankToCustomerAccountReport bkToCstmrAcctRpt;

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BankToCustomerAccountReport {
        @XmlElement(name = "GrpHdr", required = true)
        private GroupHeader grpHdr;

        @XmlElement(name = "Rpt", required = true)
        private Report rpt;
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

        @XmlElement(name = "MsgRcpt")
        private MessageRecipient msgRcpt;

        @XmlElement(name = "OrgnlBizQry", required = true)
        private OriginalBusinessQuery orgnlBizQry;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MessageRecipient {
        @XmlElement(name = "Nm")
        private String nm;

        @XmlElement(name = "Id")
        private PartyIdentification id;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PartyIdentification {
        @XmlElement(name = "OrgId")
        private OrganisationIdentification orgId;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrganisationIdentification {
        @XmlElement(name = "AnyBIC")
        private String anyBIC;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OriginalBusinessQuery {
        @XmlElement(name = "MsgId", required = true)
        private String msgId;

        @XmlElement(name = "MsgNmId", required = true)
        private String msgNmId;

        @XmlElement(name = "CreDtTm")
        private OffsetDateTime creDtTm;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Report {
        @XmlElement(name = "Id", required = true)
        private String id;

        @XmlElement(name = "FrToDt", required = true)
        private FromToDate frToDt;

        @XmlElement(name = "Acct", required = true)
        private Account acct;

        @XmlElement(name = "Bal", required = true)
        private Balance bal;

        @XmlElement(name = "Ntry")
        private List<Entry> ntry;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FromToDate {
        @XmlElement(name = "FrDtTm", required = true)
        private OffsetDateTime frDtTm;

        @XmlElement(name = "ToDtTm", required = true)
        private OffsetDateTime toDtTm;
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

        @XmlElement(name = "Ownr")
        private AccountOwner ownr;

        @XmlElement(name = "Svcr", required = true)
        private AccountServicer svcr;
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
        @XmlElement(name = "Id")
        private PartyIdentificationWithOther id;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PartyIdentificationWithOther {
        @XmlElement(name = "OrgId")
        private OrganisationIdentificationWithOther orgId;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrganisationIdentificationWithOther {
        @XmlElement(name = "Othr")
        private OtherIdentification othr;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OtherIdentification {
        @XmlElement(name = "SchmeNm")
        private SchemeName schmeNm;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SchemeName {
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
    public static class Balance {
        @XmlElement(name = "Tp", required = true)
        private BalanceType tp;

        @XmlElement(name = "Amt", required = true)
        private CurrencyAmount amt;

        @XmlElement(name = "CdtDbtInd", required = true)
        private String cdtDbtInd;

        @XmlElement(name = "Dt", required = true)
        private BalanceDate dt;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BalanceType {
        @XmlElement(name = "CdOrPrtry", required = true)
        private CodeOrProprietary cdOrPrtry;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CodeOrProprietary {
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
    public static class CurrencyAmount {
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
    public static class BalanceDate {
        @XmlElement(name = "DtTm", required = true)
        private OffsetDateTime dtTm;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Entry {
        @XmlElement(name = "Amt", required = true)
        private CurrencyAmount amt;

        @XmlElement(name = "CdtDbtInd", required = true)
        private String cdtDbtInd;

        @XmlElement(name = "Sts", required = true)
        private EntryStatus sts;

        @XmlElement(name = "BookgDt", required = true)
        private DateWrapper bookgDt;

        @XmlElement(name = "ValDt", required = true)
        private DateWrapper valDt;

        @XmlElement(name = "AcctSvcrRef")
        private String acctSvcrRef;

        @XmlElement(name = "BkTxCd", required = true)
        private BankTransactionCode bkTxCd;

        @XmlElement(name = "NtryDtls")
        private EntryDetails ntryDtls;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class EntryStatus {
        @XmlElement(name = "Prtry", required = true)
        private String prtry;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DateWrapper {
        @XmlElement(name = "Dt", required = true)
        private String dt;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BankTransactionCode {
        @XmlElement(name = "Domn", required = true)
        private Domain domn;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Domain {
        @XmlElement(name = "Cd", required = true)
        private String cd;

        @XmlElement(name = "Fmly", required = true)
        private Family fmly;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Family {
        @XmlElement(name = "Cd", required = true)
        private String cd;

        @XmlElement(name = "SubFmlyCd", required = true)
        private String subFmlyCd;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class EntryDetails {
        @XmlElement(name = "TxDtls")
        private TransactionDetails txDtls;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TransactionDetails {
        @XmlElement(name = "RltdAgts")
        private RelatedAgents rltdAgts;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RelatedAgents {
        @XmlElement(name = "InstdAgt")
        private AgentWrapper instdAgt;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AgentWrapper {
        @XmlElement(name = "FinInstnId")
        private FinancialInstitutionIdShort finInstnId;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FinancialInstitutionIdShort {
        @XmlElement(name = "BICFI")
        private String bicfi;
    }
}
