package com.p3.Server.global;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Autowired
    private ApiKeyManager apiKeyManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestUri = request.getRequestURI();

        // Whitelist endpoints that do not require API-Key
        if (requestUri.equals("/api/time/getTime") || requestUri.startsWith("/api/user/apiKey/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader("API-Key");
        if (apiKey == null || !apiKeyManager.isValidApiKey(apiKey)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API Key");
            return;
        }
        filterChain.doFilter(request, response);
    }

}