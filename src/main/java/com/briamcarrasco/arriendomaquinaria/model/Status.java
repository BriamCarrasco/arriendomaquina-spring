package com.briamcarrasco.arriendomaquinaria.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

/**
 * Entidad que representa el estado de una maquinaria en el sistema.
 * Los métodos getters y setters para los atributos id y name
 * se generan automáticamente mediante la anotación @Data de Lombok.
 */
@Data
@Entity
@Table(name = "tb_status")
public class Status {

    /**
     * Identificador único del estado.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_status", nullable = false)
    private Long id;

    /**
     * Nombre del estado.
     */
    @Column(name = "name_status", nullable = false)
    private String name;

    /**
     * Constructor por defecto.
     */
    public Status() {
        // Método vacío intencionalmente. JPA exige un constructor por defecto para las
        // entidades.
    }

}