package com.briamcarrasco.arriendomaquinaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.briamcarrasco.arriendomaquinaria.model.UserInfo;
import com.briamcarrasco.arriendomaquinaria.model.User;

/**
 * Repositorio para la entidad UserInfo.
 * Proporciona métodos para acceder y gestionar la información adicional de los
 * usuarios en la base de datos.
 */
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    /**
     * Verifica si existe información adicional para el usuario especificado.
     *
     * @param user usuario a consultar
     * @return true si existe información, false en caso contrario
     */
    boolean existsByUser(User user);

    /**
     * Busca la información adicional asociada a un usuario.
     *
     * @param user usuario a consultar
     * @return la información adicional del usuario o null si no existe
     */
    UserInfo findByUser(User user);
}