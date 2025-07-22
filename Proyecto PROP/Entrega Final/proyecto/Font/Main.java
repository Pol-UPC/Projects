package Font;
import Font.Exceptions.*;
/* todo: dejar de pasar objetos de dominio a la capa de presentación
    todo: que dominio se encargue de llamar a una clase que transforme objectos en tuplas con primitivas/Strings
*/
import Font.classes.Similitud;
import Font.domaincontrollers.DomainCtrl;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Driver de todos los controladores de dominio
 */
public class Main {

    /**
     * Opción de los menús
     */
    private static Integer opcion;

    /**
     * Lector de la entrada
      */
    private static Scanner scanner;

    /**
     * Nombre de la instancia de Usuario usada en la ejecución (Mediante su creación o inicio de sesión)
     */
    private static String userName;

    /**
     * Instancia única del controlador de dominio
     */
    private static DomainCtrl controlDominio;

    /**
     * Muestra las opciones del menú de funcionalidades del dominio
     */
    private static void mostrarFuncionalidades() {
        System.out.println("Qué quiere hacer ahora?");
        System.out.println("1. Cambiar contraseña");
        System.out.println("2. Dar de baja mi usuario");
        System.out.println("3. Consultar funcionalidades");
        System.out.println("4. Crear producto y añadirlo a la distribución de la estantería");
        System.out.println("5. Listar productos y distribución de la estantería");
        System.out.println("6. Borrar productos");
        System.out.println("7. Cambiar nombre de producto");
        System.out.println("8. Listar similitudes");
        System.out.println("9. Ver similitud entre dos productos");
        System.out.println("10. Cambiar similitud entre dos productos");
        System.out.println("11. Intercambiar la posición de dos productos en la estantería");
        System.out.println("12. Obtener distribución de estanteria");
        System.out.println("13. Cargar y añadir productos");
        System.out.println("14. Cargar estado de la aplicación");
        System.out.println("15. Guardar productos (con sus similitudes)");
        System.out.println("16. Guardar supermercado");
        System.out.println("17. Salir");
    }

    /**
     * Comprueba si se ha introducido el input con el formato adecuado, que en este caso, es un entero
     * @return el input en caso de que exista un entero en la interfaz de entrada, -1 en caso contrario
     */
    private static Integer comprobarInputInt() {
        if(scanner.hasNextInt()) {
            Integer input = scanner.nextInt();
            escanearLinea();
            return input;
        }
        Integer input = -1;
        escanearLinea();
        return input;
    }

