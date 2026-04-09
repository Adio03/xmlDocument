package com.strata.xmlDocument.application.input;

import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.MandateCancellationRequest;

public interface MandateCancellationPain011UseCase {
    String cancelMandate(MandateCancellationRequest request) throws Exception;
}
