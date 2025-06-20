package cl.perfulandia.inventario.dto;

import lombok.Data;

/**
 * MovimientoRequest.java
 * Este DTO representa una solicitud para registrar un movimiento de inventario.
 * Contiene información sobre la sucursal, el producto, la cantidad y el tipo de movimiento.
 */	
@Data
public class MovimientoRequest {
    private Long sucursalId;
    private Long productoId;
    private Integer cantidad;
    private String tipo;  // "ENTRADA" o "SALIDA"
}
