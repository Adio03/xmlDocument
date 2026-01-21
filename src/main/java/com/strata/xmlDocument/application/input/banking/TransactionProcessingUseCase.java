package com.strata.xmlDocument.application.input.banking;

import com.strata.xmlDocument.domain.model.banking.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Input port for transaction processing operations
 * Defines the business use cases related to financial transactions
 */
public interface TransactionProcessingUseCase {
    
    /**
     * Process a deposit transaction
     */
    Transaction processDeposit(DepositRequest request);
    
    /**
     * Process a withdrawal transaction
     */
    Transaction processWithdrawal(WithdrawalRequest request);
    
    /**
     * Process a transfer between accounts
     */
    Transaction processTransfer(TransferRequest request);
    
    /**
     * Get transaction history for an account
     */
    List<Transaction> getTransactionHistory(String accountId, TransactionHistoryRequest request);
    
    /**
     * Get transaction details by ID
     */
    Transaction getTransactionById(String transactionId);
    
    /**
     * Get transaction details by reference
     */
    Transaction getTransactionByReference(String reference);
    
    /**
     * Get pending transactions for an account
     */
    List<Transaction> getPendingTransactions(String accountId);
    
    // Request DTOs
    record DepositRequest(
            String accountId,
            BigDecimal amount,
            String description
    ) {}
    
    record WithdrawalRequest(
            String accountId,
            BigDecimal amount,
            String description
    ) {}
    
    record TransferRequest(
            String fromAccountId,
            String toAccountId,
            BigDecimal amount,
            String description
    ) {}
    
    record TransactionHistoryRequest(
            LocalDateTime startDate,
            LocalDateTime endDate,
            int pageSize,
            int pageNumber
    ) {}
    
    // Exceptions
    class AccountNotFoundException extends RuntimeException {
        public AccountNotFoundException(String message) {
            super(message);
        }
    }
    
    class InsufficientFundsException extends RuntimeException {
        public InsufficientFundsException(String message) {
            super(message);
        }
    }
    
    class InvalidTransactionException extends RuntimeException {
        public InvalidTransactionException(String message) {
            super(message);
        }
    }
    
    class TransactionNotFoundException extends RuntimeException {
        public TransactionNotFoundException(String message) {
            super(message);
        }
    }
}