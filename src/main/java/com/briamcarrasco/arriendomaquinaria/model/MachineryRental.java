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


@Entity
@Table(name = "tb_machinery_rentals")
@Data
public class MachineryRental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rental", nullable = false)
    private Long id;

    @Column(name = "rental_date", nullable = false)
    private Date rentalDate;

    @Column(name = "return_date", nullable = false)
    private Date returnDate;

    @ManyToOne
    @JoinColumn(name = "machinery_id", nullable = false)
    Machinery machinery;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;



}