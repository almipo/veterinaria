# Veterinaria-Mascotitas

Sistema de **gestión interna de una clínica veterinaria**, para personal autorizado (recepción y veterinarios).


## Funcionalidades

- Alta, consulta, actualización y eliminación de dueños, mascotas y veterinarios.
- Gestión de turnos: alta, consulta, agenda por veterinario y fecha, historial por mascota, cambios de estado y cancelación.
- Validación de existencia de mascota y veterinario al crear turnos mediante OpenFeign; rechazo de superposiciones de horarios.
- Inicio de sesión interno mediante JWT. El Gateway exige token en las rutas de negocio.
- Frontend en HTML, JavaScript y Bootstrap integrado en Gateway.
- Documentación Swagger de los microservicios de negocio y autenticación.

## Arquitectura

Proyecto Maven multi-módulo con Java 21, Spring Boot 4.1.1 y Spring Cloud 2025.1.3. Cada servicio de negocio emplea una organización hexagonal:

- `domain/model`: entidades y reglas de dominio; `domain/port/in` y `domain/port/out`: interfaces.
- `application`: casos de uso implementados a través de puertos.
- `adapter/in/web`: controladores REST, DTO y validaciones.
- `adapter/out/persistence`: repositorios JPA y adaptación de persistencia.
- `ms-turno/adapter/out/feign`: comunicación HTTP con Mascota y Veterinario.

**Por qué un controlador utiliza un puerto y no la implementación concreta:** reduce el acoplamiento y permite reemplazar la implementación y realizar pruebas unitarias con dobles de prueba, sin modificar el controlador (Sprint 9).

```text
Navegador / Postman
        |
        v
ms-gateway (:8080) -- filtro global JWT, frontend, Rate Limiter (Redis)
        |                         |
        | /auth/**                +-- Eureka (:8761), descubrimiento
        v                         +-- Config Server (:8888), configuraciones
ms-auth (:8085) -> auth_db
        |
        +-- /api/duenos/**       -> ms-duenio (:8081)      -> duenio_db
        +-- /api/mascotas/**     -> ms-mascota (:8082)     -> mascota_db
        +-- /api/veterinarios/** -> ms-veterinario (:8083) -> veterinario_db
        +-- /api/turnos/**      -> ms-turno (:8084)       -> turno_db
                                      | OpenFeign
                                      +--> ms-mascota y ms-veterinario
```

Cada contexto gestiona su propia base de datos; las referencias entre servicios se realizan por identificadores y APIs, no mediante relaciones JPA entre bases de datos.

### Configuración centralizada

El sistema utiliza Spring Cloud Config Server para centralizar las configuraciones de los microservicios mediante archivos YAML locales ubicados en `config-server/src/main/resources/config/`. Cada microservicio obtiene su configuración al iniciar, mientras que Eureka permite descubrir servicios por nombre, sin depender de direcciones IP o puertos fijos en la comunicación interna. Esta implementación corresponde al Sprint 10.

## Requisitos

- JDK 21 y Maven (o wrapper Maven incluido, cuando esté disponible).
- MySQL con acceso a `localhost:3306`.
- Redis en `localhost:6379` **para probar el Rate Limiter del Sprint 13**.
- Puertos 8080–8085, 8761 y 8888 libres.

## Bases de datos

```sql
CREATE DATABASE IF NOT EXISTS duenio_db;
CREATE DATABASE IF NOT EXISTS mascota_db;
CREATE DATABASE IF NOT EXISTS veterinario_db;
CREATE DATABASE IF NOT EXISTS turno_db;
CREATE DATABASE IF NOT EXISTS auth_db;
```

Las conexiones están definidas en `config-server/src/main/resources/config/ms-*.yml`. Ajustar usuario/contraseña de MySQL al entorno local. El uso de `spring.jpa.hibernate.ddl-auto=update` crea/actualiza tablas de bases **ya existentes**, pero no crea bases de datos.

