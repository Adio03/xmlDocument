package com.strata.xmlDocument.domain.service;

import com.strata.xmlDocument.application.input.AccountStatementCamt053UseCase;
import com.strata.xmlDocument.domain.model.BankToCustomerStatementCamt053;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.AccountReportRequest;
import com.strata.xmlDocument.infrastructure.adapter.output.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccountStatementCamt053Service implements AccountStatementCamt053UseCase {

    @Value("${nibss.private.key.path}")
    private String privateKeyPath;

    @Value("${nibss.public.key.path}")
    private String publicKeyPath;

    @Value("${nibss.api.url.camt053}")
    private String camt053ApiUrl;

    @Override
    public String generateAccountStatement(AccountReportRequest request) {
        try {
            BankToCustomerStatementCamt053 camt053 = buildCamt053Request(request);
            Document document = XmlDocumentConverter.marshallToDocument(camt053, BankToCustomerStatementCamt053.class);

            PrivateKey privateKey = GenerateKey.loadPrivateKey(privateKeyPath);
            PublicKey publicKey = GenerateKey.loadPublicKey(publicKeyPath);

            String rootTag = "BkToCstmrStmt";
            Signer.sign(document, privateKey);
            String encryptedData = Encrypter.encrypt(document, publicKey, rootTag);

            log.info("ENCRYPTED CAMT.053 ACCOUNT STATEMENT REQUEST DATA =====>>>>>> {}",
                    encryptedData.substring(0, Math.min(encryptedData.length(), 100)) + "...");

            HttpSender.sendXML(encryptedData, camt053ApiUrl);

            return camt053.getBkToCstmrStmt().getGrpHdr().getMsgId();
        } catch (Exception e) {
            log.error("Error generating camt.053 account statement", e);
            throw new RuntimeException("Failed to generate camt.053 account statement", e);
        }
    }

    private BankToCustomerStatementCamt053 buildCamt053Request(AccountReportRequest request) {
        return BankToCustomerStatementCamt053.builder()
                .bkToCstmrStmt(BankToCustomerStatementCamt053.BankToCustomerStatement.builder()
                        .grpHdr(BankToCustomerStatementCamt053.GroupHeader.builder()
                                .msgId(request.getOriginalQueryMessageId()) // Or generate a new one
                                .creDtTm(OffsetDateTime.now())
                                .msgRcpt(BankToCustomerStatementCamt053.MessageRecipient.builder()
                                        .nm(request.getMessageRecipientName())
                                        .id(BankToCustomerStatementCamt053.PartyIdentification.builder()
                                                .orgId(BankToCustomerStatementCamt053.OrganisationIdentification.builder()
                                                        .anyBIC(request.getMessageRecipientBIC())
                                                        .build())
                                                .build())
                                        .build())
                                .orgnlBizQry(BankToCustomerStatementCamt053.OriginalBusinessQuery.builder()
                                        .msgId(request.getOriginalQueryMessageId())
                                        .msgNmId(request.getOriginalQueryMessageType())
                                        .creDtTm(request.getOriginalQueryCreationDateTime() != null ? 
                                                OffsetDateTime.parse(request.getOriginalQueryCreationDateTime()) : null)
                                        .build())
                                .build())
                        .stmt(BankToCustomerStatementCamt053.Statement.builder()
                                .id(request.getReportId())
                                .frToDt(BankToCustomerStatementCamt053.FromToDate.builder()
                                        .frDtTm(OffsetDateTime.parse(request.getFromDateTime()))
                                        .toDtTm(OffsetDateTime.parse(request.getToDateTime()))
                                        .build())
                                .acct(BankToCustomerStatementCamt053.Account.builder()
                                        .id(BankToCustomerStatementCamt053.AccountId.builder()
                                                .iban(request.getAccountNumber())
                                                .build())
                                        .ccy(request.getAccountCurrency())
                                        .ownr(BankToCustomerStatementCamt053.AccountOwner.builder()
                                                .id(BankToCustomerStatementCamt053.PartyIdentificationWithOther.builder()
                                                        .orgId(BankToCustomerStatementCamt053.OrganisationIdentificationWithOther.builder()
                                                                .othr(BankToCustomerStatementCamt053.OtherIdentification.builder()
                                                                        .schmeNm(BankToCustomerStatementCamt053.SchemeName.builder()
                                                                                .cd(request.getSchemeCode())
                                                                                .prtry(request.getProprietaryScheme())
                                                                                .build())
                                                                        .build())
                                                                .build())
                                                        .build())
                                                .build())
                                        .svcr(BankToCustomerStatementCamt053.AccountServicer.builder()
                                                .finInstnId(BankToCustomerStatementCamt053.FinancialInstitutionId.builder()
                                                        .bicfi(request.getAccountServicerBIC())
                                                        .clrSysMmbId(BankToCustomerStatementCamt053.ClearingSystemMemberId.builder()
                                                                .mmbId(request.getAccountServicerMemberId())
                                                                .build())
                                                        .build())
                                                .build())
                                        .build())
                                .bal(Collections.singletonList(BankToCustomerStatementCamt053.Balance.builder()
                                        .tp(BankToCustomerStatementCamt053.BalanceType.builder()
                                                .prtry(request.getBalanceType())
                                                .build())
                                        .amt(BankToCustomerStatementCamt053.CurrencyAmount.builder()
                                                .ccy(request.getBalanceCurrency())
                                                .amount(request.getBalanceAmount())
                                                .build())
                                        .cdtDbtInd(request.getCreditDebitIndicator())
                                        .dt(BankToCustomerStatementCamt053.BalanceDate.builder()
                                                .dtTm(OffsetDateTime.parse(request.getBalanceDateTime()))
                                                .build())
                                        .build()))
                                .ntry(request.getEntries().stream().map(entry -> BankToCustomerStatementCamt053.Entry.builder()
                                        .amt(BankToCustomerStatementCamt053.CurrencyAmount.builder()
                                                .ccy(entry.getCurrency())
                                                .amount(entry.getAmount())
                                                .build())
                                        .cdtDbtInd(entry.getCreditDebitIndicator())
                                        .sts(BankToCustomerStatementCamt053.EntryStatus.builder()
                                                .prtry(entry.getStatus())
                                                .build())
                                        .bookgDt(BankToCustomerStatementCamt053.DateWrapper.builder()
                                                .dt(entry.getBookingDate())
                                                .build())
                                        .valDt(BankToCustomerStatementCamt053.DateWrapper.builder()
                                                .dt(entry.getValueDate())
                                                .build())
                                        .acctSvcrRef(entry.getAccountServicerReference())
                                        .bkTxCd(BankToCustomerStatementCamt053.BankTransactionCode.builder()
                                                .domn(BankToCustomerStatementCamt053.Domain.builder()
                                                        .cd(entry.getDomainCode())
                                                        .fmly(BankToCustomerStatementCamt053.Family.builder()
                                                                .cd(entry.getFamilyCode())
                                                                .subFmlyCd(entry.getSubFamilyCode())
                                                                .build())
                                                        .build())
                                                .build())
                                        .ntryDtls(BankToCustomerStatementCamt053.EntryDetails.builder()
                                                .txDtls(BankToCustomerStatementCamt053.TransactionDetails.builder()
                                                        .rltdAgts(BankToCustomerStatementCamt053.RelatedAgents.builder()
                                                                .instdAgt(BankToCustomerStatementCamt053.InstructedAgent.builder()
                                                                        .finInstnId(BankToCustomerStatementCamt053.FinancialInstitutionIdSimplified.builder()
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
