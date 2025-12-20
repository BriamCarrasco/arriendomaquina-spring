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

    private Constants() {
    }

    public static final String REDIRECT_USERS = "redirect:/api/admin/users";
    public static final String LOGIN_URL = "/login";
    public static final String HEADER_AUTHORIZACION_KEY = "Authorization";
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";
    public static final String ISSUER_INFO = "https://www.duocuc.cl/";
    public static final long TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24; // 24h
    public static final String DUPLICATE_MEDIA_ERROR = "duplicate_media";
    public static final String ERROR_KEY = "error";
    public static final String MESSAGE_KEY = "message";
    public static final String UNIQUE_CONSTRAINT_ERROR_MSG = "No se puede agregar media: posible restricción única en la base de datos";

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