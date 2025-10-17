package com.strata.xmlDocument.application.input;

import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.VerificationRequest;

public interface IdentityVerificationReportAcmt024UseCase {
    void identityVerificationReportInboundAcmt024(String encryptData) throws Exception;

    String identityVerificationReportOutboundAcmt024(VerificationRequest verificationRequest);
}
