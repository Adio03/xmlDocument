package com.strata.xmlDocument.infrastructure.adapter.input.dtos.request;

import lombok.Data;

@Data
public class MandateAmendmentRequest {
    // Group Header
    private String initiatingPartyName;

    // Original Message Information
    private String originalMessageId;
    private String originalCreationDateTime;

    // Amendment Reason
    private String amendmentReasonCode;
    private String amendmentReasonDescription;

    // Mandate
    private String mandateId;
    private String sequenceType;
    private String frequencyType;
    private String firstCollectionDate;
    private String finalCollectionDate;
    private boolean trackingIndicator;

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

    // Original Mandate
    private String originalMandateId;
}
