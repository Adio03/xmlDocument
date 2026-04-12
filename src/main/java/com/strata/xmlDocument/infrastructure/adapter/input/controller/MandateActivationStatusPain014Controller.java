package com.strata.xmlDocument.infrastructure.adapter.input.controller;

import com.strata.xmlDocument.application.input.MandateActivationStatusPain014UseCase;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.MandateActivationStatusRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mandate")
@RequiredArgsConstructor
public class MandateActivationStatusPain014Controller {

    private final MandateActivationStatusPain014UseCase statusUseCase;

    @PostMapping("/status-report")
    public ResponseEntity<String> reportActivationStatus(@RequestBody MandateActivationStatusRequest request) throws Exception {
        String msgId = statusUseCase.reportActivationStatus(request);
        return ResponseEntity.ok(msgId);
    }
}
