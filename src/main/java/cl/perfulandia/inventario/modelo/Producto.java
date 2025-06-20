package cl.perfulandia.inventario.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Producto.java
 * Este modelo representa un producto en el sistema de inventario.
 * Contiene información básica sobre el producto, como su ID, nombre, descripción, stock, stock mínimo, precio y stock total.
 */
@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;

    private String descripcion;

    private int stock;

    private int stockMinimo; 

    private double precio;

    private int stockTotal;
}


