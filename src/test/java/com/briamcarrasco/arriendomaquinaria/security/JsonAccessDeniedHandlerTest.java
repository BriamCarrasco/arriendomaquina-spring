package com.briamcarrasco.arriendomaquinaria.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class JsonAccessDeniedHandlerTest {

    @InjectMocks
    private JsonAccessDeniedHandler handler;

    @Test
    void handle_writesJsonResponseWith403Status() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AccessDeniedException exception = new AccessDeniedException("No tienes permiso");

        when(request.getRequestURI()).thenReturn("/api/admin/resource");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ServletOutputStream sos = new ServletOutputStream() {
            @Override
            public void write(int b) {
                baos.write(b);
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(jakarta.servlet.WriteListener writeListener) {
                // No implementation needed for test
            }
        };
        when(response.getOutputStream()).thenReturn(sos);

        handler.handle(request, response, exception);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response).setContentType("application/json;charset=UTF-8");

        String json = baos.toString("UTF-8");
        assertNotNull(json);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> body = mapper.readValue(json, Map.class);

        assertEquals(403, body.get("status"));
        assertEquals("Forbidden", body.get("error"));
        assertEquals("ACCESS_DENIED", body.get("code"));
        assertEquals("No tienes permiso", body.get("message"));
        assertEquals("/api/admin/resource", body.get("path"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handle_whenExceptionMessageIsNull_usesDefaultMessage() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AccessDeniedException exception = new AccessDeniedException(null);

        when(request.getRequestURI()).thenReturn("/api/test");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ServletOutputStream sos = new ServletOutputStream() {
            @Override
            public void write(int b) {
                baos.write(b);
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(jakarta.servlet.WriteListener writeListener) {
                // No implementation needed for test
            }
        };
        when(response.getOutputStream()).thenReturn(sos);

        handler.handle(request, response, exception);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response).setContentType("application/json;charset=UTF-8");

        String json = baos.toString("UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> body = mapper.readValue(json, Map.class);

        assertEquals("Acceso denegado", body.get("message"));
    }
}