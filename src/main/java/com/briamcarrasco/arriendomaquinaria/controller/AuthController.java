package com.briamcarrasco.arriendomaquinaria.controller;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.briamcarrasco.arriendomaquinaria.dto.LoginRequestDto;
import com.briamcarrasco.arriendomaquinaria.jwt.JWTAuthtenticationConfig;
import com.briamcarrasco.arriendomaquinaria.service.MyUserDetailsService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador para la autenticación de usuarios en el sistema.
 * Gestiona el proceso de inicio de sesión y la generación del token JWT.
 */
@Controller
public class AuthController {

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
     *
     * @param value valor a sanitizar
     * @return valor sin caracteres de salto de línea
     */
    private String sanitizeForHeader(String value) {
        if (value == null)
            return "";
        return value.replaceAll("[\\r\\n]", "");
    }

    private void regenerateCsrf(HttpServletRequest request, HttpServletResponse response) {
        CsrfToken token = new HttpSessionCsrfTokenRepository().generateToken(request);

        response.setHeader("XSRF-TOKEN", sanitizeForHeader(token.getToken()));

        Cookie cookie = new Cookie("XSRF-TOKEN", token.getToken());
        cookie.setHttpOnly(true); // Protegido con HttpOnly
        cookie.setSecure(false); // true sólo si usas HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(-1); // session cookie
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);
    }

    /**
     * Procesa la solicitud de inicio de sesión.
     * Verifica las credenciales del usuario, genera el token JWT y lo almacena en
     * una cookie.
     * Redirige a la página principal si el inicio de sesión es exitoso, o muestra
     * error si falla.
     *
     * @param loginRequest datos de inicio de sesión
     * @param request      solicitud HTTP
     * @param response     respuesta HTTP para agregar la cookie
     * @return redirección a la página correspondiente
     */
    @PostMapping("/auth/login")
    public String login(@ModelAttribute LoginRequestDto loginRequest,
            HttpServletRequest request,
            HttpServletResponse response) {
        try {
            UserDetails user = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return "redirect:/login?error";
            }

            // === GENERA JWT ===
            String role = user.getAuthorities().stream().findFirst().map(Object::toString).orElse("ROLE_USER");
            String token = jwtAuthtenticationConfig.getJWTToken(loginRequest.getUsername(), role);

            Cookie cookie = new Cookie("jwt_token", token.substring(7));
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(86400);
            cookie.setAttribute("SameSite", "Lax");
            response.addCookie(cookie);

            // === 🔥 REGENERAR CSRF TOKEN DESPUÉS DE LOGIN ===
            regenerateCsrf(request, response);

            return "redirect:/home";

        } catch (Exception e) {
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