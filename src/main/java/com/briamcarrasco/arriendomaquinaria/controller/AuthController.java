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


@Controller
public class AuthController {


    
    @Autowired
    private JWTAuthtenticationConfig jwtAuthtenticationConfig;
    @Autowired
    private MyUserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/auth/login")
    public String login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletResponse response) {
        try {
            UserDetails user = userDetailsService.loadUserByUsername(username);
            System.out.println("Stored password: " + user.getPassword());  // Log temporal
            System.out.println("Matches: " + passwordEncoder.matches(password, user.getPassword()));
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return "redirect:/login?error";
            }

            String role = user.getAuthorities().stream().findFirst().map(Object::toString).orElse("ROLE_USER");
            String token = jwtAuthtenticationConfig.getJWTToken(username, role);
            Cookie cookie = new Cookie("jwt_token", token.substring(TOKEN_BEARER_PREFIX.length()));  // Quita "Bearer " del valor de la cookie
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60);
            response.addCookie(cookie);
            System.out.println("Cookie set: " + cookie.getValue());  // Log temporal
            return "redirect:/home";
        } catch (Exception e) {
            e.printStackTrace();
            // Maneja cualquier excepción (ej. usuario no encontrado) redirigiendo con error
            return "redirect:/login?error";
        }
    }
// ...existing code...

    @GetMapping("/auth/login")
    public String redirectToLogin() {
        return "redirect:/login";
    }
}
