package com.strata.xmlDocument.infrastructure.adapter.input.dtos.request;

import lombok.Data;

@Data
public class PaymentRequest {
    private String destinationInstitutionId;
    private String debtorAccountIban;
    private String debtorAccountName;
    private String creditorName;
    private String creditorAccountIban;
    private String creditorAccountName;
    private String interbankSettlementAmount;
    private String interbankSettlementCurrency;
    private String NameEnquiryMsgId;// acmt023
    private String accountTier;
    private String channelCode;
 }
