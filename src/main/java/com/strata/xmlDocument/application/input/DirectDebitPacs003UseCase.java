package com.strata.xmlDocument.application.input;

import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.DirectDebitRequest;

public interface DirectDebitPacs003UseCase {
    String initiateDirectDebit(DirectDebitRequest request) throws Exception;
}
