package com.briamcarrasco.arriendomaquinaria.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import static com.briamcarrasco.arriendomaquinaria.jwt.Constants.*;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();
    return path.equals("/login") || path.equals("/auth/login") || path.startsWith("/css/")
            || path.startsWith("/js/") || path.startsWith("/images/");
    }

    private String resolveToken(HttpServletRequest request) {
        String auth = request.getHeader(HEADER_AUTHORIZACION_KEY);
        if (auth != null && auth.startsWith(TOKEN_BEARER_PREFIX)) {
            return auth.substring(TOKEN_BEARER_PREFIX.length());
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt_token".equals(cookie.getName())) {
                    return cookie.getValue();  // Devuelve el valor directamente (sin prefijo)
                }
            }
        }
        return null;
    }

    private Claims parseClaims(String rawToken) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey(SUPER_SECRET_KEY))
                .build()
                .parseSignedClaims(rawToken)
                .getPayload();
    }

    private void setAuthentication(Claims claims) {
        List<?> authorities = (List<?>) claims.get("authorities");
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        claims.getSubject(),
                        null,
                        authorities.stream().map(Object::toString).map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            String token = resolveToken(request);
            System.out.println("Resolved token: " + (token != null ? "present" : "null"));  // Log temporal
            if (token != null) {
                Claims claims = parseClaims(token);
                System.out.println("Parsed claims: " + claims);  // Log temporal
                if (claims.get("authorities") != null) {
                    setAuthentication(claims);
                    System.out.println("Authentication set for user: " + claims.getSubject());  // Log temporal
                } else {
                    System.out.println("No authorities in claims");  // Log temporal
                    SecurityContextHolder.clearContext();
                }
            } else {
                SecurityContextHolder.clearContext();
            }
            chain.doFilter(request, response);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            System.out.println("JWT exception: " + e.getMessage());  // Log temporal
            SecurityContextHolder.clearContext();
            chain.doFilter(request, response);
        }
    }
}