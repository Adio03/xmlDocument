package com.strata.xmlDocument.infrastructure.adapter.input.dtos.request;

import lombok.Data;

@Data
public class DirectDebitRequest {
    // Group Header
    private String creditorBankMemberId;
    private String debtorBankMemberId;
    private String controlSum;

    // Payment Identification
    private String instructionId;
    private String endToEndId;
    private String transactionId;

    // Transaction Details
    private String interbankSettlementAmount;
    private String interbankSettlementCurrency;
    private String interbankSettlementDate;
    private String instructedAmount;
    private String instructedCurrency;

    // Mandate Related Information
    private String mandateId;
    private String dateOfSignature;
    private String firstCollectionDate;
    private String finalCollectionDate;
    private String frequencyType;

    // Creditor
    private String creditorName;
    private String creditorAccountNumber;
    private String creditorAccountName;
    private String creditorBankBIC;

    // Instructing Agent
    private String instructingBankBIC;
    private String instructingBankMemberId;

    // Instructed Agent
    private String instructedBankBIC;
    private String instructedBankMemberId;

    // Debtor
    private String debtorName;
    private String debtorAccountNumber;
    private String debtorAccountName;
    private String debtorBankBIC;
    private String debtorBankMemberId;

    // Remittance
    private String narration;

    // Supplementary Data - Debtor
    private String debtorAccountDesignation;
    private String debtorIdType;
    private String debtorIdValue;
    private String debtorAccountTier;
    private String debtorBiometricData;

    // Supplementary Data - Creditor
    private String creditorAccountDesignation;
    private String creditorIdType;
    private String creditorIdValue;
    private String creditorAccountTier;

    // Supplementary Data - Transaction
    private String transactionLocation;
    private String nameEnquiryMsgId;
    private String channelCode;
    private boolean fixedCollectionAmount;
}
