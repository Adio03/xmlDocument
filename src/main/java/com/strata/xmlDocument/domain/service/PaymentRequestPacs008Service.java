package com.strata.xmlDocument.domain.service;

import com.strata.xmlDocument.application.input.PaymentApprovalPacs002UseCase;
import com.strata.xmlDocument.application.input.PaymentRequestPacs008UseCase;
import com.strata.xmlDocument.domain.model.PaymentApprovalPacs002;
import com.strata.xmlDocument.domain.model.PaymentRequestPacs008;
import com.strata.xmlDocument.domain.model.types.MessageTypes;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.PaymentRequestMap;
import com.strata.xmlDocument.infrastructure.adapter.output.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentRequestPacs008Service implements PaymentRequestPacs008UseCase {
    private static final String INVALID_ACCOUNT_REASON_CODE = "AC01";
    private static final String PROCESSING_ERROR_REASON_CODE = "002";
    private static final String SIGNATURE_ERROR_REASON_CODE = "901";

    @Value("${institution.id}")
    private  String institutionId;
    
    private final PaymentApprovalPacs002UseCase paymentApprovalService;

    //    @Value("${nibss.private.key.path}")
    private final String privateKeyPath = "C:\\Users\\semicolon\\IdeaProjects\\xmlDocument\\banks_private.pem";

    //    @Value("${nibss.public.key.path}")
    private final String publicKeyPath = "C:\\Users\\semicolon\\IdeaProjects\\xmlDocument\\banks_public.pem";

    @Value("${nibss.api.url.pacs008}")
    private String pacs008ApiUrl;

    private String pacs002ApiUrl;

    @Override
    public String paymentRequestPacs008(PaymentRequestMap paymentRequestMap) throws Exception {
        PaymentRequestPacs008 pacs008 = buildPacs008(paymentRequestMap);
        Document document = XmlDocumentConverter.marshallToDocument(pacs008, PaymentRequestPacs008.class);
        PrivateKey privateKey = GenerateKey.loadPrivateKey(privateKeyPath);
        PublicKey publicKey = GenerateKey.loadPublicKey(publicKeyPath);
        String rootTag = "FIToFICstmrCdtTrf";
        Signer.sign(document, privateKey);
        String encryptedData = Encrypter.encrypt(document, publicKey, rootTag);
        log.info("ENCRYPTED PAYMENT REQUEST DATA =====>>>>>> {}", encryptedData.substring(0, Math.min(encryptedData.length(), 100)) + "...");
        String url = "http://localhost:9200/api/nps/pacs008/outbound_pacs008";
        try {
            HttpSender.sendXML(encryptedData, url);
        }catch (Exception exception){
            log.error(exception.getMessage());
        }
        return pacs008.getFicoCustomerCreditTransfer().getGrpHdr().getMsgId();
    }


    private PaymentApprovalPacs002 buildPacs002Response(PaymentRequestMap paymentRequestMap, PaymentRequestPacs008 pacs008) {
        String msgId = MessageIdGenerator.generateMessageId(institutionId);
        PaymentRequestPacs008.GroupHeader ficoGrpHdr = pacs008.getFicoCustomerCreditTransfer().getGrpHdr();
        PaymentApprovalPacs002.GroupHeader groupHeader = PaymentApprovalPacs002.GroupHeader.builder()
                .msgId(msgId)
                .creDtTm(LocalDateTime.now().toString())
                .instgAgt(buildAgent(paymentRequestMap.getInstructingAgentBic(), institutionId))
                .instdAgt(buildAgent(paymentRequestMap.getInstructedAgentBic(), paymentRequestMap.getInstructedAgentBic()))
                .build();

        PaymentApprovalPacs002.OriginalGroupInfoAndStatus originalGroupInfoAndStatus = PaymentApprovalPacs002.OriginalGroupInfoAndStatus.builder()
                .orgnlMsgId(ficoGrpHdr.getMsgId())
                .orgnlMsgNmId("pacs.008.001.12")
                .orgnlCreDtTm(ficoGrpHdr.getCreDtTm().toString())
                .grpSts("ACSC")
                .build();

        PaymentApprovalPacs002.TransactionInfoAndStatus txInfoAndStatus = PaymentApprovalPacs002.TransactionInfoAndStatus.builder()
                .instgAgt(buildAgent(paymentRequestMap.getInstructingAgentBic(), institutionId))
                .instdAgt(buildAgent(paymentRequestMap.getInstructedAgentBic(), paymentRequestMap.getInstructedAgentBic()))
                .orgnlTxRef(PaymentApprovalPacs002.OriginalTransactionReference.builder()
                        .intrBkSttlmDt(paymentRequestMap.getInterbankSettlementDate())
                        .build())
                .build();

        return PaymentApprovalPacs002.builder()
                .fiToFiPmtStsRpt(PaymentApprovalPacs002.PaymentStatusReport.builder()
                        .grpHdr(groupHeader)
                        .orgnlGrpInfAndSts(originalGroupInfoAndStatus)
                        .txInfAndSts(txInfoAndStatus)
                        .build())
                .build();
    }

    private PaymentApprovalPacs002.Agent buildAgent(String bic, String memberId) {
        return PaymentApprovalPacs002.Agent.builder()
                .finInstnId(PaymentApprovalPacs002.FinancialInstitutionId.builder()
                        .bicfi(bic)
                        .clrSysMmbId(PaymentApprovalPacs002.ClearingSystemMemberId.builder()
                                .mmbId(memberId)
                                .build())
                        .build())
                .build();
    }

    private PaymentRequestPacs008 buildPacs008(PaymentRequestMap request) {
        String msgId = MessageIdGenerator.generateMessageId(institutionId);

        PaymentRequestPacs008.GroupHeader groupHeader = getGroupHeader(request, msgId);

        PaymentRequestPacs008.CreditTransferTransactionInformation creditTransfer = getCreditTransferTransactionInformation(request, msgId);

        PaymentRequestPacs008.SupplementaryData supplementaryData = getSupplementaryData(request);

        PaymentRequestPacs008.FIToFICstmrCdtTrf fico = PaymentRequestPacs008.FIToFICstmrCdtTrf.builder()
                .grpHdr(groupHeader)
                .cdtTrfTxInf(creditTransfer)
                .splmtryData(supplementaryData)
                .build();

        return PaymentRequestPacs008.builder()
                .ficoCustomerCreditTransfer(fico)
                .build();
    }

    private  PaymentRequestPacs008.CreditTransferTransactionInformation getCreditTransferTransactionInformation(PaymentRequestMap request, String msgId) {
        return PaymentRequestPacs008.CreditTransferTransactionInformation.builder()
                .pmtId(PaymentRequestPacs008.PaymentId.builder()
                        .instrId(msgId)
                        .endToEndId(msgId)
                        .txId(msgId)
                        .build())
                .pmtTpInf(PaymentRequestPacs008.PaymentTypeInformation.builder()
                        .clrChanl(request.getClearingChannel())
                        .svcLvl(PaymentRequestPacs008.Proprietary.builder()
                                .prtry(request.getServiceLevel())
                                .build())
                        .lclInstrm(PaymentRequestPacs008.Proprietary.builder()
                                .prtry(request.getLocalInstrument())
                                .build())
                        .ctgyPurp(PaymentRequestPacs008.Proprietary.builder()
                                .prtry(request.getCategoryPurpose())
                                .build())
                        .build())
                .intrBkSttlmAmt(PaymentRequestPacs008.Amount.builder()
                        .currency(request.getInterbankSettlementCurrency())
                        .value(new BigDecimal(request.getInterbankSettlementAmount()))
                        .build())
                .intrBkSttlmDt(LocalDate.parse(request.getInterbankSettlementDate()))
                .chrgBr(request.getChargesBearer())
                .instgAgt(PaymentRequestPacs008.Agent.builder()
                        .finInstnId(PaymentRequestPacs008.FinancialInstitutionId.builder()
                                .bicfi(request.getInstructingAgentBic())
                                .clrSysMmbId(PaymentRequestPacs008.MemberId.builder()
                                        .mmbId(institutionId)
                                        .build())
                                .build())
                        .build())
                .instdAgt(PaymentRequestPacs008.Agent.builder()
                        .finInstnId(PaymentRequestPacs008.FinancialInstitutionId.builder()
                                .bicfi(request.getInstructedAgentBic())
                                .clrSysMmbId(PaymentRequestPacs008.MemberId.builder()
                                        .mmbId(request.getInstructedAgentBic())
                                        .build())
                                .build())
                        .build())
                .dbtr(PaymentRequestPacs008.Party.builder()
                        .name(request.getDebtorName())
                        .build())
                .dbtrAcct(PaymentRequestPacs008.Account.builder()
                        .id(PaymentRequestPacs008.AccountId.builder()
                                .iban(request.getDebtorAccountIban())
                                .build())
                        .name(request.getDebtorAccountName())
                        .build())
                .dbtrAgt(PaymentRequestPacs008.Agent.builder()
                        .finInstnId(PaymentRequestPacs008.FinancialInstitutionId.builder()
                                .bicfi(request.getInstructingAgentBic())
                                .clrSysMmbId(PaymentRequestPacs008.MemberId.builder()
                                        .mmbId(institutionId)
                                        .build())
                                .build())
                        .build())
                .cdtrAgt(PaymentRequestPacs008.Agent.builder()
                        .finInstnId(PaymentRequestPacs008.FinancialInstitutionId.builder()
                                .bicfi(request.getInstructedAgentBic())
                                .clrSysMmbId(PaymentRequestPacs008.MemberId.builder()
                                        .mmbId(request.getInstructedAgentBic())
                                        .build())
                                .build())
                        .build())
                .cdtr(PaymentRequestPacs008.Party.builder()
                        .name(request.getCreditorName())
                        .build())
                .cdtrAcct(PaymentRequestPacs008.Account.builder()
                        .id(PaymentRequestPacs008.AccountId.builder()
                                .iban(request.getCreditorAccountIban())
                                .build())
                        .name(request.getCreditorAccountName())
                        .build())
                .instrForNxtAgt(List.of(PaymentRequestPacs008.InstructionForNextAgent.builder()
                        .instrInf(request.getDebtorInstructionInfo())
                        .build()))
                .rmtInf(PaymentRequestPacs008.RemittanceInformation.builder()
                        .ustrd(request.getRemittanceInformation())
                        .build())
                .build();
    }

    private  PaymentRequestPacs008.SupplementaryData getSupplementaryData(PaymentRequestMap request) {
        return PaymentRequestPacs008.SupplementaryData.builder()
                .plcAndNm("CustomData")
                .envlp(PaymentRequestPacs008.Envelope.builder()
                        .customData(PaymentRequestPacs008.CustomData.builder()
                                .debtorInfo(PaymentRequestPacs008.DebtorInfo.builder()
                                        .accountDesignation(request.getDebtorAccountDesignation())
                                        .idType(request.getDebtorIdType())
                                        .idValue(request.getDebtorIdValue())
                                        .accountTier(request.getDebtorAccountTier())
                                        .build())
                                .creditorInfo(PaymentRequestPacs008.CreditorInfo.builder()
                                        .accountDesignation(request.getCreditorAccountDesignation())
                                        .idType(request.getCreditorIdType())
                                        .idValue(request.getCreditorIdValue())
                                        .accountTier(request.getCreditorAccountTier())
                                        .build())
                                .transactionInfo(PaymentRequestPacs008.TransactionInfo.builder()
                                        .transactionLocation(request.getTransactionLocation())
                                        .build())
                                .build())
                        .build())
                .build();
    }

    private  PaymentRequestPacs008.GroupHeader getGroupHeader(PaymentRequestMap request, String msgId) {

        return PaymentRequestPacs008.GroupHeader.builder()
                .msgId(msgId)
                .creDtTm(LocalDateTime.now())
                .btchBookg(false)
                .nbOfTxs(1)
                .sttlmInf(PaymentRequestPacs008.SettlementInformation.builder()
                        .sttlmMtd("CLRG")
                        .build())
                .instgAgt(PaymentRequestPacs008.Agent.builder()
                        .finInstnId(PaymentRequestPacs008.FinancialInstitutionId.builder()
                                .bicfi(request.getInstructingAgentBic())
                                .clrSysMmbId(PaymentRequestPacs008.MemberId.builder()
                                        .mmbId(institutionId)
                                        .build())
                                .build())
                        .build())
                .instdAgt(PaymentRequestPacs008.Agent.builder()
                        .finInstnId(PaymentRequestPacs008.FinancialInstitutionId.builder()
                                .bicfi(request.getInstructedAgentBic())
                                .clrSysMmbId(PaymentRequestPacs008.MemberId.builder()
                                        .mmbId(request.getInstructedAgentBic())
                                        .build())
                                .build())
                        .build())
                .build();
    }

    @Override
    public void receiveInboundPacs008(String encryptedData) throws Exception {
        PrivateKey privateKey = GenerateKey.loadPrivateKey(privateKeyPath);
        PublicKey publicKey = GenerateKey.loadPublicKey(publicKeyPath);
        Document decryptedDocument = Decrypter.decrypt(encryptedData,privateKey);
        PaymentRequestPacs008 pacs008 = XmlDocumentConverter.unmarshallFromDocument(decryptedDocument, PaymentRequestPacs008.class);
        boolean isValidSignature = Signer.validateXmlSignature(decryptedDocument,publicKey);
        String decryptedStringValue = XmlDocumentConverter.documentToString(decryptedDocument);
        log.info("DECRYPTED  ========>>>>>>> {}", decryptedStringValue);
        String messageType = MessageTypes.PACS_008.name();
        FileSaver.saveDecryptMessageToFile(decryptedStringValue,messageType);

        PaymentRequestMap paymentRequestMap = mapInboundPaymentRequest(pacs008);

        if (!isValidSignature) {
            paymentApprovalService.generatePaymentApproval(
                    paymentRequestMap,
                    pacs008,
                    false,
                    SIGNATURE_ERROR_REASON_CODE,
                    "Invalid signature in PACS.008 request"
            );
            return;
        }

        try {
            String creditorAccountNumber = pacs008.getFicoCustomerCreditTransfer()
                    .getCdtTrfTxInf()
                    .getCdtrAcct()
                    .getId()
                    .getIban();

            if (!isValidAccountNumber(creditorAccountNumber)) {
                paymentApprovalService.generatePaymentApproval(
                        paymentRequestMap,
                        pacs008,
                        false,
                        INVALID_ACCOUNT_REASON_CODE,
                        "Wrong account number"
                );
                return;
            }

            paymentApprovalService.generatePaymentApproval(paymentRequestMap, pacs008);
        } catch (Exception exception) {
            log.error("Failed to process inbound PACS.008", exception);
            paymentApprovalService.generatePaymentApproval(
                    paymentRequestMap,
                    pacs008,
                    false,
                    PROCESSING_ERROR_REASON_CODE,
                    buildFailureReason(exception)
            );
        }
    }

    private PaymentRequestMap mapInboundPaymentRequest(PaymentRequestPacs008 pacs008) {
        PaymentRequestPacs008.FIToFICstmrCdtTrf transfer = pacs008.getFicoCustomerCreditTransfer();
        PaymentRequestPacs008.GroupHeader groupHeader = transfer.getGrpHdr();
        PaymentRequestPacs008.CreditTransferTransactionInformation txInfo = transfer.getCdtTrfTxInf();

        PaymentRequestMap paymentRequestMap = new PaymentRequestMap();
        paymentRequestMap.setInstructingAgentBic(extractBic(groupHeader.getInstgAgt()));
        paymentRequestMap.setInstructedAgentBic(extractBic(groupHeader.getInstdAgt()));
        paymentRequestMap.setDebtorName(txInfo.getDbtr().getName());
        paymentRequestMap.setDebtorAccountIban(txInfo.getDbtrAcct().getId().getIban());
        paymentRequestMap.setDebtorAccountName(txInfo.getDbtrAcct().getName());
        paymentRequestMap.setCreditorName(txInfo.getCdtr().getName());
        paymentRequestMap.setCreditorAccountIban(txInfo.getCdtrAcct().getId().getIban());
        paymentRequestMap.setCreditorAccountName(txInfo.getCdtrAcct().getName());
        paymentRequestMap.setInterbankSettlementAmount(txInfo.getIntrBkSttlmAmt().getValue().toPlainString());
        paymentRequestMap.setInterbankSettlementCurrency(txInfo.getIntrBkSttlmAmt().getCurrency());
        paymentRequestMap.setInterbankSettlementDate(txInfo.getIntrBkSttlmDt().toString());
        paymentRequestMap.setChargesBearer(txInfo.getChrgBr());
        if (txInfo.getPmtTpInf() != null) {
            paymentRequestMap.setClearingChannel(txInfo.getPmtTpInf().getClrChanl());
            paymentRequestMap.setServiceLevel(extractProprietaryValue(txInfo.getPmtTpInf().getSvcLvl()));
            paymentRequestMap.setLocalInstrument(extractProprietaryValue(txInfo.getPmtTpInf().getLclInstrm()));
            paymentRequestMap.setCategoryPurpose(extractProprietaryValue(txInfo.getPmtTpInf().getCtgyPurp()));
        }
        if (txInfo.getInstrForNxtAgt() != null && !txInfo.getInstrForNxtAgt().isEmpty()) {
            paymentRequestMap.setDebtorInstructionInfo(txInfo.getInstrForNxtAgt().get(0).getInstrInf());
        }
        if (txInfo.getRmtInf() != null) {
            paymentRequestMap.setRemittanceInformation(txInfo.getRmtInf().getUstrd());
        }
        if (transfer.getSplmtryData() != null && transfer.getSplmtryData().getEnvlp() != null
                && transfer.getSplmtryData().getEnvlp().getCustomData() != null) {
            PaymentRequestPacs008.CustomData customData = transfer.getSplmtryData().getEnvlp().getCustomData();
            if (customData.getDebtorInfo() != null) {
                paymentRequestMap.setDebtorAccountDesignation(customData.getDebtorInfo().getAccountDesignation());
                paymentRequestMap.setDebtorIdType(customData.getDebtorInfo().getIdType());
                paymentRequestMap.setDebtorIdValue(customData.getDebtorInfo().getIdValue());
                paymentRequestMap.setDebtorAccountTier(customData.getDebtorInfo().getAccountTier());
            }
            if (customData.getCreditorInfo() != null) {
                paymentRequestMap.setCreditorAccountDesignation(customData.getCreditorInfo().getAccountDesignation());
                paymentRequestMap.setCreditorIdType(customData.getCreditorInfo().getIdType());
                paymentRequestMap.setCreditorIdValue(customData.getCreditorInfo().getIdValue());
                paymentRequestMap.setCreditorAccountTier(customData.getCreditorInfo().getAccountTier());
            }
            if (customData.getTransactionInfo() != null) {
                paymentRequestMap.setTransactionLocation(customData.getTransactionInfo().getTransactionLocation());
            }
        }
        return paymentRequestMap;
    }

    private String extractBic(PaymentRequestPacs008.Agent agent) {
        if (agent == null || agent.getFinInstnId() == null) {
            return null;
        }
        return agent.getFinInstnId().getBicfi();
    }

    private String extractProprietaryValue(PaymentRequestPacs008.Proprietary proprietary) {
        return proprietary == null ? null : proprietary.getPrtry();
    }

    private boolean isValidAccountNumber(String accountNumber) {
        return accountNumber != null && accountNumber.matches("\\d{10}");
    }

    private String buildFailureReason(Exception exception) {
        String message = exception.getMessage();
        if (message == null || message.isBlank()) {
            return "Transaction processing failed";
        }
        return message;
    }
}
