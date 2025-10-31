# Arriendo de Maquinaria · Spring Boot + Spring Security

Pequeña app demo con **Spring Boot**, **Thymeleaf** y **Spring Security** para mostrar autenticación, vistas públicas/privadas y manejo de errores.

> Ramo: **Seguridad y Calidad en el Desarrollo de Software**  
---

## ✨ Funcionalidades actuales

- **Landing pública**: `GET /landing`
- **Login** (formulario Spring Security): `GET /login`
- **Home privado** (requiere sesión): `GET /home`
- **Logout**: `POST /logout` (enlace con `th:href="@{/logout}"`)
- **Errores personalizados**:
  - `403` Acceso denegado → `templates/error/403.html` *(queda lista; se mostrará cuando exista al menos una ruta con restricción de rol)*  
  - `404` No encontrado → `templates/error/404.html`
  - `error` genérico → `templates/error/error.html`


---

## 🔐 Seguridad

- **Autenticación** por formulario (`/login`) y redirección por defecto a `/home`.
- **Usuarios en memoria** (para pruebas), con contraseñas **BCrypt**:
  | Usuario | Contraseña       | Roles           |
  |--------:|------------------|-----------------|
  | `user`  | `userpassword`   | `USER`          |
  | `owner` | `ownerpassword`  | `OWNER`         |
  | `admin` | `adminpassword`  | `USER`, `ADMIN` |

- **Autorización**:
  - `landing`, `login` y estáticos son públicos.
  - `home` requiere sesión.
  - Páginas de error (`/403`, `/error/**`) son públicas.
  - (Preparado) Reglas por rol para usar más adelante:
    ```java
    .requestMatchers("/admin/**").hasRole("ADMIN")
    .requestMatchers("/owner/**").hasAnyRole("OWNER","ADMIN")
    ```
    > Estas rutas **no existen aún**.

- **Autorización en vistas** (Thymeleaf Security): se usan `sec:authorize` en los botones/menús del `home` para mostrar/ocultar acciones según sesión/rol (p. ej. login vs. logout, mensajes al usuario autenticado, etc.).

---

## 🗺️ Rutas implementadas

- **Públicas**
  - `GET /landing`  
  - `GET /login`  
  - Estáticos: `/style.css`, `/css/**`, `/js/**`, `/images/**`, `/webjars/**`  
  - Errores: `/403`, `/error/**` (servicio de plantillas de error)

- **Privadas**
  - `GET /home` (requiere autenticación)

---

## ▶️ Cómo ejecutar

Requisitos:
- **Java 21** 
- **Maven 4.0.0**

Pasos:
```bash
# Ejecutar
mvn spring-boot:run

# Navegar
http://localhost:8080
```

Login de prueba con cualquiera de los usuarios listados arriba.

---

## ⚠️ Errores personalizados

- **404**: prueba escribiendo una ruta inexistente, por ejemplo `/no-existe`.
- **403**: la plantilla ya está lista. Pero aún no tenemos desarrolladas paginas restringidas. Puedes **mostrar la página** visitando `/403`, pero **no será un “access denied” real** hasta que exista una ruta protegida que dispare la excepción.


---
