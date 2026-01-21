package com.strata.xmlDocument.application.input;

import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.PaymentRequestMap;

public interface PaymentRequestPacs008UseCase {
    String paymentRequestPacs008(PaymentRequestMap paymentRequestMap) throws Exception;

    void receiveInboundPacs008(String encryptedResponse) throws Exception;
}
