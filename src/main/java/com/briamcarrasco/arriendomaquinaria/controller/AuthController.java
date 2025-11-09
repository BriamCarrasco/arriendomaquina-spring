package com.briamcarrasco.arriendomaquinaria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.briamcarrasco.arriendomaquinaria.jwt.JWTAuthtenticationConfig;
import com.briamcarrasco.arriendomaquinaria.service.MyUserDetailsService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import static com.briamcarrasco.arriendomaquinaria.jwt.Constants.TOKEN_BEARER_PREFIX;

/**
 * Controlador para la autenticación de usuarios en el sistema.
 * Gestiona el proceso de inicio de sesión y la generación del token JWT.
 */
@Controller
public class AuthController {

    @Autowired
    private JWTAuthtenticationConfig jwtAuthtenticationConfig;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public String login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletResponse response) {
        try {
            UserDetails user = userDetailsService.loadUserByUsername(username);
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return "redirect:/login?error";
            }

            String role = user.getAuthorities().stream().findFirst().map(Object::toString).orElse("ROLE_USER");
            String token = jwtAuthtenticationConfig.getJWTToken(username, role);
            Cookie cookie = new Cookie("jwt_token", token.substring(TOKEN_BEARER_PREFIX.length()));
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60);
            response.addCookie(cookie);
            return "redirect:/home";
        } catch (Exception e) {
            e.printStackTrace();
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