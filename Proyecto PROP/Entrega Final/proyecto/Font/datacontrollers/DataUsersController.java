/**
 * Autor: Pol García Parra
 */
package Font.datacontrollers;
import Font.Exceptions.ErrorEliminarArchivoOriginal;
import Font.Exceptions.UsuarioNoEncontrado;
import Font.classes.Encriptacion;
import Font.classes.Usuario;
import Font.classes.Usuario.Tusuario;
import java.io.*;
import java.util.*;

public class DataUsersController {


    private static final DataUsersController lock = new DataUsersController();
    private static volatile DataUsersController instance;
    private static Encriptacion encriptacion;


    private DataUsersController() {
        encriptacion = new Encriptacion();
    }

    /**
     * Devuelve la instancia única de la clase `DataUsersController` utilizando el patrón Singleton.
     * Se utiliza un enfoque de sincronización de doble verificación para garantizar que solo se crea una instancia.
     *
     * @return La instancia única de `DataUsersController`.
     */
    public static DataUsersController getInstance() {
        DataUsersController c = instance;
        if (c == null) {
            synchronized (lock) {
                c = instance;
                if (c == null) {
                    c = new DataUsersController();
                    instance = c;
                }
            }
        }
        return c;
    }
    /**
     * Guarda un usuario en el archivo `imput_users.txt`. La contraseña del usuario se guarda en formato permutado.
     *
     * @param actual El usuario a guardar en el archivo.
     * @throws IOException Si ocurre un error al leer o escribir en el archivo.
     */
    public static void guardarUsuarioEnArchivo(Usuario actual)throws IOException{ 
            File archivo = new File("Data/imput_users.txt");

            try (
                RandomAccessFile raf = new RandomAccessFile(archivo, "rw"); 
                BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))
            ) {
                
                long length = raf.length();
                boolean needsNewLine = false;

                if (length > 0) {
                    raf.seek(length - 1);  // Posiciona el puntero en el último byte
                    int lastByte = raf.read();  // Lee el último byte
                    needsNewLine = !(lastByte == '\n'); // Comprueba si el archivo termina con salto de línea
                }

                if (needsNewLine) { // Si es necessario se añade un salto de linea
                    bw.newLine();
                }

                // Escribir el nuevo usuario
                bw.write(actual.getNombreUsuario() + " " + encriptacion.permutar_password(actual.getPassword()) + " " + actual.getRol());

            } 
        }
    /**
     * Inicializa y devuelve un mapa de usuarios a partir del archivo `imput_users.txt`.
     * Cada línea del archivo se interpreta como un usuario con nombre, contraseña y rol.
     * La contraseña se desencripta antes de ser asignada al usuario.
     *
     * @return Un `HashMap` con los usuarios leídos desde el archivo, donde la clave es el nombre de usuario.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    public HashMap <String, Usuario> inicializar_map()throws IOException{ //Inicializa el map en el gestor
        HashMap <String, Usuario> gestorUsers;
        gestorUsers = new HashMap<>();

         try (BufferedReader br = new BufferedReader(new FileReader("Data/imput_users.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(" "); // Suponiendo que el nombre y la contraseña están separados por un espacio
                 if (partes.length >= 3) { 
                    String nombre = partes[0];
                    String password = encriptacion.despermutar_password(partes[1]);
                    Tusuario rol = Tusuario.valueOf(partes[2].toUpperCase());
                    Usuario usuario = new Usuario(nombre, password,rol); 
                    gestorUsers.put(nombre, usuario); // Añadimos al HashMap
                }
            }
        } 
        return gestorUsers;
    }
    /**
     * Actualiza la contraseña de un usuario en el archivo `imput_users.txt`. La nueva contraseña
     * se guarda en formato permutado.
     *
     * @param nombreUsuario El nombre del usuario cuya contraseña debe ser actualizada.
     * @param nuevaPassword La nueva contraseña para el usuario.
     * @throws IOException Si ocurre un error al leer o escribir en el archivo.
     */
    public static void actualizarPasswordEnArchivo(String nombreUsuario, String nuevaPassword)throws IOException {

            String rutaArchivo = "Data/imput_users.txt"; 
            List<String> lineas = new ArrayList<>();
            Encriptacion encriptation = new Encriptacion();

            try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
                String linea;
            
                while ((linea = br.readLine()) != null) { // Leemos lineas
                    String[] partes = linea.split(" "); // Partimos la linea
                    
                    if (partes[0].equals(nombreUsuario)) { //Si es el usuario que quiero
                        partes[1] = encriptation.permutar_password(nuevaPassword); // Cambiar la contraseña
                        linea = String.join(" ", partes); 
                    }
                    lineas.add(linea); // Agregar la línea a la lista
                }
            } 
            // Escribir las líneas de nuevo en el archivo
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
                for (String l : lineas) {
                    bw.write(l);
                    bw.newLine();
                }
            } 
        }
    /**
     * Elimina un usuario del archivo `imput_users.txt`. Si el usuario no existe en el archivo,
     * se lanzará una excepción `ErrorEliminarArchivoOriginal`.
     *
     * @param actual El usuario a eliminar del archivo.
     * @throws IOException Si ocurre un error al leer o escribir en el archivo.
     * @throws ErrorEliminarArchivoOriginal Si no se encuentra el usuario en el archivo.
     * @throws UsuarioNoEncontrado Si hay un error al eliminar el archivo original después de la operación.
     */
    public static void  eliminarUsuarioEnArchivo(Usuario actual)throws IOException, ErrorEliminarArchivoOriginal ,UsuarioNoEncontrado{
        
        File inputFile = new File("Data/imput_users.txt");
        File tempFile = new File("Data/imput_users_temp.txt");

            try (
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            ) {
                String line;
                boolean found = false;
                
                // Leer línea por línea
                while ((line = reader.readLine()) != null) {
                    // Dividir la línea en partes
                    String[] partes = line.split(" ");
                    if (partes.length < 3) continue; //Ignoramos lineas erroneas
                    String Nombre = partes[0];
                   
                     if (Nombre.equals(actual.getNombreUsuario())) {
                        found = true; 
                        continue; // Pasamos a la siguiente iteracion
                    }

                    // Escribir la línea al archivo temporal
                    writer.write(line);
                    writer.newLine();
                }
                if (!found) {
                    throw new ErrorEliminarArchivoOriginal("Usuario no encontrado en el archivo.\n");
                }

            } 

            // Reemplazar el archivo original con el archivo temporal
            if (inputFile.delete()) {
                tempFile.renameTo(inputFile);
            } else {
                throw new UsuarioNoEncontrado("Error al eliminar el archivo original.\n");
            }
            actual = null;
    }




}
