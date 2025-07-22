package Font.domaincontrollers;



import Font.classes.GestorAlgoritmo;
import Font.classes.ResultadoAlgoritmo;
import Font.classes.Similitud;
import Font.Exceptions.SimilitudDeProductoNoExistente;

import java.util.ArrayList;

//Autor: Daniel Mejias
/**
 * Clase controladora de Algoritmos, se centra en recibir y operar las llamadas del controlador de
 * dominio que requieren usar algorimos de ordenación, usando GestorAlgoritmos para eso.
 */
public class ControladorAlgoritmo {
    private GestorAlgoritmo gestorAlgoritmo;

    public ControladorAlgoritmo () {
        gestorAlgoritmo = new GestorAlgoritmo();
    }

    /**
     * Llama a GestorAlgoritmo para ordenar los productos proporcionados según sus similitudes
     * @param tipoAlgoritmo Número que representa el tipo de algoritmo que será empleado
     * @param prods Todas las ids de los productos
     * @param sims Todas las similitudes entre productos (todas las que no estén, se considerarán como 0)
     * @return Una lista con las ids de los productos ordenadas según el algoritmo usado
     * @throws SimilitudDeProductoNoExistente en caso de que una de las similitudes intente relacionar algún producto que no exista en la lista
     */
    public ResultadoAlgoritmo ordenarProductos (Integer tipoAlgoritmo, ArrayList<Integer> prods, ArrayList<Similitud> sims) throws SimilitudDeProductoNoExistente {
        return gestorAlgoritmo.ordenarProductos(tipoAlgoritmo, prods, sims);
    }
}
