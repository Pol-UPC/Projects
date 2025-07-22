/**
 * Autor: Alex Lafuente Gonzalez
 */

package Font.datacontrollers;


import Font.Exceptions.*;
import Font.classes.Usuario;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DataController {

    /**
     * Lock para aplicar el patrón singleton con double check synchronization
     */
    private static final DataController lock = new DataController();
    /**
     * Instancia para aplicar el patrón singleton con double check synchronization
     */
    private static volatile DataController instance;

    /**
     * Instancia única del DataUsersController, al que llamará para gestionar los datos persistentes de las instancias de Usuario
     */
    private DataUsersController controladorDatosUsuarios;

    /**
     * Instancia del gestor encargado de gestionar la lectura y escritura de los jsons usados para almacenar el estado de la aplicación
     */
    private GestorCargaGuardadoJSON gestorCargaGuardadoJSON;

    /**
     * Instancia del gestor, encargado de gestionar la creación y borrado de los directorios de los usuarios
     */
    private GestorDirectoriosUsuario gestorDirectoriosUsuario;

    /** Constructora, la cual inicializa el resto de controladores **/
    private DataController() {
        // Inicializar subcontroladores de datos
        // inicializar controlador de datos de usuarios
        controladorDatosUsuarios = DataUsersController.getInstance();
        gestorCargaGuardadoJSON = new GestorCargaGuardadoJSON();
        gestorDirectoriosUsuario = new GestorDirectoriosUsuario();
    }

    /**
     * Devuelve una instancia de DataController aplicando el patrón singleton con double check synchronization
     * @return instancia de la clase única
     */
// Double check synchronization para el patrón singleton
    public static DataController getInstance() {
        DataController c = instance;
        // Si no hay ninguna instancia
        if (c == null) {
            // Mientras se espera al lock, otro thread puede haber inicializado el objeto.
            synchronized (lock) {
                c = instance;
                // Sigue siendo nulo
                if (c == null) {
                    c = new DataController();
                    instance = c;
                }
            }
        }
        return c;
    }

    public HashMap <String, Usuario> inicializarMapaUsuarios() throws IOException {
        return controladorDatosUsuarios.inicializar_map();
    }

    /**
     * Llama a ControladorUsuario para que elimine el Usuario que ha iniciado sesión y está en uso en la ejecución actual
     * , después lo actualiza en el archivo de persistencia de instancias de Usuario
     * @param actual Usuario actual de la ejecución
     * @throws IOException en caso de que se falle al acceder al fichero para eliminar el usuario actual de la ejecución
     * @throws ErrorEliminarArchivoOriginal si el usuario actual de la ejecución no se encuentra en el archivo de persistencia
     * @throws UsuarioNoEncontrado en caso de que el usuario pasado por parámetro no coincida con el usuario actual de la ejecución
     */
    public void eliminarUsuarioEnArchivo(Usuario actual) throws IOException, ErrorEliminarArchivoOriginal, UsuarioNoEncontrado {
        controladorDatosUsuarios.eliminarUsuarioEnArchivo(actual);
    }

    /**
     * Guarda la instancia del Usuario actual usado en la ejecución, en el fichero de persistencia de instancias de Usuario
     * @param actual Usuario actual usado en la ejecución
     * @throws IOException en caso de que se falle al acceder al fichero para guardar el nuevo Usuario dado de alta
     */
    public void guardarUsuarioEnArchivo(Usuario actual) throws IOException {
        controladorDatosUsuarios.guardarUsuarioEnArchivo(actual);
    }

    /**
     * Llama a gestorDirectoriosUsuario para crear el directorio del usuario
     * @param usuario usuario del cual se quiere crear el directorio
     * @throws YaExisteCarpeta en caso de que ya exista el directorio del usuario
     */
    public void crearCarpetaUsuario(String usuario) throws YaExisteCarpeta {
        gestorDirectoriosUsuario.crearCarpetaUsuario(usuario);
    }

    /**
     * Llama a gestorDirectoriosUsuario para borrar el directorio del usuario
     * @param usuario usuario del cual se quiere borrar el directorio
     * @throws NoExisteCarpeta en caso de que no exista el directorio del usuario
     */
    public void borrarCarpetaUsuario(String usuario) throws NoExisteCarpeta {
        gestorDirectoriosUsuario.borrarCarpetaUsuario(usuario);
    }

    /**
     * Actualiza en el fichero de persistencia, la contraseña del Usuario actual (que ha sido previamente cambiada durante la
     * ejecución)
     * @param nombreUsuario nombre del Usuario actual usado en la ejecución
     * @param nuevaPassword nombre de la nueva contraseña del usuario actual, a guardar en el fichero de persistencia de instancias de Usuario
     * @throws IOException en caso de que se falle al acceder al fichero para actualizar la contraseña
     */
    public void actualizarPasswordEnArchivo(String nombreUsuario, String nuevaPassword) throws IOException {
        controladorDatosUsuarios.actualizarPasswordEnArchivo(nombreUsuario, nuevaPassword);
    }

    /**
     * Llama a gestorCargaGuardadoJSON para parsear un archivo .json para poder guardar toda la información de un estado
     * @param nombreArchivo nombre del archivo al cual se quiere acceder
     * @param nombreUsuario nombre del usuario al que se quiere guardar
     * @return un hashmap con los productos en una disposición concreta, el valor de similitud que contienen, si importa
     * @throws IOException se lanza cuando ocurre un error de entrada o salida
     * @throws ParseException se lanza cuando ocurre un error al parsear el producto
     * @throws NumberFormatException se lanza cuando se trata de obtener un valor entero de una string
     * @throws CargaArchivoNoValido se lanza cuando se intenta leer una disposición de estanteria que no son un conjunto de objetos cualquiera
     */
    public HashMap <Integer, ArrayList<String>> cargarConjuntoProductos(String nombreArchivo, String nombreUsuario) throws IOException, ParseException, NumberFormatException, CargaArchivoNoValido {
        return gestorCargaGuardadoJSON.cargarConjuntoProductos(nombreArchivo, nombreUsuario);
    }

    /**
     * Llama a gestorCargaGuardadoJSON para parsear un archivo .json para poder guardar toda la información de un estado
     * @param nombreArchivo nombre del archivo al cual se quiere acceder
     * @param nombreUsuario nombre del usuario al que se quiere guardar
     * @return un hashmap con los productos en una disposición concreta, el valor de similitud que contienen, si importa
     * @throws IOException se lanza cuando ocurre un error de entrada o salida
     * @throws ParseException se lanza cuando ocurre un error al parsear el producto
     * @throws NumberFormatException se lanza cuando se trata de obtener un valor entero de una string
     * @throws CargaArchivoNoValido se lanza cuando se intenta leer un conjunto de objetos que no son una disposición de estanteria
     */
    public HashMap <Integer, ArrayList<String>> cargarSolucionEstanteria(String nombreArchivo, String nombreUsuario) throws IOException, ParseException, NumberFormatException {
        return gestorCargaGuardadoJSON.cargarSolucionEstanteria(nombreArchivo, nombreUsuario);
    }

    /**
     * Llama a gestorCargaGuardadoJSON para guardar los productos y el conjunto de similitudes de esos productos en un archivo con un nombre concreto para un usuario en concreto
     * @param productos listado arbitrario de nombres de un producto
     * @param similitudes listado de strings que contienen la información de una similitud, Cada similitud contiene dos nombres de producto y un valor de similitud, El listado puede estar vacia
     * @param nombreArchivo nombre del archivo al cual se quiere acceder.
     * @param nombreUsuario nombre del usuario al que se quiere guardar
     * @throws NoExisteNingunProducto se lanza cuando no existe ningun producto a cambiar
     * @throws IOException se lanza cuando ocurre un error de entrada o salida
     */
    public void guardarConjuntoProductos(ArrayList<String> productos, ArrayList<String> similitudes, String nombreArchivo, String nombreUsuario) throws NoExisteNingunProducto, IOException {
        gestorCargaGuardadoJSON.guardarConjuntoProductos(productos, similitudes, nombreArchivo, nombreUsuario);
    }

    /**
     * Llama a gestorCargaGuardadoJSON para guardar los productos con un orden predeterminado, un grado de similitud total relativo al orden, y el conjunto de similitudes de esos productos; en un archivo con un nombre concreto para un usuario en concreto
     * @param productos listado ordenado de nombres de un producto
     * @param gradoSimilitud valor que indica el grado de similitud total del orden de los productos
     * @param similitudes listado de strings que contienen la información de una similitud, Cada similitud contiene dos nombres de producto y un valor de similitud, El listado puede estar vacia.
     * @param nombreArchivo nombre del archivo al cual se quiere acceder
     * @param nombreUsuario nombre del usuario al que se quiere guardar
     * @throws NoExisteNingunProducto se lanza cuando no existe ningun producto a cambiar
     * @throws IOException se lanza cuando ocurre un error de entrada o salida
     * @throws DiscordanciaEstructuras se lanza cuando se sabe que hay discordancia entre el orden de los productos y las similitudes
     */
    public void guardarSolucionEstanteria(ArrayList<String> productos, Integer gradoSimilitud, ArrayList<String> similitudes, String nombreArchivo, String nombreUsuario) throws NoExisteNingunProducto, IOException, DiscordanciaEstructuras{
        gestorCargaGuardadoJSON.guardarSolucionEstanteria(productos, gradoSimilitud, similitudes, nombreArchivo, nombreUsuario);
    }

    public ArrayList<String> getArchivosGuardadoProductos(String nombreUsuario) {
        return gestorDirectoriosUsuario.obtenerListaFicherosConjunto(nombreUsuario);
    }

    public ArrayList<String> getArchivosGuardadoEstado(String nombreUsuario) {
        return gestorDirectoriosUsuario.obtenerListaFicherosEstado(nombreUsuario);
    }

    public boolean existeArchivoProductos(String nombreUsuario, String nombreArchivo) {
        return gestorDirectoriosUsuario.existeArchivoProductos(nombreUsuario, nombreArchivo);
    }

    public boolean existeArchivoEstado(String nombreUsuario, String nombreArchivo) {
        return gestorDirectoriosUsuario.existeArchivoEstado(nombreUsuario, nombreArchivo);
    }

}
