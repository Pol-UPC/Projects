// autor: Christian Conejo Raubiene

package Font.classes;

import java.util.ArrayList;
import java.util.HashMap;

import Font.Exceptions.*;
import Font.domaincontrollers.ControladorEstanteria;
import Font.domaincontrollers.ControladorProductos;

/**
 * Un Gestor de Similitudes es una estructura dedicada a mantener una organización eficiente en espacio y tiempo de las similitudes entre los productos.
 *
 * Está constituida por un primer Hashmap que tiene todas las ids (valor único representativo de un producto) como llaves a un segundo HashMap, El HashMap de cada llave contiene el conjunto de ids que son más pequeñas que la primera que a su vez están relacionadas con la primera id, Cada llave de este segundo hashmap interior lleva al valor de la similitud entre las dos ids, Nótese que al hablar de segundo HashMap se hace referencia al interior, y que la cantidad de HashMaps será igual a la cantidad de productos más uno, Un segundo HashMap solo contiene ids más pequeñas que la id que le ha llevado a él para evitar duplicidades de similitudes.
 *
 * Inicialmente si no se encuentra una llave con la id pequeña dentro del primer HashMap, se considerará que el grado de similitud entre las dos id es 0, aunque un usuario puede añadir este valor de forma manual, y quedará definido en la estructura, Nótese que esto puede cambiar el resultado de un algoritmo aproximado.
 */
public class GestorSimilitudes
{
    // Atributos Privados

    // Un mapa de ids unicamente contiene las similitudes de las ids mas pequeñas que ella
    private HashMap <Integer, HashMap <Integer, Integer>> mapaSimilitudes;

    /**
     * Lock para aplicar el patrón singleton con double check synchronization
     */
    private static final GestorSimilitudes lock = new GestorSimilitudes();
    /**
     * Instancia para aplicar el patrón singleton con double check synchronization
     */
    private static volatile GestorSimilitudes instance;

    // Funciones Publicas

    /**
     * Constructora para crear un gestor de Similitudes vacio.
     */
    private GestorSimilitudes()
    {
        mapaSimilitudes = new HashMap<Integer, HashMap<Integer, Integer>>();
    }

    /**
     * Devuelve una instancia de GestorSimilitudes aplicando el patrón singleton con double check synchronization
     * @return instancia de la clase única
     */
    // Double check synchronization para el patrón singleton
    public static GestorSimilitudes getInstance() {
        GestorSimilitudes g = instance;
        // Si no hay ninguna instancia
        if (g == null) {
            // Mientras se espera al lock, otro thread puede haber inicializado el objeto.
            synchronized (lock) {
                g = instance;
                // Sigue siendo nulo
                if (g == null) {
                    g = new GestorSimilitudes();
                    instance = g;
                }
            }
        }
        return g;
    }

    /**
     * Devuelve un array con las ids de todos los productos en similitudes.
     * @return el array del objeto creado segun el orden de tamaño de la id pequeña hacia la grande.
     */
    public ArrayList <Integer> obtenerIdsProductos()
    {
        return new ArrayList<>(mapaSimilitudes.keySet());
    }

    /**
     * Constructora para crear con los valores correctos una Similitud
     * @return el objeto creado
     * @throws NoExisteNingunProducto cuando no hay ningun producto en la lista.
     */
    public ArrayList <Similitud> obtenerTodasSimilitudes() throws NoExisteNingunProducto
    {
        if (mapaSimilitudes.isEmpty()) throw new NoExisteNingunProducto("No existe ningun producto");
        ArrayList <Similitud> arraySimilitudesAux = new ArrayList<Similitud>();
        for (Integer idGrandeAux : mapaSimilitudes.keySet())
        {
            HashMap <Integer, Integer> similarsId = mapaSimilitudes.get(idGrandeAux);
            for (Integer idPequeñaAux : similarsId.keySet())
            {
                Similitud similitudAux = new Similitud(idGrandeAux, idPequeñaAux, similarsId.get(idPequeñaAux));
                arraySimilitudesAux.add(similitudAux);
            }
        }
        return arraySimilitudesAux;
    }

