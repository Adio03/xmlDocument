package com.strata.xmlDocument.application.output.banking;

import com.strata.xmlDocument.domain.model.banking.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Output port for transaction persistence operations
 * Following hexagonal architecture principles
 */
public interface TransactionRepository {
    
    /**
     * Save a transaction to the repository
     * @param transaction the transaction to save
     * @return saved transaction
     */
    Transaction save(Transaction transaction);
    
    /**
     * Find transaction by unique identifier
     * @param transactionId the transaction ID
     * @return optional transaction
     */
    Optional<Transaction> findById(String transactionId);
    
    /**
     * Find transaction by reference number
     * @param reference the transaction reference
     * @return optional transaction
     */
    Optional<Transaction> findByReference(String reference);
    
    /**
     * Find all transactions for an account (either from or to)
     * @param accountId the account ID
     * @return list of transactions
     */
    List<Transaction> findByAccountId(String accountId);
    
    /**
     * Find transactions for an account within date range
     * @param accountId the account ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of transactions
     */
    List<Transaction> findByAccountIdAndDateRange(String accountId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find pending transactions for an account
     * @param accountId the account ID
     * @return list of pending transactions
     */
    List<Transaction> findPendingByAccountId(String accountId);
    
    /**
     * Find transactions by status
     * @param status the transaction status
     * @return list of transactions with given status
     */
    List<Transaction> findByStatus(Transaction.TransactionStatus status);
    
    /**
     * Get transaction count for an account
     * @param accountId the account ID
     * @return count of transactions
     */
    long countByAccountId(String accountId);
}