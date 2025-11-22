package com.briamcarrasco.arriendomaquinaria.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.CascadeType;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

/**
 * Entidad que representa a un usuario en el sistema.
 * Implementa la interfaz UserDetails para la autenticación y autorización con
 * Spring Security.
 * Los métodos getters y setters para los atributos id, username, password,
 * email, role y userInfo
 * se generan automáticamente mediante la anotación @Data de Lombok.
 */
@Data
@Entity
@Table(name = "tb_users")
public class User implements UserDetails {

    /**
     * Identificador único del usuario.
     */
    @Id
    @Column(name = "id_users", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Nombre de usuario.
     */
    @Column(name = "username_users", unique = true, nullable = false)
    private String username;

    /**
     * Contraseña del usuario.
     */
    @Column(name = "password_users", nullable = false)
    private String password;

    /**
     * Correo electrónico del usuario.
     */
    @Column(name = "email_users", unique = true, nullable = false)
    private String email;

    /**
     * Rol asignado al usuario.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role_users", nullable = false)
    private Role role;

    /**
     * Información adicional asociada al usuario.
     */
    @OneToOne(mappedBy = "user")
    private transient UserInfo userInfo;

        /**
     * Lista de reseñas asociadas al usuario.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private transient List<Review> reviews;

    /**
     * Enum que define los roles posibles de usuario.
     */
    public enum Role {
        ADMIN,
        USER
    }

    

    /**
     * Devuelve la colección de autoridades otorgadas al usuario.
     *
     * @return colección con la autoridad correspondiente al rol del usuario
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    /**
     * Indica si la cuenta del usuario no ha expirado.
     *
     * @return siempre true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica si la cuenta del usuario no está bloqueada.
     *
     * @return siempre true
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica si las credenciales del usuario no han expirado.
     *
     * @return siempre true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica si el usuario está habilitado.
     *
     * @return siempre true
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}