package cl.perfulandia.inventario.modelo;
import jakarta.persistence.GeneratedValue;
import java.time.LocalDateTime;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import lombok.Data;

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