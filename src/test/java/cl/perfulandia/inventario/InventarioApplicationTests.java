package cl.perfulandia.inventario;

import org.junit.jupiter.api.Test;

/**
 * Test para la aplicación de inventario.
 * Verifica que la aplicación se inicie correctamente.
 */
class InventarioApplicationTests {
    /**
     * Verifica que la aplicación de inventario se inicie correctamente.
     * Este test no realiza ninguna aserción, simplemente verifica que no se lance ninguna excepción al iniciar la aplicación.
     */
    @Test
    void main_debeEjecutarSpringApplication() {
        InventarioApplication.main(new String[] {});
    }
}
    