package cl.perfulandia.inventario.dto;

import lombok.Data;

@Data
public class SucursalDTO {
    private Long sucursalId;
    private String nombre;
    private String direccion;
    private String telefono;
    private String correo;
}
