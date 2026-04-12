package com.strata.xmlDocument.infrastructure.adapter.input.controller;

import com.strata.xmlDocument.application.input.AccountReportCamt052UseCase;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.AccountReportRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account-report")
@RequiredArgsConstructor
public class AccountReportCamt052Controller {

    private final AccountReportCamt052UseCase accountReportUseCase;

    @PostMapping("/generate")
    public ResponseEntity<String> generateAccountReport(@RequestBody AccountReportRequest request) {
        String messageId = accountReportUseCase.generateAccountReport(request);
        return ResponseEntity.ok(messageId);
    }
}
