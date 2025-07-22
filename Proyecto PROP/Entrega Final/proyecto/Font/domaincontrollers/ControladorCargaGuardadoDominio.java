/**
 * Autor: Alex Lafuente Gonzalez
 */

package Font.domaincontrollers;

import Font.Exceptions.*;
import Font.classes.Similitud;
import Font.classes.Usuario;
import Font.datacontrollers.DataController;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Clase controladora de la gestión de la comunicación con la capa de persistencia, que se centra en mandar los datos
 * a guardar en persistencia, y de organizar en dominio los datos obtenidos de la capa de persistencia
 */
public class ControladorCargaGuardadoDominio {

    /**
     * Lock para aplicar el patrón singleton con double check synchronization
     */
    private static final ControladorCargaGuardadoDominio lock = new ControladorCargaGuardadoDominio();
    /**
     * Instancia para aplicar el patrón singleton con double check synchronization
     */
    private static volatile ControladorCargaGuardadoDominio instance;

    /**
     * Instancia única del ControladorProductos
     */
    private ControladorProductos controladorProductos;
    /**
     *  Instancia única del controladorSimilitudes
     */
    private ControladorSimilitudes controladorSimilitudes;
    /**
     * Instancia única del ControladroEstanteria
     */
    private ControladorEstanteria controladorEstanteria;
    /**
     * Instancia única del ControladorUsuarios
     */
    private ControladorUsuarios controladorUsuarios;

    /**
     * Instancia única del DataController, el controlador de la capa de persistencia
     */
    private DataController controladorDatos;


    /**
     * Devuelve una instancia ControladorCargaDominio aplicando el patrón singleton con double check synchronization
     *
     * @return instancia de la clase única
     */
    // Double check synchronization para el patrón singleton
    public static ControladorCargaGuardadoDominio getInstance() {
        ControladorCargaGuardadoDominio c = instance;
        // Si no hay ninguna instancia
        if (c == null) {
            // Mientras se espera al lock, otro thread puede haber inicializado el objeto.
            synchronized (lock) {
                c = instance;
                // Sigue siendo nulo
                if (c == null) {
                    c = new ControladorCargaGuardadoDominio();
                    instance = c;
                }
            }
        }
        return c;
    }
    private ControladorCargaGuardadoDominio() {
        controladorProductos = null;
        controladorEstanteria = null;
        controladorSimilitudes = null;
        controladorUsuarios = null;
        // inicializar controlador de datos
        controladorDatos = null;

    }

    public void setControladores(ControladorProductos cp, ControladorEstanteria ce, ControladorSimilitudes cs,
                                 ControladorUsuarios cu, DataController cd) throws IOException {
        controladorProductos = cp;
        controladorEstanteria = ce;
        controladorSimilitudes = cs;
        controladorUsuarios = cu;
        controladorDatos = cd;
        // Cargar el mapa de usuarios en la capa de datos
        HashMap <String, Usuario> users = controladorDatos.inicializarMapaUsuarios();
        // Asignar el mapa cargado al gestor de usuarios de dominio
        controladorUsuarios.setGestorUsers(users);
    }

    /**
     * Función auxiliar que se encarga de lanzar excepciones y no permitirle acceso, en caso de que el Usuario no cumpla los
     * requisitos para acceder a un directorio
     * @param objetivo
     * @throws UsuarioActualNoEsGestor
     * @throws UsuarioObjetivoNoEsEmpleado
     */
    public void comprobarAccesoAdirectorioDeEmpleado(Usuario objetivo) throws UsuarioActualNoEsGestor, UsuarioObjetivoNoEsEmpleado {
        if (!controladorUsuarios.rolusuario().equals("GESTOR")) { // Comprueba rol del usuario actual
            throw new UsuarioActualNoEsGestor("El usuario actual, no es gestor e intenta acceder a directorios ajenos");
        }
        if (objetivo.getRol().toString().equals("GESTOR")) {
            throw new UsuarioObjetivoNoEsEmpleado("El usuario actual quiere acceder al directorio de un gestor");
        }
    }

    /**
     * Llama a controladorDatos para eliminar el usuario actual del registro persistente de usuarios
     * @throws ErrorEliminarArchivoOriginal si se produce un error al eliminar el archivo orignial
     * @throws UsuarioNoEncontrado si no se encuentra el usuario en el archivo de registro
     * @throws IOException si se produce un error de lectura del archivo
     * @throws NoExisteCarpeta si no existe la carpeta donde se guarda el registro de los usuarios
     * @throws PasswordIncorrecta
     */
    public void eliminarUsuarioActual(String password) throws ErrorEliminarArchivoOriginal, UsuarioNoEncontrado, IOException, NoExisteCarpeta, PasswordIncorrecta {
        Usuario actual = controladorUsuarios.pedirUsuario();
        boolean passwordCorrecta = controladorUsuarios.comprobar_user(password);
        if (!passwordCorrecta) {
            throw new PasswordIncorrecta("La contraseña introducida es incorrecta");
        }

        controladorDatos.borrarCarpetaUsuario(actual.getNombreUsuario());
        controladorDatos.eliminarUsuarioEnArchivo(actual);
    }

