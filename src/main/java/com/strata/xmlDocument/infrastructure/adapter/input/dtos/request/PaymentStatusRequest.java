package com.strata.xmlDocument.infrastructure.adapter.input.dtos.request;

import lombok.Data;

@Data
public class PaymentStatusRequest {
    private String originalMessageId;
    private String originalCreationDateTime;
    private String originalTransactionId;
    private String sourceId;
    private String destinationId;
    private String interbankSettlementDate;
    private String statusRequestId;
}