package com.strata.xmlDocument.infrastructure.adapter.input.controller;

import com.strata.xmlDocument.application.input.AccountStatementCamt053UseCase;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.AccountReportRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account-statement")
@RequiredArgsConstructor
public class AccountStatementCamt053Controller {

    private final AccountStatementCamt053UseCase accountStatementUseCase;

    @PostMapping("/generate")
    public ResponseEntity<String> generateAccountStatement(@RequestBody AccountReportRequest request) {
        String messageId = accountStatementUseCase.generateAccountStatement(request);
        return ResponseEntity.ok(messageId);
    }
}
