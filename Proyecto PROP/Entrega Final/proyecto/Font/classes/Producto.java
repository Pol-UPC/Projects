/**
 * Autor: Alex Lafuente Gonzalez
 */

package Font.classes;

/**
 * Estructura que contiene dos variables: La id representa el entero identificador de la instancia, y el nombre es la
 * String asociada a esta
 */
public class Producto {

    /** Atributos **/

    /**
     * Nombre del Producto
     */
    private String nombre;
    /**
     * Identificador del Producto
     */
    private Integer id;

    /**
     * Path de la imagen del Producto
     */
    String imagen;

    /** Constructora **/

    public Producto() {
        id = null;
        nombre = null;
        imagen = null;
    }

    /***
     * Inicializa los atributos del Producto
     * @param id id del Producto
     * @param nombre nombre del Producto
     */
    public Producto(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Producto(Integer id, String nombre, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
    }

    /** Métodos públicos **/

    /** Gets i Sets **/

    /***
     * Devuelve el nombre del Producto
     * @return nombre del Producto
     */
    public String getNombre() {
        return this.nombre;
    }

    /***
     * Devuelve la id del Producto
     * @return la id del Producto
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Devuelve el path de la imagen del Producto
     * @return el path de la imagen del Producto
     */
    public String getImagen() {
        return this.imagen;
    }

    /***
     * Asigna/cambia el nombre del Producto por uno nuevo
     * @param nombre nombre del nuevo Producto
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Asigna/cambia el path de la imagen del Producto por uno nuevo
     * @param imagen path de la imagen del Producto
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

}