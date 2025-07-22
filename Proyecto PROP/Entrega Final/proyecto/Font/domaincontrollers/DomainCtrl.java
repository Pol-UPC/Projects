/**
 * Autor: Alex Lafuente Gonzalez
 */

package Font.domaincontrollers;

import Font.Exceptions.*;
import Font.classes.ResultadoAlgoritmo;
import Font.classes.Similitud;
import Font.classes.Usuario;
import Font.datacontrollers.DataController;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Clase principal controladora de dominio, la cual se encarga de gestionar las llamadas de la capa de presentación,
 * y redirigir el trabajo al resto de controladores específicos
 */
public class DomainCtrl {
    /** Atributos **/

    /**
     * Instancia para aplicar el patrón singleton con double check synchronization
     */
    private static volatile DomainCtrl instance;

    /**
     * Instancia única del ControladroEstanteria, a la que llamará para usar GestorEstanteria
     */
    private ControladorEstanteria controladorEstanteria;
    /**
     * Instancia única del ControladorProductos, a la que llamará para usar GestorProductos
     */
    private ControladorProductos controladorProductos;
    /**
     * Instancia única del ControladorUsuarios, a la que llamará para usar GestorUsuarios
     */
    private ControladorUsuarios controladorUsuarios;
    /**
     * Instancia única del ControladorSimilitudes, a la que llamará para usar GestorSimilitudes
     */
    private ControladorSimilitudes controladorSimilitudes;
    /**
     * Instancia única del ControladorAlgorito, a la que llamará para usar GestorAlgoritmo
     */
    private ControladorAlgoritmo controladorAlgoritmo;

    /**
     * Instancia única del ControladorCargaGuardadoDominio, al que llamará para comunicarse con la capa de persistencia
     */
    private ControladorCargaGuardadoDominio controladorCargaGuardadoDominio;

    /**
     * Instancia única del DataController, el controlador de la capa de persistencia
     */
    private DataController controladorDatos;

    /** Constructora, la cual inicializa el resto de controladores
     * @throws IOException en caso de se falle al acceder a los ficheros de persistencia
     * **/
    private DomainCtrl() throws IOException {
        controladorEstanteria = ControladorEstanteria.getInstance();
        controladorUsuarios = ControladorUsuarios.getInstance();
        controladorProductos = ControladorProductos.getInstance();
        controladorSimilitudes = new ControladorSimilitudes(); // todo: aplicar singleton
        controladorAlgoritmo = new ControladorAlgoritmo(); // todo: aplicar singleton

        // Asignamos la instancia de conjunto de productos a la Estanteria
        controladorEstanteria.setProductos(controladorProductos.getListaProductos(), controladorProductos.getListaProductosPorId());
        controladorEstanteria.setGestorSimilitudes(controladorSimilitudes.getGestorSimilitudes());

        controladorDatos = controladorDatos.getInstance();

        controladorCargaGuardadoDominio = controladorCargaGuardadoDominio.getInstance();
        controladorCargaGuardadoDominio.setControladores(controladorProductos, controladorEstanteria, controladorSimilitudes, controladorUsuarios, controladorDatos);
    }

    /**
     * Funciones privadas auxiliares
     */

    /**
     * Función que se encarga de comprobar que los nombres de los Productos cumplan los siguientes criterios:
     * El input solo debe contener caracteres alfanuméricos, espacios, guiones bajos o guiones.
     * El input no puede contener la secuencia ----.
     * El input no puede comenzar con un espacio.
     * El input no puede terminar con un espacio.
     * @param input nombre del Producto
     * @throws InputIncorrecto en caso de que el nombre no cumpla algún criterio
     */
    private void checkInputProduct(String input) throws InputIncorrecto {
        if (!input.matches("^(?!\\s+)(?!.*-{4,})(?!.*\\s+$)[a-zA-Z0-9 _-]+$")){
            throw new InputIncorrecto("El nombre del producto solo debe contener caracteres alfanuméricos, espacios, guiones bajos " +
                    "o guiones.\nEl nombre no puede contener la secuencia ----.\nEl nombre no puede comenzar ni terminar con un espacio.");
        }
    }

