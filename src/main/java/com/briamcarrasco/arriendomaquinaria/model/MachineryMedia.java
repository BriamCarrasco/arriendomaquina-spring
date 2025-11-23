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

@Data
@Table(name = "tb_machinery_media")
@Entity
public class MachineryMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "vid_url")
    private String vidUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machinery_id", nullable = false)
    @JsonIgnore // evitamos enviar el objeto padre para no serializar recursivamente toda la
                // maquinaria
    private Machinery machinery;

}
