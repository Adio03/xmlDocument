package com.strata.xmlDocument.infrastructure.adapter.input.controller;

import com.strata.xmlDocument.application.input.MandateAcceptancePain012UseCase;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.MandateAcceptanceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mandate")
@RequiredArgsConstructor
public class MandateAcceptancePain012Controller {

    private final MandateAcceptancePain012UseCase mandateAcceptanceUseCase;

    @PostMapping("/accept")
    public ResponseEntity<String> reportAcceptance(@RequestBody MandateAcceptanceRequest request) throws Exception {
        String msgId = mandateAcceptanceUseCase.reportAcceptance(request);
        return ResponseEntity.ok(msgId);
    }
}
