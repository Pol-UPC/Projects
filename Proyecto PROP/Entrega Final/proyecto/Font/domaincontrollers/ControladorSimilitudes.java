// autor: Christian Conejo Raubiene

package Font.domaincontrollers;

import Font.Exceptions.*;
import Font.classes.GestorSimilitudes;
import Font.classes.Similitud;

import java.util.ArrayList;


/**
 * Classe controladora que permite que operaciones hacer del gestorSimilitudes.
 */
public class ControladorSimilitudes
{

    private GestorSimilitudes gSimilitudes;

    /**
     * Constructora del controlador de similitudes
     */
    public ControladorSimilitudes()
    {
        gSimilitudes = GestorSimilitudes.getInstance();
    }

    /**
     * Devuelve el gestor de similitudes para su llamada en otras clases.
     * @return devuelve su gestor de similitudes.
     */
    public GestorSimilitudes getGestorSimilitudes()
    {
        return gSimilitudes;
    }

    /**
     * Llama al gestor de similitdes para obtener las ids de todos los productos en el gestor
     * @return devuelve un arraylist con todas las ids
     */
    public ArrayList<Integer> obtenerIdsProductos()
    {
        return gSimilitudes.obtenerIdsProductos();
    }

    /**
     * Llama al gestor de similitudes para obtener todas las similitudes
     * @return devuelve un arraylist con todas las similitudes
     * @throws NoExisteNingunProducto se lanza cuando no existe ningun producto en la estructura de similitudes
     */
    public ArrayList <Similitud> obtenerTodasSimilitudes() throws NoExisteNingunProducto
    {
        return gSimilitudes.obtenerTodasSimilitudes();
    }

    /**
     * Llama al gestor de similitudes para obtener el valor del grado de la similitud entre dos productos.
     * @param id1 id del primer producto a obtener de la similitud
     * @param id2 id del segundo producto a obtener de la similitud
     * @return devuelve el valor del grado de la similitud entre los dos productos
     * @throws NoExisteProducto se lanza cuando uno de los dos productos no existe
     */
    public Integer obtenerSimilitud (Integer id1, Integer id2) throws NoExisteProducto
    {
        return gSimilitudes.obtenerSimilitud(id1, id2);
    }

    public void eliminarTodasSimilitudes()
    {
        gSimilitudes.eliminarTodasSimilitudes();
    }

    /**
     * Llama al gestor de similitudes para eliminar la id del producto de toda la estructura
     * @param id id del producto que se ha eliminado
     * @throws NoExisteProducto se lanza cuando la id del producto no se encuentra en la estructura
     */
    public void eliminarSimilitudesDeProducto(Integer id) throws NoExisteProducto // Elimina todas las similitudes de un producto
    {
        gSimilitudes.eliminarSimilitudesDeProducto(id);
    }

    /**
     * Llama al gestor de similitudes para añadir un producto nuevo a la estructura
     * @param id id del nuevo producto creado
     * @throws YaExisteProducto se lanza cuando el producto ya existe
     */
    public void crearSimilitudesDeProducto(int id) throws YaExisteProducto // Añade los espacios de un producto a la estructura, rellenandolos con 0
    {
        gSimilitudes.crearSimilitudesDeProducto(id);
    }

    /**
     * Llama a gestor de similitudes para cambiar la similitud entre un solo elemento
     * @param id1 id del primer producto a cambiar de la similitud
     * @param id2 id del segundo producto a cambiar de la similitud
     * @param similitud valor del grado de similitud entre los dos productos
     * @throws NoExisteProducto se lanza cuuando uno de los dos productos no existe
     * @throws ValorSimilitudFueraRango se lanza cuando el valor de la similitud esta fuera de rango
     * @throws SimilitudMismoProducto se lanza cuando se busca la similitud de un mismo producto
     */
    public void cambiarSimilitud(int id1, int id2, Integer similitud) throws NoExisteProducto, ValorSimilitudFueraRango, SimilitudMismoProducto
    {
        gSimilitudes.cambiarSimilitud(id1, id2, similitud);
    }

    /**
     * Llama al gestor de similitudes para listar por consola todas las similitudes
     */
    public void listarSimilitudes()
    {
        gSimilitudes.listarSimilitudes();
    }
}