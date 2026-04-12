package com.strata.xmlDocument.application.input;

import com.strata.xmlDocument.domain.model.PaymentRequestPacs008;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.PaymentRequestMap;

public interface PaymentApprovalPacs002UseCase {
    String generatePaymentApproval(PaymentRequestMap paymentRequestMap, PaymentRequestPacs008 originalRequest) throws Exception;
    String generatePaymentApproval(PaymentRequestMap paymentRequestMap,
                                   PaymentRequestPacs008 originalRequest,
                                   boolean approved,
                                   String reasonCode,
                                   String reasonDescription) throws Exception;
    void receiveInboundPacs002(String encryptedResponse) throws Exception;
}
