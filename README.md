# VetSystem

Sistema de gestión para la clínica veterinaria "Patitas Felices".

## Parcial 1 — Decisiones de diseño



### Relación Turno–Medicamento

Elegí una relación Many-to-Many porque un turno puede tener varios medicamentos recetados y un mismo medicamento puede aparecer en distintos turnos. La relación la agregué desde la entidad Turno usando una lista de medicamentos y se genera una tabla intermedia llamada turno_medicamento, donde quedan relacionados los ids del turno y del medicamento. No usé CascadeType.ALL porque si se elimina un turno no quiero que también se eliminen los medicamentos, ya que forman parte del catálogo y pueden usarse en otros turnos.

Para actualizar la base de datos mantuve la configuración spring.jpa.hibernate.ddl-auto=update. La elegí porque durante el desarrollo Hibernate puede actualizar automáticamente el esquema a partir de las entidades que voy creando. En este caso permite crear la tabla medicamentos y la tabla intermedia turno_medicamento sin tener que escribir una migración manual. También me sirve porque no elimina los datos que ya tenía cargados en la base. Para este parcial me pareció una solución simple porque el proyecto ya trabaja con JPA e Hibernate.

### Validación de stock

La validación de stock la implementé en la capa de servicio porque es una regla de negocio y no debería quedar en el controller. Antes de asociar un medicamento a un turno busco el medicamento y verifico que su stock sea mayor a cero. Si no tiene stock disponible lanzo una StockInsuficienteException. Esa excepción es manejada por GlobalException y devuelve un código 422 con un ErrorResponse descriptivo. Si el medicamento tiene stock, se agrega al turno y se descuenta una unidad del stock.

### Cupo de mascotas

La validación del cupo de mascotas la hice en la capa de servicio antes de guardar una nueva mascota. Consulto cuántas mascotas tiene asociadas actualmente el dueño mediante el repository. Como el modelo actual no tiene un campo para marcar una mascota como inactiva, considero activas a las mascotas que siguen registradas en la base de datos. Si el dueño ya tiene 5 mascotas, se lanza una CupoMascotasException y no se guarda la nueva mascota. Esta excepción es manejada por GlobalException y devuelve un código 422 con un mensaje indicando que se alcanzó el límite permitido.

### Solapamiento

La validación de solapamiento la hice en la capa de servicio antes de guardar un nuevo turno. Consulto si el veterinario ya tiene un turno en la misma fecha y a la misma hora. Si existe un turno en ese horario, no se guarda el nuevo turno y se lanza una TurnoSuperpuestoException. En el mensaje de error incluyo el id, la fecha y la hora del turno que está generando el conflicto. Esta excepción es manejada por GlobalException y devuelve un código 409 Conflict.

### Decisión más difícil

La parte que más me costó fue relacionar los medicamentos con los turnos y manejar al mismo tiempo el control del stock. Tuve que decidir dónde colocar esa lógica para no dejarla directamente en el controller. La termine implementando en TurnoService porque la asociación del medicamento y la validación del stock forman parte de la lógica de negocio. También tuve que asegurarme de que cuando el stock es cero no se haga la asociación y se devuelva un error 422. Cuando hay stock disponible, el medicamento se agrega al turno y se descuenta una unidad.