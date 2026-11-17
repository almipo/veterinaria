# Revisión de cumplimiento — 17/11/2026

**Fuentes:** `teoria.zip` (Sprint 1–7 y 9–14), `sistema_veterinaria.zip` actual. Sprint 8 fue una evaluación en rama paralela. La revisión es estática del código y la configuración; las pruebas de funcionamiento indicadas como "probadas" corresponden a resultados comunicados durante la sesión, no a ejecución en este entorno.

| Sprint | Evaluación | Evidencia y pendientes |
|---|---|---|
| 1 — Kickoff y dominio | Parcial / heredado | Modelos de dominio y Maven presentes. **README raíz ausente** en ZIP recibido; se agrega en este paquete. Completar integrantes/URL. |
| 2 — MVC REST Dueño | Implementado | Controlador Dueño con altas, bajas, modificaciones, consultas y filtros adicionales. |
| 3 — JPA Mascota | Parcial | JPA, entidad Mascota y búsqueda `GET /api/mascotas/duenio/{id}` presentes. No se identificó la ruta anidada histórica `/api/duenos/{id}/mascotas` de la consigna inicial; documentar migración a API del microservicio. |
| 4 — Veterinario y Turno / DTO | Implementado | Controladores, DTO, CRUD veterinario y alta/consultas/estado/cancelación de Turno. |
| 5 — Bean Validation y errores | Implementado en gran parte | Validaciones Jakarta, `GlobalExceptionHandler`, 409 por superposición observado. Revisión fina de todos los casos HTTP pendiente. |
| 6 — Testing | **Parcial** | Cuatro tests de servicios presentes; no se encontraron pruebas MockMvc de controladores. No se ejecutó `mvn test` en esta revisión. |
| 7 — Swagger, frontend y análisis | **Parcial; documentación agregada** | Springdoc y Bootstrap/JS presentes; Swagger observado funcionando. Algunas anotaciones `@ApiResponse`/`@Schema` faltan en Dueño/Turno, y no hay prueba formal de CORS. Faltaba `docs/analisis-monolito.md` (incluido aquí). El frontend actual está dentro de Gateway, no en carpeta raíz como el ejercicio inicial del monolito. |
| 8 — Evaluación | Fuera de alcance | Evaluación en rama paralela según el equipo. |
| 9 — Hexagonal y Maven multi-módulo | Implementado | Raíz Maven `pom` con ocho módulos, puertos, adaptadores, servicios. README de arquitectura se incorpora. |
| 10 — Eureka, Config Server, Actuator | Implementado / pruebas parciales | Configuración centralizada y descubrimiento. Servicios observados ejecutando. Verificar todos los endpoints Actuator y orden de arranque. |
| 11 — OpenFeign | Implementado y probado funcionalmente | Turno consulta Mascota/Veterinario y valida existencia; alta exitosa comunicada. |
| 12 — API Gateway | Implementado | Rutas lb://, frontend en Gateway y filtro logging. Se observaron consultas en :8080. Revisar CORS en entorno diferente de mismo origen. |
| 13 — Resilience4j y Redis | **Parcialmente probado** | Anotaciones Circuit Breaker/Retry en adaptadores Feign, configuración y control de errores 503. Falta demostrar transiciones del circuito con Actuator. Rate Limiter configurado con Redis; **no se comprobó respuesta 429**. La consigna marca Redis obligatorio. |
| 14 — JWT | Implementado y pruebas principales superadas | `ms-auth`, BCrypt, JWT, GlobalFilter reactivo, `X-Auth-User` agregado. Registro, login, 401 sin token/inválido, 200 con token y protección de Dueño observados. No se verificó recepción del encabezado en microservicios (opcional). |

## Prioridades antes de entregar

1. **Obligatorio/prioritario:** iniciar Redis y verificar que Turnos pueda responder a través de Gateway y que una ráfaga excedente produzca 429 (Sprint 13).
2. **Obligatorio/prioritario:** documentar/evidenciar estados del Circuit Breaker por Actuator después de múltiples fallos y recuperación (Sprint 13).
3. **Importante:** ejecutar `mvn test`; completar evidencias y tests de controlador MockMvc cuando el tiempo lo permita (Sprint 6).
4. **Importante:** completar anotaciones OpenAPI específicas del Sprint 7 (`@ApiResponse`, `@Schema`), verificar CORS y CRUD completo del frontend; el login visual está presente, pero no se validó aquí cada operación.
5. **Entrega:** copiar README y análisis del monolito, completar integrantes/URL, agregar capturas y registrar procedimientos de prueba. Hacer commit en Git; no incluir claves reales ni archivos generados de `target/`.
6. **Buenas prácticas, opcional:** restringir `/auth/register` (actualmente se acepta un rol enviado por el solicitante), usar secretos externos, proteger acceso directo a servicios y normalizar las barras finales de rutas.

## Hallazgos de mantenimiento (no bloquear una entrega funcional)

- `config-server/src/main/resources/application.yml` y `.yaml` coexisten; hay copias de configuraciones de servicios tanto en `config/` como directamente en `resources/`. Antes de borrar, comprobar cuál está cargando el Config Server.
- `eureka-server/src/main/resources/application.yml` y `.yaml` coexisten; evitar refactorizar sin una ejecución posterior de verificación.
- En el ZIP hay `.git/`, `.idea/`, directorios `target/`: es preferible excluir los artefactos compilados del ZIP definitivo de entrega cuando no se necesiten.
- Clave JWT y credenciales MySQL declaradas en YAML: aceptable únicamente para un entorno académico aislado; nunca publicarlas como secretos de producción.

## Evidencia de pruebas ya comunicadas

- `ms-auth` login y registro por 8080: HTTP 200.
- `GET /api/mascotas` con JWT: HTTP 200; sin JWT/inválido: 401.
- `GET /api/duenos` con JWT: HTTP 200 (lista vacía); sin JWT: 401.
- Creación de Turno y consulta: exitosa; horario superpuesto: HTTP 409.
- Mascota detenida: Turno devolvió 503 con mensaje de servicio no disponible; al restaurar Mascota, Turno volvió a operar.

**No afirmar todavía:** Circuit Breaker probado en estado OPEN, Retry demostrado por métricas, Rate Limiter 429, todos los tests de Maven en verde o CRUD del frontend completo.
