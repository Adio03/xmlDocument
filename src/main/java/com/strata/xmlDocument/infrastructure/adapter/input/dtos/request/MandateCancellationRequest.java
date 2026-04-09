package com.strata.xmlDocument.infrastructure.adapter.input.dtos.request;

import lombok.Data;

@Data
public class MandateCancellationRequest {
    // Original Message Information
    private String originalMessageId;
    private String originalCreationDateTime;

    // Cancellation Reason
    private String cancellationReasonCode;
    private String cancellationReasonDescription;

    // Original Mandate
    private String originalMandateId;
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
}
