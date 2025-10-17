package com.strata.xmlDocument.infrastructure.adapter.input.controller;

import com.strata.xmlDocument.application.input.PaymentStatusPacs028UseCase;
import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.PaymentStatusRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/nps/pacs028")
@RequiredArgsConstructor
public class PaymentStatusPacs028Controller {
    private final PaymentStatusPacs028UseCase paymentStatusPacs028UseCase;

    @PostMapping("/outbound_pacs028")
    public ResponseEntity<?> sendOutboundPacs028(@RequestBody PaymentStatusRequest paymentStatusRequest) throws Exception {
        String response = paymentStatusPacs028UseCase.paymentStatusRequest(paymentStatusRequest);
        return ResponseEntity.ok(response);
    }
    @PostMapping(value = "/inbound_pacs028", consumes = MediaType.APPLICATION_XML_VALUE)
    public void receiveInboundPacs028(@RequestBody String encryptedResponse) throws Exception {
        paymentStatusPacs028UseCase.receiveInboundPacs028(encryptedResponse);
    }
}
