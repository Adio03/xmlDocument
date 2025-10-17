package com.strata.xmlDocument.domain.service;

import com.strata.xmlDocument.application.input.PaymentApprovalPacs002UseCase;
import com.strata.xmlDocument.domain.model.PaymentApprovalPacs002;
import com.strata.xmlDocument.domain.model.PaymentRequestPacs008;
import com.strata.xmlDocument.domain.model.types.MessageTypes;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.PaymentRequest;
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
    public String generatePaymentApproval(PaymentRequest paymentRequest, PaymentRequestPacs008 originalRequest) throws Exception {
        PaymentApprovalPacs002 pacs002 = buildPaymentApproval(paymentRequest, originalRequest);
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

    public PaymentApprovalPacs002 buildPaymentApproval(PaymentRequest paymentRequest, PaymentRequestPacs008 originalRequest) {
        String msgId = MessageIdGenerator.generateMessageId(INSTITUTION_ID);
        PaymentRequestPacs008.GroupHeader ficoGrpHdr = originalRequest.getFicoCustomerCreditTransfer().getGrpHdr();
        
        PaymentApprovalPacs002.GroupHeader groupHeader = PaymentApprovalPacs002.GroupHeader.builder()
                .msgId(msgId)
                .creDtTm(LocalDateTime.now().toString())
                .instgAgt(buildAgent(paymentRequest.getInstructingAgentBic(), INSTITUTION_ID))
                .instdAgt(buildAgent(paymentRequest.getInstructedAgentBic(), paymentRequest.getInstructedAgentBic()))
                .build();

        PaymentApprovalPacs002.OriginalGroupInfoAndStatus originalGroupInfoAndStatus = PaymentApprovalPacs002.OriginalGroupInfoAndStatus.builder()
                .orgnlMsgId(ficoGrpHdr.getMsgId())
                .orgnlMsgNmId("pacs.008.001.12")
                .orgnlCreDtTm(ficoGrpHdr.getCreDtTm().toString())
                .grpSts("ACSC")
                .build();

        PaymentApprovalPacs002.TransactionInfoAndStatus txInfoAndStatus = PaymentApprovalPacs002.TransactionInfoAndStatus.builder()
                .instgAgt(buildAgent(paymentRequest.getInstructingAgentBic(), INSTITUTION_ID))
                .instdAgt(buildAgent(paymentRequest.getInstructedAgentBic(), paymentRequest.getInstructedAgentBic()))
                .orgnlTxRef(PaymentApprovalPacs002.OriginalTransactionReference.builder()
                        .intrBkSttlmDt(paymentRequest.getInterbankSettlementDate())
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
}