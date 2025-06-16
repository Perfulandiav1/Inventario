package cl.perfulandia.inventario.controlador;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import cl.perfulandia.inventario.assemblers.ProductoModelAssembler;
import cl.perfulandia.inventario.modelo.Producto;
import cl.perfulandia.inventario.service.ProductoService;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Producto", description = "Operaciones relacionadas con los productos")
public class ProductoController {

    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);
    private final ProductoService productoService;
    private final ProductoModelAssembler productoModelAssembler;

    public ProductoController(ProductoService productoService, ProductoModelAssembler productoModelAssembler) {
        this.productoService = productoService;
        this.productoModelAssembler = productoModelAssembler;
    }

    @Operation(summary = "Listar productos", description = "Obtiene una lista de todos los productos registrados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos obtenida correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Producto.class)))
    })
    @GetMapping("/listar")
    public CollectionModel<EntityModel<Producto>> listarProductos() {
        logger.info("Listando todos los productos");
        List<EntityModel<Producto>> productos = productoService.listarProductos().stream()
            .map(productoModelAssembler::toModel)
            .collect(Collectors.toList());
        if (productos.isEmpty()) {
            logger.warn("No se encontraron productos");
            return CollectionModel.empty();
        }
        logger.info("Total de productos encontrados: {}", productos.size());
        return CollectionModel.of(productos).add(linkTo(methodOn(ProductoController.class).listarProductos()).withSelfRel());
    }
    //
    @Operation(summary = "Obtener producto por ID", description = "Obtiene un producto específico por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto obtenido correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/obtener/{id}")
    public EntityModel<Producto> obtenerProductoPorId(@PathVariable Long id) {
        logger.info("Obteniendo producto con ID: {}", id);
        if (id == null) {
            logger.error("ID de producto no proporcionado");
            throw new IllegalArgumentException("El ID de producto no puede ser nulo");
        }
        Producto producto = productoService.obtenerProductoPorId(id);
        if (producto == null) {
            logger.error("Producto no encontrado con ID: {}", id);
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Producto no encontrado");
        }
        return EntityModel.of(producto)
            .add(linkTo(methodOn(ProductoController.class).listarProductos()).withRel("productos"))
            .add(linkTo(methodOn(ProductoController.class).obtenerProductoPorId(id)).withSelfRel());
    }
    //
    @Operation(summary = "Crear producto", description = "Crea un nuevo producto en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto creado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping("/crear")
    public ResponseEntity<EntityModel<Producto>> crearProducto(@RequestBody Producto producto) {
        logger.info("Creando nuevo producto: {}", producto);
        if (producto == null || producto.getNombre() == null || producto.getPrecio() <= 0) {
            logger.error("Datos de producto inválidos: {}", producto);
            throw new IllegalArgumentException("El nombre y el precio del producto son obligatorios y deben ser válidos");
        }
        Producto nuevoProducto = productoService.crearProducto(producto);
        logger.info("Producto creado correctamente: {}", nuevoProducto);
        // Crear el recurso de producto con HATEOAS
        EntityModel<Producto> productoResource = productoModelAssembler.toModel(nuevoProducto);
        return productoResource.getLink("self")
        .map(link -> ResponseEntity.created(link.toUri()).body(productoResource))
        .orElseGet(() -> ResponseEntity.ok(productoResource));
    }
    //
    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<EntityModel<Producto>> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        logger.info("Actualizando producto con ID: {}", id);
        if (id == null || producto == null) {
            logger.error("ID de producto o datos de actualización no proporcionados");
            throw new IllegalArgumentException("El ID de producto y los datos de actualización son obligatorios");
        }
        Producto productoActualizado = productoService.actualizarProducto(id, producto);
        logger.info("Producto actualizado correctamente: {}", productoActualizado);
        EntityModel<Producto> productoResource = productoModelAssembler.toModel(productoActualizado);
        return ResponseEntity.ok(productoResource);
    }
    //
    @Operation(summary = "Eliminar producto", description = "Elimina un producto del sistema por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        logger.info("Eliminando producto con ID: {}", id);
        if (id == null) {
            logger.error("ID de producto no proporcionado");
            return ResponseEntity.badRequest().build();
        }
        Producto producto = productoService.obtenerProductoPorId(id);
        if (producto == null) {
            logger.error("Producto no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