    /**
     * Función que se encarga de comprobar que los nombres de los Usuarios cumplan los siguientes criterios:
     * El input debe tener entre 3 y 15 caracteres (ya que comienza y termina con un carácter alfanumérico, y la parte central puede tener entre 1 y 13 caracteres).
     * El primer y el último carácter deben ser alfanuméricos (letras o dígitos).
     * La parte central puede contener caracteres alfanuméricos, puntos (.), guiones bajos (_) y guiones (-), pero no puede comenzar ni terminar con un punto, un guión bajo o un guion.
     * @param input nombre del Usuario
     * @throws InputIncorrecto en caso de que el nombre no cumpla algún criterio
     */
    private void checkInputUserName(String input) throws InputIncorrecto {
        if (!input.matches("^[a-zA-Z0-9][a-zA-Z0-9._-]{1,13}[a-zA-Z0-9]$")) {
            throw new InputIncorrecto("El nombre de usuario introducido no cumple los criterios:\nEl nombre debe tener entre 3 y 15 caracteres.\nEl primer y el último carácter " +
                    "deben ser alfanuméricos (letras o dígitos).\nEl nombre no puede terminar con un espacio.");
        }
    }

    /**
     * Función que se encarga de comprobar que las contraseñas cumplan los siguientes criterios:
     * Al menos una letra minúscula (a-z)
     * Al menos una letra mayúscula (A-Z)
     * Al menos un dígito (0-9)
     * Al menos un carácter especial del conjunto @$!%*?&
     * La longitud total del input debe estar entre 8 y 20 caracteres
     * @param input la contraseña de un usuario
     * @throws InputIncorrecto en caso de que la contraseña no cumpla algún criterio
     */
    private void checkInputPassword(String input) throws InputIncorrecto {
        if (!input.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$")) {
            throw new InputIncorrecto("La contraseña introducida no cumple con los criterios establecidos:\nAl menos " +
                    "una letra minúscula (a-z).\nAl menos una letra mayúscula (A-Z).\nAl menos un dígito (0-9).\n" +
                    "Al menos un carácter especial del conjunto (@$!%*?&).\nLa longitud total de la contraseña debe " +
                    "estar entre 8 y 20 caracteres.");
        }
    }

    /**
     * Función que se encarga de comprobar que los nombres de los archivos de guardado tengan el formato correcto,
     * teniendo en cuenta que el fichero no debe incluir la extensión
     * @param input el nombre del archivo de guardado
     * @throws InputIncorrecto en caso de que el nombre del archivo no cumpla algún criterio
     */
    private void checkInputFileName(String input) throws InputIncorrecto {
        if (!input.matches("^[a-zA-Z0-9_-]+$")) {
            throw new InputIncorrecto("El formato del nombre del archivo es incorrecto.\n" +
                    "Asegurarse de que el fichero no incluye la extension");
        }
    }

    /** Funciones que se llaman desde el controlador de presentacion **/

    /**
     * Devuelve una instancia de DomainCtrl aplicando el patrón singleton con double check synchronization
     * @return instancia de la clase única
     * @throws IOException en caso de que se falle al acceder a los ficheros de persistencia
     */
    // Double check synchronization para el patrón singleton
    public static DomainCtrl getInstance() throws IOException {
        if (instance == null) { // First check (no locking)
            synchronized (DomainCtrl.class) {
                if (instance == null) { // Second check (with locking)
                    instance = new DomainCtrl();
                }
            }
        }
        return instance;
    }

    /**
     * Llama a ControladorProductos para crear una instancia de Producto con el nombre, y a ControladorSimilitudes
     * para crear su similitud
     * @param nombre nombre del Producto
     * @throws YaExisteProducto en caso de que ya haya una instancia de Producto con el nombre
     * @throws InputIncorrecto en caso de que el nombre itroducido no cumpla los criterios
     * @throws NoExisteNingunProducto en caso de que no exista ningún producto, si no se crea correctamente la instancia de Producto, y no se pueda conseguir su id para crear su similitud
     * @throws NoExisteProducto en caso de que no exista el producto con el nombre, si no se crea correctamente la instancia de Producto, y no se pueda conseguir su id para crear su similitud
     */
    public void crearProducto(String nombre) throws YaExisteProducto, NoExisteNingunProducto, NoExisteProducto, InputIncorrecto {
        checkInputProduct(nombre);

        controladorProductos.crearProducto(nombre);
        Integer id = controladorProductos.getProducto(nombre).getId();
        controladorSimilitudes.crearSimilitudesDeProducto(id);

    }

    /**
     * Llama a ControladorEstanteria para crear y añadir una nueva instancia de Producto
     * @param nombre nombre del Producto
     * @throws NoExisteProducto en caso de que no haya ninguna instancia de Producto con el nombre
     * @throws InputIncorrecto en caso de que el nombre no cumpla los criterios
     * @throws YaExisteProducto en caso de que el Producto con nombre ya esté en la estantería
     */
    public void añadirProductoEstanteria(String nombre) throws YaExisteProducto, NoExisteProducto, InputIncorrecto {
        checkInputProduct(nombre);

       // controladorSimilitudes.listarSimilitudes();
        controladorEstanteria.añadirProductoEstanteriaOrdenada(nombre);
    }

