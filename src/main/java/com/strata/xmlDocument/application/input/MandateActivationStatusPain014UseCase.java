package com.strata.xmlDocument.application.input;

import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.MandateActivationStatusRequest;

public interface MandateActivationStatusPain014UseCase {
    String reportActivationStatus(MandateActivationStatusRequest request) throws Exception;
}
