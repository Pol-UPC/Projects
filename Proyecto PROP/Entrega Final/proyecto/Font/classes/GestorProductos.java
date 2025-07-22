/**
 * Autor: Alex Lafuente Gonzalez
 */

package Font.classes;

import Font.Exceptions.MismoNombre;
import Font.Exceptions.NoExisteNingunProducto;
import Font.Exceptions.NoExisteProducto;
import Font.Exceptions.YaExisteProducto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Clase gestora de las instancias de Producto, que contiene el conjunto de instancias de Producto y se encarga de
 * consultarlos o manipipularlos cuando recibe órdenes del ControladorProductos
 */
public class GestorProductos {

    /**
     * Estructura que guarda el conjunto de instancias de Producto de la ejecución actual, cuyo nombre es la key
     */
    private HashMap<String, Producto> productos;

    /**
     * Estructura que guarda el conjunto de instancias de Producto de la ejecución actual, cuyo id es la key
     */
    private HashMap<Integer, Producto> productosPorId;

    /**
     * Entero que indica el tamaño del pool actual de ids
     */
    // (cada vez que se añada un producto, nIds++)
    private int nIds;
    /**
     * Ids que se quedan sin producto tras este ser borrado, con la intención de ser reutilizadas cuando se cree
     * uno nuevo
     */
    private PriorityQueue<Integer> idsDisponibles;

    /**
     * Inicializa los atributos del GestorProductos
     */
    public GestorProductos() {
        idsDisponibles = new PriorityQueue<Integer>();
        productos = new HashMap<String, Producto>();
        productosPorId = new HashMap<Integer, Producto>();
        nIds = 0;
    }

    /**
     * Devuelve la lista de instancias de Productos, cuya key es el nombre
     * @return conjunto de productos instanciados
     */
    public HashMap<String, Producto> getListaProductos() {
        return productos;
    }

    /**
     * Devuelve la lista de instancias de Productos, cuya key es el id
     * @return conjunto de productos instanciados
     */
    public HashMap<Integer, Producto> getProductosPorId() {
        return productosPorId;
    }

    /**
     * Devuelve un ArrayList que contiene los nombres de todas las instancias de Producto
     * @return ArrayList que contiene los nombres de todas las instancias de Producto
     */
    public ArrayList<String> getNombresProductos() {
        ArrayList<String> nombresProducto = new ArrayList<>();
        for (String key : productos.keySet()) {
            // Se podría añadir directamente la key, pero por si acaso, le pido el nombre al usuario mapeado por la key
            // (aunque sean iguales)
            nombresProducto.add(productos.get(key).getNombre());
        }

        return nombresProducto;
    }

    /**
     * Devuelve un ArrayList que contiene los ids de todas las instancias de Producto
     * @return ArrayList que contiene los ids de todas las instancias de Producto
     */
    public ArrayList<Integer> getIdsProductos() {
        ArrayList<Integer> idsProducto = new ArrayList<>();
        for (Integer key : productosPorId.keySet()) {
            // Se podría añadir directamente la key, pero por si acaso, le pido el nombre al usuario mapeado por la key
            // (aunque sean iguales)
            idsProducto.add(productosPorId.get(key).getId());
        }

        return idsProducto;
    }

    /**
     * Cambia el nombre de una instancia de Producto
     * @param nombre nombre actual de la instancia de Producto
     * @param newNombre nuevo nombre que se le quiere asignar a la instancia de Producto
     * @throws MismoNombre en caso de que el nombre actual y nuevo coincidan
     * @throws YaExisteProducto en caso de que ya haya una instancia de Producto con el nombre newNombre
     */
    public void setNombreProducto(String nombre, String newNombre) throws MismoNombre, YaExisteProducto {
        if (nombre.equals(newNombre)) {
            throw new MismoNombre("El nombre nuevo es igual al nombre actual");
        }
        if (productos.containsKey(newNombre)) {
            throw new YaExisteProducto("Se ha tratado de cambiar el nombre del producto a uno que ya está en uso");
        }

        Producto p = productos.get(nombre);
        p.setNombre(newNombre);
        // Eliminar relacion entre la key obsoleta y el Producto
        productos.remove(nombre);
        // Añadir la relaacion entre la nueva key y el Producto
        productos.put(newNombre, p);
    }

    /**
     * Crea un nuevo producto, al que le asigna la id única más pequeña que esté disponible
     * @param nombre nombre del nuevo Producto
     * @throws YaExisteProducto en caso de que el Producto con el nombre ya exista
     */
    public void crearProducto(String nombre) throws YaExisteProducto {
        if (productos.containsKey(nombre)) {
            // Cuando haya capa de presentación, esta se encargará de mostrar los errores por pantalla
            throw new YaExisteProducto("Ya existe un producto " + nombre);

        }
        int id;
        if (idsDisponibles.isEmpty()) {
            id = nIds++;
        } else {
            id = idsDisponibles.peek();
            idsDisponibles.poll();
        }
        Producto p = new Producto(id, nombre);
        productos.put(nombre, p);
        productosPorId.put(id, p);
    }

