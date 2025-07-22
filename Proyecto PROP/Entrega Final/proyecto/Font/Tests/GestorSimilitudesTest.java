// autor: Christian Conejo Raubiene

package Font.Tests;

import Font.Exceptions.NoExisteNingunProducto;
import Font.Exceptions.NoExisteProducto;
import Font.Exceptions.ValorSimilitudFueraRango;
import Font.classes.GestorSimilitudes;
import static org.junit.Assert.*;
import Font.Exceptions.YaExisteProducto;

import Font.classes.Similitud;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

public class GestorSimilitudesTest
{

    @BeforeClass
    public static void beforeClass()
    {
        System.out.println("Inicio GestorSimilitudesTests");
    }

    @AfterClass
    public static void afterClass()
    {
        System.out.println("Final GestorSimilitudesTests");
    }

    @Before
    public void beforeTest () throws YaExisteProducto
    {
        System.out.println("Siguiente test:");
    }

    @Test
    public void crearYeliminarSimilitudesDeProductoTest1() throws YaExisteProducto, NoExisteProducto
    {
        GestorSimilitudes gS =  GestorSimilitudes.getInstance();
        gS.crearSimilitudesDeProducto(0);
        gS.crearSimilitudesDeProducto(1);
        gS.crearSimilitudesDeProducto(2);
        assertNotNull(gS);
        gS.eliminarSimilitudesDeProducto(0);
        gS.eliminarSimilitudesDeProducto(1);
        gS.eliminarSimilitudesDeProducto(2);
    }

    @Test (expected = YaExisteProducto.class)
    public void crearSimilitudesDeProductoTestError() throws YaExisteProducto
    {
        GestorSimilitudes gS = GestorSimilitudes.getInstance();
        gS.crearSimilitudesDeProducto(0);
        gS.crearSimilitudesDeProducto(1);
        gS.crearSimilitudesDeProducto(1);
    }

    @Test (expected = NoExisteProducto.class)
    public void eliminarSimilitudesDeProductoTestError() throws NoExisteProducto, YaExisteProducto
    {
        GestorSimilitudes gS = GestorSimilitudes.getInstance();
        gS.crearSimilitudesDeProducto(0);
        gS.eliminarSimilitudesDeProducto(0);
        gS.eliminarSimilitudesDeProducto(0);
    }

    @Test
    public void obtenerSimilitudTest1() throws YaExisteProducto, NoExisteProducto
    {
        GestorSimilitudes gS = GestorSimilitudes.getInstance();
        gS.crearSimilitudesDeProducto(0);
        gS.crearSimilitudesDeProducto(1);
        gS.cambiarSimilitud(0, 1, 25);

        Integer f = gS.obtenerSimilitud(0, 1);
        assertEquals(25, (int) f);
    }

    @Test
    public void obtenerSimilitudTest2() throws YaExisteProducto, NoExisteProducto
    {
        GestorSimilitudes gS = GestorSimilitudes.getInstance();
        gS.crearSimilitudesDeProducto(0);

        Integer f = gS.obtenerSimilitud(0, 0);
        assertEquals(0, (int) f);
    }

    @Test (expected = NoExisteProducto.class)
    public void obtenerSimilitudTest3() throws YaExisteProducto, NoExisteProducto
    {
        GestorSimilitudes gS = GestorSimilitudes.getInstance();
        gS.crearSimilitudesDeProducto(0);

        Integer f = gS.obtenerSimilitud(0, 100000);
    }

    @Test (expected = NoExisteProducto.class)
    public void obtenerSimilitudTest4() throws YaExisteProducto, NoExisteProducto
    {
        GestorSimilitudes gS = GestorSimilitudes.getInstance();
        gS.crearSimilitudesDeProducto(0);

        Integer f = gS.obtenerSimilitud(10, 0);
    }

    @Test
    public void cambiarSimilitudTest1() throws YaExisteProducto, NoExisteProducto//Hacer uno donde ponga valores erroneos de S, <0 y >1
    {
        GestorSimilitudes gS = GestorSimilitudes.getInstance();
        gS.crearSimilitudesDeProducto(0);
        gS.crearSimilitudesDeProducto(1);
        gS.cambiarSimilitud(0, 1, 25);

        gS.cambiarSimilitud(0, 1, 70);
        Integer f = gS.obtenerSimilitud(0, 1);
        assertEquals(70, (int) f);
    }

