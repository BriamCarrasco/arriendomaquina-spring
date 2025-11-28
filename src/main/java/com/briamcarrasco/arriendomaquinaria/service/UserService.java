package com.briamcarrasco.arriendomaquinaria.service;

import java.util.List;

import com.briamcarrasco.arriendomaquinaria.model.User;

/**
 * Servicio para la gestión de usuarios.
 * 
 * Define las operaciones principales relacionadas con la creación y búsqueda de
 * usuarios.
 */
public interface UserService {

    /**
     * Crea un nuevo usuario en el sistema.
     *
     * @param username nombre de usuario
     * @param password contraseña del usuario
     * @param email    correo electrónico del usuario
     * @return el usuario creado y persistido
     */
    User createUser(String username, String password, String email);

    User adminCreate(String username, String password, String email, String role);

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username nombre de usuario
     * @return el usuario encontrado o {@code null} si no existe
     */
    User findByUsername(String username);

    User findById(Long id);

    User updateUser(Long id, String username, String email);

    User deleteUser(Long id);

    List<User> getAllUsers();
}