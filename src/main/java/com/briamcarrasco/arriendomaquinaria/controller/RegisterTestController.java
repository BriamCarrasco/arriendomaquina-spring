package com.briamcarrasco.arriendomaquinaria.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.briamcarrasco.arriendomaquinaria.service.UserService;
import com.briamcarrasco.arriendomaquinaria.model.User;
import java.util.Map;

/**
 * Controlador para el registro de usuarios de prueba mediante la API.
 * Expone un endpoint para crear usuarios enviando los datos en formato JSON.
 */
@RestController
@RequestMapping("/api/register-test")
public class RegisterTestController {

    private final UserService userService;

    public RegisterTestController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * Recibe los datos del usuario en el cuerpo de la petición y retorna el
     * resultado del registro.
     *
     * @param request mapa con los campos username, password, email y role
     * @return respuesta HTTP con el resultado del registro
     */
    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> request) {
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