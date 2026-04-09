package com.strata.xmlDocument.application.input;

import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.MandateInitiationRequest;

public interface MandateInitiationPain009UseCase {
    String initiateMandate(MandateInitiationRequest request) throws Exception;
}
