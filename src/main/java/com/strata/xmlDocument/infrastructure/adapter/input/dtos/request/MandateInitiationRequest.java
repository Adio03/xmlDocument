package com.strata.xmlDocument.infrastructure.adapter.input.dtos.request;

import lombok.Data;

@Data
public class MandateInitiationRequest {
    // Mandate
    private String mandateId;
    private String sequenceType; // RCUR, OOFF
    private String frequencyType; // DAIL, WEEK, MNTH, QURT, YEAR
    private String firstCollectionDate; // ISO-8601
    private String finalCollectionDate; // ISO-8601
    private boolean trackingIndicator;
    private String collectionAmount;
    private String collectionCurrency;

    // Creditor
    private String creditorName;
    private String creditorAccountNumber;
    private String creditorAccountName;
    private String creditorAgentBIC;
    private String creditorAgentMemberId;

    // Debtor
    private String debtorName;
    private String debtorAccountNumber;
    private String debtorAccountName;
    private String debtorAgentBIC;
    private String debtorAgentMemberId;

    // Referred Document
    private String documentTypeCode;
    private String documentNumber;

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
    private boolean fixedCollectionAmount;
}
