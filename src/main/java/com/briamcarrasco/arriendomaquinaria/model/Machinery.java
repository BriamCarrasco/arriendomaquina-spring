package com.briamcarrasco.arriendomaquinaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_machinery") 
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Machinery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name_machinery", nullable = false)
    @NotBlank
    private String nameMachinery;

    @Column(name = "category", nullable = false)
    @NotBlank
    private String category;

    @Column(name = "status", nullable = false)
    @NotBlank
    private String status;

    @Column(name = "price_per_day", precision = 12, scale = 2, nullable = false)
    private BigDecimal pricePerDay;
}
