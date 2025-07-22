package Font.classes;

import java.util.*;

//Autor: Daniel Mejias

/**
 * Siendo una subclase de AlgoritmoOrdenacion, AlgoritmoAproximadoKruskal usa una combinación de generación
 * de MSTs por Kruskal y el recorrido de arboles por DFS para encontrar una ordenación que será
 * como mínimo la mitad de buena que la óptima.
 */
public class AlgoritmoAproximadoKruskal extends AlgoritmoOrdenacion {

    /**
     * Ordena los productos recibidos según las similitudes, intentando encontrar la mejor configuración posible
     * a través de un algoritmo aproximado que usa Kruskal y dfs
     * @param prods Todas las ids de los productos
     * @param sims Todas las similitudes entre productos
     * @return Una lista con las ids de los productos ordenadas a través de una aproximación con MSTs y la suma que da
     */
    public ResultadoAlgoritmo ordenar(ArrayList<Integer> prods, ArrayList<Similitud> sims) {

        ArrayList<Similitud> kruskal = algoritmoKruskal(prods,sims);
        ResultadoAlgoritmo ret = crearCicloHamiltoniano(kruskal,sims,prods.get(0));
        for (Integer p : prods) {
            ResultadoAlgoritmo temp = crearCicloHamiltoniano(kruskal,sims,p);
            if (temp.getSumaTotal() > ret.getSumaTotal()) {
                ret = temp;
            }
        }
        return ret;
    }

    /**
     * Genera un MST basado en las similitudes y productos proporcionados usando Kruskal.
     * @param prods Todas las ids de los productos
     * @param sims Todas las similitudes de los productos
     * @return Conjunto de similitudes representativas del MST generado
     */
    private ArrayList<Similitud> algoritmoKruskal(ArrayList<Integer> prods, ArrayList<Similitud> sims) {
        //Sortear las similitudes

        ArrayList<Similitud> similituds = (ArrayList<Similitud>) sims.clone();
        Collections.sort(similituds, new CompararPorSimilitud());

        ArrayList<Similitud> ret = new ArrayList<>();
        ArrayList<Integer> tempProds = (ArrayList<Integer>) prods.clone();

        //Generar disjoint set con tamaño: id de producto mas grande.
        int numProdMax = 0;
        for (Integer i: tempProds) {
            if (i >= numProdMax) numProdMax = i + 1;
        }
        DisjointSet ds = new DisjointSet(numProdMax);

        //El ciclo principal de kruskal, añadir en orden si no es ciclo.
        for (Similitud s: similituds) {
            if (ds.find(s.getIdGrande()) != ds.find(s.getIdPequena())) {

                ret.add(s);
                int index1 = -1;
                for (int j = 0; j < tempProds.size(); j++) {
                    if (tempProds.get(j) == s.getIdGrande()) index1 = j;
                }
                if (index1 != -1) tempProds.remove(index1);

                int index2 = -1;
                for (int j = 0; j < tempProds.size(); j++) {
                    if (tempProds.get(j) == s.getIdPequena()) index2 = j;
               }
                if (index2 != -1) tempProds.remove(index2);
              ds.union(s.getIdGrande(), s.getIdPequena());
            }
        }


        int cola = ret.getFirst().getIdPequena();
        //fix para los productos que solo tengan similitudes = 0
        for (Integer i: tempProds) {
            Similitud temp = new Similitud(cola,i,0);
            ret.add(temp);
        }
        return ret;
    }

    /**
     * Genera un ciclo hamiltoniano que pasa por todas las aristas de T
     * @param T Un MST que forma parte de G
     * @param G Un conjunto de similitudes que representan el grafo completo
     * @return El ciclo hamiltoniano resultante y la suma de valores total
     */
    private ResultadoAlgoritmo crearCicloHamiltoniano(ArrayList<Similitud> T, ArrayList<Similitud> G, int producto) {
        ResultadoAlgoritmo ret = new ResultadoAlgoritmo();
        ConjuntoProductosConectados conjuntoProductosConectado = new ConjuntoProductosConectados();
        for (Similitud t: T) {
            conjuntoProductosConectado.addArista(t.getIdGrande(),t.getIdPequena());
        }
        //Usar DFS para encontrar un ciclo hamiltoniano

        ArrayList<Integer> ciclo = doDFS(conjuntoProductosConectado,producto);


        for (int i = 0; i < ciclo.size(); i++) {
            Integer id1 = ciclo.get(i);
            Integer id2;
            if (i+1 == ciclo.size()) id2 = ciclo.get(0);
            else id2 = ciclo.get(i+1);
            if (id1 > id2) {
                Integer temp = id1;
                id1 = id2;
                id2 = temp;
            }
            int suma = 0;
            int j = 0;
            while (suma == 0 && j != G.size()) {
                if (G.get(j).getIdGrande() == id2 && G.get(j).getIdPequena() == id1) {
                    suma = G.get(j).getSimilitud();
                }
                j++;
            }
            ret.setSumaTotal(ret.getSumaTotal() + suma);
        }
        ret.setProductos(ciclo);
        return ret;
    }

