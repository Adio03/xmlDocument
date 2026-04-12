package com.strata.xmlDocument.application.input;

import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.AccountReportRequest;

public interface AccountReportCamt052UseCase {
    String generateAccountReport(AccountReportRequest request) throws Exception;
}
