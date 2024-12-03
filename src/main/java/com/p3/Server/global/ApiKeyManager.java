package com.p3.Server.global;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ApiKeyManager {
    private final Map<String, String> apiKeys = new ConcurrentHashMap<>();

    public String generateApiKey(String username) {
        String apiKey = UUID.randomUUID().toString();
        apiKeys.put(username, apiKey);
        return apiKey;
    }

    public boolean isValidApiKey(String apiKey) {
        return apiKeys.containsValue(apiKey);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void clearKeys() {
        apiKeys.clear();
    }
}