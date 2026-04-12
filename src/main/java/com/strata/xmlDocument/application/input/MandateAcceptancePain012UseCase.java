package com.strata.xmlDocument.application.input;

import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.MandateAcceptanceRequest;

public interface MandateAcceptancePain012UseCase {
    String reportAcceptance(MandateAcceptanceRequest request) throws Exception;
}