    /**
     * Llama a ControladorProductos para eliminar una instancia de Producto con el nombre, a ControladorEstanteria
     * para eliminarlo de la EstanteriaOrdenada y a ControladorSimilitudes para eliminar sus similitudes
     * @param nombre nombre del Producto
     * @throws NoExisteNingunProducto en caso de que no exista ninguna instancia de Producto
     * @throws NoExisteProducto en caso de que no exista ninguna instancia de Producto con el nombre
     * @throws InputIncorrecto en caso de que el nombre no cumpla los criterios
     */
    public void eliminarProducto(String nombre) throws NoExisteNingunProducto, NoExisteProducto, InputIncorrecto {
        checkInputProduct(nombre);

        controladorEstanteria.eliminarProducto(nombre);
        // eliminar de distribucion de Estanteria
        Integer id = controladorProductos.getProducto(nombre).getId();
        controladorSimilitudes.eliminarSimilitudesDeProducto(id);
        controladorProductos.eliminarProducto(nombre);
    }

    /**
     * Llama a ControladorProductos para cambiar el nombre de una instancia de Producto
     * @param nombre nombre actual del Producto
     * @param newNombre nombre nuevo del Producto
     * @throws MismoNombre en caso de que el nombre actual y nuevo coincidan
     * @throws InputIncorrecto en caso de que cualquiera de los dos nombres itroducidos no cumpla los criterios
     * @throws YaExisteProducto en caso de que ya haya una instancia de Producto con el nombre newNombre
     * @throws NoExisteNingunProducto en caso de que no haya ninguna instancia de Producto
     * @throws NoExisteProducto en caso de que no haya ninguna instancia de Producto con el nombre
     */
    public void setNombreProducto(String nombre, String newNombre) throws MismoNombre, InputIncorrecto, YaExisteProducto, NoExisteNingunProducto, NoExisteProducto {
        checkInputProduct(nombre);
        checkInputProduct(newNombre);

        controladorProductos.setNombreProducto(nombre, newNombre);
    }

    /**
     * Llama a controladorProductos para obtener un ArrayList que contiene los nombres de todas las instancias de Producto
     * @throws NoExisteNingunProducto en caso de que no haya ninguna instancia de Producto en el sistema
     * @return ArrayList que contiene los nombres de todas las instancias de Producto
     */
    public ArrayList<String> getProductos() throws NoExisteNingunProducto {
        // System.out.println("Todos los productos:");
        if (controladorProductos.getListaProductos().isEmpty()) {
            throw new NoExisteNingunProducto("No existe ningún producto");
        }

        ArrayList<String> nombres = controladorProductos.getNombresProductos();

        return nombres;
    }

    /**
     * Llama a controladorEstanteria para obtener una lista con las instancias de Producto ordenadas en la distribución de la EstanteriaOrdenada
     * @throws NoExisteNingunProducto en caso de que no exista ninguna instancia de Producto en la distribución de la EstanteriaOrdenada
     * @return lista con la distribución ordenada de instancias de Producto
     */
    public ArrayList<String> getDistribucionEstanteriaOrdenada() throws NoExisteNingunProducto {
        ArrayList<String> distribucionEstanteria = controladorEstanteria.getListaOrdenadaProductosPorNombre();

        return distribucionEstanteria;
    }

    /**
     * Llama a ControladorEstanteriaOrdenada para intercambiar la posición de dos productos en la distribución
     * @param n1 nombre del Producto 1
     * @param n2 nombre del Producto 2
     * @throws NoExisteProducto en caso de que no haya ninguna instancia de Producto con cualquiera de los dos nombres en la
     * EstanteriaOrdenada
     * @throws MismoNombre en caso de que el nombre de las dos instancias de Producto coincidan
     * @throws InputIncorrecto en caso de que cualquiera de los dos nombres no cumpla los criterios
     */
    public void intercambiarProductos(String n1, String n2) throws NoExisteProducto, MismoNombre, InputIncorrecto {
        checkInputProduct(n1);
        checkInputProduct(n2);

        controladorEstanteria.intercambiarProductos(n1, n2);
    }

