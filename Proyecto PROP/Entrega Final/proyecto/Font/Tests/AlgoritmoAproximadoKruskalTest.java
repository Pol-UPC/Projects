package Font.Tests;

import Font.classes.AlgoritmoAproximadoKruskal;
import Font.classes.ResultadoAlgoritmo;
import Font.classes.Similitud;
import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.*;

//Autor: Daniel Mejias
public class AlgoritmoAproximadoKruskalTest {
    private AlgoritmoAproximadoKruskal algoritmoAproximadoKruskal;

    @Before
    public void beforeTest() {
        algoritmoAproximadoKruskal = new AlgoritmoAproximadoKruskal();
    }

    @After
    public void afterTest() {
        algoritmoAproximadoKruskal = null;
    }

    @BeforeClass
    public static void beforeClass() { System.out.println("Comienza el test de AlgoritmoAproximadoKruskal"); }

    @AfterClass
    public static void afterClass() { System.out.println("Termina el test de AlgoritmoAproximadoKruskal"); }

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

        ResultadoAlgoritmo temp = algoritmoAproximadoKruskal.ordenar(productos, similitudes);


        Integer[] ordEsperadoArray = {0,3,2,1};
        ArrayList<Integer> ordEsperado = new ArrayList<>();
        for (Integer i = 0; i < ordEsperadoArray.length; i++) ordEsperado.add(ordEsperadoArray[i]);
        Assert.assertTrue("No da la suma esperada", temp.getSumaTotal() >= 80.0f);
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

        ResultadoAlgoritmo temp = algoritmoAproximadoKruskal.ordenar(productos, similitudes);

        Integer[] ordEsperadoArray = {0,6,3,8,4,7,5,2,1};
        ArrayList<Integer> ordEsperado = new ArrayList<>();
        for (Integer i = 0; i < ordEsperadoArray.length; i++) ordEsperado.add(ordEsperadoArray[i]);
        Assert.assertTrue("No da la suma esperada", temp.getSumaTotal() >= 246.0f);
        Assert.assertEquals("No da el orden esperado", ordEsperado, temp.getProductos());

    }
}