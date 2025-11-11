# Arriendo de Maquinaria · Spring Boot + Spring Security

Aplicación web demo con **Spring Boot**, **Thymeleaf**, **Spring Security** y **JWT** para el alquiler de maquinaria agrícola. Incluye autenticación, gestión de inventario, alquileres, y correcciones de seguridad basadas en OWASP Top 10.

> Ramo: **Seguridad y Calidad en el Desarrollo de Software**  
---

## ✨ Funcionalidades actuales

- **Páginas Públicas**:
  - Landing: `GET /landing` – Pantalla de bienvenida con características.
  - Búsqueda: `GET /search` – Navegar maquinaria por nombre/categoría.
  - Detalles de Maquinaria: `GET /machinerydetail?id=X` – Ver especificaciones, imágenes y estado.

- **Autenticación**:
  - Login: `GET /login` – Formulario con JWT.
  - Logout: `POST /logout` – Limpia sesión.

- **Páginas Privadas** (requiere login):
  - Home: `GET /home` – Dashboard con nombre de usuario y acciones rápidas (buscar, publicar, panel admin).
  - Publicar Maquinaria: `GET /postmachinery` – Formulario para agregar equipo nuevo con categorías/estados.
  - API Admin de Maquinaria: `GET /api/machinery` – Operaciones CRUD para admins (listar, agregar, editar, eliminar).

- **Inicialización de Datos**: Al iniciar, se siembran usuarios (admin, user1, user2), categorías (Tractores, Cosechadoras), estados (disponible, arrendada) y maquinaria/alquileres de muestra.

- **Tecnologías**: Spring Boot, Thymeleaf, Spring Security, JWT, MySQL, Bootstrap (autoalojado).

---

## 🔐 Seguridad

- **Autenticación**: Formulario (`/login`) con JWT stateless. Redirección por defecto a `/home`.
- **Usuarios en Base de Datos** (MySQL), con contraseñas **BCrypt**:
  | Usuario | Contraseña | Roles           |
  |--------:|------------|-----------------|
  | `admin` | `password` | `ADMIN`         |
  | `user1` | `password` | `USER`          |
  | `user2` | `password` | `USER`          |

- **Autorización**:
  - Páginas públicas: `landing`, `search`, `machinerydetail`, estáticos, errores.
  - Privadas: `home`, `postmachinery`, `/api/machinery` (requiere `ADMIN`).
  - Protección CSRF con cookies SameSite=Strict.
  - Content Security Policy (CSP) estricta para prevenir XSS.

- **Correcciones OWASP Top 10** (escaneadas con ZAP Proxy):
  1. **CSP: Directiva Wildcard (A05)**: Política estricta sin comodines.
  2. **Cookie sin HttpOnly (A05)**: Cookies JWT con HttpOnly=true.
  3. **Cookie sin SameSite (A01)**: Atributo SameSite=Strict agregado.
  4. **Inclusión de JS entre Dominios (A08)**: Recursos autoalojados, sin externos.

- **Autorización en Vistas**: Usando `sec:authorize` en Thymeleaf para mostrar/ocultar elementos según rol.

---

## 🗺️ Rutas implementadas

- **Públicas**
  - `GET /landing`
  - `GET /search`
  - `GET /machinerydetail?id=X`
  - `GET /login`
  - Estáticos: `/style.css`, `/css/**`, `/js/**`, `/images/**`, `/webjars/**`
  - Errores: `/403`, `/404`, `/error/**`

- **Privadas**
  - `GET /home` (requiere autenticación)
  - `GET /postmachinery` (requiere autenticación)
  - `GET /api/machinery` (requiere `ADMIN`)

---

## ▶️ Cómo ejecutar

Requisitos:
- **Java 21**
- **Maven 4.0.0**
- **MySQL** (configurado en `application.properties`)

### Opción 1: Ejecutar manualmente
```bash
# Ejecutar
mvn spring-boot:run

# Navegar
http://localhost:8080
```

### Opción 2: Usar script de variables de entorno (más fácil)
Para configurar automáticamente las variables de entorno (JWT_SECRET_KEY, DB_URL, DB_USER, DB_PASS, SERVER_PORT=8084) y ejecutar:
```powershell
# Ejecutar el script PowerShell
.\variablesentorno.ps1

# Navegar
http://localhost:8084
```

Login de prueba con cualquiera de los usuarios listados arriba.

---

## ⚠️ Errores personalizados

- **404**: Prueba rutas inexistentes, ej. `/no-existe`.
- **403**: Plantilla lista; visita `/403` para ver, o se dispara en rutas protegidas.
- **Error genérico**: `/error`.

---
