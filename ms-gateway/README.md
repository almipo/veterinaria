# ms-gateway

Punto de entrada del sistema **Veterinaria-Mascotitas**. Se encarga del enrutamiento de solicitudes, la autenticación JWT, la limitación de peticiones mediante Redis y el acceso al frontend.

## Configuración

- **Puerto:** `8080`.
- **Frontend:** http://localhost:8080/login.html
- **Config Server:** puerto `8888`.
- **Eureka Server:** puerto `8761`.
- **Archivo de configuración centralizada:** `config-server/src/main/resources/config/ms-gateway.yml`.
- **Redis:** necesario para el Rate Limiter de Turnos.

## Funcionalidades

- Enrutamiento hacia los microservicios mediante Eureka.
- Validación de tokens JWT mediante un `GlobalFilter` reactivo.
- Protección de los endpoints de negocio.
- Acceso público a los endpoints `/auth/**`.
- Limitación de solicitudes de Turnos mediante Redis.
- Alojamiento del frontend estático.

## Integración del frontend

El frontend utiliza rutas relativas, como `/api/mascotas` y `/api/duenos`, para comunicarse con los microservicios a través de Gateway.

Esto permite cambiar el puerto de Gateway sin modificar las direcciones utilizadas en JavaScript.

Las solicitudes protegidas incorporan el token JWT mediante el encabezado:

`Authorization: Bearer <token>`

## Ejecución

1. Iniciar Config Server.
2. Iniciar Eureka Server.
3. Iniciar los microservicios de negocio y `ms-auth`.
4. Iniciar `ms-gateway`.
5. Acceder al frontend desde http://localhost:8080/login.html.

## Documentación adicional

Consultar el [README general](../README.md) y la [revisión de sprints](../docs/revision-sprints.md) para conocer la arquitectura, las pruebas y los requisitos pendientes.