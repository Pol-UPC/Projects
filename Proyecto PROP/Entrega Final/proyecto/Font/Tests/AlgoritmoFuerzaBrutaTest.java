package Font.Tests;

import Font.Exceptions.SimilitudMismoProducto;
import Font.Exceptions.ValorSimilitudFueraRango;
import Font.classes.AlgoritmoAproximadoKruskal;
import Font.classes.AlgoritmoFuerzaBruta;
import Font.classes.ResultadoAlgoritmo;
import Font.classes.Similitud;
import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.*;

//Autor: Daniel Mejias
public class AlgoritmoFuerzaBrutaTest {
    private AlgoritmoFuerzaBruta algoritmoFuerzaBruta;

    @Before
    public void beforeTest() {
        algoritmoFuerzaBruta = new AlgoritmoFuerzaBruta();
    }

    @After
    public void afterTest() {
        algoritmoFuerzaBruta = null;
    }

    @BeforeClass
    public static void beforeClass() { System.out.println("Comienza el test de AlgoritmoFuerzaBruta"); }

    @AfterClass
    public static void afterClass() { System.out.println("Termina el test de AlgoritmoFuerzaBruta"); }

    @Test
    public void ordenarTest1() {
        ArrayList<Integer> productos = new ArrayList<>();
        ArrayList<Similitud> similitudes = new ArrayList<>();
        int[][] matrix =
                {
                        { 0, 10,  15,  20 },
                        { 10, 0,  35,  25 },
                        { 15, 35,  0,  30 },
                        { 20, 25, 30,   0 }
                };
        for (int i = 0; i < matrix.length; i++) {
            productos.add(i);
        }
        for (int i = 0; i < matrix.length - 1; i++) {
            for (int j = i + 1; j < matrix.length; j++) {
                similitudes.add(new StubSimilitud(i,j,matrix[i][j]));
            }
        }

        ResultadoAlgoritmo temp = algoritmoFuerzaBruta.ordenar(productos, similitudes);


        Integer[] ordEsperadoArray = {0,3,2,1};
        ArrayList<Integer> ordEsperado = new ArrayList<>();
        for (Integer i = 0; i < ordEsperadoArray.length; i++) ordEsperado.add(ordEsperadoArray[i]);
        Assert.assertEquals("No da la suma esperada", 95, temp.getSumaTotal());
        Assert.assertEquals("No da el orden esperado", ordEsperado, temp.getProductos());

    }

    @Test
    public void ordenarTest2() {
        ArrayList<Integer> productos = new ArrayList<>();
        ArrayList<Similitud> similitudes = new ArrayList<>();
        int[][] matrix = {
                {  0,29,82,46,68,52,72,42,51},
                { 29, 0,55,46,42,43,43,23,23},
                { 82,55, 0,68,46,55,23,43,41},
                { 46,46,68, 0,82,15,72,31,62},
                { 68,42,46,82, 0,74,23,52,21},
                { 52,43,55,15,74, 0,61,23,55},
                { 72,43,23,72,23,61, 0,42,23},
                { 42,23,43,31,52,23,42, 0,33},
                { 51,23,41,62,21,55,23,33, 0}
        };
        for (int i = 0; i < matrix.length; i++) {
            productos.add(i);
        }
        for (int i = 0; i < matrix.length - 1; i++) {
            for (int j = i + 1; j < matrix.length; j++) {
                similitudes.add(new StubSimilitud(i,j,matrix[i][j]));
            }
        }


        ResultadoAlgoritmo temp = new AlgoritmoFuerzaBruta().ordenar(productos, similitudes);
        ResultadoAlgoritmo temp2 = new AlgoritmoAproximadoKruskal().ordenar(temp.getProductos(), similitudes);
        ResultadoAlgoritmo temp3 = new AlgoritmoFuerzaBruta().ordenar(temp2.getProductos(), similitudes);

        Integer[] ordEsperadoArray = {7, 8, 3, 4, 5, 1, 2, 0, 6};
        ArrayList<Integer> ordEsperado = new ArrayList<>();
        for (Integer i = 0; i < ordEsperadoArray.length; i++) ordEsperado.add(ordEsperadoArray[i]);
        Assert.assertEquals("No da la suma esperada", 575, temp.getSumaTotal());
        Assert.assertEquals("No da el orden esperado", ordEsperado, temp.getProductos());

    }
}

//Autor: Daniel Mejias
class StubSimilitud extends Similitud
{
    private int idG, idP;
    private int s;


    public StubSimilitud (int idGAux, int idPAux, int similitudAux) throws SimilitudMismoProducto, ValorSimilitudFueraRango
    {
        this.idG = Math.max(idGAux, idPAux);
        this.idP = Math.min(idGAux, idPAux);
        this.s = similitudAux;
    }

    public StubSimilitud() {    }

    @Override
    public int getIdGrande() { return idG; }

    @Override
    public int getIdPequena() { return idP; }

    @Override
    public Integer getSimilitud() { return s; }
}
