package com.briamcarrasco.arriendomaquinaria.security;

import com.briamcarrasco.arriendomaquinaria.jwt.JWTAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
     * Deshabilita CSRF, establece la sesión como stateless, define rutas públicas y
     * protegidas,
     * agrega el filtro JWT y configura el logout.
     *
     * @param http objeto HttpSecurity para la configuración
     * @return la cadena de filtros de seguridad configurada
     * @throws Exception si ocurre un error en la configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/landing", "/login", "/auth/login",
                                "/css/**", "/js/**", "/images/**", "/webjars/**", "/style.css", "/api/machinery/**")
                        .permitAll()
                        .anyRequest().authenticated())
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
}