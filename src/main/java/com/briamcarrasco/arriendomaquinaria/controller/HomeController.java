package com.briamcarrasco.arriendomaquinaria.controller;

import com.briamcarrasco.arriendomaquinaria.repository.MachineryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador para la gestión de la página principal y la navegación inicial
 * del sistema.
 * Permite mostrar la página de inicio, la página de bienvenida y redireccionar
 * según el estado de autenticación.
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final MachineryRepository repo;

    /**
     * Constructor que recibe el repositorio de maquinarias.
     *
     * @param repo repositorio de maquinarias
     */
    public HomeController(MachineryRepository repo) {
        this.repo = repo;
    }

    /**
     * Muestra la página de bienvenida si el usuario no está autenticado,
     * o redirige a la página principal si ya inició sesión.
     *
     * @param authentication información de autenticación del usuario
     * @return nombre de la vista o redirección
     */
    @GetMapping("/landing")
    public String landing(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/home";
        }
        return "landing";
    }

    /**
     * Muestra la página principal con el nombre del usuario autenticado y la lista
     * de maquinarias.
     *
     * @param authentication información de autenticación del usuario
     * @param model          modelo para la vista
     * @return nombre de la vista principal
     */
    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        logger.info("Authentication in /home: {}", authentication);
        model.addAttribute("name", authentication != null ? authentication.getName() : "Invitado");
        model.addAttribute("maquinarias", repo.findAll());
        return "home";
    }

    /**
     * Redirige la ruta raíz a la página de bienvenida.
     *
     * @return redirección a la vista de bienvenida
     */
    @GetMapping("/")
    public String redirectToLanding() {
        return "redirect:/landing";
    }
}