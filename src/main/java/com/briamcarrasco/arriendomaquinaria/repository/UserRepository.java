package com.briamcarrasco.arriendomaquinaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.briamcarrasco.arriendomaquinaria.model.User;

/**
 * Repositorio para la entidad User.
 * Proporciona métodos para acceder y gestionar usuarios en la base de datos.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username nombre de usuario
     * @return el usuario encontrado o null si no existe
     */
    User findByUsername(String username);

    /**
     * Verifica si existe un usuario con el nombre de usuario especificado.
     *
     * @param username nombre de usuario
     * @return true si existe, false en caso contrario
     */
    boolean existsByUsername(String username);
}