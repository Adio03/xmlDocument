package com.strata.xmlDocument.domain.service;

import com.strata.xmlDocument.application.input.IdentityVerificationReportAcmt024UseCase;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@RequiredArgsConstructor
public class IdentityVerificationReportAcmt024Service implements IdentityVerificationReportAcmt024UseCase {
    @Override
    public void recieveAcmt024CallBack(String encryptData) {

    }
}
