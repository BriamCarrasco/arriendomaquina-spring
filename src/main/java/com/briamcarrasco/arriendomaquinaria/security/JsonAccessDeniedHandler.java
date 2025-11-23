package com.briamcarrasco.arriendomaquinaria.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler personalizado para respuestas 403 (Forbidden) con cuerpo JSON
 * uniforme.
 * 
 * Devuelve un JSON estructurado cuando el acceso es denegado por falta de
 * permisos.
 */
@Component
public class JsonAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Maneja las respuestas de acceso denegado devolviendo un JSON con información
     * del error.
     *
     * @param request               solicitud HTTP recibida
     * @param response              respuesta HTTP a enviar
     * @param accessDeniedException excepción de acceso denegado
     * @throws IOException      si ocurre un error de entrada/salida
     * @throws ServletException si ocurre un error en el servlet
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", 403);
        body.put("error", "Forbidden");
        body.put("code", "ACCESS_DENIED");
        body.put("message",
                accessDeniedException.getMessage() != null ? accessDeniedException.getMessage() : "Acceso denegado");
        body.put("path", request.getRequestURI());

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        mapper.writeValue(response.getOutputStream(), body);
    }
}