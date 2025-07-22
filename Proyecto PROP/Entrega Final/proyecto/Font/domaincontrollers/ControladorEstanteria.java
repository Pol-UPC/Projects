/**
 * Autor: Alex Lafuente Gonzalez
 */

package Font.domaincontrollers;

import Font.classes.EstanteriaOrdenada;
import Font.classes.GestorEstanteria;
import Font.Exceptions.*;
import Font.classes.GestorSimilitudes;
import Font.classes.Producto;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Clase controladora de Estanteria, la cual se encarga de gestionar las llamadas del controlador de
 * dominio relacionadas a  Estanteria, y redirigir el trabajo al GestorEstanteria
 */
public class ControladorEstanteria {

    /**
     * Atributos
     **/

    /**
     * Lock para aplicar el patrón singleton con double check synchronization
     */
    private static final ControladorEstanteria lock = new ControladorEstanteria();
    /**
     * Instancia para aplicar el patrón singleton con double check synchronization
     */
    private static volatile ControladorEstanteria instance;

    /**
     * Instancia única del GestorEstanteria, a la que llamará para usar EstanteriaOrdenada
     */
    private GestorEstanteria gestorEstanteria;


    /**
     * Constructora, la cual inicializa el GestorEstanteria
     **/
    private ControladorEstanteria() {
        gestorEstanteria = new GestorEstanteria(); //todo aplicar patron singleton
    }

    /**
     * Devuelve una instancia de ControladorEstanteria aplicando el patrón singleton con double check synchronization
     * @return instancia de la clase única
     */
    // Double check synchronization para el patrón singleton
    public static ControladorEstanteria getInstance() {
        ControladorEstanteria c = instance;
        // Si no hay ninguna instancia
        if (c == null) {
            // Mientras se espera al lock, otro thread puede haber inicializado el objeto.
            synchronized (lock) {
                c = instance;
                // Sigue siendo nulo
                if (c == null) {
                    c = new ControladorEstanteria();
                    instance = c;
                }
            }
        }
        return c;
    }

    /**
     * Gets y sets
     **/

    /**
     * Llama a GestorEstanteria para que asigne un conjunto de instancias de Producto a la Estanteria
     * @param productos conjunto de instancias de Producto
     */
    // Puede que sea distinta la estructura de datos de la EstanteriaOrdenada
    public void setProductos(HashMap<String, Producto> productos, HashMap<Integer, Producto> productosPorId) {
        gestorEstanteria.setProductos(productos, productosPorId);
    }

    /**
     * Llama a GestorEstanteria para que añada un Producto a la EstanteriaOrdenada
     * @param nombre nombre del Producto
     * @throws YaExisteProducto en caso de que ya exista el Producto en la estantería
     * @throws NoExisteProducto en caso de no exista la instancia del Producto en el sistema
     */
    public void añadirProductoEstanteriaOrdenada(String nombre) throws YaExisteProducto, NoExisteProducto {
        gestorEstanteria.añadirProductoEstanteriaOrdenada(nombre);

    }

    /**
     * Llama a GestorEstanteria para que elimine una instancia de Producto de EstanteriaOrdenada
     * @param nombre del Producto
     * @throws NoExisteProducto en caso de que no exista el Producto en la estantería
     */
    // eliminar producto de solucion/distribucion
    public void eliminarProducto(String nombre) throws NoExisteProducto {
        gestorEstanteria.eliminarProducto(nombre);
    }

    /**
     * Llama a GestorEstanteria para obtener el Id de una instancia de Producto que se encuentra en la EstanteriaOrdenada
     * @param nombre del Producto
     * @return id del producto con el nombres
     * @throws NoExisteProducto en caso de que no exista el Producto en la estantería
     */
    public Integer getIdProducto(String nombre) throws NoExisteProducto {
        return gestorEstanteria.getIdProducto(nombre);
    }

    /**
     * Llama a GestorEstanteria para obtener la instancia de EstanteriaOrdenada
     * @return instancia de la EstanteriaOrdenada
     */
    public EstanteriaOrdenada getEstanteriaOrdenada () {
        return gestorEstanteria.getEstanteriaOrdenada();
    }

    /**
     * Llama a GestorEstanteria para intercambiar la posición de dos instancias de Producto en la EstanteriaOrdenada
     * @param n1 nombre 1 de Producto
     * @param n2 nombre 2 de Producto
     * @throws NoExisteProducto en caso de que no exista cualquiera de las dos instancias de Producto
     * @throws MismoNombre en caso de que los dos nombres coincidan
     */
    public void intercambiarProductos(String n1, String n2) throws NoExisteProducto, MismoNombre {
        gestorEstanteria.intercambiarProductos(n1, n2);
    }

    /**
     * Llama a GestorEstanteria para obtener la lista ordenada de ids de las instancias de Producto
     * @return lista ordenada de las ids de las instancias de Producto
     */
    public ArrayList<Integer> getListaOrdenada() {
        return gestorEstanteria.getListaOrdenProductos();
    }

    /**
     * Llama a GestorEstanteria para obtener la lista ordenada de nombres de las instancias de Producto
     * @return lista ordenada de las nombres de las instancias de Producto
     */
    public ArrayList<String> getListaOrdenadaProductosPorNombre() {
        return gestorEstanteria.getListaOrdenProductosPorNombre();
    }

    /**
     * Llama a GestorEstanteria para asignarle su instancia de GestorSimilitudes
     * @param gS GestorSimilitudes
     */
    public void setGestorSimilitudes(GestorSimilitudes gS)
    {
        gestorEstanteria.setGestorSimilitudes(gS);
    }

    /**
     * Llama a GestorEstanteria para cambiar la similitud entre dos instancias de Producto
     * @param id1 id del primer Producto
     * @param id2 id del segundo Producto
     * @param similitud valor de la similitud
     * @throws NoExisteProducto en caso de que no esté en la estanteria el Producto con cualquiera de las ids
     */
    public void cambiarSimilitud(Integer id1, Integer id2, Integer similitud) throws NoExisteProducto {
        gestorEstanteria.cambiarSimilitud(id1, id2, similitud);
    }

    /**
     * Llama a gestorEstanteria para obtener la similitud total de los Productos en la distribución actual de la EstanteriaOrdenada
     * @return entero con el valor de la similitud total
     */
    public Integer getSimilitudTotal() {
        return gestorEstanteria.getSimilitudTotal();
    }

    /**
     * Llama a GestorEstanteria para cambiar la distribución de la estantería que se guarda, por la que se pasa como parámetro
     * @param listaOrdenada nueva distribución de la estantería
     * @param similitudTotal valor de la similitud total de la nueva distribución
     */
    public void setEstanteriaOrdenada(ArrayList <Integer> listaOrdenada, Integer similitudTotal) {
        gestorEstanteria.setEstanteriaOrdenada(listaOrdenada, similitudTotal);
    }
}