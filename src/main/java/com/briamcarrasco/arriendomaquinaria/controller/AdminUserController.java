package com.briamcarrasco.arriendomaquinaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import com.briamcarrasco.arriendomaquinaria.service.UserService;
import com.briamcarrasco.arriendomaquinaria.model.User;
import org.springframework.ui.Model;
import java.util.List;

import static com.briamcarrasco.arriendomaquinaria.jwt.Constants.REDIRECT_USERS;

@Controller
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    // Listar usuarios
    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "adminpanel";
    }

    // Crear usuario (formulario)
    @PostMapping("/create")
    public String createUser(@RequestParam String username,
            @RequestParam String password,
            @RequestParam String email) {
        userService.createUser(username, password, email);
        return REDIRECT_USERS;
    }

    // Eliminar usuario
    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long id) {
        userService.deleteUser(id);
        return REDIRECT_USERS;
    }

    // Mostrar formulario de edición
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        if (user != null) {
            model.addAttribute("user", user);
            return "editUser";
        }
        return REDIRECT_USERS;
    }

    // Procesar la actualización
    @PostMapping("/update")
    public String updateUser(@RequestParam Long id,
            @RequestParam String username,
            @RequestParam String email) {
        userService.updateUser(id, username, email);
        return REDIRECT_USERS;
    }

}
