package com.briamcarrasco.arriendomaquinaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.math.BigDecimal;

/**
 * Entidad que representa una maquinaria en el sistema.
 * Incluye información como nombre, estado, precio por día, categoría y detalles
 * adicionales.
 * Los métodos getters y setters para los atributos id, nameMachinery, status,
 * pricePerDay, category y machineryInfo
 * se generan automáticamente mediante las anotaciones de Lombok.
 */
@Entity
@Table(name = "tb_machinery")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Machinery {

    /**
     * Identificador único de la maquinaria.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Nombre de la maquinaria.
     */
    @Column(name = "name_machinery", nullable = false)
    @NotBlank
    private String nameMachinery;

    /**
     * Estado de la maquinaria.
     */
    @Column(name = "status", nullable = false)
    @NotBlank
    private String status;

    /**
     * Precio por día de arriendo de la maquinaria.
     */
    @Column(name = "price_per_day", precision = 12, scale = 2, nullable = false)
    private BigDecimal pricePerDay;

    /**
     * URL de la imagen de la maquinaria.
     */
    @Column(name = "image_url")
    private String imageUrl;

    /**
     * Categoría a la que pertenece la maquinaria.
     */
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /**
     * Información adicional de la maquinaria.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "machinery_info_id", nullable = true)
    private MachineryInfo machineryInfo;
}