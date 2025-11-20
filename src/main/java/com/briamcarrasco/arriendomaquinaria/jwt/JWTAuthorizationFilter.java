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
import static com.briamcarrasco.arriendomaquinaria.jwt.Constants.*;

/**
 * Filtro para la autorización basada en JWT en el sistema.
 * Se encarga de validar el token JWT presente en la cabecera o en las cookies
 * de la petición,
 * establecer la autenticación en el contexto de seguridad y permitir el acceso
 * a los recursos protegidos.
 * Omite el filtrado en rutas públicas como login, recursos estáticos y la API
 * pública de maquinaria.
 */
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    /**
     * Determina si la petición no debe ser filtrada por el JWT.
     *
     * @param request petición HTTP
     * @return true si la ruta es pública, false en caso contrario
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.equals("/login")
                || path.equals("/auth/login")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.startsWith("/api/machinery");
    }

    /**
     * Extrae el token JWT de la cabecera de autorización o de las cookies.
     *
     * @param request petición HTTP
     * @return el token JWT si está presente, null en caso contrario
     */
    private String resolveToken(HttpServletRequest request) {
        String auth = request.getHeader(HEADER_AUTHORIZACION_KEY);
        if (auth != null && auth.startsWith(TOKEN_BEARER_PREFIX)) {
            return auth.substring(TOKEN_BEARER_PREFIX.length());
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Parsea los claims del token JWT.
     *
     * @param rawToken token JWT en formato String
     * @return objeto Claims extraído del token
     */
    private Claims parseClaims(String rawToken) {
        return Jwts.parser()
                .verifyWith(getSigningKey(SUPER_SECRET_KEY))
                .build()
                .parseSignedClaims(rawToken)
                .getPayload();
    }

    /**
     * Establece la autenticación en el contexto de seguridad usando los claims del
     * token.
     *
     * @param claims claims extraídos del token JWT
     */
    private void setAuthentication(Claims claims) {
        List<?> authorities = (List<?>) claims.get("authorities");
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                claims.getSubject(),
                null,
                authorities.stream().map(Object::toString).map(SimpleGrantedAuthority::new)
                        .toList());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    /**
     * Realiza el filtrado de la petición, validando el token JWT y estableciendo la
     * autenticación.
     *
     * @param request  petición HTTP
     * @param response respuesta HTTP
     * @param chain    cadena de filtros
     * @throws ServletException si ocurre un error en el filtrado
     * @throws IOException      si ocurre un error de entrada/salida
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            String token = resolveToken(request);
            if (token != null) {
                Claims claims = parseClaims(token);
                if (claims.get("authorities") != null) {
                    setAuthentication(claims);
                } else {
                    SecurityContextHolder.clearContext();
                }
            } else {
                SecurityContextHolder.clearContext();
            }
            chain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            request.setAttribute("jwt_error", "TOKEN_EXPIRED");
            SecurityContextHolder.clearContext();
            chain.doFilter(request, response);
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            request.setAttribute("jwt_error", "TOKEN_INVALID");
            SecurityContextHolder.clearContext();
            chain.doFilter(request, response);
        }
    }
}