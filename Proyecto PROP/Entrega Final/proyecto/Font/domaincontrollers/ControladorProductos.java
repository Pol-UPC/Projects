/**
 * Autor: Alex Lafuente Gonzalez
 */

package Font.domaincontrollers;

import java.util.ArrayList;
import java.util.HashMap;

import Font.Exceptions.*;
import Font.classes.GestorProductos;
import Font.classes.Producto;

/**
 * Clase controladora de las instancias de Producto se encarga de gestionar las llamadas del controlador de
 * dominio relacionadas a las instancias de Producto, y redirigir el trabajo al GestorProductos
 */
public class ControladorProductos {

    /**
     * Lock para aplicar el patrón singleton con double check synchronization
     */
    private static final ControladorProductos lock = new ControladorProductos();
    /**
     * Instancia para aplicar el patrón singleton con double check synchronization
     */
    private static volatile ControladorProductos instance;

    /**
     * Instancia única del GestorProductos, a la que llamará para usar Producto
     */
    private GestorProductos gestorProductos;

    /**
     * Constructora, la cual inicializa el GestorProductos
     */
    private ControladorProductos() {
        gestorProductos = new GestorProductos();
    }

    /**
     * Devuelve una instancia ControladorProductos aplicando el patrón singleton con double check synchronization
     * @return instancia de la clase única
     */
    // Double check synchronization para el patrón singleton
    public static ControladorProductos getInstance() {
        ControladorProductos c = instance;
        // Si no hay ninguna instancia
        if (c == null) {
            // Mientras se espera al lock, otro thread puede haber inicializado el objeto.
            synchronized (lock) {
                c = instance;
                // Sigue siendo nulo
                if (c == null) {
                    c = new ControladorProductos();
                    instance = c;
                }
            }
        }
        return c;
    }

    /**
     * Devuelve la lista de instancias de Producto almacenada en GestorProcuctos, cuya key es su nombre
     * @return conjunto de instancias de Producto
     */
    public HashMap<String, Producto> getListaProductos() {
        return gestorProductos.getListaProductos();
    }

    /**
     * Devuelve la lista de instancias de Producto almacenada en GestorProcuctos, cuya key es su id
     * @return conjunto de instancias de Producto
     * @return
     */
    public HashMap<Integer, Producto> getListaProductosPorId() {
        return gestorProductos.getProductosPorId();
    }

    /**
     * Llama al GestorProductos para que cree y añada una nueva instancia de Producto al conjunto
     * @param nombre del nuevo Producto
     * @throws YaExisteProducto en caso de que ya haya una instancia de Producto con el nombre
     */
    public void crearProducto(String nombre) throws YaExisteProducto {
        gestorProductos.crearProducto(nombre);
    }

    /**
     * Llama al GestorProductos para que elimine del conjunto, una instancia de Producto
     * @param nombre nombre del Producto
     * @throws NoExisteNingunProducto en caso de que no haya ninguna instancia de Producto en el sistema
     * @throws NoExisteProducto en caso de que no haya ninguna instancia de Producto con el nombre
     */
    public void eliminarProducto(String nombre) throws NoExisteNingunProducto, NoExisteProducto {
        gestorProductos.eliminarProducto(nombre);
    }

    /**
     * Llama al GestorProductos para que compruebe si existe alguna instancia de Producto
     * @return si hay algun Producto instanciado en el sistema
     */
    public boolean existeAlgunProducto() {
        return gestorProductos.existeAlgunProducto();
    }

    /**
     * Llama al GestorProductos para que compruebe si existe alguna instancia de Producto con el nombre
     * @param nombre nombre del Producto
     * @return si existe algun Producto instanciado con el nombre
     */
    public boolean existeProducto(String nombre) {
        return gestorProductos.existeProducto(nombre);
    }

    /**
     * Llama al GestorProductos para conseguir la instancia de Producto con el nombre
     * @param nombre nombre del Producto
     * @return la instancia del Producto
     * @throws NoExisteNingunProducto en caso de que no haya ninguna instancia de Producto en el sistema
     * @throws NoExisteProducto en caso de que no haya ninguna instancia de Producto con el nombre
     */
    public Producto getProducto(String nombre) throws NoExisteNingunProducto, NoExisteProducto {
        return gestorProductos.getProducto(nombre);
    }

    /**
     * Llama al GestorProductos para conseguir la instancia de Producto con el id
     * @param id id del Producto
     * @return la instancia del Producto
     * @throws NoExisteNingunProducto en caso de que no haya ninguna instancia de Producto en el sistema
     * @throws NoExisteProducto en caso de que no haya ninguna instancia de Producto con el id
     */
    public Producto getProducto(Integer id) throws NoExisteNingunProducto, NoExisteProducto {
        return gestorProductos.getProducto(id);
    }

    /**
     * Llama al GestorProductos para obtener un ArrayList que contiene los nombres de todas las instancias de Producto
     * @return ArrayList que contiene los nombres de todas las instancias de Producto
     */
    public ArrayList<String> getNombresProductos() {
        return gestorProductos.getNombresProductos();
    }

    /**
     * Llama al GestorProductos para obtener un ArrayList que contiene los ids de todas las instancias de Producto
     * @return ArrayList que contiene los ids de todas las instancias de Producto
     */
    public ArrayList<Integer> getIdsProductos() {
        return gestorProductos.getIdsProductos();
    }

    /**
     * Llama al GestorProductos para cambiar el nombre de una instancia de Producto
     * @param nombre nombre actual de la instancia de Producto
     * @param newNombre nuevo nombre que se le quiere asignar a la instancia de Producto
     * @throws MismoNombre en caso de que el nombre actual y nuevo coincidan
     * @throws YaExisteProducto en caso de que ya haya una instancia de Producto con el nombre newNombre
     */
    public void setNombreProducto(String nombre, String newNombre) throws MismoNombre, YaExisteProducto {
        gestorProductos.setNombreProducto(nombre, newNombre);
    }

    /**
     * Llama al GestorProductos para resetear el conjunto de instancias de Producto
     */
    public void borrarProductos() {
        gestorProductos.borrarProductos();
    }

    /**
     * Llama al GestorProductos para obtener un ArrayList con las ids, de aquellos nombres de Productos pasados por parámetro
     * @param names ArrayList de nombres de instancias de Producto
     * @return ArrayList con las ids, de aquellos nombres de Productos pasados por parámetro
     * @throws NoExisteNingunProducto en caso de que no haya ninguna instancia de Producto
     * @throws NoExisteProducto en caso de que no exista algún Producto con cualquiera de los names
     */

    public ArrayList<Integer> passNamesToIds(ArrayList<String> names) throws NoExisteNingunProducto, NoExisteProducto {
        return gestorProductos.passNamesToIds(names);
    }

    public String getImagenProducto(String nombre) {
        return gestorProductos.getImagenProducto(nombre);
    }

    public void setImagenProducto(String nombreProducto, String imagen) {
        gestorProductos.setImagenProducto(nombreProducto, imagen);
    }
}
