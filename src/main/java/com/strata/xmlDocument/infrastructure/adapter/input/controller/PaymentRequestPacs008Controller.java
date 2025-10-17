package com.strata.xmlDocument.infrastructure.adapter.input.controller;

import com.strata.xmlDocument.domain.service.PaymentRequestPacs008Service;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.PaymentRequest;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/nps/pacs008")
@RestController
@RequiredArgsConstructor
public class PaymentRequestPacs008Controller {
    private final PaymentRequestPacs008Service paymentRequestPacs008Service;

    @PostMapping("/outbound_pacs008")
    public ResponseEntity<?> sendOutboundPacs008(@RequestBody PaymentRequest paymentRequest) throws Exception {
        String response = paymentRequestPacs008Service.paymentRequestPacs008(paymentRequest);
        MessageResponse messageResponse = MessageResponse.builder().messageId(response).build();
        return ResponseEntity.ok(messageResponse);
    }

    @PostMapping(value = "/inbound_pacs008", consumes = MediaType.APPLICATION_XML_VALUE)
       public void receiveInboundPacs008(@RequestBody String encryptedResponse) throws Exception {
        paymentRequestPacs008Service.receiveInboundPacs008(encryptedResponse);

        }

    }

