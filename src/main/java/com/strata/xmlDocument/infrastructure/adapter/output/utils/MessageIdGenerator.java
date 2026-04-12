package com.strata.xmlDocument.infrastructure.adapter.output.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class MessageIdGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static String generateMessageId(String sourceId) {
        return generate35CharId(sourceId);
    }

    public static String generateTxId(String sourceId) {
        return generate35CharId(sourceId);
    }

    public static String generateEndToEndId(String sourceId) {
        if (sourceId == null || sourceId.isBlank()) {
            throw new IllegalArgumentException("sourceId cannot be null or empty");
        }
        String randomDigits = generateRandomDigits(35 - sourceId.length());
        return sourceId + randomDigits;
    }

    public static String generateInstrId(String sourceId, String destId) {
        if (sourceId == null || sourceId.isBlank() || destId == null || destId.isBlank()) {
            throw new IllegalArgumentException("sourceId and destId cannot be null or empty");
        }
        String datePart = LocalDateTime.now().format(FORMATTER);
        // Source(6) + Dest(6) + Date(14) = 26. Need 9 more for 35.
        String randomDigits = generateRandomDigits(9);
        String instrId = sourceId + destId + datePart + randomDigits;
        
        if (instrId.length() > 35) {
            instrId = instrId.substring(0, 35);
        } else if (instrId.length() < 35) {
            instrId = instrId + generateRandomDigits(35 - instrId.length());
        }
        
        return instrId;
    }

    private static String generate35CharId(String sourceId) {
        if (sourceId == null || sourceId.isBlank()) {
            throw new IllegalArgumentException("sourceId cannot be null or empty");
        }

        String datePart = LocalDateTime.now().format(FORMATTER);
        int remaining = 35 - sourceId.length() - datePart.length();
        String randomDigits = generateRandomDigits(Math.max(0, remaining));

        String id = sourceId + datePart + randomDigits;

        if (id.length() > 35) {
            id = id.substring(0, 35);
        } else if (id.length() < 35) {
            id = id + generateRandomDigits(35 - id.length());
        }

        return id;
    }

    private static String generateRandomDigits(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }
}
