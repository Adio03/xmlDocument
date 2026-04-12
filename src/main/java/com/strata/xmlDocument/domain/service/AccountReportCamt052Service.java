package com.strata.xmlDocument.domain.service;

import com.strata.xmlDocument.application.input.AccountReportCamt052UseCase;
import com.strata.xmlDocument.domain.model.BankToCustomerAccountReportCamt052;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.AccountReportRequest;
import com.strata.xmlDocument.infrastructure.adapter.output.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.OffsetDateTime;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccountReportCamt052Service implements AccountReportCamt052UseCase {

    @Value("${nibss.private.key.path}")
    private String privateKeyPath;

    @Value("${nibss.public.key.path}")
    private String publicKeyPath;

    @Value("${nibss.api.url.camt052}")
    private String camt052ApiUrl;

    @Override
    public String generateAccountReport(AccountReportRequest request) {
        try {
            BankToCustomerAccountReportCamt052 camt052 = buildCamt052Request(request);
            Document document = XmlDocumentConverter.marshallToDocument(camt052, BankToCustomerAccountReportCamt052.class);

            PrivateKey privateKey = GenerateKey.loadPrivateKey(privateKeyPath);
            PublicKey publicKey = GenerateKey.loadPublicKey(publicKeyPath);

            String rootTag = "BkToCstmrAcctRpt";
            Signer.sign(document, privateKey);
            String encryptedData = Encrypter.encrypt(document, publicKey, rootTag);

            log.info("ENCRYPTED CAMT.052 ACCOUNT REPORT REQUEST DATA =====>>>>>> {}",
                    encryptedData.substring(0, Math.min(encryptedData.length(), 100)) + "...");

            HttpSender.sendXML(encryptedData, camt052ApiUrl);

            return camt052.getBkToCstmrAcctRpt().getGrpHdr().getMsgId();
        } catch (Exception e) {
            log.error("Error generating camt.052 account report", e);
            throw new RuntimeException("Failed to generate camt.052 account report", e);
        }
    }

    private BankToCustomerAccountReportCamt052 buildCamt052Request(AccountReportRequest request) {
        return BankToCustomerAccountReportCamt052.builder()
                .bkToCstmrAcctRpt(BankToCustomerAccountReportCamt052.BankToCustomerAccountReport.builder()
                        .grpHdr(BankToCustomerAccountReportCamt052.GroupHeader.builder()
                                .msgId(request.getOriginalQueryMessageId()) // Or generate a new one
                                .creDtTm(OffsetDateTime.now())
                                .msgRcpt(BankToCustomerAccountReportCamt052.MessageRecipient.builder()
                                        .nm(request.getMessageRecipientName())
                                        .id(BankToCustomerAccountReportCamt052.PartyIdentification.builder()
                                                .orgId(BankToCustomerAccountReportCamt052.OrganisationIdentification.builder()
                                                        .anyBIC(request.getMessageRecipientBIC())
                                                        .build())
                                                .build())
                                        .build())
                                .orgnlBizQry(BankToCustomerAccountReportCamt052.OriginalBusinessQuery.builder()
                                        .msgId(request.getOriginalQueryMessageId())
                                        .msgNmId(request.getOriginalQueryMessageType())
                                        .creDtTm(request.getOriginalQueryCreationDateTime() != null ? 
                                                OffsetDateTime.parse(request.getOriginalQueryCreationDateTime()) : null)
                                        .build())
                                .build())
                        .rpt(BankToCustomerAccountReportCamt052.Report.builder()
                                .id(request.getReportId())
                                .frToDt(BankToCustomerAccountReportCamt052.FromToDate.builder()
                                        .frDtTm(OffsetDateTime.parse(request.getFromDateTime()))
                                        .toDtTm(OffsetDateTime.parse(request.getToDateTime()))
                                        .build())
                                .acct(BankToCustomerAccountReportCamt052.Account.builder()
                                        .id(BankToCustomerAccountReportCamt052.AccountId.builder()
                                                .iban(request.getAccountNumber())
                                                .build())
                                        .ccy(request.getAccountCurrency())
                                        .ownr(BankToCustomerAccountReportCamt052.AccountOwner.builder()
                                                .id(BankToCustomerAccountReportCamt052.PartyIdentificationWithOther.builder()
                                                        .orgId(BankToCustomerAccountReportCamt052.OrganisationIdentificationWithOther.builder()
                                                                .othr(BankToCustomerAccountReportCamt052.OtherIdentification.builder()
                                                                        .schmeNm(BankToCustomerAccountReportCamt052.SchemeName.builder()
                                                                                .cd(request.getSchemeCode())
                                                                                .prtry(request.getProprietaryScheme())
                                                                                .build())
                                                                        .build())
                                                                .build())
                                                        .build())
                                                .build())
                                        .svcr(BankToCustomerAccountReportCamt052.AccountServicer.builder()
                                                .finInstnId(BankToCustomerAccountReportCamt052.FinancialInstitutionId.builder()
                                                        .bicfi(request.getAccountServicerBIC())
                                                        .clrSysMmbId(BankToCustomerAccountReportCamt052.ClearingSystemMemberId.builder()
                                                                .mmbId(request.getAccountServicerMemberId())
                                                                .build())
                                                        .build())
                                                .build())
                                        .build())
                                .bal(BankToCustomerAccountReportCamt052.Balance.builder()
                                        .tp(BankToCustomerAccountReportCamt052.BalanceType.builder()
                                                .cdOrPrtry(BankToCustomerAccountReportCamt052.CodeOrProprietary.builder()
                                                        .prtry(request.getBalanceType())
                                                        .build())
                                                .build())
                                        .amt(BankToCustomerAccountReportCamt052.CurrencyAmount.builder()
                                                .ccy(request.getBalanceCurrency())
                                                .amount(request.getBalanceAmount())
                                                .build())
                                        .cdtDbtInd(request.getCreditDebitIndicator())
                                        .dt(BankToCustomerAccountReportCamt052.BalanceDate.builder()
                                                .dtTm(OffsetDateTime.parse(request.getBalanceDateTime()))
                                                .build())
                                        .build())
                                .ntry(request.getEntries().stream().map(entry -> BankToCustomerAccountReportCamt052.Entry.builder()
                                        .amt(BankToCustomerAccountReportCamt052.CurrencyAmount.builder()
                                                .ccy(entry.getCurrency())
                                                .amount(entry.getAmount())
                                                .build())
                                        .cdtDbtInd(entry.getCreditDebitIndicator())
                                        .sts(BankToCustomerAccountReportCamt052.EntryStatus.builder()
                                                .prtry(entry.getStatus())
                                                .build())
                                        .bookgDt(BankToCustomerAccountReportCamt052.DateWrapper.builder()
                                                .dt(entry.getBookingDate())
                                                .build())
                                        .valDt(BankToCustomerAccountReportCamt052.DateWrapper.builder()
                                                .dt(entry.getValueDate())
                                                .build())
                                        .acctSvcrRef(entry.getAccountServicerReference())
                                        .bkTxCd(BankToCustomerAccountReportCamt052.BankTransactionCode.builder()
                                                .domn(BankToCustomerAccountReportCamt052.Domain.builder()
                                                        .cd(entry.getDomainCode())
                                                        .fmly(BankToCustomerAccountReportCamt052.Family.builder()
                                                                .cd(entry.getFamilyCode())
                                                                .subFmlyCd(entry.getSubFamilyCode())
                                                                .build())
                                                        .build())
                                                .build())
                                        .ntryDtls(BankToCustomerAccountReportCamt052.EntryDetails.builder()
                                                .txDtls(BankToCustomerAccountReportCamt052.TransactionDetails.builder()
                                                        .rltdAgts(BankToCustomerAccountReportCamt052.RelatedAgents.builder()
                                                                .instdAgt(BankToCustomerAccountReportCamt052.InstructedAgent.builder()
                                                                        .finInstnId(BankToCustomerAccountReportCamt052.FinancialInstitutionIdSimplified.builder()
                                                                                .bicfi(entry.getInstructedAgentBIC())
                                                                                .build())
                                                                        .build())
                                                                .build())
                                                        .build())
                                                .build())
                                        .build()).collect(Collectors.toList()))
                                .build())
                        .build())
                .build();
    }
}
