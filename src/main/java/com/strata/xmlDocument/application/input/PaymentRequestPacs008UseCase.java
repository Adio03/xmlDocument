package com.strata.xmlDocument.application.input;

import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.PaymentRequest;

public interface PaymentRequestPacs008UseCase {
    String paymentRequestPacs008(PaymentRequest paymentRequest) throws Exception;

    void receiveInboundPacs008(String encryptedResponse) throws Exception;
}
