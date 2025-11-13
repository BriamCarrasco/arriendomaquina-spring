package com.briamcarrasco.arriendomaquinaria.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Entidad que representa una categoría de maquinaria en el sistema.
 * Incluye el identificador, nombre y descripción de la categoría.
 * Los métodos getters y setters para los atributos id, name y description
 * se generan automáticamente mediante la anotación @Data de Lombok.
 */
@Data
@Entity
@Table(name = "tb_category")
public class Category {

    /**
     * Identificador único de la categoría.
     */
    @Id
    @Column(name = "id_category", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de la categoría.
     */
    @Column(name = "name_category", nullable = false, unique = true)
    private String name;

    /**
     * Descripción de la categoría.
     */
    @Column(name = "description_category")
    private String description;

    /**
     * Constructor por defecto.
     * 
     * Este constructor está vacío porque la clase utiliza la anotación @Data de
     * Lombok,
     * que genera automáticamente los métodos getters, setters y otros constructores
     * necesarios.
     * El constructor vacío es requerido por JPA para la creación de instancias
     * mediante reflexión.
     */
    public Category() {
        // Método vacío intencionalmente. JPA exige un constructor por defecto para las
        // entidades.
    }

}