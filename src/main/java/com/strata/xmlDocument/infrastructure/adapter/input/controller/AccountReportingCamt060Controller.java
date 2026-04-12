package com.strata.xmlDocument.infrastructure.adapter.input.controller;

import com.strata.xmlDocument.application.input.AccountReportingCamt060UseCase;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.AccountReportingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account-reporting")
@RequiredArgsConstructor
public class AccountReportingCamt060Controller {

    private final AccountReportingCamt060UseCase reportingUseCase;

    @PostMapping("/request")
    public ResponseEntity<String> requestAccountReporting(@RequestBody AccountReportingRequest request) throws Exception {
        String msgId = reportingUseCase.requestAccountReporting(request);
        return ResponseEntity.ok(msgId);
    }
}