    /**
     * Escanea y devuelve la siguiente línea si la hay
     * @return la siguiente línea en caso de que exista en la interfaz de entrada, null en caso contrario
     */
    private static String escanearLinea() {
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        }
        return null;
    }

    /**
     *
     */
    private static void printEmpleados() {
        ArrayList<String> nombresEmpleados = controlDominio.getNombresEmpleados();
        if (nombresEmpleados.isEmpty()) {
            System.out.println("No hay ningún empleado");
            return;
        }

        for (String nombre : nombresEmpleados) {
            System.out.println(nombre);
        }
    }

    private static void printArchivosProductos(String nombreUsuario) {
        ArrayList<String> nombresArchivos = controlDominio.getArchivosGuardadoProductos(nombreUsuario);
        for (String nombreArchivo : nombresArchivos) {
            System.out.println(nombreArchivo);
        }
    }

    private static void printArchivosEstado(String nombreUsuario) {
        ArrayList<String> nombresArchivos = controlDominio.getArchivosGuardadoEstado(nombreUsuario);
        for (String nombreArchivo : nombresArchivos) {
            System.out.println(nombreArchivo);
        }
    }

    /**
     * Comprueba si existe ya un archivo de guardado de Productos de un usuario, y devuelve la decisión
     * @param nombreArchivo nombre del archivo de guardado de Productos
     * @param nombreUsuario nombre del Usuario de quien se quiere comprobar
     * @return si se va a proceder con el guardado del archivo en el subdirectorio del usuario y con el nombre nombreArchivo
     */
    private static boolean checkSobreEscrituraProductos(String nombreArchivo, String nombreUsuario) {
        if (controlDominio.existeArchivoProductos(nombreUsuario, nombreArchivo)) {
            String decision = null;
            do {
                System.out.println("Ya existe un archivo de guardado llamado " + nombreArchivo
                        + ". ¿Quiere sobreescribirlo?(s/n)");
                decision = escanearLinea();
            }
            while (!(decision.equals("s") || decision.equals("n")));

            if (decision.equals("n")) {
                return false;
            }
        }
        return true;
    }

    /**
     * Comprueba si existe ya un archivo de guardado de estado de un usuario, y devuelve la decisión
     * @param nombreArchivo nombre del archivo de guardado de estado
     * @param nombreUsuario nombre del Usuario de quien se quiere comprobar
     * @return si se va a proceder con el guardado del archivo en el subdirectorio del usuario y con el nombre nombreArchivo
     */
    private static boolean checkSobreEscrituraEstado(String nombreArchivo, String nombreUsuario) {
        if (controlDominio.existeArchivoEstado(nombreUsuario, nombreArchivo)) {
            String decision = null;
            do {
                System.out.println("Ya existe un archivo de guardado llamado " + nombreArchivo
                        + ". ¿Quiere sobreescribirlo?(s/n)");
                decision = escanearLinea();
            }
            while (!(decision.equals("s") || decision.equals("n")));

            if (decision.equals("n")) {
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args) {
        // Variables necesarias
        boolean iniciado = false, salir = false;
        opcion = -1;
        String name, password;
        scanner = new Scanner(System.in);

        //Inicializamos la capa de dominio
        controlDominio = null;
        try {
            controlDominio = DomainCtrl.getInstance();
        }
        catch (IOException e) {
            System.out.println("Error al inicializar el mapa de usuarios des del archivo");
            System.out.println("Este error puede ser causado por no ejecutar la clase en el path correcto (subgrup-prop43.5/Entrega 3)");
            salir = true;
        }

        while(!salir){
            while (!iniciado && !salir) { // iniciado sessión / Darse de alta
                //Evaluacion inicial
                System.out.println("Bienvenido: Que desea hacer?");
                System.out.println("1. Introducir credenciales");
                System.out.println("2. Darse de alta");
                System.out.println("3. Cerrar el programa");

                opcion = comprobarInputInt();
                /**
                 * Menú de inicio de sesión
                 */
                switch (opcion) {
                    case -1: // Input incorrecto en menú inicio
                        System.out.println("Input inválido (debe ser un entero)");
                        break;
                    case 1:
                        try {
                            System.out.println("Introduza el nombre del usuario:");
                            name = escanearLinea();
                            System.out.println("Introduzca la contraseña del usuario:");
                            password = escanearLinea();
                            iniciado = controlDominio.iniciarSession(name, password);
                            System.out.println("Se ha iniciado sesión con el usuario " + name); // todo: añadir tUsuario
                            userName = name;
                        }
                        catch ( UsuarioIncorrecto | PasswordIncorrecta e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    case 2:
                        System.out.println("Introducza su nombre:");
                        String nombre = escanearLinea();
                        System.out.println("Introduzca su contraseña:");
                        String key = escanearLinea();
                        System.out.println("Introduzca su rol ('Empleado' o 'Gestor')");
                        String rol = escanearLinea();
                        try {
                            controlDominio.altaUsuario(nombre, key, rol);
                            System.out.println("Se ha creado correctamente el usuario con rol de " + rol + ", llamado " + nombre);
                            userName = nombre;
                            iniciado = true;
                        }
                        catch (InputIncorrecto | NoExisteRol e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        catch (IOException e) {
                            System.out.println("Error al guardar el usuario " + nombre + " en el archivo");
                        }
                        catch (YaExisteUsuario e) {
                            System.out.println(e.getMessage());
                            String sugerencia = controlDominio.sugerir_usuario(nombre);
                            System.out.println("Le sugerimos el nombre: " + sugerencia);
                        }
                        break;
                    case 3:
                        salir = true;
                        break;
                    default:
                        System.out.println("Opcion no válida");
                }
                if (!salir) {
                    System.out.println();
                    System.out.println("Pulse intro para proceder");
                    escanearLinea();
                }
            }

            if(!salir){
                mostrarFuncionalidades();
                opcion = comprobarInputInt();

                /**
                 * Menú de funcionalidades
                 */
                switch(opcion) {
                    case -1: // Input incorrecto en menú funcionalidades
                        System.out.println("Input inválido (debe ser un entero)");
                        break;
                    case 1: // Cambiar contraseña
                        System.out.println("Ingrese la contraseña actual del usuario actual, llamado " + userName + ", para proceder: ");
                        String oldPassword = escanearLinea();
                        System.out.println("Ingrese su nueva contraseña: ");
                        String newPassword = escanearLinea();
                        try {
                            controlDominio.cambiarPassword(oldPassword, newPassword); // Método de cambio de contraseña
                            System.out.println("Contraseña cambiada del usuario actual, llamado " + userName + ", exitosamente.");
                        }
                        catch (InputIncorrecto | PasswordIncorrecta e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        catch (IOException e) {
                            System.out.println("Error al acceder al archivo para actualizar la contraseña del usuario en uso, llamado " + userName);
                        }
                        break;
                    case 2: // Dar de baja el usuario
                        System.out.println("Ingrese la contraseña actual del usuario actual, llamado " + userName + ", para proceder (en caso de éxito, se cerrará el programa): ");
                        password = escanearLinea();
                        try {
                            controlDominio.eliminarUsuarioActual(password); // Método de eliminación de usuario
                            System.out.println("Se ha eliminado el usuario en uso, " + userName);
                            salir = true;
                            System.out.println("Procederemos a sacarle del sistema.");
                        }
                        catch (ErrorEliminarArchivoOriginal | UsuarioNoEncontrado | IOException | PasswordIncorrecta e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    case 3: // Consultar funcionalidades disponibles
                        break;

                    case 4:
                        // Crear Producto y añadirlo a la Estanteria
                        System.out.println("Nombre del producto: ");
                        String nombre = escanearLinea();

                        // Crear el Producto
                        try {
                            controlDominio.crearProducto(nombre);
                            System.out.println("Se ha quedado correctamente el producto " + nombre);

                            // Añadir producto a la estanteria
                            try {
                                controlDominio.añadirProductoEstanteria(nombre);
                                System.out.println("Se ha añadido correctamente el producto " + nombre + " a la estantería");
                            }
                            catch (YaExisteProducto | NoExisteProducto | InputIncorrecto e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        }
                        catch (InputIncorrecto | YaExisteProducto e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        catch (NoExisteNingunProducto e) {
                            System.out.println("Error: Fallo en la creación del Producto. El sistema no detecta ninguna instancia");
                        }
                        catch (NoExisteProducto e) {
                            System.out.println("Fallo al crear producto con nombre " + nombre + " el sistema no detecta su instancia");
                        }
                        break;
                    case 5:
                        // Listar productos
                        try {
                            ArrayList<String> nombresProductos = controlDominio.getProductos();
                            System.out.println("Lista de Productos:");
                            for (String nombreProd : nombresProductos) {
                                System.out.println(nombreProd);
                            }

                            ArrayList<String> distribucionEstanteria = controlDominio.getDistribucionEstanteriaOrdenada();
                            System.out.println();
                            System.out.println("Distribución ordenada de la estantería:");
                            for (String nombreProducto : distribucionEstanteria) {
                                System.out.println(nombreProducto);
                            }
                            System.out.println();
                            System.out.println("Grado de Similitud Total: " + controlDominio.getSimilitudTotal());
                            System.out.println();

                        }
                        catch (NoExisteNingunProducto e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    case 6:
                        // Borrar Productos
                        System.out.println("¿Qué Producto borrar?");
                        nombre = escanearLinea();
                        try {
                            controlDominio.eliminarProducto(nombre);
                            System.out.println("Se ha borrado correctamente el producto " + nombre);
                        }
                        catch (NoExisteNingunProducto | NoExisteProducto | InputIncorrecto e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;

                    case 7:
                        // Cambiar nombre Producto
                        System.out.println("¿De qué producto cambiar nombre?");
                        nombre = escanearLinea();
                        System.out.println("Introduzca nuevo nombre: ");
                        String newNombre = escanearLinea();
                        try {
                            controlDominio.setNombreProducto(nombre, newNombre);
                            System.out.println("Se ha cambiado correctamente el nombre del producto " + nombre + " a " + newNombre);
                        }
                        catch (InputIncorrecto | MismoNombre | YaExisteProducto | NoExisteNingunProducto | NoExisteProducto  e ) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    case 8: // Listar similitudes
                        try {
                            /* todo: dejar de pasar objetos de dominio a la capa de presentación
                               todo: que dominio se encargue de llamar a una clase que transforme objectos en tuplas con primitivas/Strings
                             */
                            ArrayList<Similitud> listaSimilitudes = controlDominio.getSimilitudes();
                            System.out.println("Similitudes entre productos (listados en el orden de la estantería): ");

                            for (Similitud sim : listaSimilitudes) {
                                System.out.println(sim.getIdGrande() + ";" + sim.getIdPequena() + " = " + sim.getSimilitud());
                            }

                            System.out.println();
                            System.out.println("(Si la similitud entre dos productos es 0, no se lista)");
                        }
                        catch (NoExisteNingunProducto e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    case 9: // Ver similitud
                        System.out.println("Introduzca el nombre del primer producto: ");
                        nombre = escanearLinea();
                        System.out.println("Introduzca el nombre del segundo producto: ");
                        String nombre2 = escanearLinea();
                        try {
                            Integer similitud = controlDominio.getSimilitud(nombre, nombre2);
                            System.out.println("La similitud entre " + nombre + " y " + nombre2 + "es " + similitud);
                        }
                        catch (NoExisteProducto | NoExisteNingunProducto | InputIncorrecto e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    case 10: // cambiar similitudes
                        System.out.println("Introduzca el nombre del primer producto: ");
                        nombre = escanearLinea();
                        System.out.println("Introduzca el nombre del segundo producto: ");
                        nombre2 = escanearLinea();
                        System.out.println("Introduzca el valor de la nueva similitud (entero entre 0 y 100): ");
                        Integer value;

                        value = comprobarInputInt();
                        if (value == -1) {
                            System.out.println("Input inválido (debe ser un número entero entre 0 y 100)");
                            break;
                        }

                        try {
                            controlDominio.cambiarSimilitudes(nombre, nombre2, value);
                            System.out.println("Se ha cambiado correctamente la similitud entre " + nombre + " y " + nombre2 + " a "
                                    + value);
                        }
                        catch (NoExisteNingunProducto | NoExisteProducto | ValorSimilitudFueraRango | SimilitudMismoProducto | InputIncorrecto e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    case 11: // Intercambiar productos en Estanteria
                        System.out.println("Introduzca el nombre del primer producto: ");
                        nombre = escanearLinea();
                        System.out.println("Introduzca el nombre del segundo producto: ");
                        nombre2 = escanearLinea();
                        try {
                            controlDominio.intercambiarProductos(nombre, nombre2);
                            System.out.println("Se ha intercambiado correctamente la posición de " + nombre + " y " + nombre2 + "en la estantería");
                        }
                        catch (NoExisteProducto | MismoNombre | InputIncorrecto e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    case 12: // Ordenar estanteria (con algoritmo)
                        System.out.println("1. Algoritmo de fuerza bruta");
                        System.out.println("2. Algoritmo aproximado de Kruskal");
                        Integer tipoAlgoritmo;

                        tipoAlgoritmo = comprobarInputInt();
                        if (tipoAlgoritmo == -1) {
                            System.out.println("Input inválido, el formato debe de ser un entero");
                            break;
                        }

                        try {
                            controlDominio.ordenarProductos(tipoAlgoritmo - 1);
                            System.out.println("La estantería se ha ordenado correctamente");
                        }
                        catch (SimilitudDeProductoNoExistente | NoExisteNingunProducto | NoExisteAlgoritmo e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    case 13: // Cargar productos
                        System.out.println("1. Cargar fichero de guardado propio, del usuario " + userName);
                        System.out.println("2. Cargar fichero de guardado de un empleado");

                        Integer choose = null;

                        choose = comprobarInputInt();

                        if (choose == -1) {
                            System.out.println("Input inválido, el formato debe de ser un entero");
                            break;
                        }

                        String usuarioObjetivo = null;
                        String nombreArchivo = null;

                        if (choose == 1) {
                            try {
                                System.out.println("Introduzca el nombre del archivo a cargar (que aparezca en la siguiente lista): ");
                                printArchivosProductos(controlDominio.getNombreUsuarioActual());
                                nombreArchivo = escanearLinea();
                                controlDominio.cargarYañadirProductosPropios(nombreArchivo);
                                System.out.println("Se han cargado los productos correctamente desde el archivo " + nombreArchivo + ".json");
                            }
                            catch (IOException | InputIncorrecto | UsuarioActualNoEsGestor e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                            catch (ParseException | NoExisteProducto | YaExisteProducto | NumberFormatException | NoExisteNingunProducto e) {
                                throw new RuntimeException(e);
                            }
                        }

                        else if (choose == 2 && controlDominio.usuarioActualEsGestor()) {
                            printEmpleados();

                            System.out.println("Pulse intro para proceder");
                            System.out.println("¿De qué usuario cargar productos?");
                            usuarioObjetivo = escanearLinea();

                            System.out.println("Introduzca el nombre del archivo a cargar (que aparezca en la siguiente lista): ");
                            printArchivosProductos(usuarioObjetivo);
                            nombreArchivo = escanearLinea();
                            try {
                                controlDominio.cargarYañadirProductosDeEmpleado(nombreArchivo, usuarioObjetivo);
                                System.out.println("Se han cargado los productos correctamente desde el archivo " + nombreArchivo + ".json");
                            }
                            catch (IOException | UsuarioActualNoEsGestor |
                                   UsuarioObjetivoNoEsEmpleado | UsuarioIncorrecto | InputIncorrecto e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                            catch (NoExisteNingunProducto | NoExisteProducto | YaExisteProducto | ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else if (choose == 2) {
                            System.out.println("El usuario en uso, llamado " + userName + ", no es Gestor, por lo que no puede acceder a " +
                                    "los archivos del resto de empleados");
                        }
                        break;
                    case 14: // Cargar estado
                        System.out.println("1. Cargar fichero de guardado propio, del usuario " + userName);
                        System.out.println("2. Cargar fichero de guardado de un empleado");

                        choose = null;

                        choose = comprobarInputInt();
                        if (choose == -1) {
                            System.out.println("Input inválido, el formato debe de ser un entero");
                            break;
                        }

                        usuarioObjetivo = null;
                        nombreArchivo = null;

                        if (choose == 1) {
                            try {
                                System.out.println("Introduzca el nombre del archivo a cargar (que aparezca en la siguiente lista): ");
                                printArchivosEstado(controlDominio.getNombreUsuarioActual());
                                nombreArchivo = escanearLinea();
                                controlDominio.cargarEstadoPropio(nombreArchivo);
                                System.out.println("Se ha cargado el estado correctamente desde el archivo " + nombreArchivo + ".json");
                            }
                            catch (IOException | InputIncorrecto e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                            catch (ParseException | NumberFormatException | NoExisteProducto | YaExisteProducto | NoExisteNingunProducto e) {
                                throw new RuntimeException(e);
                            }
                        }

                        else if (choose == 2 && controlDominio.usuarioActualEsGestor()) {
                            printEmpleados();

                            System.out.println("Pulse intro para proceder");
                            System.out.println("¿De qué usuario cargar estado?");
                            usuarioObjetivo = escanearLinea();

                            System.out.println("Introduzca el nombre del archivo a cargar (que aparezca en la siguiente lista): ");
                            printArchivosEstado(usuarioObjetivo);
                            nombreArchivo = escanearLinea();
                            try {
                                controlDominio.cargarEstadoDeEmpleado(nombreArchivo, usuarioObjetivo);
                                System.out.println("Se ha cargado el estado correctamente desde el archivo " + nombreArchivo + ".json");
                            }
                            catch (IOException | UsuarioActualNoEsGestor |
                                   UsuarioObjetivoNoEsEmpleado | UsuarioIncorrecto | InputIncorrecto e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                            catch (NoExisteNingunProducto | NoExisteProducto | YaExisteProducto | ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else if (choose == 2) {
                            System.out.println("El usuario en uso, llamado " + userName + ", no es Gestor, por lo que no puede acceder a " +
                                    "los archivos del resto de empleados");
                        }
                        break;
                    case 15: // Guardar productos y similitudes
                        System.out.println("1. Guardar en fichero de guardado propio, del usuario " + userName);
                        System.out.println("2. Guardar en fichero de guardado de un empleado");

                        choose = null;

                        choose = comprobarInputInt();
                        if (choose == -1) {
                            System.out.println("Input inválido, el formato debe de ser un entero");
                            break;
                        }

                        usuarioObjetivo = null;
                        nombreArchivo = null;

                        if (choose == 1) {
                            try {
                                System.out.println("Introduzca el nombre del archivo a guardar: ");
                                nombreArchivo = escanearLinea();
                                boolean sobreescribir = checkSobreEscrituraProductos(nombreArchivo, controlDominio.getNombreUsuarioActual());
                                if (!sobreescribir) {
                                    break;
                                }

                                controlDominio.guardarProductosPropios(nombreArchivo);
                                System.out.println("Se han guardado correctamente los productos en el archivo " + nombreArchivo + ".json");
                            }
                            catch (NoExisteNingunProducto | IOException | InputIncorrecto | NoExisteProducto e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        }

                        else if (choose == 2 && controlDominio.usuarioActualEsGestor()) {
                            printEmpleados();

                            System.out.println("Pulse intro para proceder");
                            System.out.println("¿De qué usuario guardar estado?");
                            usuarioObjetivo = escanearLinea();

                            System.out.println("Introduzca el nombre del archivo a guardar: ");
                            nombreArchivo = escanearLinea();
                            boolean sobreescribir = checkSobreEscrituraProductos(nombreArchivo, usuarioObjetivo);
                            if (!sobreescribir) {
                                break;
                            }
                            try {
                                controlDominio.guardarProductosDeEmpleado(nombreArchivo, usuarioObjetivo);
                                System.out.println("Se han guardado correctamente los productos en el archivo " + nombreArchivo + ".json");
                            }
                            catch (IOException | UsuarioActualNoEsGestor |
                                   UsuarioObjetivoNoEsEmpleado | UsuarioIncorrecto | InputIncorrecto | NoExisteNingunProducto e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                            catch (NoExisteProducto | YaExisteProducto | ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else if (choose == 2) {
                            System.out.println("El usuario en uso, llamado " + userName + ", no es Gestor, por lo que no puede acceder a " +
                                    "los archivos del resto de empleados");
                        }
                        break;
                    case 16: // Guardar estado (productos + similitudes + distribución de la estantería)
                        System.out.println("1. Guardar en fichero de guardado propio, del usuario " + userName);
                        System.out.println("2. Guardar en fichero de guardado de un empleado");

                        choose = null;

                        choose = comprobarInputInt();
                        if (choose == -1) {
                            System.out.println("Input inválido, el formato debe de ser un entero");
                            break;
                        }

                        usuarioObjetivo = null;
                        nombreArchivo = null;

                        if (choose == 1) {
                            try {
                                System.out.println("Introduzca el nombre del archivo a guardar: ");
                                nombreArchivo = escanearLinea();
                                boolean sobreescribir = checkSobreEscrituraEstado(nombreArchivo, controlDominio.getNombreUsuarioActual());
                                if (!sobreescribir) {
                                    break;
                                }
                                controlDominio.guardarEstadoPropio(nombreArchivo);
                                System.out.println("Se ha guardado correctamente el estado en el archivo " + nombreArchivo + ".json");
                            }
                            catch (IOException | NoExisteNingunProducto | DiscordanciaEstructuras | InputIncorrecto | UsuarioActualNoEsGestor e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                            catch (NoExisteProducto e) {
                                throw new RuntimeException(e);
                            }

                        }

                        else if (choose == 2 && controlDominio.usuarioActualEsGestor()) {
                            printEmpleados();

                            System.out.println("Pulse intro para proceder");
                            System.out.println("¿De qué usuario guardar estado?");
                            usuarioObjetivo = escanearLinea();

                            System.out.println("Introduzca el nombre del archivo a guardar: ");
                            nombreArchivo = escanearLinea();
                            boolean sobreescribir = checkSobreEscrituraEstado(nombreArchivo, usuarioObjetivo);
                            if (!sobreescribir) {
                                break;
                            }
                            try {
                                controlDominio.guardarEstadoAEmpleado(nombreArchivo, usuarioObjetivo);
                                System.out.println("Se ha guardado correctamente el estado en el archivo " + nombreArchivo + ".json");
                            }
                            catch (IOException | UsuarioActualNoEsGestor |
                                   UsuarioObjetivoNoEsEmpleado | UsuarioIncorrecto | InputIncorrecto | NoExisteNingunProducto e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                            catch (NoExisteProducto e) {
                                throw new RuntimeException(e);
                            }


                        }
                        else if (choose == 2) {
                            System.out.println("El usuario en uso, llamado " + userName + ", no es Gestor, por lo que no puede acceder a " +
                                    "los archivos del resto de empleados");
                        }
                    break;
                    case 17: // Salir del programa
                        salir = true;
                        break;
                    default: //Otro número
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
                if (!salir) {
                    System.out.println();
                    System.out.println("Pulse intro para proceder");
                    escanearLinea();
                }
            }
        }
        System.out.print("Final de la ejecución.");
    }
}