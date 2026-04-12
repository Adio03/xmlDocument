package com.strata.xmlDocument.infrastructure.adapter.input.controller;

import com.strata.xmlDocument.application.input.MandateActivationPain013UseCase;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.MandateActivationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mandate")
@RequiredArgsConstructor
public class MandateActivationPain013Controller {

    private final MandateActivationPain013UseCase mandateActivationUseCase;

    @PostMapping("/activate")
    public ResponseEntity<String> activateMandate(@RequestBody MandateActivationRequest request) throws Exception {
        String msgId = mandateActivationUseCase.activateMandate(request);
        return ResponseEntity.ok(msgId);
    }
}
