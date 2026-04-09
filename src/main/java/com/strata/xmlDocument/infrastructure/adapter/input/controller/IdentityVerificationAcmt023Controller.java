package com.strata.xmlDocument.infrastructure.adapter.input.controller;

import com.strata.xmlDocument.application.input.IdentityVerificationAcmt023UseCase;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.VerificationRequest;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nps/acmt023")
@RequiredArgsConstructor
@Slf4j
public class IdentityVerificationAcmt023Controller {

    private final IdentityVerificationAcmt023UseCase  identityVerificationAcmt023UseCase;


    @PostMapping("/outbound_acmt023")
    public ResponseEntity<MessageResponse> outboundAcmt023(@RequestBody VerificationRequest request) throws Exception {
        String response = identityVerificationAcmt023UseCase.identityVerificationOutBoundAcmt023(request);
        MessageResponse messageResponse = MessageResponse.builder().messageId(response).build();
        return ResponseEntity.accepted().body(messageResponse);
    }
    @PostMapping(value = "/inbound_acmt023", consumes = MediaType.APPLICATION_XML_VALUE)
    public void inboundAcmt023(@RequestBody String inboundAcmt023) throws Exception {
        log.info("Incoming request =============================>>>>>>>>>>>>>>>>>>>>>>>>");
        identityVerificationAcmt023UseCase.identityVerificationInBoundAcmt023(inboundAcmt023);

    }


    }