    /**
     * Llama a ControladorProductos para obtener los Productos indicados por las IDs, y a ControladorEstanteria y ControladorSimilitudes
     * para cambiar la similitud entre las dos instancias de Productos
     * @param n1 nombre del primer Producto
     * @param n2 nombre del segundo Producto
     * @param value valor de la nueva Similitud
     * @throws NoExisteProducto en caso de que no exista en la estantaria cualquiera de los dos Productos
     * @throws NoExisteNingunProducto en caso de que no exista ninguna instancia de Producto con similitudes
     * @throws ValorSimilitudFueraRango en caso de que el valor de la similitud esté fuera del rango válido
     * @throws SimilitudMismoProducto en caso de que se intente cambiar la similitud de un mismo producto, puesto que un producto no es similar consigo mismo.
     * @throws InputIncorrecto en caso de que cualquiera de los dos nombres no cumpla los criterios
     */
    public void cambiarSimilitudes(String n1, String n2, Integer value) throws NoExisteProducto, NoExisteNingunProducto, ValorSimilitudFueraRango, SimilitudMismoProducto, InputIncorrecto {
        checkInputProduct(n1);
        checkInputProduct(n2);

        Integer id1 = controladorProductos.getProducto(n1).getId();
        Integer id2 = controladorProductos.getProducto(n2).getId();
        controladorEstanteria.cambiarSimilitud(id1, id2, value);
        controladorSimilitudes.cambiarSimilitud(id1, id2, value);
    }

    /**
     * Muestra por pantalla la similitud entre dos instancias de Producto
     * @param nombre1 id del Producto 1
     * @param nombre2 id del Producto 2
     * @throws NoExisteProducto en caso de que no exista ninguna instancia de Producto que tenga cualquiera de los dos nombres
     * @throws InputIncorrecto en caso de que cualquiera de los dos nombres no cumpla los criterios
     * @throws NoExisteNingunProducto en caso de que no exista ninguna instancia de Producto
     */
    public Integer getSimilitud(String nombre1, String nombre2) throws NoExisteProducto, NoExisteNingunProducto, InputIncorrecto {
        checkInputProduct(nombre1);
        checkInputProduct(nombre2);

        Integer id1 = controladorProductos.getProducto(nombre1).getId();
        Integer id2 = controladorProductos.getProducto(nombre2).getId();
        return controladorSimilitudes.obtenerSimilitud(id1, id2);
    }

    /**
     * Llama a controladorEstanteria para obtener la similitud total de los Productos en la distribución actual de la EstanteriaOrdenada
     * @return entero con el valor de la similitud total
     * @throws NoExisteNingunProducto en caso de que no exista ninguna instancia de Producto en la distribución de la EstanteriaOrdenada
     */
    public Integer getSimilitudTotal() throws NoExisteNingunProducto {
        if (getProductos().isEmpty()) {
            throw new NoExisteNingunProducto("No existe ningún producto");
        }
        return controladorEstanteria.getSimilitudTotal();
    }

    /**
     * Llama a controladorSimilitudes para obtener la estructura que contiene las similitudes de todas las instancias de Producto
     * @return hashmap que apunta a otro hashmap, donde están las similitudes de todos los Productos
     * @throws NoExisteNingunProducto en caso de que no exista ninguna instancia de Producto
     */
    public ArrayList<Similitud> getSimilitudes() throws NoExisteNingunProducto {
        return controladorSimilitudes.obtenerTodasSimilitudes();
    }

    /**
     * Llama a ControladorUsuarios para iniciar sesión con un Usuario
     * @param Nombre nombre del Usuario
     * @param Password contraseña del Usuario
     * @return Devuelve true si el inicio de sessión es exitoso. De cualquier otra manera sale con excepcion.
     * @throws UsuarioIncorrecto en caso de que el usuario pasado por parámetro no coincida con el usuario actual de la ejecución
     * @throws PasswordIncorrecta en caso de que la contraseña no coincida con la del usuario actual de la ejecución
     */
    public boolean iniciarSession(String Nombre, String Password) throws UsuarioIncorrecto, PasswordIncorrecta {
        // checkInputUserName(Nombre);
        // checkInputPassword(Password);

        return controladorUsuarios.iniciarSession(Nombre, Password);
    }

