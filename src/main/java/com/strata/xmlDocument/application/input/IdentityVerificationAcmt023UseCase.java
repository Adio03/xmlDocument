package com.strata.xmlDocument.application.input;

import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.VerificationRequest;

public interface IdentityVerificationAcmt023UseCase {
    String identityVerificationOutBoundAcmt023(VerificationRequest verificationRequest) throws Exception;
    void identityVerificationInBoundAcmt023(String encryptedData) throws Exception;
}
