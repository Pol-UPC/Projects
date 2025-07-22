package Font.classes;

import java.util.ArrayList;

import Font.Exceptions.SimilitudDeProductoNoExistente;

//Autor: Daniel Mejias
/**
 * La clase GestoAlgoritmo contiene múltiples clases que deben ser subclase de AlgoritmoOrdenación, de
 * tal manera que puede ser llamada para ordenar productos usando una de las subclases de AlgoritmoOrdenación
 * guardadas en la lista.
 */
public class GestorAlgoritmo {
    ArrayList<AlgoritmoOrdenacion> algoritmos;

    /**
     * Inicializa el GestorAlgoritmo, creando los dos algoritmos que utilitza.
     */
    public GestorAlgoritmo() {
        algoritmos = new ArrayList<>();
        algoritmos.add(new AlgoritmoFuerzaBruta());
        algoritmos.add(new AlgoritmoAproximadoKruskal());
    }

    /**
     * Llama a uno de sus algoritmos para ordenar los productos proporcionados según sus similitudes
     * @param tipoAlgoritmo Número que representa el tipo de algoritmo que será empleado
     * @param prods Todas las ids de los productos
     * @param sims Todas las similitudes entre productos (todas las que no estén, se considerarán como 0)
     * @return Una lista con las ids de los productos ordenadas según el algoritmo usado
     * @throws SimilitudDeProductoNoExistente en caso de que una de las similitudes intente relacionar algún producto que no exista en la lista
     */
    public ResultadoAlgoritmo ordenarProductos(Integer tipoAlgoritmo, ArrayList<Integer> prods, ArrayList<Similitud> sims) throws SimilitudDeProductoNoExistente {
        for (Similitud s: sims) {
            if (!prods.contains(s.getIdPequena())) throw new SimilitudDeProductoNoExistente("Una de las similitudes intenta relacionarse con el producto " + s.getIdPequena() + " el cual no existe dentro de la lista de productos proporcionada.");
            if (!prods.contains(s.getIdGrande())) throw new SimilitudDeProductoNoExistente("Una de las similitudes intenta relacionarse con el producto " + s.getIdGrande() + " el cual no existe dentro de la lista de productos proporcionada.");
        }
        ResultadoAlgoritmo retCasosExtra =  new ResultadoAlgoritmo();
        if (prods.size() == 0) {
            return retCasosExtra;
        }
        if (prods.size() == 1) {
            retCasosExtra.setProductos(prods);
            return retCasosExtra;
        }
        ResultadoAlgoritmo ret = algoritmos.get(tipoAlgoritmo).ordenar(prods,sims);
        //System.out.println(ret.getSumaTotal());
        return ret;
    }
}
