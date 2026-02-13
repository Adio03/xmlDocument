package com.strata.xmlDocument.domain.model.banking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transactions")
public class Transaction {
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String reference;
    
    private String fromAccountId;
    private String toAccountId;
    private TransactionType type;
    private BigDecimal amount;
    
    @Builder.Default
    private String currency = "USD";
    
    private String description;
    
    @Builder.Default
    private TransactionStatus status = TransactionStatus.PENDING;
    
    private String failureReason;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    
    // Balance snapshots for audit trail
    private BigDecimal fromAccountBalanceBefore;
    private BigDecimal fromAccountBalanceAfter;
    private BigDecimal toAccountBalanceBefore;
    private BigDecimal toAccountBalanceAfter;
    
    public enum TransactionType {
        DEPOSIT("DEP", "Deposit"),
        WITHDRAWAL("WTH", "Withdrawal"),
        TRANSFER("TRF", "Transfer");
        
        private final String code;
        private final String description;
        
        TransactionType(String code, String description) {
            this.code = code;
            this.description = description;
        }
        
        public String getCode() { return code; }
        public String getDescription() { return description; }
    }
    
    public enum TransactionStatus {
        PENDING("Transaction is pending processing"),
        PROCESSING("Transaction is being processed"),
        COMPLETED("Transaction completed successfully"),
        FAILED("Transaction failed"),
        CANCELLED("Transaction was cancelled");
        
        private final String description;
        
        TransactionStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() { return description; }
    }
    
    // Business logic methods
    public boolean isCompleted() {
        return status == TransactionStatus.COMPLETED;
    }
    
    public boolean isPending() {
        return status == TransactionStatus.PENDING;
    }
    
    public void complete() {
        this.status = TransactionStatus.COMPLETED;
        this.processedAt = LocalDateTime.now();
    }
    
    public void fail(String reason) {
        this.status = TransactionStatus.FAILED;
        this.failureReason = reason;
        this.processedAt = LocalDateTime.now();
    }
    
    public void process() {
        this.status = TransactionStatus.PROCESSING;
    }
    
    public boolean isDeposit() {
        return type == TransactionType.DEPOSIT;
    }
    
    public boolean isWithdrawal() {
        return type == TransactionType.WITHDRAWAL;
    }
    
    public boolean isTransfer() {
        return type == TransactionType.TRANSFER;
    }
}