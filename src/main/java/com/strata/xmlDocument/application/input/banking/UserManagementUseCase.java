package com.strata.xmlDocument.application.input.banking;

import com.strata.xmlDocument.domain.model.banking.User;

/**
 * Input port for user management operations
 * Defines the business use cases related to user management
 */
public interface UserManagementUseCase {
    
    /**
     * Register a new user in the system
     */
    User registerUser(UserRegistrationRequest request);
    
    /**
     * Authenticate user login
     */
    AuthenticationResult authenticateUser(UserLoginRequest request);
    
    /**
     * Get user profile by ID
     */
    User getUserProfile(String userId);
    
    /**
     * Update user profile information
     */
    User updateUserProfile(String userId, UserProfileUpdateRequest request);
    
    /**
     * Activate user account after verification
     */
    void activateUser(String userId);
    
    /**
     * Suspend user account
     */
    void suspendUser(String userId, String reason);
    
    // Request DTOs
    record UserRegistrationRequest(
            String username,
            String email,
            String password,
            String firstName,
            String lastName,
            String phoneNumber,
            String address,
            String nationalId
    ) {}
    
    record UserLoginRequest(
            String usernameOrEmail,
            String password
    ) {}
    
    record UserProfileUpdateRequest(
            String firstName,
            String lastName,
            String phoneNumber,
            String address
    ) {}
    
    record AuthenticationResult(
            boolean success,
            User user,
            String token,
            String message
    ) {}
    
    // Exceptions
    class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }
    
    class InvalidCredentialsException extends RuntimeException {
        public InvalidCredentialsException(String message) {
            super(message);
        }
    }
    
    class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}