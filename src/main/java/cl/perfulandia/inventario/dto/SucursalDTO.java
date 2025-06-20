package cl.perfulandia.inventario.dto;

import lombok.Data;

/**
 * SucursalDTO.java
 * Este DTO representa una sucursal en el sistema de inventario.
 * Contiene información básica sobre la sucursal, como su ID, nombre, dirección, teléfono y correo electrónico.
 */	
@Data
public class SucursalDTO {
    private Long sucursalId;
    private String nombre;
    private String direccion;
    private String telefono;
    private String correo;
}
