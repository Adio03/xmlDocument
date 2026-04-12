package com.strata.xmlDocument.infrastructure.adapter.input.dtos.request;

import lombok.Data;

@Data
public class MandateActivationRequest {
    // Group Header
    private String initiatingPartyName;
    private String clientId;

    // Payment Information
    private String paymentInformationId;
    private String requestedExecutionDateTime;

    // Debtor
    private String debtorName;
    private String debtorAccountNumber;
    private String debtorAccountName;
    private String debtorAccountCurrency;
    private String debtorAgentBIC;
    private String debtorAgentMemberId;

    // Transaction Details
    private String endToEndId;
    private String instructedAmount;
    private String instructedCurrency;
    private String paymentPurpose;

    // Creditor
    private String creditorName;
    private String creditorAccountNumber;
    private String creditorAccountName;
    private String creditorAgentBIC;
    private String creditorAgentMemberId;

    // Supplementary Data - Debtor
    private String debtorAccountDesignation;
    private String debtorIdType;
    private String debtorIdValue;
    private String debtorAccountTier;
    private String debtorBiometricData;
    private String debtorAddressLine;
    private String debtorPhoneNumber;
    private String debtorEmailAddress;

    // Supplementary Data - Creditor
    private String creditorAccountDesignation;
    private String creditorIdType;
    private String creditorIdValue;
    private String creditorAccountTier;

    // Supplementary Data - Transaction
    private String transactionLocation;
    private String channelCode;
    private String mandateCategory;
}
