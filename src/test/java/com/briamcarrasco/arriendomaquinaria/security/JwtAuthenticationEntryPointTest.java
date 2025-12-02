package com.briamcarrasco.arriendomaquinaria.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class JwtAuthenticationEntryPointTest {

    private final JwtAuthenticationEntryPoint entryPoint = new JwtAuthenticationEntryPoint();
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void commence_withTokenExpired_returnsExpiredJson() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/expired");
        request.setAttribute("jwt_error", "TOKEN_EXPIRED");

        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authEx = mock(AuthenticationException.class);

        entryPoint.commence(request, response, authEx);

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getContentType()).isEqualTo("application/json;charset=UTF-8");

        Map<?, ?> body = mapper.readValue(response.getContentAsString(), Map.class);
        assertThat(((Number) body.get("status")).intValue()).isEqualTo(401);
        assertThat(body.get("error")).isEqualTo("Unauthorized");
        assertThat(body.get("code")).isEqualTo("TOKEN_EXPIRED");
        assertThat(body.get("message")).isEqualTo("El token ha expirado");
        assertThat(body.get("path")).isEqualTo("/api/expired");
        assertThat(body.get("timestamp")).isNotNull();
        Instant.parse((String) body.get("timestamp"));
    }

    @Test
    void commence_withTokenInvalid_returnsInvalidJson() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/invalid");
        request.setAttribute("jwt_error", "TOKEN_INVALID");

        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authEx = mock(AuthenticationException.class);

        entryPoint.commence(request, response, authEx);

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getContentType()).isEqualTo("application/json;charset=UTF-8");

        Map<?, ?> body = mapper.readValue(response.getContentAsString(), Map.class);
        assertThat(((Number) body.get("status")).intValue()).isEqualTo(401);
        assertThat(body.get("error")).isEqualTo("Unauthorized");
        assertThat(body.get("code")).isEqualTo("TOKEN_INVALID");
        assertThat(body.get("message")).isEqualTo("El token es inválido");
        assertThat(body.get("path")).isEqualTo("/api/invalid");
        assertThat(body.get("timestamp")).isNotNull();
        Instant.parse((String) body.get("timestamp"));
    }

    @Test
    void commence_withNoJwtError_returnsUnauthorizedJson() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/no-token");
        // no setAttribute for "jwt_error" -> default branch

        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authEx = mock(AuthenticationException.class);

        entryPoint.commence(request, response, authEx);

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getContentType()).isEqualTo("application/json;charset=UTF-8");

        Map<?, ?> body = mapper.readValue(response.getContentAsString(), Map.class);
        assertThat(((Number) body.get("status")).intValue()).isEqualTo(401);
        assertThat(body.get("error")).isEqualTo("Unauthorized");
        assertThat(body.get("code")).isEqualTo("UNAUTHORIZED");
        assertThat(body.get("message")).isEqualTo("No autenticado o token ausente");
        assertThat(body.get("path")).isEqualTo("/api/no-token");
        assertThat(body.get("timestamp")).isNotNull();
        Instant.parse((String) body.get("timestamp"));
    }
}