    /**
     * Llama a controladorDatos para guardar al usuario actual en el registro de usuarios
     * @throws YaExisteUsuario en caso de que ya exista un Usuario con el nombre
     * @throws IOException en caso de que se falle al acceder al fichero para guardar el nuevo Usuario dado de alta
     * @throws NoExisteRol en caso de que el rol introducido no exista
     */
    public void guardarUsuarioEnArchivo() throws YaExisteUsuario, IOException, NoExisteRol, YaExisteCarpeta {
        Usuario actual = controladorUsuarios.pedirUsuario();
        controladorDatos.guardarUsuarioEnArchivo(actual);
        controladorDatos.crearCarpetaUsuario(actual.getNombreUsuario());
    }

    /**
     * Llama a controladorDatos para guardar la contraseña del usuario actual en persistencia
     * @throws IOException en caso de que se falle al leer el archivo
     */
    public void guardarPasswordEnArchivo() throws IOException {
        Usuario actual = controladorUsuarios.pedirUsuario();
        controladorDatos.actualizarPasswordEnArchivo(actual.getNombreUsuario(), actual.getPassword());
    }

    /**
     *
     * @param nombre nombre del Producto
     * @throws YaExisteProducto en caso de que ya haya una instancia de Producto con el nombre
     * @throws NoExisteNingunProducto en caso de que no exista ningún producto, si no se crea correctamente la instancia de Producto, y no se pueda conseguir su id para crear su similitud
     * @throws NoExisteProducto en caso de que no exista el producto con el nombre, si no se crea correctamente la instancia de Producto, y no se pueda conseguir su id para crear su similitud
     */
    private void crearProducto(String nombre, String imagen) throws YaExisteProducto, NoExisteProducto, NoExisteNingunProducto {
        controladorProductos.crearProducto(nombre);
        Integer id = controladorProductos.getProducto(nombre).getId();
        if (imagen != null) {
            controladorProductos.setImagenProducto(nombre, imagen);
        }
        controladorSimilitudes.crearSimilitudesDeProducto(id);
    }

    private String[] parseProducto(String unparsedProducto) {
        String[] parsedProducto = unparsedProducto.split("----");
        return parsedProducto;
    }

    /**
     * Función auxiliar que llama a controladorDatos para añadir las instancias de Producto,
     * y tanto a ControladorEstanteria, como a ControladorSimilitud parar añadir las instancias de Similitud atribuidas
     * a los Productos (todo esto con los datos previamente cargados desde la capa de persistencia)
     * @param nombreArchivo
     * @param nombreUsuario nombre del usuario del cual cargar sus productos + similitudes guardadas
     * @throws IOException en caso de se falle al acceder a los ficheros de persistencia
     * @throws ParseException en caso de que se falle al tratar de parsear el json
     * @throws NumberFormatException en caso de que haya un error con los formatos de los números
     * @throws NoExisteProducto
     * @throws YaExisteProducto
     * @throws NoExisteNingunProducto
     * @throws InputIncorrecto
     */
    public void cargarProductosYsimilitudesDeArchivo(String nombreArchivo, String nombreUsuario) throws IOException, ParseException, NumberFormatException, NoExisteProducto, YaExisteProducto, NoExisteNingunProducto, InputIncorrecto {
        HashMap<Integer, ArrayList<String>> elementosEstanteria = controladorDatos.cargarConjuntoProductos(nombreArchivo, nombreUsuario);

        // Obtenemos un mapa en el cual la key más pequeña apunta al conjunto de Strings que són nombres de Productos
        // La segunda (y última) key, es el conjunto de parámetros necesarios para crear una similitud (id1, id2, valor)
        // estos están juntos en una sola string y los caracteres "-\-", actuan como separadores de cada valor

        Integer keyProductos, keySimilitudes;
        Integer[] array = elementosEstanteria.keySet().toArray(new Integer[0]);

        // La clave de productos es la primera del mapa, mientras que la de similitudes, es la segunda
        keyProductos = Math.min(array[0], array[1]);
        keySimilitudes = Math.max(array[0], array[1]);

        // Creamos todos los productos y los añadimos a la distribución de la estantería
        for (String nombreProducto : elementosEstanteria.get(keyProductos)) {
            String[] parsedProducto = parseProducto(nombreProducto);
            // Crear producto
            if (controladorProductos.existeProducto(parsedProducto[0])) continue;
            if(parsedProducto.length == 2) {
                crearProducto(parsedProducto[0], parsedProducto[1]);
            }
            else {
                crearProducto(parsedProducto[0], null);
            }
            controladorEstanteria.añadirProductoEstanteriaOrdenada(parsedProducto[0]);
        }
        // Cambiamos todas las similitudes, por las guardadas en el fichero
        for (String similitud : elementosEstanteria.get(keySimilitudes)) {
            String[] partesSimilitud = similitud.split("----");

            Integer IdProd1 = controladorProductos.getProducto(partesSimilitud[0]).getId();
            Integer IdProd2 = controladorProductos.getProducto(partesSimilitud[1]).getId();

            try {
                controladorEstanteria.cambiarSimilitud(IdProd1, IdProd2, Integer.parseInt(partesSimilitud[2]));
                controladorSimilitudes.cambiarSimilitud(IdProd1, IdProd2, Integer.parseInt(partesSimilitud[2]));
            }
            catch (NoExisteProducto | ValorSimilitudFueraRango | SimilitudMismoProducto e) {
                // ignorar evento -> similitud corrupta/inválida no se añade, se sigue con el resto
            }
        }
    }

