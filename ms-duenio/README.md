# ms-duenio

Dueños: CRUD, búsqueda por email, nombre, apellido y búsqueda combinada.

- Puerto local: **8081**.
- Ruta de comprobación: `http://localhost:8081/api/duenos`.
- Requisitos: iniciar Config Server (`8888`) y Eureka (`8761`) antes de los módulos cliente.
- Configuración: revisar `config-server/src/main/resources/config/ms-duenio.yml`
- El entorno de desarrollo utiliza las bases MySQL definidas en Config Server.
- Swagger UI: `http://localhost:8081/swagger-ui.html`.
- Las rutas de negocio deben consumirse preferentemente a través de Gateway con `Authorization: Bearer <JWT>`.

Consultar [`../README.md`](../README.md) y [`../docs/revision-sprints.md`](../docs/revision-sprints.md) para arquitectura, pruebas y pendientes.
