package com.briamcarrasco.arriendomaquinaria.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JWTAuthorizationFilterTest {


    private final JWTAuthorizationFilter filter = new JWTAuthorizationFilter();

    private Key signingKey() {
        return Constants.getSigningKey(Constants.JWT_SECRET_KEY);
    }

    private String buildToken(List<String> authorities, Instant exp) {
        var builder = Jwts.builder()
                .issuer(Constants.ISSUER_INFO)
                .subject("test-user")
                .expiration(Date.from(exp));
        if (authorities != null) {
            builder.claim("authorities", authorities);
        }
        return builder.signWith(signingKey()).compact();
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldNotFilter_variasRutas() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setServletPath("/login");
        assertThat(filter.shouldNotFilter(req)).isTrue();

        req.setServletPath("/auth/login");
        assertThat(filter.shouldNotFilter(req)).isTrue();

        req.setServletPath("/css/style.css");
        assertThat(filter.shouldNotFilter(req)).isTrue();

        req.setServletPath("/js/app.js");
        assertThat(filter.shouldNotFilter(req)).isTrue();

        req.setServletPath("/images/logo.png");
        assertThat(filter.shouldNotFilter(req)).isTrue();

        req.setServletPath("/api/machinery");
        assertThat(filter.shouldNotFilter(req)).isTrue();

        req.setServletPath("/api/machinery/123");
        assertThat(filter.shouldNotFilter(req)).isTrue();

        // Ruta que NO debe ser pública (comentario en código)
        req.setServletPath("/api/machinery-media");
        assertThat(filter.shouldNotFilter(req)).isFalse();

        // Ruta arbitraria protegida
        req.setServletPath("/api/other");
        assertThat(filter.shouldNotFilter(req)).isFalse();
    }

    @Test
    void doFilterInternal_tokenValidoConAuthorities_autentica() throws Exception {
        String token = buildToken(List.of("ROLE_USER", "ROLE_ADMIN"),
                Instant.now().plusSeconds(3600));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/protected");
        request.addHeader(Constants.HEADER_AUTHORIZACION_KEY, Constants.TOKEN_BEARER_PREFIX + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        CountingFilterChain chain = new CountingFilterChain();

        filter.doFilterInternal(request, response, chain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNotNull();
        assertThat(auth.getName()).isEqualTo("test-user");
        assertThat(auth.getAuthorities()).extracting(Object::toString)
                .containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
        assertThat(chain.count).isEqualTo(1);
    }

    @Test
    void doFilterInternal_tokenValidoSinAuthorities_contextLimpio() throws Exception {
        String token = buildToken(null, Instant.now().plusSeconds(3600));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/protected");
        request.addHeader(Constants.HEADER_AUTHORIZACION_KEY, Constants.TOKEN_BEARER_PREFIX + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        CountingFilterChain chain = new CountingFilterChain();

        filter.doFilterInternal(request, response, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        assertThat(chain.count).isEqualTo(1);
    }

    @Test
    void doFilterInternal_sinToken_contextLimpio() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/protected");
        MockHttpServletResponse response = new MockHttpServletResponse();
        CountingFilterChain chain = new CountingFilterChain();

        filter.doFilterInternal(request, response, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        assertThat(chain.count).isEqualTo(1);
        assertThat(request.getAttribute("jwt_error")).isNull();
    }

    @Test
    void doFilterInternal_tokenExpirado_setAttributeExpired() throws Exception {
        String expiredToken = buildToken(List.of("ROLE_USER"),
                Instant.now().minusSeconds(60));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/protected");
        request.addHeader(Constants.HEADER_AUTHORIZACION_KEY, Constants.TOKEN_BEARER_PREFIX + expiredToken);
        MockHttpServletResponse response = new MockHttpServletResponse();
        CountingFilterChain chain = new CountingFilterChain();

        filter.doFilterInternal(request, response, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        assertThat(request.getAttribute("jwt_error")).isEqualTo("TOKEN_EXPIRED");
        assertThat(chain.count).isEqualTo(1);
    }

    @Test
    void doFilterInternal_tokenInvalido_setAttributeInvalid() throws Exception {
        // Token malformado (no estructura JWT válida firmada)
        String invalidToken = "abc.def.ghi";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/protected");
        request.addHeader(Constants.HEADER_AUTHORIZACION_KEY, Constants.TOKEN_BEARER_PREFIX + invalidToken);
        MockHttpServletResponse response = new MockHttpServletResponse();
        CountingFilterChain chain = new CountingFilterChain();

        filter.doFilterInternal(request, response, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        assertThat(request.getAttribute("jwt_error")).isEqualTo("TOKEN_INVALID");
        assertThat(chain.count).isEqualTo(1);
    }

    @Test
    void doFilterInternal_tokenEnCookie_autentica() throws Exception {
        String token = buildToken(List.of("ROLE_USER"),
                Instant.now().plusSeconds(1800));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/protected");
        request.setCookies(new Cookie("jwt_token", token));
        MockHttpServletResponse response = new MockHttpServletResponse();
        CountingFilterChain chain = new CountingFilterChain();

        filter.doFilterInternal(request, response, chain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNotNull();
        assertThat(auth.getName()).isEqualTo("test-user");
        assertThat(auth.getAuthorities()).extracting(Object::toString)
                .containsExactly("ROLE_USER");
        assertThat(chain.count).isEqualTo(1);
    }

    @Test
    void resolveToken_headerSinPrefijoPeroCookiePresente_devuelveTokenDeCookie() throws Exception {
        String token = buildToken(List.of("ROLE_USER"), Instant.now().plusSeconds(1800));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/protected");
        // Header presente pero sin prefijo
        request.addHeader(Constants.HEADER_AUTHORIZACION_KEY, "Token " + token);
        // Cookie válida
        request.setCookies(new Cookie("jwt_token", token));
        MockHttpServletResponse response = new MockHttpServletResponse();
        CountingFilterChain chain = new CountingFilterChain();

        filter.doFilterInternal(request, response, chain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNotNull();
        assertThat(auth.getName()).isEqualTo("test-user");
        assertThat(auth.getAuthorities()).extracting(Object::toString)
                .containsExactly("ROLE_USER");
        assertThat(chain.count).isEqualTo(1);
    }

    @Test
    void resolveToken_variasCookiesUnaJwtToken_devuelveTokenDeJwtToken() throws Exception {
        String token = buildToken(List.of("ROLE_USER"), Instant.now().plusSeconds(1800));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/protected");
        // Sin header Authorization válido
        request.setCookies(
                new Cookie("other_cookie", "valor"),
                new Cookie("jwt_token", token),
                new Cookie("another", "x"));
        MockHttpServletResponse response = new MockHttpServletResponse();
        CountingFilterChain chain = new CountingFilterChain();

        filter.doFilterInternal(request, response, chain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNotNull();
        assertThat(auth.getName()).isEqualTo("test-user");
        assertThat(auth.getAuthorities()).extracting(Object::toString)
                .containsExactly("ROLE_USER");
        assertThat(chain.count).isEqualTo(1);
    }

    @Test
    void resolveToken_variasCookiesSinJwtToken_retornaNull() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/protected");
        // Cookies presentes, pero ninguna llamada "jwt_token"
        request.setCookies(
                new Cookie("other_cookie", "valor"),
                new Cookie("another", "x"));
        MockHttpServletResponse response = new MockHttpServletResponse();
        CountingFilterChain chain = new CountingFilterChain();

        filter.doFilterInternal(request, response, chain);

        // No debe autenticarse
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNull();
        assertThat(chain.count).isEqualTo(1);
    }

    // Cadena de filtros contadora
    private static class CountingFilterChain implements FilterChain {
        int count = 0;

        @Override
        public void doFilter(jakarta.servlet.ServletRequest request,
                jakarta.servlet.ServletResponse response) throws IOException, ServletException {
            count++;
        }
    }
}