// autor: Christian Conejo Raubiene

package Font.Tests;

import Font.Exceptions.ValorSimilitudFueraRango;
import Font.classes.Similitud;
import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SimilitudesTest
{

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
    }

    @Test
    public void crearSimilitud1()
    {
        Similitud similitud = new Similitud(4, 3, 70);
        assertEquals(4, similitud.getIdGrande());
        assertEquals(3, similitud.getIdPequena());
        assertEquals(70, (int) similitud.getSimilitud());
    }

    @Test (expected = ValorSimilitudFueraRango.class)
    public void crearSimilitud2()
    {
        Similitud similitud = new Similitud(4, 3, 130);
    }

    @Test (expected = ValorSimilitudFueraRango.class)
    public void crearSimilitud3()
    {
        Similitud similitud = new Similitud(4, 3, -3);
    }
}