    @Test (expected = NoExisteProducto.class)
    public void cambiarSimilitudTest2() throws YaExisteProducto, NoExisteProducto//Hacer uno donde ponga valores erroneos de S, <0 y >1
    {
        GestorSimilitudes gS = GestorSimilitudes.getInstance();
        gS.crearSimilitudesDeProducto(0);

        gS.cambiarSimilitud(0, 1000, 7);
    }

    @Test (expected = NoExisteProducto.class)
    public void cambiarSimilitudTest3() throws YaExisteProducto, NoExisteProducto//Hacer uno donde ponga valores erroneos de S, <0 y >1
    {
        GestorSimilitudes gS = GestorSimilitudes.getInstance();
        gS.crearSimilitudesDeProducto(0);

        gS.cambiarSimilitud(10000, 0, 7);
    }

    @Test (expected = ValorSimilitudFueraRango.class)
    public void cambiarSimilitudTest4() throws YaExisteProducto, NoExisteProducto, ValorSimilitudFueraRango//Hacer uno donde ponga valores erroneos de S, <0 y >1
    {
        GestorSimilitudes gS = GestorSimilitudes.getInstance();
        gS.crearSimilitudesDeProducto(0);
        gS.crearSimilitudesDeProducto(1);
        gS.cambiarSimilitud(0, 1, -2);
    }

    @Test (expected = ValorSimilitudFueraRango.class)
    public void cambiarSimilitudTest5() throws YaExisteProducto, NoExisteProducto, ValorSimilitudFueraRango//Hacer uno donde ponga valores erroneos de S, <0 y >1
    {
        GestorSimilitudes gS = GestorSimilitudes.getInstance();
        gS.crearSimilitudesDeProducto(0);
        gS.crearSimilitudesDeProducto(1);
        gS.cambiarSimilitud(0, 1, 101);
    }

    @Test
    public void obtenerTodasSimilitudesTest1() throws NoExisteNingunProducto, YaExisteProducto, NoExisteProducto, ValorSimilitudFueraRango
    {
        GestorSimilitudes gS = GestorSimilitudes.getInstance();
        gS.crearSimilitudesDeProducto(0);
        gS.crearSimilitudesDeProducto(1);
        gS.crearSimilitudesDeProducto(2);
        gS.cambiarSimilitud(0, 1, 1);
        gS.cambiarSimilitud(0, 2, 10);
        gS.cambiarSimilitud(1, 2, 100);

        ArrayList <Similitud> arrayProductesAux = gS.obtenerTodasSimilitudes();
        assertNotNull(arrayProductesAux);
        assertFalse(arrayProductesAux.isEmpty());
    }

    @Test
    public void obtenerTodasSimilitudesTest2() throws NoExisteNingunProducto, YaExisteProducto, ValorSimilitudFueraRango
    {
        GestorSimilitudes gS = GestorSimilitudes.getInstance();
        gS.crearSimilitudesDeProducto(0);
        gS.crearSimilitudesDeProducto(1);
        gS.crearSimilitudesDeProducto(2);

        ArrayList <Similitud> arrayProductesAux = gS.obtenerTodasSimilitudes();
        assertNotNull(arrayProductesAux);
        assertTrue(arrayProductesAux.isEmpty());
    }

    @Test (expected = NoExisteNingunProducto.class)
    public void obtenerTodasSimilitudesTest3() throws NoExisteNingunProducto, ValorSimilitudFueraRango
    {
        GestorSimilitudes gS = GestorSimilitudes.getInstance();

        ArrayList <Similitud> arrayProductesAux = gS.obtenerTodasSimilitudes();
    }

    @Test
    public void  obtenerIdsProductosTest1() throws YaExisteProducto
    {
        GestorSimilitudes gS = GestorSimilitudes.getInstance();
        gS.crearSimilitudesDeProducto(0);
        gS.crearSimilitudesDeProducto(1);
        gS.crearSimilitudesDeProducto(2);

        ArrayList<Integer> aux = new ArrayList<Integer>();
        aux.add(0);
        aux.add(1);
        aux.add(2);
        assertEquals(aux, gS.obtenerIdsProductos());
    }

    @Test
    public void  obtenerIdsProductosTest2() throws YaExisteProducto
    {
        GestorSimilitudes gS = GestorSimilitudes.getInstance();
        ArrayList<Integer> aux = new ArrayList<Integer>();
        assertEquals(aux, gS.obtenerIdsProductos());
    }


}
