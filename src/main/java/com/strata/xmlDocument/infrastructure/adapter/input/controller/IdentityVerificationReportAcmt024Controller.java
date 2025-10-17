package com.strata.xmlDocument.infrastructure.adapter.input.controller;

import com.strata.xmlDocument.application.input.IdentityVerificationReportAcmt024UseCase;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.VerificationRequest;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/nps/acmt024")
@RequiredArgsConstructor
public class IdentityVerificationReportAcmt024Controller {
    private final IdentityVerificationReportAcmt024UseCase identityVerificationReportAcmt024UseCase;


    @PostMapping(value = "/inbound_acmt024", consumes = MediaType.APPLICATION_XML_VALUE)
    public void inboundAcmt024(@RequestBody String acmt024Xml) throws Exception {
        identityVerificationReportAcmt024UseCase.identityVerificationReportInboundAcmt024(acmt024Xml);
    }

    @PostMapping("/outbound_acmt024")
    public ResponseEntity<MessageResponse> outboundAcmt024(@RequestBody VerificationRequest verificationRequest) {
        String verificationReport = identityVerificationReportAcmt024UseCase.identityVerificationReportOutboundAcmt024(verificationRequest);
        MessageResponse messageResponse = MessageResponse.builder().messageId(verificationReport).build();
        return ResponseEntity.ok(messageResponse);
    }
}