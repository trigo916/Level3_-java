package com.school.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Token manager for user authentication
 */
public class TokenManager {
    // Token storage: token -> username
    private static Map<String, String> tokenMap = new HashMap<>();
    
    // Reverse lookup: username -> token
    private static Map<String, String> userTokenMap = new HashMap<>();
    
    // Token expiration time in milliseconds (2 hours)
    private static final long TOKEN_EXPIRY = 2 * 60 * 60 * 1000;
    
    // Token expiry time storage: token -> expiry time
    private static Map<String, Long> tokenExpiry = new HashMap<>();
    
    /**
     * Generate a new token for a user
     * @param username User's username
     * @return Generated token
     */
    public static synchronized String generateToken(String username) {
        // If user already has a token, invalidate it
        if (userTokenMap.containsKey(username)) {
            String oldToken = userTokenMap.get(username);
            tokenMap.remove(oldToken);
            tokenExpiry.remove(oldToken);
        }
        
        // Generate new token
        String token = UUID.randomUUID().toString().replace("-", "");
        
        // Store token with user info
        tokenMap.put(token, username);
        userTokenMap.put(username, token);
        tokenExpiry.put(token, System.currentTimeMillis() + TOKEN_EXPIRY);
        
        return token;
    }
    
    /**
     * Validate a token
     * @param token Token to validate
     * @return Username if valid, null otherwise
     */
    public static synchronized String validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        
        // Check if token exists
        if (!tokenMap.containsKey(token)) {
            return null;
        }
        
        // Check if token expired
        Long expiryTime = tokenExpiry.get(token);
        if (expiryTime == null || System.currentTimeMillis() > expiryTime) {
            // Token expired, remove it
            removeToken(token);
            return null;
        }
        
        // Token valid, return username
        return tokenMap.get(token);
    }
    
    /**
     * Remove a token
     * @param token Token to remove
     */
    public static synchronized void removeToken(String token) {
        if (token != null && tokenMap.containsKey(token)) {
            String username = tokenMap.get(token);
            tokenMap.remove(token);
            userTokenMap.remove(username);
            tokenExpiry.remove(token);
        }
    }
    
    /**
     * Remove a user's token by username
     * @param username Username
     */
    public static synchronized void removeUserToken(String username) {
        if (username != null && userTokenMap.containsKey(username)) {
            String token = userTokenMap.get(username);
            userTokenMap.remove(username);
            tokenMap.remove(token);
            tokenExpiry.remove(token);
        }
    }
} 