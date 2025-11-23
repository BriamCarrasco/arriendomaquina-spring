package com.briamcarrasco.arriendomaquinaria.dto;

import lombok.Data;

/**
 * DTO para recibir los datos de inicio de sesión de un usuario.
 * Incluye el nombre de usuario y la contraseña.
 */
@Data
public class LoginRequestDto {
    /**
     * Nombre de usuario para el inicio de sesión.
     */
    private String username;

    /**
     * Contraseña para el inicio de sesión.
     */
    private String password;
}