package com.strata.xmlDocument.infrastructure.adapter.input.controller;

import com.strata.xmlDocument.application.input.IdentityVerificationReportAcmt024UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/nps/callback")
@RequiredArgsConstructor
public class IdentityVerificationReportAcmt024Controller {
    private final IdentityVerificationReportAcmt024UseCase identityVerificationReportAcmt024UseCase;


    @PostMapping(value = "/acmt024", consumes = MediaType.APPLICATION_XML_VALUE)
    public void receiveAcmt024CallBack(@RequestBody String acmt024Xml) {


    }
}