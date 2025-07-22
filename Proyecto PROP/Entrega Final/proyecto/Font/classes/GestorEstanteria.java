// autor: Christian Conejo Raubiene

package Font.classes;

import java.util.HashMap;
import java.util.ArrayList;

import Font.Exceptions.*;

/**
 * Un GestorEstanteria es una estructura que manipula un conjunto de productos y una estructura de EstanteriaOrdenada con el fin de utilizar las funciones a mayor nivel con el nombre de los productos en vez de ids, A su vez, comprueba la mayoría de errores.
 */
public class GestorEstanteria
{

    /** Atributos **/

    private HashMap<String, Producto> productosPorStringConjunto;
    private HashMap<Integer, Producto> productosPorIdConjunto;

    private EstanteriaOrdenada estanteriaOrdenada;

    /**
     * Constructora para crear una gestora Estanteria.
     */
    public GestorEstanteria()
    {
        estanteriaOrdenada = new EstanteriaOrdenada();
        productosPorStringConjunto = new HashMap<String, Producto>();
        productosPorIdConjunto = new HashMap<Integer, Producto>();
    }

    /**
     * Constructora para llenar directamente una gestoraEstatnteria
     * @param eOAux es la estanteriaOrdenada que se usara a continuación.
     * @param p2Aux es la estructura con la que conseguimos un producto segun su id
     * @param pAux es la estructura con la que conseguimos un producto segun su nombre
     * @param gSAux es el gestorSimilitudes
     */
    public GestorEstanteria(EstanteriaOrdenada eOAux, HashMap<String, Producto> pAux, HashMap<Integer, Producto> p2Aux, GestorSimilitudes gSAux)
    {
        estanteriaOrdenada = eOAux;
        productosPorStringConjunto = pAux;
        productosPorIdConjunto = p2Aux;
        estanteriaOrdenada.setGestorSimilitudes(gSAux);
    }

    /** Gets y sets **/

    /**
     * Define los productos que se usaran para el gestor.
     * @param productosString es el que define productos segun su nombre.
     * @param productosId es el que define productos segun su id.
     */
    public void setProductos(HashMap<String, Producto> productosString, HashMap<Integer, Producto> productosId)
    {
        productosPorStringConjunto = productosString;
        productosPorIdConjunto = productosId;
    }

    /**
     * Traspasa para su uso un Gestor de Similitudes.
     * @param gS el objeto que se quiere pasar.
     */
    public void setGestorSimilitudes(GestorSimilitudes gS)
    {
        estanteriaOrdenada.setGestorSimilitudes(gS);
    }

    /**
     * Llama a EstanteriaOrdenada para que defina una estanteriaOrdenada,
     * @param listaOrdenada estanteria Ordenada a definir
     * @param similitudTotal vallor total de ls Aimilitud
     */
    public void setEstanteriaOrdenada(ArrayList <Integer> listaOrdenada, Integer similitudTotal)
    {
        estanteriaOrdenada.setEstanteriaOrdenada(listaOrdenada, similitudTotal);
    }

    /**
     * Añade un producto al final de la estanteria ordenada.
     * @param nombreProducto es el nombre que identifica al producto
     * @throws YaExisteProducto cuando el producto a añadir ya esta añadidod en la Estanteria Ordenada.
     * @throws NoExisteProducto cuando el producto no esta en la Estanteria ORdenada.
     */
    public void añadirProductoEstanteriaOrdenada(String nombreProducto) throws YaExisteProducto, NoExisteProducto {
        if (!productosPorStringConjunto.containsKey(nombreProducto)) throw new NoExisteProducto("No existe el producto llamado " + nombreProducto + ".");
        Integer id = productosPorStringConjunto.get(nombreProducto).getId();
        if (estanteriaOrdenada.estaColocado(id)) throw new YaExisteProducto("El producto " + nombreProducto + " ya esta colocado en la estanteria");
        estanteriaOrdenada.añadirProducto(id);
    }

    // Posible añadir segun posicion en el futuro.

    /**
     * Llama a Estanteria Ordenada para eliminar un producto de la Estanteria Ordenada.
     * @param nombreProducto como clave externa para obtener la id de los productos.
     * @throws NoExisteProducto cuando el producto no existe.
     */
    public void eliminarProducto(String nombreProducto) throws NoExisteProducto {
        if (!productosPorStringConjunto.containsKey(nombreProducto)) throw new NoExisteProducto("No existe el producto llamado " + nombreProducto + ".");
        Integer id = productosPorStringConjunto.get(nombreProducto).getId();
        if (!estanteriaOrdenada.estaColocado(id)) return;
        estanteriaOrdenada.eliminarProducto(id);
    }


