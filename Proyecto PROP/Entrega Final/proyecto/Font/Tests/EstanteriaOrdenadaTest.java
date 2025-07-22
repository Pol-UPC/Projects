// autor: Christian Conejo Raubiene

package Font.Tests;

import Font.Exceptions.NoExisteProducto;
import Font.Exceptions.ValorSimilitudFueraRango;
import Font.classes.EstanteriaOrdenada;
import Font.classes.GestorSimilitudes;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class EstanteriaOrdenadaTest
{

    @Mock GestorSimilitudes gSMock = Mockito.mock(GestorSimilitudes.class);
    ArrayList <Integer> listaEOAux;
    EstanteriaOrdenada eOPrueba = new EstanteriaOrdenada();

    @BeforeClass
    public static void beforeClass()
    {
        System.out.println("Inicio SimilitudesTests");
    }

    @AfterClass
    public static void afterClass()
    {
        System.out.println("Final SimilitudesTests");
    }

    @Before
    public void beforeTest ()
    {
        System.out.println("Siguiente test:");
        listaEOAux = new ArrayList<Integer>();
        listaEOAux.add(0);
        listaEOAux.add(1);
        eOPrueba = new EstanteriaOrdenada();
        eOPrueba.setGestorSimilitudes(gSMock);

    }

    @Test
    public void constructoraEstanteriaOrdenadaTest()
    {
        EstanteriaOrdenada eOPrueba2 = new EstanteriaOrdenada();
        assertNotNull(eOPrueba2);
    }

    @Test
    public void setEstanteriaOrdenadaTest1()
    {
        listaEOAux.add(4);
        listaEOAux.add(2);
        listaEOAux.add(6);
        listaEOAux.add(0);

        eOPrueba.setEstanteriaOrdenada(listaEOAux, 0);
        assertEquals(0, (int) eOPrueba.getSimilitudTotal());
    }

    @Test
    public void setEstanteriaOrdenadaTest2()
    {
        listaEOAux = new ArrayList<Integer>();

        eOPrueba.setEstanteriaOrdenada(listaEOAux, 3);
        assertEquals(3, (int) eOPrueba.getSimilitudTotal());
    }

    @Test (expected = ValorSimilitudFueraRango.class)
    public void setEstanteriaOrdenadaTest3()
    {
        listaEOAux.add(4);
        listaEOAux.add(2);
        listaEOAux.add(6);
        listaEOAux.add(8);

        EstanteriaOrdenada eOPrueba = new EstanteriaOrdenada();
        eOPrueba.setEstanteriaOrdenada(listaEOAux, -7);
    }

    @Test
    public void estaColocadoTest1()
    {
        EstanteriaOrdenada eOPrueba = new EstanteriaOrdenada();
        eOPrueba.setEstanteriaOrdenada(listaEOAux, 3);

        assertTrue(eOPrueba.estaColocado(0));
        assertEquals(3, (int) eOPrueba.getSimilitudTotal());
    }

    @Test
    public void estaColocadoTest2()
    {
        EstanteriaOrdenada eOPrueba = new EstanteriaOrdenada();
        eOPrueba.setEstanteriaOrdenada(listaEOAux, 3);

        assertFalse(eOPrueba.estaColocado(15));
        assertEquals(3, (int) eOPrueba.getSimilitudTotal());
    }

    @Test
    public void cambiarSimilitudTest() throws NoExisteProducto {
        when(gSMock.obtenerSimilitud(0, 1)).thenReturn(100);

        eOPrueba.setEstanteriaOrdenada(listaEOAux, 200);
        eOPrueba.cambiarSimilitud(0, 1, 1);
        assertEquals(2, (int) eOPrueba.getSimilitudTotal());
    }

    @Test (expected = ValorSimilitudFueraRango.class)
    public void cambiarSimilitudTest2() throws NoExisteProducto, ValorSimilitudFueraRango {
        when(gSMock.obtenerSimilitud(0, 1)).thenReturn(100);

        EstanteriaOrdenada eOPrueba = new EstanteriaOrdenada();
        eOPrueba.setEstanteriaOrdenada(listaEOAux, 200);
        eOPrueba.cambiarSimilitud(0, 1, -30);
    }

    @Test (expected = ValorSimilitudFueraRango.class)
    public void cambiarSimilitudTest3() throws NoExisteProducto, ValorSimilitudFueraRango {
        when(gSMock.obtenerSimilitud(0, 1)).thenReturn(100);

        EstanteriaOrdenada eOPrueba = new EstanteriaOrdenada();
        eOPrueba.setEstanteriaOrdenada(listaEOAux, 200);
        eOPrueba.cambiarSimilitud(0, 1, 3000);
    }

    @Test
    public void cambiarSimilitudTest4() throws NoExisteProducto, ValorSimilitudFueraRango {
        when(gSMock.obtenerSimilitud(0, 1)).thenReturn(100);
        listaEOAux = new ArrayList<Integer>();
        listaEOAux.add(0);
        listaEOAux.add(-1);
        listaEOAux.add(1);
        eOPrueba.setEstanteriaOrdenada(listaEOAux, 200);
        eOPrueba.cambiarSimilitud(0, 1, 1);
        assertEquals(2, (int) eOPrueba.getSimilitudTotal());
    }

    @Test
    public void cambiarSimilitudTest5() throws NoExisteProducto, ValorSimilitudFueraRango {
        when(gSMock.obtenerSimilitud(0, 1)).thenReturn(100);
        listaEOAux = new ArrayList<Integer>();
        listaEOAux.add(-1);
        listaEOAux.add(0);
        listaEOAux.add(-2);
        listaEOAux.add(1);
        eOPrueba.setEstanteriaOrdenada(listaEOAux, 200);
        eOPrueba.cambiarSimilitud(0, 1, 1);
        assertEquals(2, (int) eOPrueba.getSimilitudTotal());
    }

    @Test
    public void cambiarSimilitudTest7() throws NoExisteProducto, ValorSimilitudFueraRango {
        when(gSMock.obtenerSimilitud(0, 1)).thenReturn(100);
        listaEOAux.add(2);
        eOPrueba.setEstanteriaOrdenada(listaEOAux, 100);
        eOPrueba.cambiarSimilitud(0, 1, 1);
        assertEquals(1, (int) eOPrueba.getSimilitudTotal());
    }

    @Test
    public void cambiarSimilitudTest8() throws NoExisteProducto, ValorSimilitudFueraRango {
        when(gSMock.obtenerSimilitud(0, 2)).thenReturn(10);
        listaEOAux.add(2);
        eOPrueba.setEstanteriaOrdenada(listaEOAux, 100);
        eOPrueba.cambiarSimilitud(0, 2, 1);
        assertEquals(91, (int) eOPrueba.getSimilitudTotal());
    }

    @Test
    public void cambiarSimilitudTest9() throws NoExisteProducto, ValorSimilitudFueraRango {
        when(gSMock.obtenerSimilitud(0, 2)).thenReturn(100);
        listaEOAux.add(2);
        listaEOAux.add(3);
        eOPrueba.setEstanteriaOrdenada(listaEOAux, 0);
        eOPrueba.cambiarSimilitud(0, 2, 1);
        assertEquals(0, (int) eOPrueba.getSimilitudTotal());
    }

    @Test
    public void anadirProductoEstanteriaTest1() throws NoExisteProducto {
        listaEOAux = new ArrayList<Integer>();
        eOPrueba.setEstanteriaOrdenada(listaEOAux, 0);
        eOPrueba.añadirProducto(0);
        assertEquals(0, (int) eOPrueba.getSimilitudTotal());
    }

    @Test
    public void anadirProductoEstanteriaTest2() throws NoExisteProducto {
        when(gSMock.obtenerSimilitud(0, 1)).thenReturn(100);
        listaEOAux = new ArrayList<Integer>();
        listaEOAux.add(0);
        eOPrueba.setEstanteriaOrdenada(listaEOAux, 0);
        eOPrueba.añadirProducto(1);
        assertEquals(200, (int) eOPrueba.getSimilitudTotal());
    }

    @Test
    public void anadirProductoEstanteriaTest3() throws NoExisteProducto {
        when(gSMock.obtenerSimilitud(1, 0)).thenReturn(100);
        when(gSMock.obtenerSimilitud(0, 2)).thenReturn(10);
        when(gSMock.obtenerSimilitud(1, 2)).thenReturn(1);
        eOPrueba.setEstanteriaOrdenada(listaEOAux, 200);
        eOPrueba.añadirProducto(2);
        assertEquals(111, (int) eOPrueba.getSimilitudTotal());
    }

    @Test
    public void anadirProductoEstanteriaTest4() throws NoExisteProducto {
        listaEOAux = new ArrayList<Integer>();
        listaEOAux.add(-1);
        eOPrueba.setEstanteriaOrdenada(listaEOAux, 0);
        eOPrueba.añadirProducto(0);
        assertEquals(0, (int) eOPrueba.getSimilitudTotal());
    }

    @Test
    public void anadirProductoEstanteriaTest5() throws NoExisteProducto {
        when(gSMock.obtenerSimilitud(0, 1)).thenReturn(10);
        listaEOAux = new ArrayList<Integer>();
        listaEOAux.add(-1);
        listaEOAux.add(0);
        eOPrueba.setEstanteriaOrdenada(listaEOAux, 0);
        eOPrueba.añadirProducto(1);
        assertEquals(20, (int) eOPrueba.getSimilitudTotal());
    }

    @Test
    public void anadirProductoEstanteriaTest6() throws NoExisteProducto {
        when(gSMock.obtenerSimilitud(0, 1)).thenReturn(10);
        listaEOAux = new ArrayList<Integer>();
        listaEOAux.add(0);
        listaEOAux.add(-1);
        eOPrueba.setEstanteriaOrdenada(listaEOAux, 0);
        eOPrueba.añadirProducto(1);
        assertEquals(20, (int) eOPrueba.getSimilitudTotal());
    }

    @Test
    public void anadirProductoEstanteriaTest7() throws NoExisteProducto {
        when(gSMock.obtenerSimilitud(0, 1)).thenReturn(10);
        listaEOAux = new ArrayList<Integer>();
        listaEOAux.add(-1);
        listaEOAux.add(0);
        listaEOAux.add(-2);
        eOPrueba.setEstanteriaOrdenada(listaEOAux, 0);
        eOPrueba.añadirProducto(1);
        assertEquals(20, (int) eOPrueba.getSimilitudTotal());
    }

    @Test
    public void eliminarProductoEstanteriaTest1() throws NoExisteProducto {
        listaEOAux = new ArrayList<Integer>();
        listaEOAux.add(0);
        eOPrueba.setEstanteriaOrdenada(listaEOAux, 0);
        eOPrueba.eliminarProducto(0);
        assertEquals(0, (int) eOPrueba.getSimilitudTotal());
    }

    @Test
    public void eliminarProductoEstanteriaTest2() throws NoExisteProducto {
        eOPrueba.setEstanteriaOrdenada(listaEOAux, 120);
        eOPrueba.eliminarProducto(0);
        assertEquals(0, (int) eOPrueba.getSimilitudTotal());
    }

    @Test
    public void eliminarProductoEstanteriaTest3() throws NoExisteProducto {
        when(gSMock.obtenerSimilitud(1, 0)).thenReturn(1);
        when(gSMock.obtenerSimilitud(2, 0)).thenReturn(10);
        when(gSMock.obtenerSimilitud(2, 1)).thenReturn(100);
        listaEOAux.add(2);
        eOPrueba.setEstanteriaOrdenada(listaEOAux, 111);
        eOPrueba.eliminarProducto(2);
        assertEquals(2, (int) eOPrueba.getSimilitudTotal());
    }

    @Test
    public void eliminarProductoEstanteriaTest4() throws NoExisteProducto {
        when(gSMock.obtenerSimilitud(1, 0)).thenReturn(1);
        when(gSMock.obtenerSimilitud(2, 0)).thenReturn(10);
        when(gSMock.obtenerSimilitud(2, 1)).thenReturn(100);
        listaEOAux.add(-1);
        listaEOAux.add(2);
        eOPrueba.setEstanteriaOrdenada(listaEOAux, 111);
        eOPrueba.eliminarProducto(2);
        assertEquals(2, (int) eOPrueba.getSimilitudTotal());
    }

    @Test
    public void eliminarProductoEstanteriaTest5() throws NoExisteProducto {
        when(gSMock.obtenerSimilitud(1, 0)).thenReturn(1);
        when(gSMock.obtenerSimilitud(2, 0)).thenReturn(10);
        when(gSMock.obtenerSimilitud(2, 1)).thenReturn(100);
        listaEOAux = new ArrayList<Integer>();
        listaEOAux.add(-1);
        listaEOAux.add(0);
        listaEOAux.add(1);
        listaEOAux.add(-2);
        listaEOAux.add(2);
        eOPrueba.setEstanteriaOrdenada(listaEOAux, 111);
        eOPrueba.eliminarProducto(2);
        assertEquals(2, (int) eOPrueba.getSimilitudTotal());
    }

    @Test
    public void intercambiarProductosEstanteriaTest1() throws NoExisteProducto {
        when(gSMock.obtenerSimilitud(0, 1)).thenReturn(1);
        eOPrueba.setEstanteriaOrdenada(listaEOAux, 2);
        eOPrueba.intercambiarProductos(0, 1);
        assertEquals(2, (int) eOPrueba.getSimilitudTotal());
    }

    @Test
    public void intercambiarProductosEstanteriaTest2() throws NoExisteProducto {
        when(gSMock.obtenerSimilitud(1, 0)).thenReturn(10);
        when(gSMock.obtenerSimilitud(1, 2)).thenReturn(100);
        when(gSMock.obtenerSimilitud(3, 2)).thenReturn(6);
        when(gSMock.obtenerSimilitud(3, 0)).thenReturn(90);
        when(gSMock.obtenerSimilitud(3, 1)).thenReturn(1);
        when(gSMock.obtenerSimilitud(2, 0)).thenReturn(2);
        listaEOAux = new ArrayList<Integer>();
        listaEOAux.add(0);
        listaEOAux.add(1);
        listaEOAux.add(2);
        listaEOAux.add(3);
        eOPrueba.setEstanteriaOrdenada(listaEOAux, 206);
        eOPrueba.intercambiarProductos(2, 3);
        assertEquals(18, (int) eOPrueba.getSimilitudTotal());
    }

    @Test
    public void intercambiarProductosEstanteriaTest3() throws NoExisteProducto {
        when(gSMock.obtenerSimilitud(1, 0)).thenReturn(10);
        when(gSMock.obtenerSimilitud(1, 2)).thenReturn(100);
        when(gSMock.obtenerSimilitud(3, 2)).thenReturn(6);
        when(gSMock.obtenerSimilitud(3, 0)).thenReturn(90);
        when(gSMock.obtenerSimilitud(3, 1)).thenReturn(1);
        when(gSMock.obtenerSimilitud(2, 0)).thenReturn(2);
        listaEOAux = new ArrayList<Integer>();
        listaEOAux.add(0);
        listaEOAux.add(1);
        listaEOAux.add(-1);
        listaEOAux.add(2);
        listaEOAux.add(3);
        eOPrueba.setEstanteriaOrdenada(listaEOAux, 206);
        eOPrueba.intercambiarProductos(2, 3);
        assertEquals(18, (int) eOPrueba.getSimilitudTotal());
    }

    @Test
    public void intercambiarProductosEstanteriaTest4() throws NoExisteProducto {
        when(gSMock.obtenerSimilitud(1, 0)).thenReturn(10);
        when(gSMock.obtenerSimilitud(1, 2)).thenReturn(100);
        when(gSMock.obtenerSimilitud(3, 2)).thenReturn(6);
        when(gSMock.obtenerSimilitud(3, 0)).thenReturn(90);
        when(gSMock.obtenerSimilitud(3, 1)).thenReturn(1);
        when(gSMock.obtenerSimilitud(2, 0)).thenReturn(2);
        listaEOAux = new ArrayList<Integer>();
        listaEOAux.add(0);
        listaEOAux.add(1);
        listaEOAux.add(2);
        listaEOAux.add(-1);
        listaEOAux.add(3);
        eOPrueba.setEstanteriaOrdenada(listaEOAux, 206);
        eOPrueba.intercambiarProductos(2, 3);
        assertEquals(18, (int) eOPrueba.getSimilitudTotal());
    }

    @Test
    public void intercambiarProductosEstanteriaTest5() throws NoExisteProducto {
        when(gSMock.obtenerSimilitud(1, 0)).thenReturn(10);
        when(gSMock.obtenerSimilitud(1, 2)).thenReturn(100);
        when(gSMock.obtenerSimilitud(3, 2)).thenReturn(6);
        when(gSMock.obtenerSimilitud(3, 0)).thenReturn(90);
        when(gSMock.obtenerSimilitud(3, 1)).thenReturn(1);
        when(gSMock.obtenerSimilitud(2, 0)).thenReturn(2);
        listaEOAux = new ArrayList<Integer>();
        listaEOAux.add(0);
        listaEOAux.add(1);
        listaEOAux.add(-1);
        listaEOAux.add(2);
        listaEOAux.add(-2);
        listaEOAux.add(3);
        listaEOAux.add(-3);
        eOPrueba.setEstanteriaOrdenada(listaEOAux, 206);
        eOPrueba.intercambiarProductos(2, 3);
        assertEquals(18, (int) eOPrueba.getSimilitudTotal());
    }
}
