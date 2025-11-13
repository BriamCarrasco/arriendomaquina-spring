package com.briamcarrasco.arriendomaquinaria.service;

import com.briamcarrasco.arriendomaquinaria.model.User;
import com.briamcarrasco.arriendomaquinaria.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio para la gestión de usuarios en el sistema de arriendo de maquinaria.
 * Proporciona métodos para crear usuarios y manejar la lógica relacionada con
 * ellos.
 *
 */
@Service
public class UserService {

    
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Crea un nuevo usuario en el sistema.
     *
     * @param username nombre de usuario
     * @param password contraseña del usuario (será encriptada)
     * @param email    correo electrónico del usuario
     * @param role     rol asignado al usuario
     * @return el usuario creado y persistido
     */
    public User createUser(String username, String password, String email, User.Role role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole(role);
        return userRepository.save(user);
    }
}