    /**
     * Llama a ControladorCargaDominio para cambiar la contraseña de un Usuario y guardarla en persistencia
     * @param newPassword nueva contraseña
     * @throws InputIncorrecto en caso de que la contraseña introducida no cumpla los criterios
     * @throws IOException en caso de que se falle al acceder al fichero para actualizar la contraseña
     */
    public void cambiarPassword(String oldPassword, String newPassword) throws InputIncorrecto, PasswordIncorrecta, IOException {
        checkInputPassword(oldPassword);
        checkInputPassword(newPassword);

        boolean passwordCorrecta = controladorUsuarios.comprobar_user(oldPassword);
        if (!passwordCorrecta) {
            throw new PasswordIncorrecta("La contraseña introducida es incorrecta");
        }

        // Cambiar password del usuario actual de la ejecución
        controladorUsuarios.cambiarPassword(newPassword);

        controladorCargaGuardadoDominio.guardarPasswordEnArchivo();
    }

    /**
     * Llama a ControladorUsuario para comprobar si la contraseña aportada coincide con la contraseña registrada del Usuario
     * @param oldPassword contraseña a comprobar
     * @return si la contraseña aportada coincide con la del Usuario
     * @throws InputIncorrecto en caso de que la contraseña itroducida no cumpla los criterios
     */
    public boolean comprobar_user(String oldPassword) throws InputIncorrecto {
        checkInputPassword(oldPassword);

        return controladorUsuarios.comprobar_user(oldPassword);
    }

    /**
     * Llama a ControladorUsuario para que elimine el Usuario que ha iniciado sesión y está en uso en la ejecución actual
     * , después lo actualiza en el archivo de persistencia de instancias de Usuario
     * @throws ErrorEliminarArchivoOriginal si se produce un error al eliminar el archivo orignial
     * @throws UsuarioNoEncontrado si no se encuentra el usuario en el archivo de registro
     * @throws IOException en caso de que se falle al acceder al fichero para eliminar el usuario actual de la ejecución
     * @throws NoExisteCarpeta en caso de que no exista la carpeta con el registro de usuarios
     * @throws PasswordIncorrecta
     */
    public void eliminarUsuarioActual(String password) throws ErrorEliminarArchivoOriginal, UsuarioNoEncontrado, IOException, NoExisteCarpeta, PasswordIncorrecta {
        // checkInputPassword(password);
        controladorCargaGuardadoDominio.eliminarUsuarioActual(password);
        controladorUsuarios.eliminarUsuarioActual();
    }

    /**
     * Llama a ControladorUsuario para registrar a un nuevo Usuario en el sistema
     * @param nombre nombre del Usuario con el que se quiere iniciar sesión
     * @param password contraseña del Usuario
     * @param rol rol del Usuario en el supermercado
     * @throws InputIncorrecto en caso de que el nombre itroducido no cumpla los criterios
     * @throws YaExisteUsuario en caso de que ya exista un Usuario con el nombre
     * @throws IOException en caso de que se falle al acceder al fichero para guardar el nuevo Usuario dado de alta
     * @throws NoExisteRol en caso de que el rol introducido no exista
     */
    public void altaUsuario(String nombre, String password, String rol) throws InputIncorrecto, YaExisteUsuario, IOException, NoExisteRol, YaExisteCarpeta {
        checkInputUserName(nombre);
        checkInputPassword(password);
        if (rol.isBlank()) {
            throw new InputIncorrecto("Se ha intentado crear un usuario con un rol vacío");
        }

        controladorUsuarios.altaUsuario(nombre, password, rol);
        controladorCargaGuardadoDominio.guardarUsuarioEnArchivo();
    }

    /**
     * Llama a ControladorUsuario para sugerir un nombre para un nuevo Usuario, si se ha intentado crear un Usuario con un nombre ya asignado
     * @param tryUsuario nombre del intento de creación de Usuario, el cual ya existe en el sistema
     * @return nombre de Usuario sugerido por el sistema
     */
    public String sugerir_usuario(String tryUsuario) {
        return controladorUsuarios.sugerir_usuario(tryUsuario);
    }

    /**
     * Llama a controladorUsuarios para obtener un ArrayList con los nombres de los usuarios registrados
     * @return Arraylist con los nombres de todos los usuarios registrados
     */
    public ArrayList<String> getNombresUsuarios() {
        HashMap<String, Usuario> users = controladorUsuarios.getGestorUsers();
        ArrayList <String> nombresUsuario = new ArrayList<>();
        for (String user : users.keySet()) {
            String nombre = users.get(user).getNombreUsuario();
            nombresUsuario.add(nombre);
        }
        return nombresUsuario;
    }

