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
                // Vulnerabilidad 1: Habilitar protección Anti-CSRF
                // Usamos CookieCsrfTokenRepository para almacenar el token en una cookie
                // accesible desde JavaScript (necesario para aplicaciones con frontend dinámico)
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                
                // Configuración de manejo de sesiones
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
                // Definición de rutas públicas y protegidas
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/landing", "/login", "/auth/login",
                                "/css/**", "/js/**", "/images/**", "/webjars/**", "/style.css", "/api/machinery/**",
                                "/search", "/search/**") // permitir búsqueda desde landing (público)
                        .permitAll()
                        .anyRequest().authenticated())
                
                // Vulnerabilidades 2, 3, 4: Configuración de encabezados de seguridad
                .headers(headers -> headers
                        // Content Security Policy mejorado:
                        // - Eliminamos 'unsafe-inline' de script-src (Vulnerabilidad 4)
                        // - Definimos directivas específicas sin comodines (Vulnerabilidad 2)
                        // - Agregamos form-action y frame-ancestors (Vulnerabilidad 3)
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives(
                                        "default-src 'self'; " +
                                        "script-src 'self' https://cdn.jsdelivr.net; " +
                                        "style-src 'self' https://cdn.jsdelivr.net; " +
                                        "img-src 'self' data: https:; " +
                                        "form-action 'self'; " +          // Restringe envío de formularios
                                        "frame-ancestors 'none'; " +       // Previene clickjacking
                                        "object-src 'none';"               // Bloquea plugins inseguros
                                )
                        )
                        // Evitar que la aplicación sea embebida en iframes
                        .frameOptions(frame -> frame.deny())
                        // HSTS para forzar HTTPS en producción
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000))  // 1 año
                )
                
                // Agregar filtro JWT antes de la autenticación estándar
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                
                // Configuración de logout
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
}