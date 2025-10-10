package com.strata.xmlDocument.infrastructure.adapter.output.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
            System.out.println("Message sent successfully");
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }
}

