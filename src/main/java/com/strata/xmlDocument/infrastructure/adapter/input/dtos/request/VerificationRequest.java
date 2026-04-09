package com.strata.xmlDocument.infrastructure.adapter.input.dtos.request;

import lombok.Data;

@Data
public class VerificationRequest {

    private String beneficiaryId;
    private String partyToVerifyName;
    private String accountNumber;

}
