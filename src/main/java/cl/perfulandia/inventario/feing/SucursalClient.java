package cl.perfulandia.inventario.feing;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import cl.perfulandia.inventario.dto.SucursalDTO;

/**
 * SucursalClient.java
 * Este cliente Feign se utiliza para interactuar con el servicio de sucursales.
 * Proporciona métodos para obtener todas las sucursales y buscar una sucursal por su ID.
 */
@FeignClient(name = "sucursal-service", url = "http://localhost:8080")
public interface SucursalClient {
    /*
     * Obtiene todas las sucursales disponibles.
     * @return Una lista de SucursalDTO que representa todas las sucursales.
     */
    @GetMapping("/api/sucursal")
    List<SucursalDTO> obtenerTodasSucursales();

    /*
     * Obtiene una sucursal específica por su ID.
     * @param id El ID de la sucursal a buscar.
     * @return Un SucursalDTO que representa la sucursal encontrada.
     */
    @GetMapping("/api/sucursal/{id}")
    SucursalDTO obtenerSucursalPorId(@PathVariable("id") Long id);
}


