package com.strata.xmlDocument.infrastructure.adapter.input.controller;

import com.strata.xmlDocument.application.input.IdentityVerificationAcmt023UseCase;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.VerificationRequest;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/xml")
@RequiredArgsConstructor
public class XmlCommunicationController {

    private final IdentityVerificationAcmt023UseCase  identityVerificationAcmt023UseCase;


    @PostMapping("/send")
    public ResponseEntity<MessageResponse> sendXmlRequest(@RequestBody VerificationRequest request) throws Exception {
        String response = identityVerificationAcmt023UseCase.identityVerification(request);
        MessageResponse messageResponse = MessageResponse.builder().messageId(response).build();
        return ResponseEntity.accepted().body(messageResponse);
    }


}