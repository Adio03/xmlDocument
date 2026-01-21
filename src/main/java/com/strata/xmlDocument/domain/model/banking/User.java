package com.strata.xmlDocument.domain.model.banking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String username;
    
    @Indexed(unique = true)
    private String email;
    
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    
    @Indexed(unique = true)
    private String nationalId; // For KYC compliance
    
    @Builder.Default
    private UserStatus status = UserStatus.PENDING_VERIFICATION;
    
    @Builder.Default
    private List<String> accountIds = new ArrayList<>();
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;
    
    public enum UserStatus {
        PENDING_VERIFICATION,
        ACTIVE,
        SUSPENDED,
        CLOSED
    }
    
    // Business logic methods
    public boolean isActive() {
        return status == UserStatus.ACTIVE;
    }
    
    public void activate() {
        this.status = UserStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void suspend() {
        this.status = UserStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void addAccount(String accountId) {
        if (accountIds == null) {
            accountIds = new ArrayList<>();
        }
        if (!accountIds.contains(accountId)) {
            accountIds.add(accountId);
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void updateLastLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }
}