    /**
     * Llama a controladorUsuarios para obtener un ArrayList con los nombres de los usuarios registrados que son empleados
     * @return Arraylist con los nombres de todos los usuarios registrados que son empleados
     */
    public ArrayList<String> getNombresEmpleados() {
        HashMap<String, Usuario> users = controladorUsuarios.getGestorUsers();
        ArrayList <String> nombresUsuario = new ArrayList<>();
        for (String userName : users.keySet()) {
            Usuario usuario = users.get(userName);
            if (!usuario.getRol().toString().equals("GESTOR")) {
                nombresUsuario.add(usuario.getNombreUsuario());
            }
        }
        return nombresUsuario;
    }

    /**
     * Llama a controladorUsuarios, para obtener si el rol del Usuario actual de la ejecución, es Gestor
     * @return si el rol del Usuario actual de la ejecución es Gestor
     */
    public boolean usuarioActualEsGestor() {
        return controladorUsuarios.rolusuario().equals("GESTOR");
    }

    /**
     * Llama a ControladorEstanteria para obtener su lista de Productos y a ControladorSimilitudes para conseguir su lista de simimlitudes,
     * Luego llama a ControladorAlgoritmo pasándole los productos y similitudes, para que retorne la distribucion ordenada
     * Esta se coloca en la EstanteriaOrdenada llamando a ControladorEstanteria
     * @param tipoAlgoritmo entero que indica que algoritmo de ordenación utilizar (0 para fuerza bruta y 1 para Kruskal)
     * @throws SimilitudDeProductoNoExistente en caso de que una de las similitudes intente relacionar algún producto que no exista en la lista
     * @throws NoExisteNingunProducto en caso de que en la estanteria no haya ninguna instancia de Producto
     * @throws NoExisteAlgoritmo en caso de que el entero tipoAlgoritmo no corresponda a ningun algoritmo de ordenación ofrecido
     */
    public void ordenarProductos(int tipoAlgoritmo) throws SimilitudDeProductoNoExistente, NoExisteNingunProducto, NoExisteAlgoritmo {
        if (tipoAlgoritmo > 1 || tipoAlgoritmo < 0) {
            throw new NoExisteAlgoritmo("No existe el algoritmo seleccionado");
        }

        ArrayList<Integer> prods = controladorEstanteria.getListaOrdenada();
        ArrayList<Similitud> sims = controladorSimilitudes.obtenerTodasSimilitudes();
        ResultadoAlgoritmo resultadoAlgoritmo = controladorAlgoritmo.ordenarProductos(tipoAlgoritmo, prods, sims);
        controladorEstanteria.setEstanteriaOrdenada(resultadoAlgoritmo.getProductos(), resultadoAlgoritmo.getSumaTotal());
    }

    /**
     * Llama a controladorDatos para cargar las instancias de Producto, y las instancias de Similitud atribuidas a estos,
     * y las añade al conjunto de instancias del sistema
     * @param nombreArchivo nombre del archivo de donde cargar los productos y similitudes
     * @throws IOException en caso de se falle al acceder a los ficheros de persistencia
     * @throws ParseException en caso de que se falle al tratar de parsear el json
     * @throws NumberFormatException en caso de que haya un error con los formatos de los números
     * @throws NoExisteProducto
     * @throws InputIncorrecto
     * @throws YaExisteProducto
     * @throws NoExisteNingunProducto
     * @throws UsuarioActualNoEsGestor
     */
    public void cargarYañadirProductosPropios(String nombreArchivo) throws IOException, ParseException, NumberFormatException, NoExisteProducto, YaExisteProducto, NoExisteNingunProducto, InputIncorrecto, UsuarioActualNoEsGestor {
        checkInputFileName(nombreArchivo);
        String nombreUsuarioActual = controladorUsuarios.pedirUsuario().getNombreUsuario();
        if (!usuarioActualEsGestor()) {
            throw new UsuarioActualNoEsGestor("El usuario tratando de cargar y añadir productos, no es un gestor");
        }
        controladorCargaGuardadoDominio.cargarProductosYsimilitudesDeArchivo(nombreArchivo, nombreUsuarioActual);
    }

