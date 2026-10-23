# UD3 User Role CRUD

Proyecto desarrollado con Spring Boot para gestionar usuarios y roles mediante una relación muchos a muchos.

## Tecnologías

- Java 21
- Spring Boot
- Thymeleaf
- JdbcTemplate
- MariaDB
- Docker
- Maven
- Bootstrap

## Funcionalidades

- CRUD completo de usuarios.
- CRUD completo de roles.
- Asignación de uno o varios roles a cada usuario.
- Relación muchos a muchos mediante la tabla `user_roles`.
- Validaciones de formularios.
- Control de usuarios y correos duplicados.
- Control de nombres de roles duplicados.

## Base de datos

La aplicación utiliza MariaDB mediante Docker.

Para levantar la base de datos:

```bash
docker compose up -d
