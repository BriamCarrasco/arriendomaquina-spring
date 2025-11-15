package com.briamcarrasco.arriendomaquinaria.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Entidad que representa la información adicional de un usuario en el sistema.
 * Incluye datos personales como nombre, apellido, teléfono, dirección, ciudad y
 * país.
 * Está relacionada de forma uno a uno con la entidad User.
 */
@Data
@Entity
@Table(name = "tb_usersinfo")
public class UserInfo {

    /**
     * Identificador único de la información adicional del usuario.
     */
    @Id
    @Column(name = "id_userinfo", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Nombre del usuario.
     */
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    /**
     * Apellido del usuario.
     */
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    /**
     * Número de teléfono del usuario.
     */
    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    /**
     * Dirección del usuario.
     */
    @Column(name = "address", nullable = false, length = 100)
    private String address;

    /**
     * Ciudad de residencia del usuario.
     */
    @Column(name = "city", nullable = false, length = 50)
    private String city;

    /**
     * País de residencia del usuario.
     */
    @Column(name = "country", nullable = false, length = 50)
    private String country;

    /**
     * Relación uno a uno con la entidad User.
     */
    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;


    /*
     * Getters y setters generados por Lombok(@Data)
     */
}