package com.briamcarrasco.arriendomaquinaria.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JWTAuthtenticationConfigTest {

        @Autowired
        private JWTAuthtenticationConfig config;

        @Value("${jwt.secret}")
        private String jwtSecret;

        @Test
        void getJWTToken_debeGenerarTokenConPrefijoBearerYClaimsCorrectos() {
                String username = "user@test.com";
                String role = "ROLE_ADMIN";

                String result = config.getJWTToken(username, role);

                // 1) Prefijo
                assertThat(result).startsWith(Constants.TOKEN_BEARER_PREFIX);

                String rawToken = result.substring(Constants.TOKEN_BEARER_PREFIX.length());

                // 2) Parsear token y revisar claims
                Claims claims = Jwts.parser()
                                .verifyWith(Constants.getSigningKey(jwtSecret))
                                .build()
                                .parseSignedClaims(rawToken)
                                .getPayload();

                // subject
                assertThat(claims.getSubject()).isEqualTo(username);

                // authorities
                Object authoritiesObj = claims.get("authorities");
                assertThat(authoritiesObj).isInstanceOf(List.class);

                @SuppressWarnings("unchecked")
                List<String> authorities = (List<String>) authoritiesObj;
                assertThat(authorities).containsExactly("ROLE_ADMIN");

                // issuedAt y expiration
                Date issuedAt = claims.getIssuedAt();
                Date expiration = claims.getExpiration();
                assertThat(issuedAt).isNotNull();
                assertThat(expiration).isNotNull();
                assertThat(expiration.getTime())
                                .isGreaterThan(issuedAt.getTime());

                // expiración consistente con TOKEN_EXPIRATION_TIME (tolerancia de 1s)
                long diff = expiration.getTime() - issuedAt.getTime();
                assertThat(diff)
                                .isBetween(Constants.TOKEN_EXPIRATION_TIME - 1000,
                                                Constants.TOKEN_EXPIRATION_TIME + 1000);
        }

        @Test
        void getJWTToken_funcionaConOtroRol() {
                String result = config.getJWTToken("otro@user.cl", "ROLE_USER");
                assertThat(result).startsWith(Constants.TOKEN_BEARER_PREFIX);

                String rawToken = result.substring(Constants.TOKEN_BEARER_PREFIX.length());
                Claims claims = Jwts.parser()
                                .verifyWith(Constants.getSigningKey(jwtSecret))
                                .build()
                                .parseSignedClaims(rawToken)
                                .getPayload();

                @SuppressWarnings("unchecked")
                List<String> authorities = (List<String>) claims.get("authorities");
                assertThat(authorities).containsExactly("ROLE_USER");
        }
}