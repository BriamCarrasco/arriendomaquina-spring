package com.briamcarrasco.arriendomaquinaria.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;

/**
 * Entidad que representa la información adicional de una maquinaria en el
 * sistema.
 * Incluye la descripción y la relación con la entidad Machinery.
 * Los métodos getters y setters para los atributos id, description y machinery
 * se generan automáticamente mediante la anotación @Data de Lombok.
 */
@Data
@Entity
@Table(name = "tb_machinery_info")
public class MachineryInfo {

    /**
     * Identificador único de la información de maquinaria.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_machinery_info", nullable = false)
    private Long id;

    /**
     * Descripción de la maquinaria.
     */
    @Column(name = "description_machinery_info")
    private String description;

    /**
     * Relación uno a uno con la entidad Machinery.
     */
    @OneToOne(mappedBy = "machineryInfo")
    private Machinery machinery;

    /**
     * Constructor por defecto.
     */
    public MachineryInfo() {
        // Método vacío intencionalmente. JPA exige un constructor por defecto para las
        // entidades.
    }

}