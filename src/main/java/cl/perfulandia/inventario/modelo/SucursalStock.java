package cl.perfulandia.inventario.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

/**
 * SucursalStock.java
 * Este modelo representa el stock de un producto en una sucursal específica.
 * Contiene información sobre la sucursal, el producto y la cantidad de stock disponible.
 */
@Entity
@Data
public class SucursalStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long sucursalId;
    @ManyToOne
    @JoinColumn(name = "producto_id")  // Esta columna será la FK a Producto
    private Producto producto;
    private int stock;
}