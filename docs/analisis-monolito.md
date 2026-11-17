# Sprint 7 — Análisis del monolito

El sistema comenzó como una aplicación unificada. Al evolucionar hacia microservicios, los principales problemas previstos fueron:

1. **Escalar solamente Turnos.** En un monolito se escala todo el proceso, incluso módulos que no lo necesitan. Separar `ms-turno` permite dimensionar su capacidad de forma independiente, aunque añade latencia y comunicación de red.
2. **Falla en Veterinarios.** Un error grave en un monolito puede afectar el mismo proceso de Dueños, Mascotas y Turnos. En microservicios el aislamiento reduce el alcance, pero Turnos depende de Veterinario: por eso se usan Feign, timeouts, Retry y Circuit Breaker.
3. **Elegir tecnologías de persistencia distintas.** En un monolito las tablas suelen compartir una base y esquema. Los microservicios permiten que cada contexto gestione su almacenamiento (por ejemplo, MySQL para Dueños y otro motor para historial), a costa de consistencia distribuida y coordinación entre servicios. **El proyecto actual utiliza MySQL en todos los contextos; MongoDB es solo un ejemplo de evolución, no una implementación existente.**
4. **Dos equipos trabajando en paralelo.** Modificaciones sobre la misma aplicación aumentan los conflictos de código y despliegue. Las fronteras entre servicios y contratos HTTP facilitan el trabajo paralelo, pero obligan a coordinar versiones de APIs y pruebas de integración.
5. **Desplegar solo cambios de Mascota.** En el monolito se recompila y despliega toda la aplicación. En microservicios se puede desplegar Mascota por separado, controlando compatibilidad de sus contratos con Turnos y disponibilidad durante el cambio.

**Conclusión:** la arquitectura de microservicios mejora el aislamiento, la escalabilidad selectiva y la autonomía de despliegue, a cambio de mayor complejidad operativa (Eureka, Config Server, Gateway, monitoreo, tolerancia a fallos y seguridad distribuida). Esta justificación responde a las cinco preguntas del Sprint 7.
