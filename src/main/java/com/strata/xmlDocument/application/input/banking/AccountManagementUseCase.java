package com.strata.xmlDocument.application.input.banking;

import com.strata.xmlDocument.domain.model.banking.Account;

import java.math.BigDecimal;
import java.util.List;

/**
 * Input port for account management operations
 * Defines the business use cases related to account management
 */
public interface AccountManagementUseCase {
    
    /**
     * Create a new account for a user
     */
    Account createAccount(AccountCreateRequest request);
    
    /**
     * Get account details by ID
     */
    Account getAccountById(String accountId);
    
    /**
     * Get account details by account number
     */
    Account getAccountByNumber(String accountNumber);
    
    /**
     * Get all accounts for a user
     */
    List<Account> getUserAccounts(String userId);
    
    /**
     * Get account balance
     */
    AccountBalanceResponse getAccountBalance(String accountId);
    
    /**
     * Set overdraft limit for an account
     */
    void setOverdraftLimit(String accountId, BigDecimal overdraftLimit);
    
    /**
     * Freeze account (prevent transactions)
     */
    void freezeAccount(String accountId, String reason);
    
    /**
     * Unfreeze account
     */
    void unfreezeAccount(String accountId);
    
    // Request DTOs
    record AccountCreateRequest(
            String userId,
            Account.AccountType accountType,
            String currency,
            BigDecimal initialDeposit,
            BigDecimal overdraftLimit
    ) {}
    
    record AccountBalanceResponse(
            String accountId,
            String accountNumber,
            BigDecimal currentBalance,
            BigDecimal availableBalance,
            BigDecimal overdraftLimit,
            String currency,
            Account.AccountStatus status
    ) {}
    
    // Exceptions
    class AccountNotFoundException extends RuntimeException {
        public AccountNotFoundException(String message) {
            super(message);
        }
    }
    
    class AccountCreationException extends RuntimeException {
        public AccountCreationException(String message) {
            super(message);
        }
    }
    
    class InvalidOperationException extends RuntimeException {
        public InvalidOperationException(String message) {
            super(message);
        }
    }
    
    class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}