    /**
     * Comprueba si el usuario tiene permiso, y llama a ControladorDatos para cargar y añadir los Productos
     * (y sus similitudes), además de su distribución en la estantería que un empleado tiene guardados en un archivo
     * @param nombreArchivo nombre del archivo de guardado
     * @param usuarioObjetivo nombre del usuario que tiene el archivo
     * @throws UsuarioIncorrecto
     * @throws UsuarioActualNoEsGestor
     * @throws UsuarioObjetivoNoEsEmpleado
     * @throws NoExisteProducto
     * @throws InputIncorrecto
     * @throws YaExisteProducto
     * @throws NoExisteNingunProducto
     * @throws IOException en caso de se falle al acceder a los ficheros de persistencia
     * @throws ParseException en caso de que se falle al tratar de parsear el json
     */
    public void cargarYañadirProductosDeEmpleado(String nombreArchivo, String usuarioObjetivo) throws UsuarioIncorrecto, UsuarioActualNoEsGestor, UsuarioObjetivoNoEsEmpleado, NoExisteProducto, InputIncorrecto, YaExisteProducto, NoExisteNingunProducto, IOException, ParseException {
        checkInputFileName(nombreArchivo);
        Usuario objetivo = controladorUsuarios.getUser(usuarioObjetivo);

        // Si pasa de esta función sin lanzar ninguna excepción, significa que el usuario tiene permiso de acceso
        controladorCargaGuardadoDominio.comprobarAccesoAdirectorioDeEmpleado(objetivo);
        controladorCargaGuardadoDominio.cargarProductosYsimilitudesDeArchivo(nombreArchivo, objetivo.getNombreUsuario());
    }

    /**
     * Comprueba si el usuario tiene permiso, y llama a ControladorDatos para cargar y sobreescribir los Productos
     * (y sus similitudes), además de su distribución en la estantería que un empleado tiene guardados en un archivo
     * @param nombreArchivo nombre del archivo de guardado
     * @param usuarioObjetivo nombre del usuario que tiene el archivo
     * @param nombreArchivo
     * @param usuarioObjetivo
     * @throws UsuarioIncorrecto
     * @throws UsuarioActualNoEsGestor
     * @throws UsuarioObjetivoNoEsEmpleado
     * @throws NoExisteProducto
     * @throws InputIncorrecto
     * @throws YaExisteProducto
     * @throws NoExisteNingunProducto
     * @throws IOException
     * @throws ParseException
     */
    public void cargarEstadoDeEmpleado(String nombreArchivo, String usuarioObjetivo)  throws UsuarioIncorrecto, UsuarioActualNoEsGestor, UsuarioObjetivoNoEsEmpleado, NoExisteProducto, InputIncorrecto, YaExisteProducto, NoExisteNingunProducto, IOException, ParseException {
        checkInputFileName(nombreArchivo);
        Usuario objetivo = controladorUsuarios.getUser(usuarioObjetivo);

        // Si pasa de esta función sin lanzar ninguna excepción, significa que el usuario tiene permiso de acceso
        controladorCargaGuardadoDominio.comprobarAccesoAdirectorioDeEmpleado(objetivo);
        controladorCargaGuardadoDominio.cargarEstadoDeArchivo(nombreArchivo, objetivo.getNombreUsuario());
    }

    /**
     * Llama a ControladorDatos para cargar y sobreescribir los Productos
     * (y sus similitudes), además de su distribución en la estantería que el usuario actual tiene guardados en un archivo
     * @param nombreArchivo nombre del archivo de donde cargar los productos y similitudes
     * @throws IOException en caso de se falle al acceder a los ficheros de persistencia
     * @throws ParseException en caso de que se falle al tratar de parsear el json
     * @throws NumberFormatException en caso de que haya un error con los formatos de los números
     * @throws NoExisteProducto
     * @throws InputIncorrecto
     * @throws YaExisteProducto
     * @throws NoExisteNingunProducto
     */
    public void cargarEstadoPropio(String nombreArchivo) throws IOException, ParseException, NumberFormatException, NoExisteProducto, InputIncorrecto, YaExisteProducto, NoExisteNingunProducto {
        checkInputFileName(nombreArchivo);
        String nombreUsuarioActual = controladorUsuarios.pedirUsuario().getNombreUsuario();
        controladorCargaGuardadoDominio.cargarEstadoDeArchivo(nombreArchivo, nombreUsuarioActual);
    }

    /**

     * Llama al controladorCargaGuardadoDominio para guardar en persistencia un conjunto de Productos con sus similitudes
     * @param nombreArchivo nombre del archivo donde guardar los datos
     * @throws NoExisteNingunProducto se lanza cuando no existe ningun producto a cambiar
     * @throws IOException se lanza cuando ocurre un error de entrada o salida
     * @throws NoExisteProducto
     */
    public void guardarProductosPropios(String nombreArchivo) throws NoExisteNingunProducto, IOException, NoExisteProducto, InputIncorrecto {
        checkInputFileName(nombreArchivo);
        String nombreUsuarioActual = controladorUsuarios.pedirUsuario().getNombreUsuario();
        controladorCargaGuardadoDominio.guardarProductosYsimilitudesEnArchivo(nombreArchivo, nombreUsuarioActual);
    }

