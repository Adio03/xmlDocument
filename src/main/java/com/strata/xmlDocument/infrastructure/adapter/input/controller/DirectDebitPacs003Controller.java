package com.strata.xmlDocument.infrastructure.adapter.input.controller;

import com.strata.xmlDocument.application.input.DirectDebitPacs003UseCase;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.DirectDebitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/direct-debit")
@RequiredArgsConstructor
public class DirectDebitPacs003Controller {

    private final DirectDebitPacs003UseCase directDebitUseCase;

    @PostMapping("/initiate")
    public ResponseEntity<String> initiateDirectDebit(@RequestBody DirectDebitRequest request) throws Exception {
        String msgId = directDebitUseCase.initiateDirectDebit(request);
        return ResponseEntity.ok(msgId);
    }
}
