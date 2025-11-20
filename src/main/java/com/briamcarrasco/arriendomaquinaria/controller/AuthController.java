package com.briamcarrasco.arriendomaquinaria.controller;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.briamcarrasco.arriendomaquinaria.dto.LoginRequestDto;
import com.briamcarrasco.arriendomaquinaria.jwt.JWTAuthtenticationConfig;
import com.briamcarrasco.arriendomaquinaria.service.MyUserDetailsService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import static com.briamcarrasco.arriendomaquinaria.jwt.Constants.TOKEN_BEARER_PREFIX;
import static com.briamcarrasco.arriendomaquinaria.jwt.Constants.TOKEN_EXPIRATION_TIME;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador para la autenticación de usuarios en el sistema.
 * Gestiona el proceso de inicio de sesión y la generación del token JWT.
 */
@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final JWTAuthtenticationConfig jwtAuthtenticationConfig;
    private final MyUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            JWTAuthtenticationConfig jwtAuthtenticationConfig,
            MyUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        this.jwtAuthtenticationConfig = jwtAuthtenticationConfig;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Sanitiza el valor para evitar CRLF injection.
     */
    private String sanitizeForHeader(String value) {
        if (value == null)
            return "";
        return value.replaceAll("[\\r\\n]", "");
    }

    /**
     * Procesa la solicitud de inicio de sesión.
     * Verifica las credenciales del usuario, genera el token JWT y lo almacena en
     * una cookie.
     * Redirige a la página principal si el inicio de sesión es exitoso, o muestra
     * error si falla.
     *
     * @param username nombre de usuario
     * @param password contraseña del usuario
     * @param response respuesta HTTP para agregar la cookie
     * @return redirección a la página correspondiente
     */
    @PostMapping("/auth/login")
    public String login(@ModelAttribute LoginRequestDto loginRequest, HttpServletResponse response) {
        try {
            UserDetails user = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return "redirect:/login?error";
            }
            String role = user.getAuthorities().stream().findFirst().map(Object::toString).orElse("ROLE_USER");
            String token = jwtAuthtenticationConfig.getJWTToken(loginRequest.getUsername(), role);
            String sanitizedToken = sanitizeForHeader(token.substring(TOKEN_BEARER_PREFIX.length()));
            Cookie cookie = new Cookie("jwt_token", sanitizedToken);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge((int) (TOKEN_EXPIRATION_TIME / 1000));
            response.addCookie(cookie);
            response.setHeader("Set-Cookie",
                    String.format("jwt_token=%s; Max-Age=%d; Path=/; HttpOnly; SameSite=Strict", sanitizedToken,
                            cookie.getMaxAge()));
            return "redirect:/home";
        } catch (Exception e) {
            logger.error("Error en el proceso de login", e);
            return "redirect:/login?error";
        }
    }

    /**
     * Redirige la ruta de autenticación al formulario de login.
     *
     * @return redirección a la página de login
     */
    @GetMapping("/auth/login")
    public String redirectToLogin() {
        return "redirect:/login";
    }
}