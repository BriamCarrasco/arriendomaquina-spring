package com.briamcarrasco.arriendomaquinaria.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * AuthenticationEntryPoint personalizado para devolver siempre un JSON uniforme
 * en respuestas 401 (Unauthorized) cuando el token está ausente, inválido o
 * expirado.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Maneja las respuestas de autenticación fallida devolviendo un JSON con
     * información del error.
     *
     * @param request       solicitud HTTP recibida
     * @param response      respuesta HTTP a enviar
     * @param authException excepción de autenticación
     * @throws IOException      si ocurre un error de entrada/salida
     * @throws ServletException si ocurre un error en el servlet
     */
    @Override
    public void commence(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        String reason = (String) request.getAttribute("jwt_error");
        String code;
        String message;
        if ("TOKEN_EXPIRED".equals(reason)) {
            code = "TOKEN_EXPIRED";
            message = "El token ha expirado";
        } else if ("TOKEN_INVALID".equals(reason)) {
            code = "TOKEN_INVALID";
            message = "El token es inválido";
        } else {
            code = "UNAUTHORIZED";
            message = "No autenticado o token ausente";
        }

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", 401);
        body.put("error", "Unauthorized");
        body.put("code", code);
        body.put("message", message);
        body.put("path", request.getRequestURI());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        mapper.writeValue(response.getOutputStream(), body);
    }
}