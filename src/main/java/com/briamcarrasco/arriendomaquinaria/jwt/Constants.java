package com.briamcarrasco.arriendomaquinaria.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class Constants {

    // Spring Security
    public static final String LOGIN_URL = "/login";
    public static final String HEADER_AUTHORIZACION_KEY = "Authorization";
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";

    // JWT
    public static final String ISSUER_INFO = "https://www.duocuc.cl/";
    public static final long TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24; // 24h
    public static final String SUPER_SECRET_KEY = "mySecretKey12345678901234567890123456789012";

    public static SecretKey getSigningKey(String base64) {
        byte[] keyBytes = Decoders.BASE64.decode(base64);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}