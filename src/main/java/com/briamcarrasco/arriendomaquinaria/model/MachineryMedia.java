package com.briamcarrasco.arriendomaquinaria.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entidad que representa un archivo multimedia asociado a una maquinaria.
 * Permite almacenar la URL de una imagen o video y la relación con la
 * maquinaria correspondiente.
 */
@Data
@Table(name = "tb_machinery_media")
@Entity
public class MachineryMedia {

    /**
     * Identificador único del archivo multimedia.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /**
     * URL de la imagen asociada.
     */
    @Column(name = "img_url")
    private String imgUrl;

    /**
     * URL del video asociado.
     */
    @Column(name = "vid_url")
    private String vidUrl;

    /**
     * Maquinaria a la que pertenece el archivo multimedia.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machinery_id", nullable = false)
    @JsonIgnore
    private Machinery machinery;

}