# ms-auth

Autenticación: registro, login, BCrypt, JWT y repositorio de usuarios.

- Puerto local: **8085**.
- Ruta de comprobación: `http://localhost:8085/auth/login`.
- Requisitos: iniciar Config Server (`8888`) y Eureka (`8761`) antes de los módulos cliente.
- Configuración: revisar `config-server/src/main/resources/config/ms-auth.yml`
- El entorno de desarrollo utiliza las bases MySQL definidas en Config Server.
- Swagger UI: `http://localhost:8085/swagger-ui.html`.
- Las rutas de negocio deben consumirse preferentemente a través de Gateway con `Authorization: Bearer <JWT>`.

Consultar [`../README.md`](../README.md) y [`../docs/revision-sprints.md`](../docs/revision-sprints.md) para arquitectura, pruebas y pendientes.
