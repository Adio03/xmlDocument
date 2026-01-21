package com.strata.xmlDocument.application.output.banking;

import com.strata.xmlDocument.domain.model.banking.Account;

import java.util.List;
import java.util.Optional;

/**
 * Output port for account persistence operations
 * Following hexagonal architecture principles
 */
public interface AccountRepository {
    
    /**
     * Save an account to the repository
     * @param account the account to save
     * @return saved account
     */
    Account save(Account account);
    
    /**
     * Find account by unique identifier
     * @param accountId the account ID
     * @return optional account
     */
    Optional<Account> findById(String accountId);
    
    /**
     * Find account by account number
     * @param accountNumber the account number
     * @return optional account
     */
    Optional<Account> findByAccountNumber(String accountNumber);
    
    /**
     * Find all accounts for a user
     * @param userId the user ID
     * @return list of user's accounts
     */
    List<Account> findByUserId(String userId);
    
    /**
     * Find active accounts for a user
     * @param userId the user ID
     * @return list of active accounts
     */
    List<Account> findByUserIdAndStatus(String userId, Account.AccountStatus status);
    
    /**
     * Check if account number exists
     * @param accountNumber the account number to check
     * @return true if exists
     */
    boolean existsByAccountNumber(String accountNumber);
    
    /**
     * Delete account by ID
     * @param accountId the account ID
     */
    void deleteById(String accountId);
}