//package com.strata.xmlDocument.domain.service.banking;
//
//import com.strata.xmlDocument.application.input.banking.UserManagementUseCase;
//import com.strata.xmlDocument.application.output.banking.UserRepository;
//import com.strata.xmlDocument.domain.model.banking.User;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
///**
// * Domain service implementing user management business logic
// * Contains the core business rules for user operations
// */
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class UserService implements UserManagementUseCase {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final SecurityService securityService;
//
//    @Override
//    public User registerUser(UserRegistrationRequest request) {
//        log.info("Registering new user with username: {}", request.username());
//
//        // Validate input
//        validateRegistrationRequest(request);
//
//        // Check if user already exists
//        if (userRepository.existsByUsername(request.username())) {
//            throw new UserAlreadyExistsException("Username already exists: " + request.username());
//        }
//
//        if (userRepository.existsByEmail(request.email())) {
//            throw new UserAlreadyExistsException("Email already exists: " + request.email());
//        }
//
//        // Check for duplicate national ID
//        if (userRepository.findByNationalId(request.nationalId()).isPresent()) {
//            throw new UserAlreadyExistsException("National ID already registered");
//        }
//
//        // Create user entity
//        User user = User.builder()
//                .username(request.username())
//                .email(request.email())
//                .passwordHash(passwordEncoder.encode(request.password()))
//                .firstName(request.firstName())
//                .lastName(request.lastName())
//                .phoneNumber(request.phoneNumber())
//                .address(request.address())
//                .nationalId(request.nationalId())
//                .status(User.UserStatus.PENDING_VERIFICATION)
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//
//        User savedUser = userRepository.save(user);
//        log.info("User registered successfully with ID: {}", savedUser.getId());
//
//        return savedUser;
//    }
//
//    @Override
//    public AuthenticationResult authenticateUser(UserLoginRequest request) {
//        log.info("Authenticating user: {}", request.usernameOrEmail());
//
//        // Find user by username or email
//        User user = userRepository.findByUsername(request.usernameOrEmail())
//                .or(() -> userRepository.findByEmail(request.usernameOrEmail()))
//                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));
//
//        // Check password
//        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
//            log.warn("Failed login attempt for user: {}", request.usernameOrEmail());
//            throw new InvalidCredentialsException("Invalid credentials");
//        }
//
//        // Check user status
//        if (!user.isActive()) {
//            throw new InvalidCredentialsException("Account is not active: " + user.getStatus());
//        }
//
//        // Update last login
//        user.updateLastLogin();
//        userRepository.save(user);
//
//        // Generate authentication token
//        String token = securityService.generateAuthToken(user);
//
//        log.info("User authenticated successfully: {}", user.getId());
//
//        return new AuthenticationResult(true, user, token, "Authentication successful");
//    }
//
//    @Override
//    public User getUserProfile(String userId) {
//        return userRepository.findById(userId)
//                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
//    }
//
//    @Override
//    public User updateUserProfile(String userId, UserProfileUpdateRequest request) {
//        log.info("Updating profile for user: {}", userId);
//
//        User user = getUserProfile(userId);
//
//        // Update allowed fields
//        if (request.firstName() != null && !request.firstName().trim().isEmpty()) {
//            user.setFirstName(request.firstName().trim());
//        }
//
//        if (request.lastName() != null && !request.lastName().trim().isEmpty()) {
//            user.setLastName(request.lastName().trim());
//        }
//
//        if (request.phoneNumber() != null && !request.phoneNumber().trim().isEmpty()) {
//            user.setPhoneNumber(request.phoneNumber().trim());
//        }
//
//        if (request.address() != null && !request.address().trim().isEmpty()) {
//            user.setAddress(request.address().trim());
//        }
//
//        user.setUpdatedAt(LocalDateTime.now());
//
//        User updatedUser = userRepository.save(user);
//        log.info("Profile updated successfully for user: {}", userId);
//
//        return updatedUser;
//    }
//
//    @Override
//    public void activateUser(String userId) {
//        log.info("Activating user: {}", userId);
//
//        User user = getUserProfile(userId);
//        user.activate();
//
//        userRepository.save(user);
//        log.info("User activated successfully: {}", userId);
//    }
//
//    @Override
//    public void suspendUser(String userId, String reason) {
//        log.warn("Suspending user: {} for reason: {}", userId, reason);
//
//        User user = getUserProfile(userId);
//        user.suspend();
//
//        userRepository.save(user);
//        log.info("User suspended successfully: {}", userId);
//    }
//
//    private void validateRegistrationRequest(UserRegistrationRequest request) {
//        if (request.username() == null || request.username().trim().length() < 3) {
//            throw new IllegalArgumentException("Username must be at least 3 characters long");
//        }
//
//        if (request.email() == null || !isValidEmail(request.email())) {
//            throw new IllegalArgumentException("Valid email address is required");
//        }
//
//        validatePassword(request.password());
//
//        if (request.firstName() == null || request.firstName().trim().isEmpty()) {
//            throw new IllegalArgumentException("First name is required");
//        }
//
//        if (request.lastName() == null || request.lastName().trim().isEmpty()) {
//            throw new IllegalArgumentException("Last name is required");
//        }
//
//        if (request.nationalId() == null || request.nationalId().trim().isEmpty()) {
//            throw new IllegalArgumentException("National ID is required for KYC compliance");
//        }
//    }
//
//    private void validatePassword(String password) {
//        if (password == null || password.length() < 8) {
//            throw new IllegalArgumentException("Password must be at least 8 characters long");
//        }
//
//        if (!password.matches(".*[A-Z].*")) {
//            throw new IllegalArgumentException("Password must contain at least one uppercase letter");
//        }
//
//        if (!password.matches(".*[a-z].*")) {
//            throw new IllegalArgumentException("Password must contain at least one lowercase letter");
//        }
//
//        if (!password.matches(".*[0-9].*")) {
//            throw new IllegalArgumentException("Password must contain at least one digit");
//        }
//    }
//
//    private boolean isValidEmail(String email) {
//        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
//    }
//}