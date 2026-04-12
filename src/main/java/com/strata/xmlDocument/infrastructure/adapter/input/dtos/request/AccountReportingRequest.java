package com.strata.xmlDocument.infrastructure.adapter.input.dtos.request;

import lombok.Data;

@Data
public class AccountReportingRequest {
    // Group Header
    private String messageSenderBIC;
    private String messageSenderMemberId;

    // Reporting Request
    private String reportingRequestId;
    private String requestedMessageType; // INTRADAY, STATEMENT, BALANCE etc.

    // Account
    private String accountNumber;
    private String accountCurrency;

    // Account Owner
    private String accountOwnerBIC;
    private String accountOwnerMemberId;

    // Account Servicer
    private String accountServicerBIC;
    private String accountServicerMemberId;

    // Reporting Period
    private String fromDate;
    private String toDate;
    private String reportingPeriodType; // ALLL, CURR, PREV

    // Supplementary Data - Creditor
    private String accountDesignation;
    private String idType;
    private String idValue;
    private String accountTier;

    // Supplementary Data - Transaction
    private String transactionLocation;
    private String channelCode;
    private boolean fixedCollectionAmount;
    private String mandateCode;
}
