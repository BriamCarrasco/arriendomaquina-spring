package com.briamcarrasco.arriendomaquinaria.security;

import com.briamcarrasco.arriendomaquinaria.jwt.JWTAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuración de seguridad para la aplicación.
 * Define la política de autenticación, autorización y manejo de sesiones
 * utilizando JWT.
 * Permite el acceso público a ciertas rutas y protege el resto mediante
 * autenticación.
 * Aplica cabeceras de seguridad como Content Security Policy (CSP), HSTS y
 * Frame Options.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

        private final JWTAuthorizationFilter jwtAuthorizationFilter;
        private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
        private final JsonAccessDeniedHandler jsonAccessDeniedHandler;

        /**
         * Constructor que recibe el filtro de autorización JWT.
         *
         * @param jwtAuthorizationFilter filtro para validar tokens JWT
         */
        public WebSecurityConfig(JWTAuthorizationFilter jwtAuthorizationFilter,
                        JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                        JsonAccessDeniedHandler jsonAccessDeniedHandler) {
                this.jwtAuthorizationFilter = jwtAuthorizationFilter;
                this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
                this.jsonAccessDeniedHandler = jsonAccessDeniedHandler;
        }

        /**
         * Configura la cadena de filtros de seguridad.
         * Habilita protección CSRF, define rutas públicas y protegidas,
         * configura encabezados de seguridad (CSP, HSTS, frameOptions),
         * agrega el filtro JWT y configura el logout.
         *
         * @param http objeto HttpSecurity para la configuración
         * @return la cadena de filtros de seguridad configurada
         * @throws Exception si ocurre un error en la configuración
         */
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf
                                                .csrfTokenRepository(cookieCsrfTokenRepository())
                                                .ignoringRequestMatchers("/logout",
                                                                "/api/reviews/**",
                                                                "/api/machinery-media/**"))
                                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                                .accessDeniedHandler(jsonAccessDeniedHandler))
                                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/", "/landing", "/login", "/auth/login",
                                                                "/css/**", "/js/**", "/images/**", "/webjars/**",
                                                                "/style.css", "/api/machinery/**", "register",
                                                                "/search", "/search/**", "/bootstrap.min.css",
                                                                "/bootstrap.bundle.min.js", "/favicon.ico",
                                                                "/register/user",
                                                                "/public/**", "/api/reviews/**",
                                                                "/api/machinery-media/**")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .headers(headers -> headers
                                                .contentSecurityPolicy(csp -> csp
                                                                .policyDirectives(
                                                                                "default-src 'self'; " +
                                                                                                "base-uri 'self'; " +
                                                                                                "script-src 'self' https://cdn.jsdelivr.net; "
                                                                                                +
                                                                                                "style-src 'self' https://cdn.jsdelivr.net; "
                                                                                                +
                                                                                                "img-src 'self' data: https:; "
                                                                                                +
                                                                                                "font-src 'self' data:; "
                                                                                                +
                                                                                                "connect-src 'self'; " +
                                                                                                "form-action 'self'; " +
                                                                                                "frame-ancestors 'none'; "
                                                                                                +
                                                                                                "object-src 'none'; " +
                                                                                                // allow media from
                                                                                                // https external hosts
                                                                                                // and common players
                                                                                                "media-src 'self' https:; "
                                                                                                +
                                                                                                // allow embedding
                                                                                                // trusted players
                                                                                                // (YouTube, Vimeo)
                                                                                                "frame-src 'self' https://www.youtube.com https://www.youtube-nocookie.com https://player.vimeo.com; "
                                                                                                +
                                                                                                "upgrade-insecure-requests;"))
                                                .frameOptions(frame -> frame.deny())
                                                .httpStrictTransportSecurity(hsts -> hsts
                                                                .includeSubDomains(true)
                                                                .maxAgeInSeconds(31536000)))
                                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .deleteCookies("jwt_token")
                                                .logoutSuccessUrl("/login?logout")
                                                .permitAll());

                return http.build();
        }

        /**
         * Proporciona el codificador de contraseñas BCrypt.
         *
         * @return instancia de PasswordEncoder
         */
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        /**
         * Repositorio de tokens CSRF basado en cookie con bandera HttpOnly activada.
         * El token sigue disponible para los formularios (Thymeleaf lo expone como
         * atributo),
         * pero no puede ser leído directamente por JavaScript, mitigando ataques XSS.
         *
         * @return instancia configurada de CookieCsrfTokenRepository
         */
        @Bean
        public CookieCsrfTokenRepository cookieCsrfTokenRepository() {
                CookieCsrfTokenRepository repo = new CookieCsrfTokenRepository();
                repo.setCookieCustomizer(builder -> builder
                                .httpOnly(false)
                                .sameSite("Strict")
                                .path("/"));
                return repo;
        }

}