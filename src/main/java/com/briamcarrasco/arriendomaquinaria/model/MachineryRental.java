package com.briamcarrasco.arriendomaquinaria.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.Data;
import java.util.Date;

/**
 * Entidad que representa un arriendo de maquinaria en el sistema.
 * Incluye información sobre la fecha de arriendo, fecha de devolución, la
 * maquinaria y el usuario asociado.
 * Los métodos getters y setters para los atributos id, rentalDate, returnDate,
 * machinery y user
 * se generan automáticamente mediante la anotación @Data de Lombok.
 */
@Entity
@Table(name = "tb_machinery_rentals")
@Data
public class MachineryRental {

    /**
     * Identificador único del arriendo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rental", nullable = false)
    private Long id;

    /**
     * Fecha en que inicia el arriendo.
     */
    @Column(name = "rental_date", nullable = false)
    private Date rentalDate;

    /**
     * Fecha en que se debe devolver la maquinaria.
     */
    @Column(name = "return_date", nullable = false)
    private Date returnDate;

    /**
     * Maquinaria asociada al arriendo.
     */
    @ManyToOne
    @JoinColumn(name = "machinery_id", nullable = false)
    Machinery machinery;

    /**
     * Usuario que realiza el arriendo.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

}