    /**
     * Llama a EstanteriaOrdenada para actualizar el valor de grado de la similitud total, si hiciera falta, tras el cambio de similitudes
     * @param id1 id del primer producto a cambiar similitud
     * @param id2 id del segundo producto a cambiar similitud
     * @param similitud similitud es el valor del grado de la similitud a cambiar
     * @throws NoExisteProducto se lanza cuando uno de los dos productos no existe.
     */
    public void cambiarSimilitud(Integer id1, Integer id2, Integer similitud) throws NoExisteProducto, ValorSimilitudFueraRango
    {
        if (!estanteriaOrdenada.estaColocado(id1)) return;
        if (!estanteriaOrdenada.estaColocado(id2)) return;
        estanteriaOrdenada.cambiarSimilitud(id1, id2, similitud);
    }

    /**
     * Intercambia la posicion de dos productos en la solución actual.
     * @param nombreProducto1 como clave externa del producto
     * @param nombreProducto2 como clave externa del producto
     * @throws NoExisteProducto cuando no existe el producto 1 o 2, o no esta colocado en la estanteria.
     * @throws MismoNombre cuando se quiere intercambiar el mismo producto.
     */
    public void intercambiarProductos (String nombreProducto1, String nombreProducto2) throws NoExisteProducto, MismoNombre
    {
        if (nombreProducto1.equals(nombreProducto2)) throw new MismoNombre("Estas tratando de intercambiar el mismo producto (" + nombreProducto1 + ").");
        if (!productosPorStringConjunto.containsKey(nombreProducto1)) throw new NoExisteProducto("El producto " + nombreProducto1 + " no existe.");
        if (!productosPorStringConjunto.containsKey(nombreProducto2)) throw new NoExisteProducto("El producto " + nombreProducto2 + " no existe.");
        Integer id2 = productosPorStringConjunto.get(nombreProducto2).getId();
        Integer id1 = productosPorStringConjunto.get(nombreProducto1).getId();
        if (!estanteriaOrdenada.estaColocado(id1)) throw new NoExisteProducto("El producto " + nombreProducto1 + " no esta en la estanteria.");
        else if (!estanteriaOrdenada.estaColocado(id2)) throw new NoExisteProducto("El producto " + nombreProducto2  + " no esta en la estanteria.");
        estanteriaOrdenada.intercambiarProductos(id1, id2);
    }

    /**
     * Obtiene la id de un producto segn su nombre.
     * @param nombreProducto como clave externa del producto
     * @return id del producto llamado nombre.
     * @throws NoExisteProducto cuando el producto no esta en la estanteria.
     */
    public Integer getIdProducto(String nombreProducto) throws NoExisteProducto
    {
        if (!productosPorStringConjunto.containsKey(nombreProducto)) throw new NoExisteProducto("El producto " + nombreProducto + " no esta en la estanteria.");
        return productosPorStringConjunto.get(nombreProducto).getId();
    }

    /**
     * Obtiene la similitud total de al EstanteriaOrdenada que gestiona
     * @return valor de la similitud total
     */
    public Integer getSimilitudTotal()
    {
        return estanteriaOrdenada.getSimilitudTotal();
    }

    /**
     *Devuelve la EstanteriaOrdenada.
     * @return EstanteriaOrdenada usada
     */
    public EstanteriaOrdenada getEstanteriaOrdenada() {
        return estanteriaOrdenada;
    }

    /**
     * Llama a EstanteriaOrdenada para obtener la lista de las ids ordenadas de una forma determinada.
     * @return Array list de ids de productos
     */
    public ArrayList <Integer> getListaOrdenProductos()
    {
        return estanteriaOrdenada.getListaOrdenProductos();
    }

    /**
     * Llama a EstanteriaOrdenada para obtener la lista de los nombres de los productos ordenados de una forma determinada.
     * @return Array list de ids de productos
     */
    public ArrayList <String> getListaOrdenProductosPorNombre()
    {
        ArrayList<Integer> eOaux = estanteriaOrdenada.getListaOrdenProductos();
        ArrayList<String> eOreal = new ArrayList<String>();
        for (Integer i : eOaux)
        {
            if (i != -1) eOreal.add(productosPorIdConjunto.get(i).getNombre());
        }
        return eOreal;
    }

    /**
     *Llama a EstanteriaOrdenada para obtener el listado de productos segun sus ids, e imprime todas las necesarias.
     */
    public void listarProductos()
    {
        ArrayList<Integer> listaOrdenada = estanteriaOrdenada.getListaOrdenProductos();
        for (Integer id : listaOrdenada) {
            if (id != -1) System.out.print(id + " ");
        }
        System.out.println();
        System.out.println("Grado de Similitud Total: " + estanteriaOrdenada.getSimilitudTotal());
        System.out.println();
    }

}
