package com.strata.xmlDocument.application.input;

import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.AccountReportRequest;

public interface AccountStatementCamt053UseCase {
    String generateAccountStatement(AccountReportRequest request);
}
