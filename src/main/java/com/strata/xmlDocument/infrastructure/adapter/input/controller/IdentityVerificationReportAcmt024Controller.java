package com.strata.xmlDocument.infrastructure.adapter.input.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/nps/callback")
public class IdentityVerificationReportAcmt024Controller {

    @PostMapping(value = "/acmt024", consumes = MediaType.APPLICATION_XML_VALUE)
    public void receiveAcmt024CallBack(@RequestBody String acmt024Xml) {


    }
}