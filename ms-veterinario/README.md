# ms-veterinario

Veterinarios: CRUD y búsqueda por especialidad.

- Puerto local: **8083**.
- Ruta de comprobación: `http://localhost:8083/api/veterinarios`.
- Requisitos: iniciar Config Server (`8888`) y Eureka (`8761`) antes de los módulos cliente.
- Configuración: revisar `config-server/src/main/resources/config/ms-veterinario.yml`
- El entorno de desarrollo utiliza las bases MySQL definidas en Config Server.
- Swagger UI: `http://localhost:8083/swagger-ui.html`.
- Las rutas de negocio deben consumirse preferentemente a través de Gateway con `Authorization: Bearer <JWT>`.

Consultar [`../README.md`](../README.md) y [`../docs/revision-sprints.md`](../docs/revision-sprints.md) para arquitectura, pruebas y pendientes.
