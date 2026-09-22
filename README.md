# VetSystem

Sistema de gestión para la clínica veterinaria "Patitas Felices".

## Parcial 1 — Decisiones de diseño



### Relación Turno–Medicamento

Elegí una relación Many-to-Many porque un turno puede tener varios medicamentos recetados y un mismo medicamento puede aparecer en distintos turnos. La relación la agregué desde la entidad Turno usando una lista de medicamentos y se genera una tabla intermedia llamada turno_medicamento, donde quedan relacionados los ids del turno y del medicamento. No usé CascadeType.ALL porque si se elimina un turno no quiero que también se eliminen los medicamentos, ya que forman parte del catálogo y pueden usarse en otros turnos.

Para actualizar la base de datos mantuve la configuración spring.jpa.hibernate.ddl-auto=update. La elegí porque durante el desarrollo Hibernate puede actualizar automáticamente el esquema a partir de las entidades que voy creando. En este caso permite crear la tabla medicamentos y la tabla intermedia turno_medicamento sin tener que escribir una migración manual. También me sirve porque no elimina los datos que ya tenía cargados en la base. Para este parcial me pareció una solución simple porque el proyecto ya trabaja con JPA e Hibernate.

### Validación de stock

La validación de stock la implementé en la capa de servicio porque es una regla de negocio y no debería quedar en el controller. Antes de asociar un medicamento a un turno busco el medicamento y verifico que su stock sea mayor a cero. Si no tiene stock disponible lanzo una StockInsuficienteException. Esa excepción es manejada por GlobalException y devuelve un código 422 con un ErrorResponse descriptivo. Si el medicamento tiene stock, se agrega al turno y se descuenta una unidad del stock.