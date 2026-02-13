package com.strata.xmlDocument.application.input;

import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.PaymentStatusRequest;

public interface PaymentStatusPacs028UseCase {
    String paymentStatusRequest(PaymentStatusRequest paymentStatusRequest) throws Exception;
    void receiveInboundPacs028(String encryptedResponse) throws Exception;
}