package com.strata.xmlDocument.infrastructure.adapter.output.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
@Slf4j
public class HttpSender {


    private static final HttpClient client = HttpClient.newHttpClient();


    public static void sendXML(String encryptData, String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/xml")
                    .POST(HttpRequest.BodyPublishers.ofString(encryptData))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.discarding());
            log.info("Message sent successfully");
        } catch (Exception e) {
            log.error("Error sending message: {}", e.getMessage());
        }
    }
}

