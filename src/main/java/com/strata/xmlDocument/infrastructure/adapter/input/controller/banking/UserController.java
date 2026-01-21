//package com.strata.xmlDocument.infrastructure.adapter.input.controller.banking;
//
//import com.strata.xmlDocument.application.input.banking.UserManagementUseCase;
//import com.strata.xmlDocument.application.input.banking.UserManagementUseCase.*;
//import com.strata.xmlDocument.domain.model.banking.User;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Size;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
///**
// * REST controller for user management operations
// * Provides HTTP endpoints for user registration, authentication, and profile management
// */
//@RestController
//@RequestMapping("/api/v1/banking/users")
//@RequiredArgsConstructor
//@Slf4j
//@Tag(name = "Banking - User Management", description = "User registration, authentication, and profile management")
//public class UserController {
//
//    private final UserManagementUseCase userManagementUseCase;
//
//    @PostMapping("/register")
//    @Operation(summary = "Register new user", description = "Register a new user in the banking system")
//    public ResponseEntity<UserRegistrationResponse> registerUser(
//            @Valid @RequestBody UserRegistrationDto request) {
//
//        log.info("User registration request for username: {}", request.username());
//
//        UserRegistrationRequest useCaseRequest = new UserRegistrationRequest(
//                request.username(),
//                request.email(),
//                request.password(),
//                request.firstName(),
//                request.lastName(),
//                request.phoneNumber(),
//                request.address(),
//                request.nationalId()
//        );
//
//        User user = userManagementUseCase.registerUser(useCaseRequest);
//
//        UserRegistrationResponse response = new UserRegistrationResponse(
//                user.getId(),
//                user.getUsername(),
//                user.getEmail(),
//                user.getStatus().toString(),
//                "User registered successfully. Account pending verification."
//        );
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//
//    @PostMapping("/login")
//    @Operation(summary = "User login", description = "Authenticate user and return access token")
//    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDto request) {
//
//        log.info("Login request for user: {}", request.usernameOrEmail());
//
//        UserLoginRequest useCaseRequest = new UserLoginRequest(
//                request.usernameOrEmail(),
//                request.password()
//        );
//
//        AuthenticationResult authResult = userManagementUseCase.authenticateUser(useCaseRequest);
//
//        LoginResponse response = new LoginResponse(
//                authResult.success(),
//                authResult.token(),
//                authResult.user().getId(),
//                authResult.user().getUsername(),
//                authResult.user().getFirstName() + " " + authResult.user().getLastName(),
//                authResult.message()
//        );
//
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/{userId}/profile")
//    @Operation(summary = "Get user profile", description = "Retrieve user profile information")
//    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable String userId) {
//
//        log.info("Profile request for user: {}", userId);
//
//        User user = userManagementUseCase.getUserProfile(userId);
//
//        UserProfileResponse response = new UserProfileResponse(
//                user.getId(),
//                user.getUsername(),
//                user.getEmail(),
//                user.getFirstName(),
//                user.getLastName(),
//                user.getPhoneNumber(),
//                user.getAddress(),
//                user.getStatus().toString(),
//                user.getCreatedAt(),
//                user.getLastLoginAt()
//        );
//
//        return ResponseEntity.ok(response);
//    }
//
//    @PutMapping("/{userId}/profile")
//    @Operation(summary = "Update user profile", description = "Update user profile information")
//    public ResponseEntity<UserProfileResponse> updateUserProfile(
//            @PathVariable String userId,
//            @Valid @RequestBody UserProfileUpdateDto request) {
//
//        log.info("Profile update request for user: {}", userId);
//
//        UserProfileUpdateRequest useCaseRequest = new UserProfileUpdateRequest(
//                request.firstName(),
//                request.lastName(),
//                request.phoneNumber(),
//                request.address()
//        );
//
//        User user = userManagementUseCase.updateUserProfile(userId, useCaseRequest);
//
//        UserProfileResponse response = new UserProfileResponse(
//                user.getId(),
//                user.getUsername(),
//                user.getEmail(),
//                user.getFirstName(),
//                user.getLastName(),
//                user.getPhoneNumber(),
//                user.getAddress(),
//                user.getStatus().toString(),
//                user.getCreatedAt(),
//                user.getLastLoginAt()
//        );
//
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/{userId}/activate")
//    @Operation(summary = "Activate user", description = "Activate user account")
//    public ResponseEntity<ApiMessageResponse> activateUser(@PathVariable String userId) {
//
//        log.info("User activation request for user: {}", userId);
//
//        userManagementUseCase.activateUser(userId);
//
//        ApiMessageResponse response = new ApiMessageResponse(
//                "User activated successfully"
//        );
//
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/{userId}/suspend")
//    @Operation(summary = "Suspend user", description = "Suspend user account")
//    public ResponseEntity<ApiMessageResponse> suspendUser(
//            @PathVariable String userId,
//            @RequestBody SuspendUserRequest request) {
//
//        log.warn("User suspension request for user: {} - reason: {}", userId, request.reason());
//
//        userManagementUseCase.suspendUser(userId, request.reason());
//
//        ApiMessageResponse response = new ApiMessageResponse(
//                "User suspended successfully"
//        );
//
//        return ResponseEntity.ok(response);
//    }
//
//    // DTOs for request validation
//    public record UserRegistrationDto(
//            @NotBlank @Size(min = 3, max = 50) String username,
//            @NotBlank @Email String email,
//            @NotBlank @Size(min = 8) String password,
//            @NotBlank String firstName,
//            @NotBlank String lastName,
//            @NotBlank String phoneNumber,
//            @NotBlank String address,
//            @NotBlank String nationalId
//    ) {}
//
//    public record UserLoginDto(
//            @NotBlank String usernameOrEmail,
//            @NotBlank String password
//    ) {}
//
//    public record UserProfileUpdateDto(
//            String firstName,
//            String lastName,
//            String phoneNumber,
//            String address
//    ) {}
//
//    public record SuspendUserRequest(
//            @NotBlank String reason
//    ) {}
//
//    // Response DTOs
//    public record UserRegistrationResponse(
//            String userId,
//            String username,
//            String email,
//            String status,
//            String message
//    ) {}
//
//    public record LoginResponse(
//            boolean success,
//            String token,
//            String userId,
//            String username,
//            String fullName,
//            String message
//    ) {}
//
//    public record UserProfileResponse(
//            String userId,
//            String username,
//            String email,
//            String firstName,
//            String lastName,
//            String phoneNumber,
//            String address,
//            String status,
//            java.time.LocalDateTime createdAt,
//            java.time.LocalDateTime lastLoginAt
//    ) {}
//
//    public record ApiMessageResponse(
//            String message
//    ) {}
//}