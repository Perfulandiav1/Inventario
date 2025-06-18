package cl.perfulandia.inventario.controlador;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import cl.perfulandia.inventario.assemblers.MovimientoModelAssembler;
import cl.perfulandia.inventario.modelo.Movimiento;
import cl.perfulandia.inventario.service.MovimientoService;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/movimientos")
@Tag(name = "Movimientos", description = "Operaciones relacionadas con los movimientos de inventario")
public class MovimientoController {
    private static final Logger logger = LoggerFactory.getLogger(MovimientoController.class);
    @Autowired
    private MovimientoService movimientoService;
    private final MovimientoModelAssembler movimientoAssembler;
    public MovimientoController(MovimientoModelAssembler movimientoAssembler) {
        this.movimientoAssembler = movimientoAssembler;
    }
    // Registrar un nuevo movimiento
    @Operation(summary = "Registrar un nuevo movimiento", description = "Registra un movimiento de inventario para una sucursal y producto específicos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Movimiento registrado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Movimiento.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping("/registrar")
    public ResponseEntity<EntityModel<Movimiento>> registrarMovimiento(@RequestBody Movimiento movimiento) {
        logger.info("Registrando movimiento: {}", movimiento);
        if (movimiento.getSucursalId() == null || movimiento.getProducto() == null || movimiento.getCantidad() <= 0 || movimiento.getTipo() == null) {
            logger.error("Datos de movimiento inválidos: {}", movimiento);
            return ResponseEntity.badRequest().build();
        }
        Movimiento movimientoRegistrado = movimientoService.registrarMovimiento(
            movimiento.getSucursalId(), movimiento.getProducto().getId(), movimiento.getCantidad(), movimiento.getTipo());
        logger.info("Movimiento registrado correctamente: {}", movimientoRegistrado);
        if (movimientoRegistrado == null) {
            logger.error("Error al registrar el movimiento: {}", movimientoRegistrado);
            return ResponseEntity.badRequest().build();
        }
        EntityModel<Movimiento> movimientoResource = movimientoAssembler.toModel(movimientoRegistrado);
        return movimientoResource.getLink("self")
                .map(link -> ResponseEntity.created(link.toUri()).body(movimientoResource))
                .orElseGet(() -> ResponseEntity.ok(movimientoResource));
    }
    
    // Listar todos los movimientos
    @Operation(summary = "Listar todos los movimientos", description = "Obtiene una lista de todos los movimientos registrados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de movimientos obtenida correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Movimiento.class)))
    })
    @GetMapping("/listar")
    public CollectionModel<EntityModel<Movimiento>> listarMovimientos() {
        logger.info("Listando todos los movimientos de inventario");
        List<Movimiento> movimientos = movimientoService.listarMovimientos();
        if (movimientos.isEmpty()) {
            logger.warn("No se encontraron movimientos de inventario");
            return CollectionModel.of(Collections.<EntityModel<Movimiento>>emptyList())
                    .add(linkTo(methodOn(MovimientoController.class).listarMovimientos()).withSelfRel());
        }
        List<EntityModel<Movimiento>> movimientosModel = movimientos.stream()
                .map(movimientoAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(movimientosModel)
                .add(linkTo(methodOn(MovimientoController.class).listarMovimientos()).withSelfRel());
    }

    // Obtener un movimiento por ID
    @Operation(summary = "Obtener un movimiento por ID", description = "Obtiene un movimiento específico por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Movimiento obtenido correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Movimiento.class))),
        @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
    })
    @GetMapping("/obtener/{id}")
    public EntityModel<Movimiento> obtenerMovimiento(@PathVariable Long id) {
        logger.info("Obteniendo movimiento con ID: {}", id);
        if (id == null) {
            logger.error("ID de movimiento no proporcionado");
            return movimientoAssembler.toModel(null)
                    .add(linkTo(methodOn(MovimientoController.class).listarMovimientos()).withSelfRel());
        }
        Movimiento movimiento = movimientoService.obtenerMovimientoPorId(id);
        if (movimiento == null) {
            logger.warn("Movimiento no encontrado con ID: {}", id);
            return movimientoAssembler.toModel(null)
                    .add(linkTo(methodOn(MovimientoController.class).listarMovimientos()).withSelfRel());
        }
        return movimientoAssembler.toModel(movimiento)
                .add(linkTo(methodOn(MovimientoController.class).listarMovimientos()).withSelfRel());
    }
    // Listar movimientos por sucursal
    @Operation(summary = "Listar movimientos por sucursal", description = "Obtiene una lista de movimientos asociados a una sucursal específica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de movimientos obtenida correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Movimiento.class))),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<List<Movimiento>> movimientosPorSucursal(@PathVariable Long sucursalId) {
        logger.info("Listando movimientos para la sucursal con ID: {}", sucursalId);
        if (sucursalId == null) {
            logger.error("ID de sucursal no proporcionado");
            return ResponseEntity.badRequest().build();
        }
        List<Movimiento> movimientos = movimientoService.listarPorSucursal(sucursalId);
        if (movimientos.isEmpty()) {
            logger.warn("No se encontraron movimientos para la sucursal con ID: {}", sucursalId);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(movimientos);
    }
    // Listar movimientos por producto
    @Operation(summary = "Listar movimientos por producto", description = "Obtiene una lista de movimientos asociados a un producto específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de movimientos obtenida correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Movimiento.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Movimiento>> movimientosPorProducto(@PathVariable Long productoId) {
        logger.info("Listando movimientos para el producto con ID: {}", productoId);
        if (productoId == null) {
            logger.error("ID de producto no proporcionado");
            return ResponseEntity.badRequest().build();
        }
        logger.info("Obteniendo movimientos para el producto con ID: {}", productoId);
        List<Movimiento> movimientos = movimientoService.listarPorProducto(productoId);
        if (movimientos.isEmpty()) {
            logger.warn("No se encontraron movimientos para el producto con ID: {}", productoId);
            return ResponseEntity.notFound().build();
        }
        logger.info("Total de movimientos encontrados: {}", movimientos.size());
        return ResponseEntity.ok(movimientos);
    }
    // Eliminar un movimiento por ID
    @Operation(summary = "Eliminar un movimiento por ID", description = "Elimina un movimiento específico por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Movimiento eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarMovimiento(@PathVariable Long id) {
        logger.info("Eliminando movimiento con ID: {}", id);
        if (id == null) {
            logger.error("ID de movimiento no proporcionado");
            return ResponseEntity.badRequest().build();
        }
        Movimiento movimiento = movimientoService.obtenerMovimientoPorId(id);
        if (movimiento == null) {
            logger.warn("Movimiento no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("Eliminando movimiento con ID: {}", id);
        movimientoService.eliminarMovimiento(id);
        return ResponseEntity.noContent().build();
    }
}
