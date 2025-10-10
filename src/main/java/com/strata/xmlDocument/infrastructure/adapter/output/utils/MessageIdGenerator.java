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
        if (sourceId == null || sourceId.isBlank()) {
            throw new IllegalArgumentException("sourceId cannot be null or empty");
        }

        String datePart = LocalDateTime.now().format(FORMATTER);
        String randomDigits = generateRandomDigits(15);

        String msgId = sourceId + datePart + randomDigits;

        if (msgId.length() != 35) {
            throw new IllegalStateException(
                    "Generated MsgId length is not 35 characters. Got: " + msgId.length() + " (" + msgId + ")"
            );
        }

        return msgId;
    }

    private static String generateRandomDigits(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }



    public static void main(String[] args) {
        String sourceId = "999058";
        String msgId = generateMessageId(sourceId);
       log.info("Generated MsgId: {} ",  msgId);
    }
}
