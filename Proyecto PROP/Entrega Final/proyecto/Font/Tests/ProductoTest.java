package Font.Tests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Assert.*;
import Font.classes.*;

import static org.junit.Assert.*;

public class ProductoTest {
    @BeforeClass
    public static void beforeClass() {
        System.out.println("Inicio del test de Producto");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("Final del test de Producto");
    }

    /* todo Cuando funcione esta función
    @org.junit.Test
    public void setEstanteria() {
    }
     */

    @org.junit.Test
    public void crearProducto1() {
        Producto producto = new Producto(1, "Agua");
        assertEquals("El id debería ser igual a 1", new Integer(1), producto.getId());
        assertEquals("El nombre debería ser igual a Agua", "Agua", producto.getNombre());
    }

    @org.junit.Test
    public void crearProducto2() {
        Producto producto = new Producto(1000, "Leche");
        assertEquals("El id debería ser igual a 1000", new Integer(1000), producto.getId());
        assertEquals("El nombre debería ser igual a Leche", "Leche", producto.getNombre());
    }

/*    @org.junit.Test(expected = )
    public void crearProducto3() {
        Producto producto = new Producto(1, "Agua");
        assertEquals("El id debería ser igual a 1", new Integer(1), producto.getId());
        assertEquals("El nombre debería ser igual a Agua", "Agua", producto.getNombre());
    }

 */

    @org.junit.Test
    public void getNombre() {
        Producto producto = new Producto(1, "Agua");
        assertEquals("El nombre debería ser agua", "Agua", producto.getNombre());
    }

    @org.junit.Test
    public void getId() {
        Producto producto = new Producto(1, "Agua");
        assertEquals("El id debería ser 1", new Integer(1), producto.getId());
    }

    @org.junit.Test
    public void setNombre() {
        Producto producto = new Producto(1, "Agua");
        producto.setNombre("Nuevo");
        assertEquals("El nombre debería ser Nuevo", "Nuevo", producto.getNombre());
    }
}