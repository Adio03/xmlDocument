package com.strata.xmlDocument.application.input;

import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.MandateAmendmentRequest;

public interface MandateAmendmentPain010UseCase {
    String amendMandate(MandateAmendmentRequest request) throws Exception;
}
