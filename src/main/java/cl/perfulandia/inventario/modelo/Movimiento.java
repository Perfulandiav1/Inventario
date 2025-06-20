package cl.perfulandia.inventario.modelo;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

/**
 * Movimiento.java
 * Este modelo representa un movimiento de inventario en el sistema.
 * Contiene información sobre el producto, la sucursal, la cantidad, el tipo de movimiento (entrada o salida) y la fecha y hora del movimiento.
 */
@Entity
@Data
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    private Long sucursalId;  // ID de la sucursal
    private int cantidad;
    private String tipo; // "ENTRADA" o "SALIDA"
    private LocalDateTime fechaHora;
}