    public void guardarProductosDeEmpleado(String nombreArchivo, String usuarioObjetivo) throws UsuarioIncorrecto, UsuarioActualNoEsGestor, UsuarioObjetivoNoEsEmpleado, NoExisteProducto, YaExisteProducto, NoExisteNingunProducto, IOException, ParseException, InputIncorrecto {
        checkInputFileName(nombreArchivo);
        Usuario objetivo = controladorUsuarios.getUser(usuarioObjetivo);
        // Si pasa de esta función sin lanzar ninguna excepción, significa que el usuario tiene permiso de acceso
        controladorCargaGuardadoDominio.comprobarAccesoAdirectorioDeEmpleado(objetivo);
        controladorCargaGuardadoDominio.guardarProductosYsimilitudesEnArchivo(nombreArchivo, objetivo.getNombreUsuario());
    }

    /**
     *
     * Llama al controladorCargaGuardadoDominio para guardar en persistencia un conjunto de Productos con sus similitudes
     * y su distribución en la estantería
     * @param nombreArchivo nombreArchivo nombre del archivo donde guardar los datos
     * @throws NoExisteNingunProducto se lanza cuando no existe ningun producto a cambiar
     * @throws IOException se lanza cuando ocurre un error de entrada o salida
     * @throws DiscordanciaEstructuras se lanza cuando se sabe que hay discordancia entre el orden de los productos y las similitudes
     * @throws NoExisteProducto
     * @throws UsuarioActualNoEsGestor
     */
    public void guardarEstadoPropio(String nombreArchivo) throws NoExisteNingunProducto, IOException, DiscordanciaEstructuras, NoExisteProducto, InputIncorrecto, UsuarioActualNoEsGestor {
        checkInputFileName(nombreArchivo);
        if (!usuarioActualEsGestor()) {
            throw new UsuarioActualNoEsGestor("El usuario intentando guardar su estado no es un gestor");
        }
        String nombreUsuarioActual = controladorUsuarios.pedirUsuario().getNombreUsuario();
        controladorCargaGuardadoDominio.guardarEstadoEnArhivo(nombreArchivo, nombreUsuarioActual);
    }

    public void guardarEstadoAEmpleado(String nombreArchivo, String usuarioObjetivo) throws InputIncorrecto, UsuarioIncorrecto, UsuarioActualNoEsGestor, UsuarioObjetivoNoEsEmpleado, NoExisteProducto, NoExisteNingunProducto, IOException {
        checkInputFileName(nombreArchivo);
        Usuario objetivo = controladorUsuarios.getUser(usuarioObjetivo);
        // Si pasa de esta función sin lanzar ninguna excepción, significa que el usuario tiene permiso de acceso
        controladorCargaGuardadoDominio.comprobarAccesoAdirectorioDeEmpleado(objetivo);
        controladorCargaGuardadoDominio.guardarEstadoEnArhivo(nombreArchivo, objetivo.getNombreUsuario());
    }

    public String getNombreUsuarioActual() {
        return controladorUsuarios.pedirUsuario().getNombreUsuario();
    }

    /**
     * Llama al controladorProductos para resetear el conjunto de instancias de Producto y al controladorSimilitudes
     * para borrar las similitudes entre las instancias de Producto
     */
    public void resetearProductos() {
        controladorProductos.borrarProductos();
        controladorSimilitudes.eliminarTodasSimilitudes();
    }

    public ArrayList<String> getArchivosGuardadoProductos(String nombreUsuario) {
        return controladorCargaGuardadoDominio.getArchivosGuardadoProductos(nombreUsuario);
    }

    public ArrayList<String> getArchivosGuardadoEstado(String nombreUsuario) {
        return controladorCargaGuardadoDominio.getArchivosGuardadoEstado(nombreUsuario);
    }

    public boolean existeArchivoProductos(String nombreUsuario, String nombreArchivo) {
        return controladorCargaGuardadoDominio.existeArchivoProductos(nombreUsuario, nombreArchivo);
    }

    public boolean existeArchivoEstado(String nombreUsuario, String nombreArchivo) {
        return controladorCargaGuardadoDominio.existeArchivoEstado(nombreUsuario, nombreArchivo);
    }

    public String getImagenProducto(String nombre) {
        return controladorProductos.getImagenProducto(nombre);
    }

    public void setImagenProducto(String nombreProducto, String imagen) {
        controladorProductos.setImagenProducto(nombreProducto, imagen);
    }
}