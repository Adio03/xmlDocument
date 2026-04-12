package com.strata.xmlDocument.application.input;

import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.MandateActivationRequest;

public interface MandateActivationPain013UseCase {
    String activateMandate(MandateActivationRequest request) throws Exception;
}
