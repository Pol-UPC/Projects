// autor: Christian Conejo Raubiene

package Font.Tests;

import Font.Exceptions.*;
import Font.classes.EstanteriaOrdenada;
import Font.classes.GestorEstanteria;
import Font.classes.GestorSimilitudes;
import Font.classes.Producto;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.HashMap;


public class GestorEstanteriaTest
{
    @Mock EstanteriaOrdenada eOMock = Mockito.mock(EstanteriaOrdenada.class);
    @Mock Producto paMock = Mockito.mock(Producto.class);
    @Mock Producto pbMock = Mockito.mock(Producto.class);
    @Mock Producto pcMock = Mockito.mock(Producto.class);
    @Mock GestorSimilitudes gSMock = Mockito.mock(GestorSimilitudes.class);
    HashMap<String, Producto> pC;
    HashMap<Integer, Producto> pCid;
    GestorEstanteria gEPrueba = new GestorEstanteria (eOMock, pC, pCid, gSMock);

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
        pC = new HashMap<String, Producto>();
        pC.put("a", paMock);
        pC.put("b", pbMock);
        pC.put("c", pcMock);
        gEPrueba.setProductos(pC, pCid);
    }


    @Test
    public void anadirProductoEOTest1() throws NoExisteProducto, YaExisteProducto {
        when(paMock.getId()).thenReturn(1);
        when(pbMock.getId()).thenReturn(2);
        when(pcMock.getId()).thenReturn(3);
        when(eOMock.estaColocado(1)).thenReturn(Boolean.FALSE);
        when(eOMock.estaColocado(2)).thenReturn(Boolean.TRUE);
        when(eOMock.estaColocado(3)).thenReturn(Boolean.TRUE);
        // Las operaciones vacias de Mocks por defecto no hacen nada.

        gEPrueba.añadirProductoEstanteriaOrdenada("a");
    }

    @Test (expected = YaExisteProducto.class)
    public void anadirProductoEOTest2() throws NoExisteProducto, YaExisteProducto {
        when(pC.get("a").getId()).thenReturn(1);
        when(eOMock.estaColocado(1)).thenReturn(Boolean.TRUE);
        // Las operaciones vacias de Mocks por defecto no hacen nada.

        gEPrueba.añadirProductoEstanteriaOrdenada("a");
    }

    @Test
    public void eliminarProductoEOTest1() throws NoExisteProducto
    {
        when(pC.get("a").getId()).thenReturn(1);
        when(eOMock.estaColocado(1)).thenReturn(Boolean.TRUE);
        // Las operaciones vacias de Mocks por defecto no hacen nada.

        gEPrueba.eliminarProducto("a");
    }

    @Test (expected = NoExisteProducto.class)
    public void eliminarProductoEOTest2() throws NoExisteProducto
    {
        when(paMock.getId()).thenReturn(1);
        when(eOMock.estaColocado(1)).thenReturn(Boolean.FALSE);
        pC.remove("a");
        // Las operaciones vacias de Mocks por defecto no hacen nada.

        gEPrueba.eliminarProducto("a");
    }

    @Test
    public void intercambiarProductoEOTest1() throws NoExisteProducto, MismoNombre
    {
        when(pC.get("a").getId()).thenReturn(1);
        when(pC.get("b").getId()).thenReturn(2);
        when(eOMock.estaColocado(1)).thenReturn(Boolean.TRUE);
        when(eOMock.estaColocado(2)).thenReturn(Boolean.TRUE);
        // Las operaciones vacias de Mocks por defecto no hacen nada.

        gEPrueba.intercambiarProductos("a", "b");
    }

    @Test (expected = MismoNombre.class)
    public void intercambiarProductoEOTest2() throws NoExisteProducto, MismoNombre
    {
        when(pC.get("a").getId()).thenReturn(1);
        when(eOMock.estaColocado(1)).thenReturn(Boolean.TRUE);
        // Las operaciones vacias de Mocks por defecto no hacen nada.

        gEPrueba.intercambiarProductos("a", "a");
    }

    @Test (expected =NoExisteProducto.class)
    public void intercambiarProductoEOTest3() throws NoExisteProducto, MismoNombre
    {
        when(pC.get("a").getId()).thenReturn(1);
        when(pC.get("b").getId()).thenReturn(2);
        when(eOMock.estaColocado(1)).thenReturn(Boolean.FALSE);
        when(eOMock.estaColocado(2)).thenReturn(Boolean.TRUE);
        // Las operaciones vacias de Mocks por defecto no hacen nada.

        gEPrueba.intercambiarProductos("a", "b");
    }

    @Test (expected =NoExisteProducto.class)
    public void intercambiarProductoEOTest4() throws NoExisteProducto, MismoNombre
    {
        when(pC.get("a").getId()).thenReturn(1);
        when(pC.get("b").getId()).thenReturn(2);
        when(eOMock.estaColocado(1)).thenReturn(Boolean.TRUE);
        when(eOMock.estaColocado(2)).thenReturn(Boolean.FALSE);
        // Las operaciones vacias de Mocks por defecto no hacen nada.

        gEPrueba.intercambiarProductos("a", "b");
    }

    @Test
    public void getIdProductoTest1() throws NoExisteProducto
    {
        when(paMock.getId()).thenReturn(1);

        Integer idAux = gEPrueba.getIdProducto("a");
        assertEquals((Integer)1, idAux);
    }

    @Test (expected = NoExisteProducto.class)
    public void getIdProductoTest2() throws NoExisteProducto
    {
        pC.remove("a");
        gEPrueba.setProductos(pC, pCid);
        when(paMock.getId()).thenReturn(1);

        Integer idAux = gEPrueba.getIdProducto("a");
    }

    @Test
    public void getEstanteriaOrdenadaTest()
    {
        EstanteriaOrdenada eOAux = gEPrueba.getEstanteriaOrdenada();
        assertSame(eOMock, eOAux);
    }

}
