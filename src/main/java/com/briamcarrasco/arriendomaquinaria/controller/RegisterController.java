package com.briamcarrasco.arriendomaquinaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.briamcarrasco.arriendomaquinaria.model.User;
import com.briamcarrasco.arriendomaquinaria.service.UserService;
import org.springframework.ui.Model;


/**
 * Controlador para el registro de usuarios en el sistema.
 * Gestiona la ruta para mostrar la vista de registro de usuario.
 */
@Controller
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }



    @PostMapping("/register/user")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.createUser(user.getUsername(), user.getPassword(), user.getEmail());
            return "redirect:/login?registered";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
    

    /**
     * Muestra la página de registro de usuario.
     *
     * @return nombre de la vista de registro
     */
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

}