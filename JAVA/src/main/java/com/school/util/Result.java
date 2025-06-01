package com.school.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Unified response format for API results
 */
public class Result {
    // Result status codes
    public static final int SUCCESS = 200;
    public static final int ERROR = 500;
    public static final int NOT_FOUND = 404;
    public static final int UNAUTHORIZED = 401;
    
    private int code;           // Status code
    private String message;     // Message
    private Object data;        // Result data
    
    public Result() {
    }
    
    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    /**
     * Success with data
     * @param data Result data
     * @return Result object
     */
    public static Result success(Object data) {
        return new Result(SUCCESS, "操作成功", data);
    }
    
    /**
     * Success with message
     * @param message Success message
     * @return Result object
     */
    public static Result success(String message) {
        return new Result(SUCCESS, message);
    }
    
    /**
     * Success with message and data
     * @param message Success message
     * @param data Result data
     * @return Result object
     */
    public static Result success(String message, Object data) {
        return new Result(SUCCESS, message, data);
    }
    
    /**
     * Error with message
     * @param message Error message
     * @return Result object
     */
    public static Result error(String message) {
        return new Result(ERROR, message);
    }
    
    /**
     * Error with custom code and message
     * @param code Error code
     * @param message Error message
     * @return Result object
     */
    public static Result error(int code, String message) {
        return new Result(code, message);
    }
    
    /**
     * Convert Result to Map
     * @return Map representation of Result
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("message", message);
        if (data != null) {
            map.put("data", data);
        }
        return map;
    }
    
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
} 