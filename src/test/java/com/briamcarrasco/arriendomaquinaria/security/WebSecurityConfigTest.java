package com.briamcarrasco.arriendomaquinaria.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.briamcarrasco.arriendomaquinaria.jwt.JWTAuthorizationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.http.ResponseCookie;

class WebSecurityConfigTest {

    @Test
    void passwordEncoder_returnsBCryptPasswordEncoder() {
        WebSecurityConfig config = new WebSecurityConfig(
                mock(JWTAuthorizationFilter.class),
                mock(JwtAuthenticationEntryPoint.class),
                mock(JsonAccessDeniedHandler.class));
        PasswordEncoder encoder = config.passwordEncoder();
        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);
        assertNotEquals("1234", encoder.encode("1234")); 
    }

    @Test
    void cookieCsrfTokenRepository_returnsConfiguredInstance() {
        WebSecurityConfig config = new WebSecurityConfig(
                mock(JWTAuthorizationFilter.class),
                mock(JwtAuthenticationEntryPoint.class),
                mock(JsonAccessDeniedHandler.class));
        CookieCsrfTokenRepository repo = config.cookieCsrfTokenRepository();
        assertNotNull(repo);
        // El path puede ser null en test, solo verifica la instancia
        assertTrue(repo instanceof CookieCsrfTokenRepository);
    }

    @Test
    void securityFilterChain_configuresHttpSecurity() {
        WebSecurityConfig config = new WebSecurityConfig(
                mock(JWTAuthorizationFilter.class),
                mock(JwtAuthenticationEntryPoint.class),
                mock(JsonAccessDeniedHandler.class));
        HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);

        assertDoesNotThrow(() -> config.securityFilterChain(http));
    }

    @Test
    void cookieCsrfTokenRepository_cookieCustomizerIsApplied() throws Exception {
        WebSecurityConfig config = new WebSecurityConfig(
                mock(JWTAuthorizationFilter.class),
                mock(JwtAuthenticationEntryPoint.class),
                mock(JsonAccessDeniedHandler.class));
        CookieCsrfTokenRepository repo = config.cookieCsrfTokenRepository();

        // Obtener el customizer por reflexión (sin hacer cast genérico)
        java.lang.reflect.Field field = CookieCsrfTokenRepository.class.getDeclaredField("cookieCustomizer");
        field.setAccessible(true);
        Object customizerObj = field.get(repo);

        // Llamar al método accept(Object) por reflexión evitando unchecked cast
        java.lang.reflect.Method acceptMethod = customizerObj.getClass().getMethod("accept", Object.class);

        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from("csrf", "token");
        acceptMethod.invoke(customizerObj, builder); // ejecuta el lambda sobre el builder

        ResponseCookie cookie = builder.build();
        assertEquals("Lax", cookie.getSameSite());
        assertFalse(cookie.isHttpOnly());
        assertFalse(cookie.isSecure());
        assertEquals("/", cookie.getPath());
    }
}