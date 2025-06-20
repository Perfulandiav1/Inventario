package cl.perfulandia.inventario.modelo;
import jakarta.persistence.GeneratedValue;
import java.time.LocalDateTime;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import lombok.Data;

/**
 * AlertaInventario.java
 * Este modelo representa una alerta de inventario en el sistema.
 * Contiene información sobre el producto, la sucursal, el stock actual, un mensaje y la fecha y hora de la alerta.
 */	
@Entity
@Data
public class AlertaInventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productoId;
    private Long sucursalId;
    private int stockActual;
    private String mensaje;
    private LocalDateTime fechaHora;
}