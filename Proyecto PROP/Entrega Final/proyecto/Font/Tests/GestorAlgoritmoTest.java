package Font.Tests;

import Font.Exceptions.SimilitudDeProductoNoExistente;
import Font.classes.GestorAlgoritmo;
import Font.classes.Similitud;
import org.junit.*;

import java.util.ArrayList;

//Autor: Daniel Mejias
public class GestorAlgoritmoTest {
    GestorAlgoritmo gestorAlgoritmo;

    @Before
    public void beforeTest() {
        gestorAlgoritmo = new GestorAlgoritmo();
    }

    @After
    public void afterTest() {
        gestorAlgoritmo = null;
    }

    @BeforeClass
    public static void beforeClass() { System.out.println("Comienza el test de GestorAlgoritmo"); }

    @AfterClass
    public static void afterClass() { System.out.println("Termina el test de GestorAlgoritmo"); }

    @Test (expected = SimilitudDeProductoNoExistente.class)
    public void ordenarProductosTest1() throws SimilitudDeProductoNoExistente {
        ArrayList<Integer> productos = new ArrayList<>();
        ArrayList<Similitud> similitudes = new ArrayList<>();
        productos.add(0);
        similitudes.add(new StubSimilitud(1,0,50));
        gestorAlgoritmo.ordenarProductos(1,productos,similitudes);
    }

}