package com.strata.xmlDocument.infrastructure.adapter.input.controller;

import com.strata.xmlDocument.application.input.MandateCancellationPain011UseCase;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.MandateCancellationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mandate")
@RequiredArgsConstructor
public class MandateCancellationPain011Controller {

    private final MandateCancellationPain011UseCase mandateCancellationUseCase;

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelMandate(@RequestBody MandateCancellationRequest request) throws Exception {
        String msgId = mandateCancellationUseCase.cancelMandate(request);
        return ResponseEntity.ok(msgId);
    }
}