    /**
     * Elimina un producto existente, y añade su id a la pool de disponibles, para reciclarla
     * @param nombre nombre del Producto a eliminar
     * @throws NoExisteNingunProducto en caso de que no haya ninguna instancia de Producto en el sistema
     * @throws NoExisteProducto en caso de que no haya ninguna instancia de Producto con el nombre
     */
    public void eliminarProducto(String nombre) throws NoExisteNingunProducto, NoExisteProducto {
        if (productos.isEmpty()) {
            throw new NoExisteNingunProducto("No existe ningún producto\n");
        }
        if (!productos.containsKey(nombre)) {
            throw new NoExisteProducto("No existe ningún producto llamado " + nombre);
        }

        // Añadir id al conjunto de ids disponibles para los futuros nuevos productos
        Integer id = productos.get(nombre).getId();
        idsDisponibles.offer(id);
        productos.remove(nombre);
        productos.remove(id);
    }

    /**
     * Devuelve si hay algún Producto instanciado
     * @return si existe alguna instancia de Producto en el sistema
     */
    public boolean existeAlgunProducto() {
        return !productos.isEmpty();
    }

    /**
     * Devuelve si hay algun producto con el nombre instanciado
     * @param nombre del Producto
     * @return si existe alguna instancia de Producto con el nombre en el sistema
     */
    public boolean existeProducto(String nombre) {
        return productos.containsKey(nombre);
    }

    /**
     * Devuelve si hay algun producto con el id instanciado
     * @param id del Producto
     * @return si existe alguna instancia de Producto con el id en el sistema
     */
    public boolean existeProducto(Integer id) {
        return productosPorId.containsKey(id);
    }

    /**
     * Devuelve un producto del conjunto de instancias
     * @param nombre del Producto
     * @return la instancia de Producto con el nombre
     * @throws NoExisteNingunProducto en caso de que no haya ninguna instancia de Producto en el sistema
     * @throws NoExisteProducto en caso de que no haya ninguna instancia de Producto con el nombre
     */
    public Producto getProducto(String nombre) throws NoExisteNingunProducto, NoExisteProducto {
        if (productos.isEmpty()) {
            throw new NoExisteNingunProducto("No existe ningún producto\n");
        }
        if (!productos.containsKey(nombre)) {
            throw new NoExisteProducto("No existe ningún producto llamado " + nombre);
        }

        return productos.get(nombre);
    }

    /**
     * Devuelve un producto del conjunto de instancias
     * @param id del Producto
     * @return la instancia de Producto con el nombre
     * @throws NoExisteNingunProducto en caso de que no haya ninguna instancia de Producto en el sistema
     * @throws NoExisteProducto en caso de que no haya ninguna instancia de Producto con el nombre
     */
    public Producto getProducto(Integer id) throws NoExisteNingunProducto, NoExisteProducto {
        if (productosPorId.isEmpty()) {
            throw new NoExisteNingunProducto("No existe ningún producto\n");
        }
        if (!productosPorId.containsKey(id)) {
            throw new NoExisteProducto("No existe ningún producto con Id " + id);
        }

        return productosPorId.get(id);
    }

    public void setProductos(HashMap<String, Producto> p) {
        productos = p;
    }

    public String getImagenProducto(String nombre) {
        return productos.get(nombre).getImagen();
    }

    public void setImagenProducto(String nombreProducto, String imagen) {
        productos.get(nombreProducto).setImagen(imagen);
    }

    /**
     * Resetea el conjunto de instancias de Producto
     */
    public void borrarProductos() {
        productos.clear();
        productosPorId.clear();
        nIds = 0;
        idsDisponibles.clear();
    }

    public void setProductosPorId(HashMap<Integer, Producto> p) {
        productosPorId = p;
    }

    /**
     * Devuelve un ArrayList con las ids, de aquellos nombres de Productos pasados por parámetro
     * @param names ArrayList de nombres de instancias de Producto
     * @return ArrayList con las ids, de aquellos nombres de Productos pasados por parámetro
     * @throws NoExisteNingunProducto en caso de que no haya ninguna instancia de Producto
     * @throws NoExisteProducto en caso de que no exista algún Producto con cualquiera de los names
     */
    public ArrayList<Integer> passNamesToIds(ArrayList<String> names) throws NoExisteNingunProducto, NoExisteProducto {
        ArrayList<Integer> ids = new ArrayList<>();
        if (!existeAlgunProducto()) {
            throw new NoExisteNingunProducto("No hay ningun producto creado");
        }

        for (String name : names) {
            Integer id = productos.get(name).getId();
            if (id == null) {
                throw new NoExisteProducto("No existe el producto " + name);
            }
            ids.add(id);
        }
        return ids;
    }
}
