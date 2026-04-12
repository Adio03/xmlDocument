package com.strata.xmlDocument.domain.service;

import com.strata.xmlDocument.application.input.AccountReportingCamt060UseCase;
import com.strata.xmlDocument.domain.model.AccountReportingRequestCamt060;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.AccountReportingRequest;
import com.strata.xmlDocument.infrastructure.adapter.output.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.OffsetDateTime;

@Service
@Slf4j
public class AccountReportingCamt060Service implements AccountReportingCamt060UseCase {

    private static final String INSTITUTION_ID = "999058";

    @Value("${nibss.private.key.path}")
    private String privateKeyPath;

    @Value("${nibss.public.key.path}")
    private String publicKeyPath;

    @Value("${nibss.api.url.camt060}")
    private String camt060ApiUrl;

    @Override
    public String requestAccountReporting(AccountReportingRequest request) throws Exception {
        AccountReportingRequestCamt060 camt060 = buildCamt060Request(request);
        Document document = XmlDocumentConverter.marshallToDocument(camt060, AccountReportingRequestCamt060.class);

        PrivateKey privateKey = GenerateKey.loadPrivateKey(privateKeyPath);
        PublicKey publicKey = GenerateKey.loadPublicKey(publicKeyPath);

        String rootTag = "AcctRptgReq";
        Signer.sign(document, privateKey);
        String encryptedData = Encrypter.encrypt(document, publicKey, rootTag);

        log.info("ENCRYPTED ACCOUNT REPORTING REQUEST DATA =====>>>>>> {}",
                encryptedData.substring(0, Math.min(encryptedData.length(), 100)) + "...");

        HttpSender.sendXML(encryptedData, camt060ApiUrl);

        return camt060.getAcctRptgReq().getGrpHdr().getMsgId();
    }

    private AccountReportingRequestCamt060 buildCamt060Request(AccountReportingRequest request) {
        String msgId = MessageIdGenerator.generateMessageId(INSTITUTION_ID);

        return AccountReportingRequestCamt060.builder()
                .acctRptgReq(AccountReportingRequestCamt060.AccountReportingRequest.builder()
                        .grpHdr(AccountReportingRequestCamt060.GroupHeader.builder()
                                .msgId(msgId)
                                .creDtTm(OffsetDateTime.now())
                                .msgSndr(AccountReportingRequestCamt060.MessageSender.builder()
                                        .agt(AccountReportingRequestCamt060.Agent.builder()
                                                .finInstnId(AccountReportingRequestCamt060.FinancialInstitutionId.builder()
                                                        .bicfi(request.getMessageSenderBIC())
                                                        .clrSysMmbId(AccountReportingRequestCamt060.ClearingSystemMemberId.builder()
                                                                .mmbId(request.getMessageSenderMemberId())
                                                                .build())
                                                        .build())
                                                .build())
                                        .build())
                                .build())
                        .rptgReq(AccountReportingRequestCamt060.ReportingRequest.builder()
                                .id(request.getReportingRequestId())
                                .reqdMsgNmId(request.getRequestedMessageType())
                                .acct(AccountReportingRequestCamt060.Account.builder()
                                        .id(AccountReportingRequestCamt060.AccountId.builder()
                                                .iban(request.getAccountNumber())
                                                .build())
                                        .ccy(request.getAccountCurrency())
                                        .build())
                                .acctOwnr(AccountReportingRequestCamt060.AccountOwner.builder()
                                        .agt(AccountReportingRequestCamt060.Agent.builder()
                                                .finInstnId(AccountReportingRequestCamt060.FinancialInstitutionId.builder()
                                                        .bicfi(request.getAccountOwnerBIC())
                                                        .clrSysMmbId(AccountReportingRequestCamt060.ClearingSystemMemberId.builder()
                                                                .mmbId(request.getAccountOwnerMemberId())
                                                                .build())
                                                        .build())
                                                .build())
                                        .build())
                                .acctSvcr(AccountReportingRequestCamt060.AccountServicer.builder()
                                        .finInstnId(AccountReportingRequestCamt060.FinancialInstitutionId.builder()
                                                .bicfi(request.getAccountServicerBIC())
                                                .clrSysMmbId(AccountReportingRequestCamt060.ClearingSystemMemberId.builder()
                                                        .mmbId(request.getAccountServicerMemberId())
                                                        .build())
                                                .build())
                                        .build())
                                .rptgPrd(AccountReportingRequestCamt060.ReportingPeriod.builder()
                                        .frToDt(AccountReportingRequestCamt060.FromToDate.builder()
                                                .frDt(request.getFromDate())
                                                .toDt(request.getToDate())
                                                .build())
                                        .tp(request.getReportingPeriodType())
                                        .build())
                                .build())
                        .splmtryData(AccountReportingRequestCamt060.SupplementaryData.builder()
                                .plcAndNm("AdditionalVerificationDetails")
                                .envlp(AccountReportingRequestCamt060.Envelope.builder()
                                        .customData(AccountReportingRequestCamt060.CustomData.builder()
                                                .creditorInfo(AccountReportingRequestCamt060.CreditorInfo.builder()
                                                        .accountDesignation(request.getAccountDesignation())
                                                        .idType(request.getIdType())
                                                        .idValue(request.getIdValue())
                                                        .accountTier(request.getAccountTier())
                                                        .build())
                                                .transactionInfo(AccountReportingRequestCamt060.TransactionInfo.builder()
                                                        .transactionLocation(request.getTransactionLocation())
                                                        .channelCode(request.getChannelCode())
                                                        .fixedCollectionAmount(request.isFixedCollectionAmount())
                                                        .mandateCode(request.getMandateCode())
                                                        .build())
                                                .build())
                                        .build())
                                .build())
                        .build())
                .build();
    }
}
