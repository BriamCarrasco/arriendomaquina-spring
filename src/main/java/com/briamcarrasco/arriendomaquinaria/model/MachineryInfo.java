package com.briamcarrasco.arriendomaquinaria.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;

@Data
@Entity
@Table(name = "tb_machinery_info")
public class MachineryInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_machinery_info", nullable = false)
    private Long id;

    @Column(name = "description_machinery_info")
    private String description;

    @OneToOne(mappedBy = "machineryInfo")
    private Machinery machinery;

    public MachineryInfo() {
    }

    
}
