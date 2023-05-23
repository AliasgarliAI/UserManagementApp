package com.company.constant;

public class SecurityConstants {
    public static final long TOKEN_EXPIRATION_TIME = 432_000_000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String TOKEN_ISSUER = "Alakbar Aliasgarli";
    public static final String TOKEN_SOURCE = "User Management Portal";
    public static final String AUTHORITIES = "authorities";
    public static final String ROLES = "roles";
    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = {"/login", "/register/**","/reset" ,"/reset/**", "/user/image/**", };

    public static final String[] SWAGGER_PUBLIC_URLS = {"/swagger-ui/**","/v3/api-docs/**"};

    private SecurityConstants(){}
}
