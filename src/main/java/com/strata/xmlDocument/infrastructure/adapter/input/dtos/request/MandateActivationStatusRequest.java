package com.strata.xmlDocument.infrastructure.adapter.input.dtos.request;

import lombok.Data;

@Data
public class MandateActivationStatusRequest {
    // Group Header
    private String initiatingPartyName;
    private String creditorName;
    private String creditorAccountNumber;
    private String creditorAccountName;
    private String debtorName;
    private String debtorAccountNumber;
    private String debtorAccountName;
    private String forwardingAgentBIC;
    private String forwardingAgentMemberId;
    private String debtorAgentBIC;
    private String debtorAgentMemberId;
    private String creditorAgentBIC;
    private String creditorAgentMemberId;

    // Original Group Info
    private String originalMessageId;
    private String originalCreationDateTime;
    private String groupStatus;

    // Original Payment Info
    private String originalPaymentInformationId;
    private String originalEndToEndId;
    private String transactionStatus;
}
