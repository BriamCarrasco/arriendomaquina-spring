package com.briamcarrasco.arriendomaquinaria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.briamcarrasco.arriendomaquinaria.service.UserService;
import com.briamcarrasco.arriendomaquinaria.model.User;
import java.util.Map;

@RestController
@RequestMapping("/api/register-test")
public class RegisterTestController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");
            String email = request.get("email");
            String roleStr = request.get("role");

            if (username == null || password == null || email == null || roleStr == null) {
                return ResponseEntity.badRequest().body("Faltan campos obligatorios");
            }

            User.Role role = User.Role.valueOf(roleStr.toUpperCase());
            User user = userService.createUser(username, password, email, role);
            return ResponseEntity.ok("Usuario registrado: " + user.getUsername());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al registrar usuario: " + e.getMessage());
        }
    }

    
}
