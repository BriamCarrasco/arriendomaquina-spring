package com.briamcarrasco.arriendomaquinaria.security;

import com.briamcarrasco.arriendomaquinaria.jwt.JWTAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
public class WebSecurityConfig {

        private final JWTAuthorizationFilter jwtAuthorizationFilter;

        /**
         * Constructor que recibe el filtro de autorización JWT.
         *
         * @param jwtAuthorizationFilter filtro para validar tokens JWT
         */
        public WebSecurityConfig(JWTAuthorizationFilter jwtAuthorizationFilter) {
                this.jwtAuthorizationFilter = jwtAuthorizationFilter;
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
                                                .csrfTokenRepository(cookieCsrfTokenRepository()))
                                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/", "/landing", "/login", "/auth/login",
                                                                "/css/**", "/js/**", "/images/**", "/webjars/**",
                                                                "/style.css", "/api/machinery/**",
                                                                "/search", "/search/**")
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
                                                                                                "img-src 'self' data:; "
                                                                                                +
                                                                                                "font-src 'self' data:; "
                                                                                                +
                                                                                                "connect-src 'self'; " +
                                                                                                "form-action 'self'; " +
                                                                                                "frame-ancestors 'none'; "
                                                                                                +
                                                                                                "object-src 'none'; " +
                                                                                                "media-src 'self'; " +
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
                                .httpOnly(true)
                                .sameSite("Strict")
                                .path("/"));
                return repo;
        }

}