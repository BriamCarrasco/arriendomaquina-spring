package com.briamcarrasco.arriendomaquinaria.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table (name = "tb_usersinfo")
public class UserInfo {

    @Id
    @Column(name = "id_userinfo", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @Column(name = "address", nullable = false, length = 100)
    private String address;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "country", nullable = false, length = 50)
    private String country;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    public UserInfo() {
    }
}
