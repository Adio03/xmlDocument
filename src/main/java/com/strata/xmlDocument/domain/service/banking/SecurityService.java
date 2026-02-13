//package com.strata.xmlDocument.domain.service.banking;
//
//import com.strata.xmlDocument.domain.model.banking.User;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.security.Key;
//import java.security.MessageDigest;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.Base64;
//import java.util.Date;
//import java.util.UUID;
//
///**
// * Domain service for security operations
// * Handles JWT tokens and security utilities for banking
// */
//@Service
//@Slf4j
//public class SecurityService {
//
//    private final Key jwtKey;
//    private final long jwtExpiration;
//
//    public SecurityService(
//            @Value("${banking.security.jwt.secret:your-very-secure-secret-key-for-jwt-tokens}") String secret,
//            @Value("${banking.security.jwt.expiration:86400000}") long expiration) {
//        this.jwtKey = Keys.hmacShaKeyFor(secret.getBytes());
//        this.jwtExpiration = expiration;
//    }
//
//    /**
//     * Generate JWT authentication token for user
//     */
//    public String generateAuthToken(User user) {
//        Date now = new Date();
//        Date expiry = new Date(now.getTime() + jwtExpiration);
//
//        return Jwts.builder()
//                .setSubject(user.getId())
//                .claim("username", user.getUsername())
//                .claim("email", user.getEmail())
//                .claim("firstName", user.getFirstName())
//                .claim("lastName", user.getLastName())
//                .setIssuedAt(now)
//                .setExpiration(expiry)
//                .signWith(jwtKey, SignatureAlgorithm.HS512)
//                .compact();
//    }
//
//    /**
//     * Validate JWT authentication token
//     */
//    public boolean validateAuthToken(String token) {
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(jwtKey)
//                    .build()
//                    .parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            log.error("Invalid JWT token: {}", e.getMessage());
//            return false;
//        }
//    }
//
//    /**
//     * Extract user ID from JWT token
//     */
//    public String extractUserIdFromToken(String token) {
//        try {
//            Claims claims = Jwts.parserBuilder()
//                    .setSigningKey(jwtKey)
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();
//
//            return claims.getSubject();
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Invalid token format", e);
//        }
//    }
//
//    /**
//     * Extract username from JWT token
//     */
//    public String extractUsernameFromToken(String token) {
//        try {
//            Claims claims = Jwts.parserBuilder()
//                    .setSigningKey(jwtKey)
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();
//
//            return claims.get("username", String.class);
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Invalid token format", e);
//        }
//    }
//
//    /**
//     * Generate secure account number
//     */
//    public String generateSecureAccountNumber(String userId, String accountType) {
//        String data = userId + accountType + System.currentTimeMillis();
//        String hash = generateSHA256Hash(data);
//
//        // Take first 12 characters and format as account number
//        String accountNumber = hash.substring(0, 12).toUpperCase();
//
//        // Format: XXXX-XXXX-XXXX
//        return String.format("%s-%s-%s",
//                accountNumber.substring(0, 4),
//                accountNumber.substring(4, 8),
//                accountNumber.substring(8, 12));
//    }
//
//    /**
//     * Generate transaction reference number
//     */
//    public String generateTransactionReference(String transactionType) {
//        String timestamp = String.valueOf(System.currentTimeMillis());
//        String data = transactionType + timestamp + UUID.randomUUID().toString();
//        String hash = generateSHA256Hash(data);
//
//        // Format: TYPE-XXXXXX
//        return transactionType.toUpperCase() + "-" + hash.substring(0, 8).toUpperCase();
//    }
//
//    /**
//     * Mask sensitive information for logging
//     */
//    public String maskAccountNumber(String accountNumber) {
//        if (accountNumber == null || accountNumber.length() < 4) {
//            return "****";
//        }
//
//        String lastFour = accountNumber.substring(accountNumber.length() - 4);
//        return "****-****-" + lastFour;
//    }
//
//    /**
//     * Mask email for logging
//     */
//    public String maskEmail(String email) {
//        if (email == null || !email.contains("@")) {
//            return "****@****";
//        }
//
//        String[] parts = email.split("@");
//        String localPart = parts[0];
//        String domain = parts[1];
//
//        if (localPart.length() <= 2) {
//            return "**@" + domain;
//        }
//
//        return localPart.substring(0, 2) + "**@" + domain;
//    }
//
//    /**
//     * Generate SHA256 hash
//     */
//    private String generateSHA256Hash(String data) {
//        try {
//            MessageDigest digest = MessageDigest.getInstance("SHA-256");
//            byte[] hash = digest.digest(data.getBytes("UTF-8"));
//            return Base64.getEncoder().encodeToString(hash);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to generate hash", e);
//        }
//    }
//}