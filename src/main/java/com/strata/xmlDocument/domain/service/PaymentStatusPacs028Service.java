package com.strata.xmlDocument.domain.service;

import com.strata.xmlDocument.application.input.PaymentStatusPacs028UseCase;
import com.strata.xmlDocument.domain.model.PaymentStatusPacs028;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.PaymentStatusRequest;
import com.strata.xmlDocument.infrastructure.adapter.output.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class PaymentStatusPacs028Service implements PaymentStatusPacs028UseCase {

    private static final String INSTITUTION_ID = "999058";

    @Value("${nibss.private.key.path}")
    private String privateKeyPath;

    @Value("${nibss.public.key.path}")
    private String publicKeyPath;

    @Value("${nibss.api.url.pacs028}")
    private String pacs028ApiUrl;

    @Override
    public String paymentStatusRequest(PaymentStatusRequest paymentStatusRequest) throws Exception {
        PaymentStatusPacs028 pacs028 = buildPacs028Request(paymentStatusRequest);
        Document document = XmlDocumentConverter.marshallToDocument(pacs028, PaymentStatusPacs028.class);
        
        PrivateKey privateKey = GenerateKey.loadPrivateKey(privateKeyPath);
        PublicKey publicKey = GenerateKey.loadPublicKey(publicKeyPath);
        
        String rootTag = "FIToFIPmtStsReq";
        Signer.sign(document, privateKey);
        String encryptedData = Encrypter.encrypt(document, publicKey, rootTag);
        
        log.info("ENCRYPTED PAYMENT STATUS REQUEST DATA =====>>>>>> {}", 
                encryptedData.substring(0, Math.min(encryptedData.length(), 100)) + "...");
        
        HttpSender.sendXML(encryptedData, pacs028ApiUrl);
        
        return pacs028.getFiToFIPmtStsReq().getGrpHdr().getMsgId();
    }

    @Override
    public void receiveInboundPacs028(String encryptedResponse) throws Exception {
        PrivateKey privateKey = GenerateKey.loadPrivateKey(privateKeyPath);
        String decryptedResponse = Decrypter.decryptToString(encryptedResponse, privateKey);
        
        log.info("PAYMENT STATUS CALLBACK RECEIVED =====>>>>>> {}", 
                decryptedResponse.substring(0, Math.min(decryptedResponse.length(), 200)) + "...");
        Document responseDocument = XmlDocumentConverter.parseXmlString(decryptedResponse);
        
        PublicKey publicKey = GenerateKey.loadPublicKey(publicKeyPath);
        boolean isValid = Signer.validateXmlSignature(responseDocument, publicKey);
        
        if (isValid) {
            log.info("Payment status callback signature verification successful");
            processPaymentStatusResponse(decryptedResponse);
        } else {
            log.error("Payment status callback signature verification failed");
            throw new SecurityException("Invalid signature in payment status response");
        }
    }

    private PaymentStatusPacs028 buildPacs028Request(PaymentStatusRequest request) {
        String msgId = MessageIdGenerator.generateMessageId(INSTITUTION_ID);
        String statusRequestId = request.getStatusRequestId() != null ? 
                request.getStatusRequestId() : MessageIdGenerator.generateMessageId(INSTITUTION_ID);

        PaymentStatusPacs028.GroupHeader groupHeader = PaymentStatusPacs028.GroupHeader.builder()
                .msgId(msgId)
                .creDtTm(OffsetDateTime.now())
                .instgAgt(PaymentStatusPacs028.Agent.builder()
                        .finInstnId(PaymentStatusPacs028.FinancialInstitutionIdentification.builder()
                                .clrSysMmbId(PaymentStatusPacs028.ClearingSystemMemberIdentification.builder()
                                        .mmbId(request.getSourceId())
                                        .build())
                                .build())
                        .build())
                .build();

        PaymentStatusPacs028.OriginalGroupInformation originalGroupInfo = PaymentStatusPacs028.OriginalGroupInformation.builder()
                .orgnlMsgId(request.getOriginalMessageId())
                .orgnlMsgNmId("pacs.008.001.12")
                .orgnlCreDtTm(OffsetDateTime.parse(request.getOriginalCreationDateTime()))
                .build();

        // Transaction Information
        PaymentStatusPacs028.TransactionInformation txInfo = PaymentStatusPacs028.TransactionInformation.builder()
                .stsReqId(statusRequestId)
                .orgnlTxId(request.getOriginalTransactionId())
                .instgAgt(PaymentStatusPacs028.Agent.builder()
                        .finInstnId(PaymentStatusPacs028.FinancialInstitutionIdentification.builder()
                                .bicfi(request.getSourceId())
                                .clrSysMmbId(PaymentStatusPacs028.ClearingSystemMemberIdentification.builder()
                                        .mmbId(request.getSourceId())
                                        .build())
                                .build())
                        .build())
                .instdAgt(PaymentStatusPacs028.Agent.builder()
                        .finInstnId(PaymentStatusPacs028.FinancialInstitutionIdentification.builder()
                                .bicfi(request.getDestinationId())
                                .clrSysMmbId(PaymentStatusPacs028.ClearingSystemMemberIdentification.builder()
                                        .mmbId(request.getDestinationId())
                                        .build())
                                .build())
                        .build())
                .orgnlTxRef(PaymentStatusPacs028.OriginalTransactionReference.builder()
                        .intrBkSttlmDt(LocalDate.parse(request.getInterbankSettlementDate()))
                        .build())
                .build();

        // Build the main request
        PaymentStatusPacs028.FIToFIPmtStsReq fiToFIPmtStsReq = PaymentStatusPacs028.FIToFIPmtStsReq.builder()
                .grpHdr(groupHeader)
                .orgnlGrpInf(originalGroupInfo)
                .txInf(txInfo)
                .build();

        return PaymentStatusPacs028.builder()
                .fiToFIPmtStsReq(fiToFIPmtStsReq)
                .build();
    }

    private void processPaymentStatusResponse(String responseXml) {
        log.info("Processing payment status response: {}", responseXml.substring(0, Math.min(responseXml.length(), 100)) + "...");

        try {
            FileSaver.saveDecryptMessageToFile(responseXml, "payment_status_response_" + System.currentTimeMillis() + ".xml");
        } catch (Exception e) {
            log.error("Failed to save payment status response to file", e);
        }
    }
}