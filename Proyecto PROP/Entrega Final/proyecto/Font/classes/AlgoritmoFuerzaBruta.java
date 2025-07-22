package Font.classes;

import java.util.ArrayList;

//Autor: Daniel Mejias

/**
 * Siendo una subclase de AlgoritmoOrdenacion, AlgoritmoFuerzaBruta implementa un algoritmo lento
 * de recursión que prueba todas las opciones posibles y devuelve la que de mayor suma.
 */
public class AlgoritmoFuerzaBruta extends AlgoritmoOrdenacion {

    /**
     * Ordena los productos recibidos según las similitudes, encontrando la mejor configuración posible
     * a través de una recursión de fuerza bruta
     * @param prods Todas las ids de los productos
     * @param sims Todas las similitudes entre productos
     * @return Una lista con las ids de los productos ordenadas a través de fuerza bruta y la suma que da
     */
    public ResultadoAlgoritmo ordenar(ArrayList<Integer> prods, ArrayList<Similitud> sims) {
//
        ResultadoAlgoritmo ret = subAlgoritmoFuerzaBruta(prods,sims,new ArrayList<>());
        return ret;
    }

    /**
     * Recursivamente prueba todas las opciones posibles y en cada paso recursivo se queda con la configuración
     * con más puntos
     * @param prods Las ids de los productos, esta pierde un producto en cada paso de la recursión
     * @param sims Todas las similitudes entre productos
     * @param solucion La lista de productos que dará esa rama
     * @return Una lista con las ids de los productos ordenados y la suma total que da la ordenación
     */
    private ResultadoAlgoritmo subAlgoritmoFuerzaBruta(ArrayList<Integer> prods, ArrayList<Similitud> sims, ArrayList<Integer> solucion) {
        ResultadoAlgoritmo ret = new ResultadoAlgoritmo();

        if (prods.isEmpty()) {
            for (int i = 0; i < solucion.size(); i++) {
                Integer id1 = solucion.get(i);
                Integer id2;
                if (i+1 == solucion.size()) id2 = solucion.get(0);
                else id2 = solucion.get(i+1);
                if (id1 > id2) {
                    Integer temp = id1;
                    id1 = id2;
                    id2 = temp;
                }
                int suma = 0;
                int j = 0;
                while (suma == 0 && j != sims.size()) {
                    if (sims.get(j).getIdGrande() == id2 && sims.get(j).getIdPequena() == id1) {
                        suma = sims.get(j).getSimilitud();
                    }
                    j++;
                }
                ret.setSumaTotal(ret.getSumaTotal() + suma);
            }
            ret.setProductos(solucion);
        } else {
            for (int i = 0; i < prods.size(); i++) {
                ArrayList<Integer> tempProds = (ArrayList<Integer>) prods.clone();
                ArrayList<Integer> tempSol = (ArrayList<Integer>) solucion.clone();
                //Eliminar producto de la lista
                int temp = tempProds.get(i);
                tempProds.remove(i);
                tempSol.add(temp);
                ResultadoAlgoritmo tempRes = subAlgoritmoFuerzaBruta(tempProds, sims, tempSol);
                if (tempRes.getSumaTotal() >= ret.getSumaTotal()) {
                    ret = tempRes;
                }
            }
        }





        return ret;
    }
}
