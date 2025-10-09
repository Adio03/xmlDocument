package com.strata.xmlDocument.infrastructure.adapter.input;

import jdk.jfr.DataAmount;
import lombok.Data;

@Data
public class VerificationRequest {
    private String msgId;
    private String sendingPartyName;
    private String sourceId;
    private String beneficiaryId;
    private String partyToVerifyName;
    private String accountNumber;
}
