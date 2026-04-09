package com.strata.xmlDocument.infrastructure.adapter.input.controller;

import com.strata.xmlDocument.application.input.MandateAmendmentPain010UseCase;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.MandateAmendmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mandate")
@RequiredArgsConstructor
public class MandateAmendmentPain010Controller {

    private final MandateAmendmentPain010UseCase mandateAmendmentUseCase;

    @PostMapping("/amend")
    public ResponseEntity<String> amendMandate(@RequestBody MandateAmendmentRequest request) throws Exception {
        String msgId = mandateAmendmentUseCase.amendMandate(request);
        return ResponseEntity.ok(msgId);
    }
}