    /**
     * Función auxiliar que lama a controladorDatos para sobreescribir las instancias de Producto,
     * y tanto a ControladorEstanteria, como a ControladorSimilitud parar sobreescribir las instancias de
     * Similitud atribuidas a los Productos, y a ControladorEstanteria para sobreescribir la distribución de la estanteria
     * @param nombreArchivo
     * @param nombreUsuario
     * @throws IOException
     * @throws ParseException
     * @throws NoExisteProducto
     * @throws YaExisteProducto
     * @throws NoExisteNingunProducto
     * @throws InputIncorrecto
     */
    public void cargarEstadoDeArchivo(String nombreArchivo, String nombreUsuario) throws IOException, ParseException, NoExisteProducto, YaExisteProducto, NoExisteNingunProducto {
        HashMap<Integer, ArrayList<String>> elementosEstanteria = controladorDatos.cargarSolucionEstanteria(nombreArchivo, nombreUsuario);

        // Obtenemos un mapa en el cual la key más pequeña apunta al conjunto de Strings que són nombres de Productos
        // La segunda (y última) key, es el conjunto de parámetros necesarios para crear una similitud (id1, id2, valor)
        // estos están juntos en una sola string y los caracteres "-\-", actuan como separadores de cada valor

        Integer keyProductos, keySimilitudes;
        Integer[] array = elementosEstanteria.keySet().toArray(new Integer[0]);

        // La clave de productos es la primera del mapa, mientras que la de similitudes, es la segunda
        keyProductos = Math.min(array[0], array[1]);
        keySimilitudes = Math.max(array[0], array[1]);

        // Reseteamos las instancias de Producto
        controladorProductos.borrarProductos(); // elimina entradas de los hashmaps, pero los conserva
       controladorSimilitudes.eliminarTodasSimilitudes();

        // Creamos todos los productos y los añadimos a la distribución de la estantería
        ArrayList<String> unparsedNombresProductos = elementosEstanteria.get(keyProductos);
        ArrayList<String> parsedProductos = new ArrayList<>();
        for (String nombreProducto : unparsedNombresProductos) {
            String[] parsedProducto = parseProducto(nombreProducto);
            parsedProductos.add(parsedProducto[0]);
            // Crear producto
            if(parsedProducto.length == 2) {
                crearProducto(parsedProducto[0], parsedProducto[1]);
            }
            else {
                crearProducto(parsedProducto[0], null);
            }
        }
        // Obtenemos los ids correspondientes a los nombres (respetando el orden de cargado)
        ArrayList<Integer> idsProductos = controladorProductos.passNamesToIds(parsedProductos);

        Integer similitudTotal = keyProductos; // la key de los productos es la similitud total
        controladorEstanteria.setEstanteriaOrdenada(idsProductos, similitudTotal);

        // Cambiamos todas las similitudes, por las guardadas en el fichero
        for (String similitud : elementosEstanteria.get(keySimilitudes)) {
            String[] partesSimilitud = similitud.split("----");

            Integer IdProd1 = controladorProductos.getProducto(partesSimilitud[0]).getId();
            Integer IdProd2 = controladorProductos.getProducto(partesSimilitud[1]).getId();

            try {
                controladorSimilitudes.cambiarSimilitud(IdProd1, IdProd2, Integer.parseInt(partesSimilitud[2]));
            }
            catch (NoExisteProducto | ValorSimilitudFueraRango | SimilitudMismoProducto e) {
                // ignorar evento -> similitud corrupta/inválida no se añade, se sigue con el resto
            }
        }
    }

