package com.briamcarrasco.arriendomaquinaria.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.briamcarrasco.arriendomaquinaria.model.User;
import com.briamcarrasco.arriendomaquinaria.repository.UserRepository;

/**
 * Implementación del servicio de usuarios.
 * 
 * Gestiona la lógica relacionada con la creación y búsqueda de usuarios,
 * incluyendo la encriptación de contraseñas y la persistencia en la base de
 * datos.
 *
 * @see UserService
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor para la inyección de dependencias.
     *
     * @param userRepository  repositorio de usuarios
     * @param passwordEncoder codificador de contraseñas
     */
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Crea un nuevo usuario en el sistema.
     *
     * @param username nombre de usuario
     * @param password contraseña del usuario (será encriptada)
     * @param email    correo electrónico del usuario
     * @return el usuario creado y persistido
     * @throws IllegalArgumentException si el nombre de usuario ya existe
     */
    @Override
    public User createUser(String username, String password, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("El nombre de usuario ya existe.");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole(User.Role.USER);
        return userRepository.save(user);
    }

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username nombre de usuario
     * @return el usuario encontrado o {@code null} si no existe
     */
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}