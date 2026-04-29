package com.strata.xmlDocument.domain.service;

import com.strata.xmlDocument.application.input.PaymentApprovalPacs002UseCase;
import com.strata.xmlDocument.domain.model.PaymentApprovalPacs002;
import com.strata.xmlDocument.domain.model.PaymentRequestPacs008;
import com.strata.xmlDocument.domain.model.types.MessageTypes;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.PaymentRequestMap;
import com.strata.xmlDocument.infrastructure.adapter.output.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;

@Service
@Slf4j
public class PaymentApprovalPacs002Service implements PaymentApprovalPacs002UseCase {

    private static final String INSTITUTION_ID = "999058";

    @Value("${nibss.private.key.path}")
    private String privateKeyPath;

    @Value("${nibss.public.key.path}")
    private String publicKeyPath;

    @Value("${nibss.api.url.pacs002}")
    private String pacs002ApiUrl;

    @Override
    public String generatePaymentApproval(PaymentRequestMap paymentRequestMap, PaymentRequestPacs008 originalRequest) throws Exception {
        return generatePaymentApproval(paymentRequestMap, originalRequest, true, null, null);
    }

    @Override
    public String generatePaymentApproval(PaymentRequestMap paymentRequestMap,
                                          PaymentRequestPacs008 originalRequest,
                                          boolean approved,
                                          String reasonCode,
                                          String reasonDescription) throws Exception {
        PaymentApprovalPacs002 pacs002 = buildPaymentApproval(paymentRequestMap, originalRequest, approved, reasonCode, reasonDescription);
        Document pacs002Document = XmlDocumentConverter.marshallToDocument(pacs002, PaymentApprovalPacs002.class);
        
        PrivateKey privateKey = GenerateKey.loadPrivateKey(privateKeyPath);
        PublicKey publicKey = GenerateKey.loadPublicKey(publicKeyPath);
        
        Signer.sign(pacs002Document, privateKey);
        String pacs002Encrypted = Encrypter.encrypt(pacs002Document, publicKey, "FIToFIPmtStsRpt");
        
        log.info("ENCRYPTED PAYMENT APPROVAL DATA =====>>>>>> {}", 
                pacs002Encrypted.substring(0, Math.min(pacs002Encrypted.length(), 100)) + "...");
        
        HttpSender.sendXML(pacs002Encrypted, pacs002ApiUrl);
        
        return pacs002.getFiToFiPmtStsRpt().getGrpHdr().getMsgId();
    }

    public PaymentApprovalPacs002 buildPaymentApproval(PaymentRequestMap paymentRequestMap, PaymentRequestPacs008 originalRequest) {
        return buildPaymentApproval(paymentRequestMap, originalRequest, true, null, null);
    }

    public PaymentApprovalPacs002 buildPaymentApproval(PaymentRequestMap paymentRequestMap,
                                                       PaymentRequestPacs008 originalRequest,
                                                       boolean approved,
                                                       String reasonCode,
                                                       String reasonDescription) {
        String msgId = MessageIdGenerator.generateMessageId(INSTITUTION_ID);
        PaymentRequestPacs008.GroupHeader ficoGrpHdr = originalRequest.getFicoCustomerCreditTransfer().getGrpHdr();
        String instructedMemberId = extractMemberId(
                ficoGrpHdr.getInstdAgt() != null ? ficoGrpHdr.getInstdAgt().getFinInstnId() : null,
                paymentRequestMap.getInstructedAgentBic()
        );
        String instructingMemberId = extractMemberId(
                ficoGrpHdr.getInstgAgt() != null ? ficoGrpHdr.getInstgAgt().getFinInstnId() : null,
                INSTITUTION_ID
        );
        
        PaymentApprovalPacs002.GroupHeader groupHeader = PaymentApprovalPacs002.GroupHeader.builder()
                .msgId(msgId)
                .creDtTm(LocalDateTime.now().toString())
                .instgAgt(buildAgent(paymentRequestMap.getInstructingAgentBic(), instructingMemberId))
                .instdAgt(buildAgent(paymentRequestMap.getInstructedAgentBic(), instructedMemberId))
                .build();

        PaymentApprovalPacs002.OriginalGroupInfoAndStatus originalGroupInfoAndStatus = PaymentApprovalPacs002.OriginalGroupInfoAndStatus.builder()
                .orgnlMsgId(ficoGrpHdr.getMsgId())
                .orgnlMsgNmId("pacs.008.001.12")
                .orgnlCreDtTm(ficoGrpHdr.getCreDtTm().toString())
                .grpSts(approved ? "ACSC" : "RJCT")
                .build();

        PaymentRequestPacs008.PaymentId pmtId = originalRequest.getFicoCustomerCreditTransfer()
                .getCdtTrfTxInf().getPmtId();

        PaymentApprovalPacs002.TransactionInfoAndStatus txInfoAndStatus = PaymentApprovalPacs002.TransactionInfoAndStatus.builder()
                .stsId(approved ? "AUTH" : "RJCT")
                .orgnlInstrId(pmtId.getInstrId())
                .orgnlEndToEndId(pmtId.getEndToEndId())
                .orgnlTxId(pmtId.getTxId())
                .stsRsnInf(approved ? null : PaymentApprovalPacs002.StatusReasonInformation.builder()
                        .rsn(PaymentApprovalPacs002.StatusReason.builder()
                                .prtry(defaultIfBlank(reasonCode, "002"))
                                .build())
                        .addtlInf(defaultIfBlank(reasonDescription, "Transaction processing failed"))
                        .build())
                .instgAgt(buildAgent(paymentRequestMap.getInstructingAgentBic(), instructingMemberId))
                .instdAgt(buildAgent(paymentRequestMap.getInstructedAgentBic(), instructedMemberId))
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

    @Override
    public void receiveInboundPacs002(String encryptedData) throws Exception {
        PrivateKey privateKey = GenerateKey.loadPrivateKey(privateKeyPath);
        PublicKey publicKey = GenerateKey.loadPublicKey(publicKeyPath);
        Document decryptedDocument = Decrypter.decrypt(encryptedData,privateKey);
        Signer.validateXmlSignature(decryptedDocument,publicKey);
        String decryptedStringValue = XmlDocumentConverter.documentToString(decryptedDocument);
        log.info("DECRYPTED  ========>>>>>>> {}", decryptedStringValue);
        String messageType = MessageTypes.PACS_002.name();
        FileSaver.saveDecryptMessageToFile(decryptedStringValue,messageType);
    }

    private String extractMemberId(PaymentRequestPacs008.FinancialInstitutionId finInstnId, String fallback) {
        if (finInstnId != null && finInstnId.getClrSysMmbId() != null && finInstnId.getClrSysMmbId().getMmbId() != null) {
            return finInstnId.getClrSysMmbId().getMmbId();
        }
        return fallback;
    }

    private String defaultIfBlank(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
