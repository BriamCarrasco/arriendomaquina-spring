package com.briamcarrasco.arriendomaquinaria.security;

import com.fasterxml.jackson.core.type.TypeReference;
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
    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {
    };

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

        Map<String, Object> body = mapper.readValue(response.getContentAsString(), MAP_TYPE);
        assertThat(body)
                .containsEntry("status", Integer.valueOf(401))
                .containsEntry("error", "Unauthorized")
                .containsEntry("code", "TOKEN_EXPIRED")
                .containsEntry("message", "El token ha expirado")
                .containsEntry("path", "/api/expired")
                .containsKey("timestamp");
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

        Map<String, Object> body = mapper.readValue(response.getContentAsString(), MAP_TYPE);
        assertThat(body)
                .containsEntry("status", Integer.valueOf(401))
                .containsEntry("error", "Unauthorized")
                .containsEntry("code", "TOKEN_INVALID")
                .containsEntry("message", "El token es inválido")
                .containsEntry("path", "/api/invalid")
                .containsKey("timestamp");
        Instant.parse((String) body.get("timestamp"));
    }

    @Test
    void commence_withNoJwtError_returnsUnauthorizedJson() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/no-token");

        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authEx = mock(AuthenticationException.class);

        entryPoint.commence(request, response, authEx);

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getContentType()).isEqualTo("application/json;charset=UTF-8");

        Map<String, Object> body = mapper.readValue(response.getContentAsString(), MAP_TYPE);
        assertThat(body)
                .containsEntry("status", Integer.valueOf(401))
                .containsEntry("error", "Unauthorized")
                .containsEntry("code", "UNAUTHORIZED")
                .containsEntry("message", "No autenticado o token ausente")
                .containsEntry("path", "/api/no-token")
                .containsKey("timestamp");
        Instant.parse((String) body.get("timestamp"));
    }
}