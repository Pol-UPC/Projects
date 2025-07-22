package Font.classes;

import java.text.Normalizer;
import java.util.*;

//Autor: Daniel Mejias
/**
 * AlgoritmoOrdenación es la clase generica abstracta que usan los algoritmos de ordenación, tan solo
 * incluye una constructora básica y la clase abstracta "ordenar"
 */
public abstract class AlgoritmoOrdenacion {

    public AlgoritmoOrdenacion() {
    }


    /**
     * Ordena los productos recibidos segun las similitudes intentando encontrar la mejor configuración posible
     * @param prods Todas las ids de los productos
     * @param sims Todas las similitudes entre productos (todas las que no estén, se considerarán como 0)
     * @return Una lista con las ids de los productos ordenadas según el algoritmo usado y la suma que da
     */
    public abstract ResultadoAlgoritmo ordenar(ArrayList<Integer> prods, ArrayList<Similitud> sims);


}

