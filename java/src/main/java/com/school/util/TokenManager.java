package com.school.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 用户认证令牌管理器
 * 此类提供了生成、验证、移除令牌的方法，用于用户认证。
 */
public class TokenManager {
    // 存储令牌与用户名的映射关系
    private static Map<String, String> tokenMap = new HashMap<>();

    // 存储用户名与令牌的映射关系
    private static Map<String, String> userTokenMap = new HashMap<>();

    // 令牌过期时间（毫秒），默认设置为2小时
    private static final long TOKEN_EXPIRY = 2 * 60 * 60 * 1000;

    // 存储令牌与其过期时间的映射关系
    private static Map<String, Long> tokenExpiry = new HashMap<>();

    /**
     * 为用户生成新的令牌
     * @param username 用户名
     * @return 生成的令牌
     */
    public static synchronized String generateToken(String username) {
        // 如果用户已有一个令牌，先使其失效
        if (userTokenMap.containsKey(username)) {
            String oldToken = userTokenMap.get(username);
            tokenMap.remove(oldToken);
            tokenExpiry.remove(oldToken);
        }

        // 生成新的令牌，使用UUID并移除其中的"-"字符
        String token = UUID.randomUUID().toString().replace("-", "");

        // 存储令牌及其关联的用户信息
        tokenMap.put(token, username);
        userTokenMap.put(username, token);
        tokenExpiry.put(token, System.currentTimeMillis() + TOKEN_EXPIRY);

        return token;
    }

    /**
     * 验证令牌的有效性
     * @param token 要验证的令牌
     * @return 如果令牌有效，返回关联的用户名；否则返回null
     */
    public static synchronized String validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }

        // 检查令牌是否存在
        if (!tokenMap.containsKey(token)) {
            return null;
        }

        // 检查令牌是否已过期
        Long expiryTime = tokenExpiry.get(token);
        if (expiryTime == null || System.currentTimeMillis() > expiryTime) {
            // 令牌已过期，移除它
            removeToken(token);
            return null;
        }

        // 令牌有效，返回关联的用户名
        return tokenMap.get(token);
    }

    /**
     * 移除令牌
     * @param token 要移除的令牌
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
     * 通过用户名移除用户的令牌
     * @param username 用户名
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