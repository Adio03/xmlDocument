package com.strata.xmlDocument.application.input;

import com.strata.xmlDocument.infrastructure.adapter.input.VerificationRequest;

public interface IdentityVerificationAcmt023UseCase {
    String identityVerification(VerificationRequest verificationRequest) throws Exception;
}
