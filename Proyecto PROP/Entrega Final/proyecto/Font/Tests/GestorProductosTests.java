package Font.Tests;

import Font.Exceptions.NoExisteProducto;
import Font.classes.*;
import Font.Exceptions.NoExisteNingunProducto;
import Font.Exceptions.YaExisteProducto;
import org.junit.*;

import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.mockito.*;

public class GestorProductosTests {
    @Mock Producto prodMock1 = Mockito.mock(Producto.class);
    @Mock Producto prodMock2 = Mockito.mock(Producto.class);
    @Mock Producto prodMock3 = Mockito.mock(Producto.class);
    HashMap<String, Producto> productos;
    GestorProductos gPrueba = new GestorProductos();

    @Before
    public void beforeTest ()
    {
        System.out.println("Siguiente test:");
        productos = new HashMap<String, Producto>();
        productos.put("p1", prodMock1);
        productos.put("p2", prodMock2);
        productos.put("p3", prodMock3);
        gPrueba.setProductos(productos);
    }

    @BeforeClass
    public static void beforeClass()
    {
        System.out.println("Inicio test GestorProducto");
    }

    @AfterClass
    public static void afterClass()
    {
        System.out.println("Final test GestorProducto");
    }

    @Test
    public void existeAlgunProducto() {
        GestorProductos gPrueba2 = new GestorProductos();
        assertFalse("No tendria que existir ningun producto", gPrueba2.existeAlgunProducto());
    }

    @Test
    public void existeAlgunProducto2() {
        assertTrue("Tendria que existir algun producto", gPrueba.existeAlgunProducto());
    }

    @Test
    public void existeProducto1() {
        assertTrue("Tendria que existir algun producto", gPrueba.existeProducto("p1"));
    }

    @Test
    public void existeProducto2() {
        assertFalse("Tendria que existir algun producto", gPrueba.existeProducto("leche"));
    }

    @Test
    public void getListaProductos() {
        assertEquals("Los dos conjuntos deberían ser iguales", productos, gPrueba.getListaProductos());
    }

    @Test
    public void crearProducto() throws YaExisteProducto, NoExisteProducto, NoExisteNingunProducto {
        gPrueba.crearProducto("leche");
        Integer id = gPrueba.getProducto("leche").getId();
        assertTrue("Se tendria que haber creado un producto", gPrueba.existeProducto("leche"));
        assertEquals(0, (int) id);
    }

    @Test (expected = YaExisteProducto.class)
    public void crearProducto2() throws YaExisteProducto {
        gPrueba.crearProducto("leche");
        gPrueba.crearProducto("leche");
    }

    @Test
    public void eliminarProducto() throws NoExisteProducto, NoExisteNingunProducto {
        gPrueba.eliminarProducto("p1");
    }

    @Test (expected = NoExisteNingunProducto.class)
    public void eliminarProducto2() throws NoExisteProducto, NoExisteNingunProducto {
        GestorProductos gPrueba2 = new GestorProductos();
        gPrueba2.eliminarProducto("leche");
    }

    @Test (expected = NoExisteProducto.class)
    public void eliminarProducto3() throws NoExisteProducto, NoExisteNingunProducto {
        gPrueba.eliminarProducto("leche");
    }

    @Test
    public void getProducto1() throws NoExisteProducto, NoExisteNingunProducto {
        gPrueba.getProducto("p1");
    }

    @Test (expected = NoExisteNingunProducto.class)
    public void getProducto2() throws NoExisteProducto, NoExisteNingunProducto {
        GestorProductos gPrueba2 = new GestorProductos();
        gPrueba2.getProducto("leche");
    }

    @Test (expected = NoExisteProducto.class)
    public void getProducto3() throws NoExisteProducto, NoExisteNingunProducto {
        gPrueba.getProducto("leche");
    }

    /*
    @Test
    public void cargarProductos() {
    }

    @Test
    public void guardarProductos() {
    }
    */
}