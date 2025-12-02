package com.briamcarrasco.arriendomaquinaria.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.briamcarrasco.arriendomaquinaria.jwt.Constants.*;

/**
 * Clase de configuración para la generación de tokens JWT en el sistema.
 * Permite crear tokens JWT con información de usuario y roles.
 */
@Configuration
public class JWTAuthtenticationConfig {

        /**
         * Genera un token JWT para el usuario y rol especificados.
         *
         * @param username nombre de usuario
         * @param role     rol del usuario
         * @return token JWT generado
         */
        public String getJWTToken(String username, String role) {
                List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                                .commaSeparatedStringToAuthorityList(role);

                Map<String, Object> claims = new HashMap<>();
                claims.put("authorities", grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .toList());

                String token = Jwts.builder()
                                .claims(claims)
                                .subject(username)
                                .issuedAt(new Date(System.currentTimeMillis()))
                                .expiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                                .signWith(getSigningKey(JWT_SECRET_KEY))
                                .compact();

                return TOKEN_BEARER_PREFIX + token;
        }

}