**Seguridad:** en la versión académica actual, la clave JWT y las credenciales de conexión se encuentran en archivos de configuración. Para un despliegue real deben migrarse a variables de entorno/gestor de secretos y rotarse. El endpoint público `/auth/register` permite escoger un rol: es una limitación conocida, no apta para producción sin restringir el alta de administradores. La protección JWT de Gateway no protege accesos directos a los puertos internos.

## Arranque local

En IntelliJ, abrir **el `pom.xml` raíz** y recargar Maven. Iniciar en orden:

1. `ConfigServerApplication` — `http://localhost:8888`.
2. `EurekaServerApplication` — `http://localhost:8761`.
3. `MsDuenioApplication` — 8081.
4. `MsMascotaApplication` — 8082.
5. `MsVeterinarioApplication` — 8083.
6. `MsTurnoApplication` — 8084.
7. `MsAuthApplication` — 8085.
8. `MsGatewayApplication` — 8080.

Los servicios deben aparecer **UP** en Eureka. Iniciar Redis para probar el endpoint de Turnos con limitación de solicitudes. Docker no es obligatorio si MySQL y Redis están instalados localmente; evitar contenedores que publiquen el puerto 8080 cuando Gateway lo utilice.

## Frontend

Abrir `http://localhost:8080/login.html`. El personal inicia sesión con credenciales previamente registradas; el frontend mantiene el JWT en `sessionStorage` durante la sesión de la pestaña y lo envía en `Authorization: Bearer <token>`. Al cerrar sesión se elimina el token de la pestaña. No hay formulario público de registro, acorde al uso interno previsto.

## Endpoints principales

| Función | Ruta (a través de Gateway :8080) |
|---|---|
| Registro / login | `POST /auth/register`, `POST /auth/login` |
| Dueños | `/api/duenos` |
| Mascotas | `/api/mascotas`, `/api/mascotas/duenio/{id}` |
| Veterinarios | `/api/veterinarios`, `/api/veterinarios/especialidad/{especialidad}` |
| Turnos | `/api/turnos`, `/api/turnos/agenda?veterinarioId=1&fecha=2026-11-20` |
| Historial por mascota | `/api/turnos/mascota/{id}` |
| Turnos por veterinario | `/api/turnos/veterinario/{id}` |

Los CRUD y las operaciones disponibles se consultan en Swagger, directamente en `http://localhost:8081/swagger-ui.html` (Dueño), `:8082` (Mascota), `:8083` (Veterinario), `:8084` (Turno), `:8085` (Auth). **Swagger no está centralizado en el puerto 8080.**

Para operaciones protegidas, enviar el JWT válido mediante `Authorization: Bearer <token>`. Usar rutas sin barra final en las colecciones, por ejemplo `/api/mascotas`.

## Verificación y pruebas

- Login y registro vía Gateway: `200` y JWT generado.
- `GET /api/mascotas` sin token o con token inválido: `401`; con token válido: `200`.
- Turno válido: respuesta de creación y persistencia; horario superpuesto del mismo veterinario: `409`.
- Indisponibilidad de Mascota: respuesta controlada `503`; al restaurar el servicio, vuelve a responder.
- **Pendiente de verificar:** transiciones `CLOSED → OPEN → HALF_OPEN → CLOSED` de Resilience4j y Redis Rate Limiter (`429`).
- Pruebas unitarias existentes: `MsDuenio`, `MsMascota`, `MsVeterinario`, `MsTurno` (servicios). Ejecutar `mvn test` desde la raíz y registrar el resultado; no se certificó `BUILD SUCCESS` en este análisis.

## Documentación adicional

- [`docs/revision-sprints.md`](docs/revision-sprints.md): matriz de cumplimiento, evidencias y pendientes.
- [`docs/analisis-monolito.md`](docs/analisis-monolito.md): cinco problemas de diseño requeridos en Sprint 7.
- README de cada módulo: rol, puerto y pruebas básicas.

## Alcance y limitaciones

Proyecto académico funcional en desarrollo local. No incluye despliegue productivo, cierre automático de sesiones en servidor, autorización efectiva por rol ni administración de usuarios desde el frontend. Sprint 8: evaluación realizada en una rama paralela.
