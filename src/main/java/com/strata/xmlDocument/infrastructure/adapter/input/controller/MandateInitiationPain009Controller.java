package com.strata.xmlDocument.infrastructure.adapter.input.controller;

import com.strata.xmlDocument.application.input.MandateInitiationPain009UseCase;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.MandateInitiationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mandate")
@RequiredArgsConstructor
public class MandateInitiationPain009Controller {

    private final MandateInitiationPain009UseCase mandateInitiationUseCase;

    @PostMapping("/initiate")
    public ResponseEntity<String> initiateMandate(@RequestBody MandateInitiationRequest request) throws Exception {
        String msgId = mandateInitiationUseCase.initiateMandate(request);
        return ResponseEntity.ok(msgId);
    }
}
