package com.briamcarrasco.arriendomaquinaria.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

/**
 * Clase que contiene constantes y utilidades para la configuración de JWT y
 * seguridad en el sistema.
 * Incluye rutas, claves, parámetros de expiración y métodos para obtener la
 * clave de firma.
 */
public class Constants {

    // Spring Security
    public static final String LOGIN_URL = "/login";
    public static final String HEADER_AUTHORIZACION_KEY = "Authorization";
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";

    // JWT
    public static final String ISSUER_INFO = "https://www.duocuc.cl/";
    public static final long TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24; // 24h
    public static final String SUPER_SECRET_KEY = getEnvSecret();

    /**
     * Obtiene la clave secreta JWT desde las variables de entorno.
     *
     * @return clave secreta en formato String
     * @throws IllegalStateException si la variable de entorno no está definida
     */
    private static String getEnvSecret() {
        String key = System.getenv("JWT_SECRET_KEY");
        if (key == null || key.isEmpty()) {
            throw new IllegalStateException("JWT_SECRET_KEY no está definida en las variables de entorno.");
        }
        return key;
    }

    /**
     * Decodifica la clave secreta en base64 y retorna el objeto SecretKey para
     * firmar JWT.
     *
     * @param base64 clave secreta en base64
     * @return objeto SecretKey para la firma
     */
    public static SecretKey getSigningKey(String base64) {
        byte[] keyBytes = Decoders.BASE64.decode(base64);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}