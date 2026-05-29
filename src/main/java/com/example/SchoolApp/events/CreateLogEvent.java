package com.example.SchoolApp.events;

public record CreateLogEvent(
        String username,
        String requestUri,
        Boolean authHeader,
        String requestBody,
        int responseStatus,
        String requestMethod,
        String remoteIp,
        String remoteHost,
        String remotePort,
        String originDomain,
        String responseBody,
        boolean success
) {
}