    private String similitudToString(Similitud similitud) throws NoExisteProducto, NoExisteNingunProducto {
        Integer idProd1 = similitud.getIdGrande();
        Integer idProd2 = similitud.getIdPequena();
        String grado = String.valueOf(similitud.getSimilitud());

        String nombreProducto1 = controladorProductos.getProducto(idProd1).getNombre();
        String nombreProducto2 = controladorProductos.getProducto(idProd2).getNombre();
        String similitudToString = nombreProducto1 + "----" + nombreProducto2 + "----" + grado;
        return similitudToString;
    }

    private ArrayList<String> unparseProductos(ArrayList<String> nombresProductos) throws NoExisteProducto, NoExisteNingunProducto {
        ArrayList<String> unparsed = new ArrayList<>();
        for (String nombre : nombresProductos) {
            String imagen = controladorProductos.getProducto(nombre).getImagen();
            if (imagen != null) {
                unparsed.add(nombre + "----" + imagen);
            }
            else {
                unparsed.add(nombre);
            }
        }
        return unparsed;
    }

    /**
     * Llama al controladorDatos para guardar en persistencia el conjunto de instancias de Producto de la ejecución, y sus similitudes
     * @param nombreArchivo nombre del archivo
     * @param nombreUsuario nombre del usuario que quiere guardar los datos
     * @throws NoExisteNingunProducto se lanza cuando no existe ningun producto a cambiar
     * @throws IOException se lanza cuando ocurre un error de entrada o salida
     * @throws NoExisteProducto
     */
    public void guardarProductosYsimilitudesEnArchivo(String nombreArchivo, String nombreUsuario) throws NoExisteNingunProducto, IOException, NoExisteProducto {
        ArrayList<String> nombresProductos = controladorProductos.getNombresProductos();
        ArrayList<String> unparsedProductos = unparseProductos(nombresProductos);
        ArrayList<Similitud> similitudes = controladorSimilitudes.obtenerTodasSimilitudes();
        ArrayList<String> unparsedSimilitudes = new ArrayList<>();
        for (Similitud similitud : similitudes) {
            unparsedSimilitudes.add(similitudToString(similitud));
        }

        controladorDatos.guardarConjuntoProductos(unparsedProductos, unparsedSimilitudes, nombreArchivo, nombreUsuario);
    }

    /**
     * Llama al controladorDatos para guardar en persistencia el conjunto de instancias de Producto de la ejecución,
     * sus similitudes y su distribución de la estantería
     * @param nombreArchivo nombre del archivo
     * @param nombreUsuario nombre del usuario que quiere guardar los datos
     * @throws NoExisteNingunProducto se lanza cuando no existe ningun producto a cambiar
     * @throws IOException se lanza cuando ocurre un error de entrada o salida
     * @throws DiscordanciaEstructuras se lanza cuando se sabe que hay discordancia entre el orden de los productos y las similitudes
     * @throws NoExisteProducto
     */
    public void guardarEstadoEnArhivo(String nombreArchivo, String nombreUsuario) throws NoExisteNingunProducto, IOException, DiscordanciaEstructuras, NoExisteProducto {
        ArrayList<String> distribucionProductos = controladorEstanteria.getListaOrdenadaProductosPorNombre();
        ArrayList<String> unparsedProductos = unparseProductos(distribucionProductos);
        Integer gradoSimimlitud = controladorEstanteria.getSimilitudTotal();
        ArrayList<Similitud> similitudes = controladorSimilitudes.obtenerTodasSimilitudes();
        ArrayList<String> unparsedSimilitudes = new ArrayList<>();
        for (Similitud similitud : similitudes) {
            unparsedSimilitudes.add(similitudToString(similitud));
        }

        controladorDatos.guardarSolucionEstanteria(unparsedProductos, gradoSimimlitud, unparsedSimilitudes,
                nombreArchivo, nombreUsuario);
    }

    public ArrayList<String> getArchivosGuardadoProductos(String nombreUsuario) {
        return controladorDatos.getArchivosGuardadoProductos(nombreUsuario);
    }

    public ArrayList<String> getArchivosGuardadoEstado(String nombreUsuario) {
        return controladorDatos.getArchivosGuardadoEstado(nombreUsuario);
    }

    public boolean existeArchivoProductos(String nombreUsuario, String nombreArchivo) {
        return controladorDatos.existeArchivoProductos(nombreUsuario, nombreArchivo);
    }

    public boolean existeArchivoEstado(String nombreUsuario, String nombreArchivo) {
        return controladorDatos.existeArchivoEstado(nombreUsuario, nombreArchivo);
    }
}