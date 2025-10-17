package com.strata.xmlDocument.infrastructure.adapter.input.dtos.request;

public class verificationReportRequest {
    private String receiverName;
    private String receivingInstitutionId;
    private String inboundAcmt023MessageIdReceived;
    private String creditAmountReceivedInboundAcmt023;
    private boolean verificationResponse;
    private String accountNumber;
    private String accountName;
    private String idType;  // BVN,NIN
    private String idValue;
    private String accountTier;
    private String accountDesignation;
    private String beneficiaryBankId;
    private String beneficiaryBankCode;

}

