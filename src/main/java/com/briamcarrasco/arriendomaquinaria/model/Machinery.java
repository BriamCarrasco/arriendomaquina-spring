package com.briamcarrasco.arriendomaquinaria.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "maquinaria") //tabla en MySQL
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Machinery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String categoria;
    private String estado;

    @Column(name = "precio_dia", precision = 12, scale = 2)
    private BigDecimal precioDia;
}
