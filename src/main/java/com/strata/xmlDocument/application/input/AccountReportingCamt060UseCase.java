package com.strata.xmlDocument.application.input;

import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.AccountReportingRequest;

public interface AccountReportingCamt060UseCase {
    String requestAccountReporting(AccountReportingRequest request) throws Exception;
}
