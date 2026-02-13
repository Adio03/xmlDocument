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
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "accounts")
public class Account {
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String accountNumber;
    
    private String userId;
    private AccountType accountType;
    
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;
    
    @Builder.Default
    private BigDecimal overdraftLimit = BigDecimal.ZERO;
    
    @Builder.Default
    private String currency = "USD";
    
    @Builder.Default
    private AccountStatus status = AccountStatus.ACTIVE;
    
    @Builder.Default
    private List<String> transactionIds = new ArrayList<>();
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public enum AccountType {
        CHECKING("CHK", "Checking Account"),
        SAVINGS("SAV", "Savings Account"),
        BUSINESS("BUS", "Business Account");
        
        private final String code;
        private final String description;
        
        AccountType(String code, String description) {
            this.code = code;
            this.description = description;
        }
        
        public String getCode() { return code; }
        public String getDescription() { return description; }
    }
    
    public enum AccountStatus {
        ACTIVE,
        INACTIVE,
        FROZEN,
        CLOSED
    }
    
    // Business logic methods
    public boolean canDebit(BigDecimal amount) {
        if (status != AccountStatus.ACTIVE) return false;
        if (amount.compareTo(BigDecimal.ZERO) <= 0) return false;
        
        BigDecimal availableBalance = balance.add(overdraftLimit);
        return availableBalance.compareTo(amount) >= 0;
    }
    
    public void debit(BigDecimal amount) {
        if (!canDebit(amount)) {
            throw new IllegalArgumentException("Insufficient funds or invalid amount");
        }
        this.balance = this.balance.subtract(amount);
        this.updatedAt = LocalDateTime.now();
    }
    
    public void credit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Credit amount must be positive");
        }
        this.balance = this.balance.add(amount);
        this.updatedAt = LocalDateTime.now();
    }
    
    public BigDecimal getAvailableBalance() {
        return balance.add(overdraftLimit);
    }
    
    public boolean isActive() {
        return status == AccountStatus.ACTIVE;
    }
    
    public void addTransaction(String transactionId) {
        if (transactionIds == null) {
            transactionIds = new ArrayList<>();
        }
        transactionIds.add(transactionId);
        this.updatedAt = LocalDateTime.now();
    }
}