package com.cse308.sbuify.security;

/**
 * Security-related constants.
 */
public class SecurityConstants {
    public static final String LOGIN_URL = "/api/login";
    public static final String SIGN_UP_URL = "/api/users";
    public static final String RESET_URL = "/api/reset-password";
    public static final String CHANGE_PASS_URL = "/api/change-password";
    public static final String ADMIN_PATTERN = "/api/admins/**";
    public static final String CUSTOMER_PATTERN = "/api/customer/**";
    public static final String CRON_PATTERN = "/api/cron/**";
    public static final String IMAGE_PATTERN = "/static/i/**";
    public static final String SECRET = "70733b8a257ec1b86ac59f4dfd82309e";
    public static final String HEADER_NAME = "Authorization";
    public static final String HEADER_PREFIX = "Bearer ";
}