    /**
     * Guarda en la estructura de datos de similitudes para ir a recuperarse.
     * @param id1 es la id pequeña por la cual se identifica la Similitud
     * @param id2 es la id grande por la cual se identifica la Similitud
     * @return el objeto creado
     * @throws NoExisteProducto "No existe el producto id1 o id2")";
     */
    // Cambia el valor de la similitud entre dos productos.
    public Integer obtenerSimilitud (Integer id1, Integer id2) throws NoExisteProducto
    {
        if (!mapaSimilitudes.containsKey(id1)) throw new NoExisteProducto ("No existe ninguna similitud con el producto " + id1 + ".");
        if (!mapaSimilitudes.containsKey(id2)) throw new NoExisteProducto ("No existe ninguna similitud con el producto " + id2 + ".");

        if (id1.equals(id2)) return 0;

        Integer idGrande = Math.max(id1, id2);
        Integer idPequeña = Math.min(id1, id2);

        HashMap <Integer, Integer> similitudesAuxiliares = mapaSimilitudes.get(idGrande);
        return similitudesAuxiliares.getOrDefault(idPequeña, 0);
    }

    /**
     * Destructora para eliminar con los valores correctos una Similitud.
     * @param id id del producto por eliminar
     * @throws NoExisteProducto se lanza cuando el producto a eliminar no existe realmente.
     */
    public void eliminarSimilitudesDeProducto(Integer id) throws NoExisteProducto // Elimina todas las similitudes de un producto
    {
        if (!mapaSimilitudes.containsKey(id)) throw new NoExisteProducto( "El producto " + id + " no existe entre las similitudes.");
        mapaSimilitudes.remove(id);
        for (Integer idAux : mapaSimilitudes.keySet())
        {
            if (id < idAux)
                mapaSimilitudes.get(idAux).remove(id);
        }
    }

    /**
     * Añade un producto nuevo a la estructura de similitudes para que pueda ser reconocido.
     * @param id id del producto nuevo.
     * @throws YaExisteProducto salta si tal producto ya existia.
     */
    public void crearSimilitudesDeProducto(int id) throws YaExisteProducto // Añade los espacios de un producto a la estructura, rellenandolos con 0
    {
        if (mapaSimilitudes.containsKey(id)) throw new YaExisteProducto("El producto " + id + "ya existe entre las similitudes.");

        HashMap <Integer, Integer> SimilitudesAuxiliares = new HashMap<Integer, Integer>();
        mapaSimilitudes.put(id, SimilitudesAuxiliares);
    }

    /**
     * Cambia la similitud entre dos productos existentes segun sus ids.
     * @param id1 id de uno de los productos
     * @param id2 id de uno de los productos
     * @param similitud es el valor nuevo de la similitud entre los dos productos.
     * @throws NoExisteProducto salta si uno de los dos productos identificados por la id no existe.
     * @throws ValorSimilitudFueraRango salta si la similitud no esta dentro del rango deseado.
     * @throws SimilitudMismoProducto salta si se intenta cambiar la similitud de un mismo producto, pues un producto no es similar con el mismo.
     */
    public void cambiarSimilitud(Integer id1, Integer id2, Integer similitud) throws NoExisteProducto, ValorSimilitudFueraRango, SimilitudMismoProducto
    {
        if (!mapaSimilitudes.containsKey(id1)) throw new NoExisteProducto ("No existe ninguna similitud con el producto " + id1 + ".");
        if (!mapaSimilitudes.containsKey(id2)) throw new NoExisteProducto ("No existe ninguna similitud con el producto " + id2 + ".");
        if (id1 == id2) throw new SimilitudMismoProducto("No puedes crear una similitud del mismo producto.");
        if (similitud < 0 || similitud > 100) throw new ValorSimilitudFueraRango("El valor atribuido a la similitud entre " + id1 + " y " + id2 + " (" + similitud + ") tiene que estar entre 0 y 100.");
        if (similitud == -0) similitud = 0;

        Integer idGrande, idPequeña;
        idGrande = Math.max(id1, id2);
        idPequeña = Math.min(id1, id2);
        Integer similitudAux = similitud;

        HashMap <Integer, Integer> similitudesAuxiliares = mapaSimilitudes.get(idGrande);
        similitudesAuxiliares.put(idPequeña, similitudAux);
        mapaSimilitudes.put(idGrande, similitudesAuxiliares);
    }

    /**
     * Elimina todas las similitudes.
     */
    public void eliminarTodasSimilitudes()
    {
        mapaSimilitudes = new HashMap<Integer, HashMap<Integer, Integer>>();
    }

    /**
     * Lista por pantalla todas las similitudes que se hayan definido. Dos productos sin similitud definida tienen un grado de similitud de 0.
     */
    public void listarSimilitudes()
    {
        for (Integer idGrandeAux : mapaSimilitudes.keySet())
        {
            HashMap<Integer, Integer> similitudesAux = mapaSimilitudes.get(idGrandeAux);
            for (Integer idPequeñaAux : similitudesAux.keySet())
            {
                System.out.println(idPequeñaAux+ ";" + idGrandeAux + " = " + similitudesAux.get(idPequeñaAux));
            }
        }
    }
}