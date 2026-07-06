# Minimarket Plus Backend

Backend desarrollado con Spring Boot para la gestión del sistema **Minimarket Plus**, el cual implementa una arquitectura REST con autenticación mediante JWT y documentación automática utilizando OpenAPI (Swagger).

## Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- JWT (JSON Web Token)
- SpringDoc OpenAPI
- Swagger UI
- Maven
- H2 Database (o la base de datos configurada)

## Funcionalidades

El sistema permite administrar:

- Usuarios
- Productos
- Categorías
- Inventario
- Carrito de compras
- Ventas
- Detalle de ventas
- Autenticación de usuarios mediante JWT

## Ejecución del proyecto

1. Clonar el repositorio.

```bash
git clone https://github.com/CristianJTM/Exp3_S7_Grupo11_DESARROLLO-BACKEND-II_002A.git
```

2. Ingresar al proyecto.

```bash
cd Exp3_S7_Grupo11_DESARROLLO-BACKEND-II_002A
```

3. Ejecutar la aplicación desde el IDE o mediante Maven.

```bash
mvn spring-boot:run
```

También puede ejecutarse desde la clase:

```
MinimarketApplication.java
```

## Acceso a Swagger UI

Con la aplicación en ejecución, ingresar a:

```
http://localhost:8080/swagger-ui/index.html
```

Desde Swagger UI es posible:

- Consultar todos los endpoints disponibles.
- Visualizar parámetros y respuestas.
- Probar los servicios REST.
- Revisar los modelos (Schemas) utilizados por la API.

## Documento OpenAPI

La especificación OpenAPI puede obtenerse desde:

```
http://localhost:8080/v3/api-docs
```

Este archivo puede importarse directamente en Postman para validar la consistencia de los endpoints.

## Seguridad

La aplicación utiliza autenticación basada en JSON Web Token (JWT) para proteger los recursos de la API.

### Endpoints públicos

Los siguientes endpoints pueden accederse sin autenticación:

- `/auth/**` (inicio de sesión y registro de usuarios)
- `/public/**`
- `/h2-console/**`
- `/v3/api-docs/**`
- `/swagger-ui/**`
- `/swagger-ui.html`

### Control de acceso por roles

| Endpoint | Rol requerido |
|----------|---------------|
| `/api/usuarios/**` | ADMINISTRADOR |
| `/api/categorias/**` | ADMINISTRADOR |
| `/api/productos/**` | CAJERO o ADMINISTRADOR |
| `/api/inventario/**` | CAJERO o ADMINISTRADOR |
| `/api/ventas/**` | CAJERO o ADMINISTRADOR |
| `/api/detalle-ventas/**` | CAJERO o ADMINISTRADOR |
| `/api/carrito/**` | CLIENTE, CAJERO o ADMINISTRADOR |

Todos los demás endpoints requieren autenticación.

## Estructura del proyecto

```
src
├── main
│   ├── java
│   │   └── com.minimarket
│   │       ├── controller
│   │       ├── entity
│   │       ├── repository
│   │       ├── security
│   │       ├── service
│   │       └── util
│   └── resources
└── test
    └── java
```

## Documentación

La documentación de la API fue generada utilizando **SpringDoc OpenAPI**, incluyendo:

- Descripción de la API.
- Documentación de todos los endpoints REST.
- Parámetros de entrada.
- Respuestas HTTP.
- Modelos de datos (Schemas).

