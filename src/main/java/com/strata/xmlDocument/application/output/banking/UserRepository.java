package com.strata.xmlDocument.application.output.banking;

import com.strata.xmlDocument.domain.model.banking.User;

import java.util.Optional;

/**
 * Output port for user persistence operations
 * Following hexagonal architecture principles
 */
public interface UserRepository {
    
    /**
     * Save a user to the repository
     * @param user the user to save
     * @return saved user
     */
    User save(User user);
    
    /**
     * Find user by unique identifier
     * @param userId the user ID
     * @return optional user
     */
    Optional<User> findById(String userId);
    
    /**
     * Find user by username
     * @param username the username
     * @return optional user
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find user by email
     * @param email the email address
     * @return optional user
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find user by national ID for KYC purposes
     * @param nationalId the national identification number
     * @return optional user
     */
    Optional<User> findByNationalId(String nationalId);
    
    /**
     * Check if username is already taken
     * @param username the username to check
     * @return true if exists
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if email is already registered
     * @param email the email to check
     * @return true if exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Delete user by ID
     * @param userId the user ID
     */
    void deleteById(String userId);
}