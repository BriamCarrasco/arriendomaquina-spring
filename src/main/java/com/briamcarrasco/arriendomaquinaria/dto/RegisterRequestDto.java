package com.briamcarrasco.arriendomaquinaria.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO para recibir los datos de registro de un usuario.
 * Incluye el nombre de usuario, contraseña y correo electrónico.
 */
@Data
public class RegisterRequestDto {
    /**
     * Nombre de usuario para el registro.
     */
    @NotBlank
    private String username;

    /**
     * Contraseña para el registro.
     */
    @NotBlank
    private String password;

    /**
     * Correo electrónico del usuario.
     */
    @Email
    @NotBlank
    private String email;
}