    /**
     * Genera una lista de ids de productos resultante del recorrer un grafo con dfs
     * @param conj Un grafo de productos en forma de ConjuntoProductosConectados
     * @return Lista de integers representativos de los nodos recorridos en el dfs
     */
    private ArrayList<Integer> doDFS(ConjuntoProductosConectados conj, int producto) {
        Stack<Integer> pila = new Stack<>();
        pila.add(producto);
        ArrayList<Integer> ret = new ArrayList<>();
        ArrayList<Integer> visitados = new ArrayList<>();
        visitados.add(pila.peek());
        while (!pila.isEmpty()) {
            Integer tope = pila.peek();
            ret.add(tope);
            pila.pop();
            for (Integer p: conj.getConectados(tope, visitados)) {
                visitados.add(p);
                pila.add(p);
            }
        }
        return ret;
    }


}

//Autor: Daniel Mejias

/**
 * Una clase auxiliar para AlgoritmoAproximadoKruskal, CompararPorSimilitud es usada simplemente para
 * poder ordenar rápidamente los objetos Similitud por su atributo de similitud
 */
class CompararPorSimilitud implements Comparator<Similitud> {
    /**
     * Función auxiliar usada para comparar dos Similitud según su grado de similitud
     * @param sim1 Primera Similitud
     * @param sim2 Segunda Similitud
     * @return Resultado de la comparación
     */
    public int compare (Similitud sim1, Similitud sim2) {
        int ret = (sim2.getSimilitud() - sim1.getSimilitud());
        if (ret > 0) ret = 1;
        else if (ret == 0) ret = 0;
        else if (ret < 0) ret = -1;
        return (int) ret;
    }
}

//Autor: Daniel Mejias
/**
 * Una clase auxiliar para AlgoritmoAproximadoKruskal, ConjuntoProductosConectados se trata básicamente
 * de un hashmap de objetos de la clase ProductoConectado, para poder acceder a uno por id de manera rápida
 * y eficiente.
 */
class ConjuntoProductosConectados {
    HashMap<Integer, ProductoConectado> misProductos;

    public ConjuntoProductosConectados () { misProductos = new HashMap<>(); }

    /**
     * Conecta los dos productos concretados, si uno de los productos no existe. Se crea y se une.
     * @param id1 Primera id
     * @param id2 Segunda id
     */
    public void addArista (Integer id1, Integer id2) {
        if (!misProductos.containsKey(id1)) misProductos.put(id1, new ProductoConectado());
        if (!misProductos.containsKey(id2)) misProductos.put(id2, new ProductoConectado());
        misProductos.get(id1).addHijo(id2);
        misProductos.get(id2).addHijo(id1);
    }



    /**
     * Devuelve todos los productos guardados
     * @return Mapa de los productos
     */
    public HashMap<Integer,ProductoConectado> getProductosConectados() {
        return misProductos;
    }

    /**
     * Encuentra un producto según su id y devuelve los que están conectados
     * @param id Id del producto que se quiere investigar
     * @param mask Conjunto de productos que se quieren omitir
     * @return Los productos conectados menos los que hay en mask
     */
    public ArrayList<Integer> getConectados (Integer id, ArrayList<Integer> mask) {
        ProductoConectado temp = misProductos.get(id);
        ArrayList<Integer> ret = new ArrayList<>();
        for (Integer p : temp.getHijos()) {
            if (!mask.contains(p)) {
                ret.add(p);
            }
        }
        return  ret;
    }



}

//Autor: Daniel Mejias
/**
 * Una clase auxiliar para AlgoritmoAproximadoKruskal, ProductoConectado representa un producto y todos los
 * productos que tiene conectado (hijos), se usa en particular durante el DFS para poder saber a los
 * nodos adyacentes de un nodo arbitrario fácilmente.
 */
class ProductoConectado {
    ArrayList<Integer> hijos;

    public ProductoConectado() {
        hijos = new ArrayList<>();
    }

    public void addHijo(Integer prod) {
        hijos.add(prod);
    }

    public ArrayList<Integer> getHijos () {
        return hijos;
    }
}



//Autor: Daniel Mejias
/**
 * Una clase auxiliar para AlgoritmoAproximadoKruskal, DisjointSet es una estructura de datos muy eficiente
 * y popular usada para agrupar nodos (que comienzan separados) y saber a que grupo pertenecen rápidamente.
 * Se usa en la creación del MST para saber que nodos ya están conectados, así si se quieren conectar dos nods
 * que pertenecen al mismo grupo (ya estaban conectados) sabremos que formarian un ciclo.
 */
class DisjointSet {
    Integer[] ds;
    public DisjointSet(int size) {
        ds = new Integer[size];
        for (int i = 0; i < size; i++) {
            ds[i] = -1;
        }
    }

    /**
     * Encuentra el grupo al que pertenece un nodo
     * @param x Id del nodo
     * @return Id del grupo al que pertenece
     */
    public int find (int x) {
        if (ds[x] == -1) {
            return x;
        } else {
            return find(ds[x]);
        }
    }

    /**
     * Une el grupo de un nodo al grupo al que pertenece otro nodo
     * @param x Primer nodo
     * @param y Segundo nodo
     */
    public void union (int x, int y) {
        ds[find(x)] = y;
    }
}
