
package Font.datacontrollers;

import Font.Exceptions.NoExisteCarpeta;
import Font.Exceptions.YaExisteCarpeta;

import java.io.File;
import java.util.ArrayList;

/**
 * Clase que controla las carpetas de los usuarios en la carpeta de Usuarios
 */
public class GestorDirectoriosUsuario
{
    /**
     * Se crea todas las carpetas necesarias dentro del proyecto para que el usuario tenga una en el sitio adecuado
     * @param usuario nombre del usuario a crear carpeta
     * @throws YaExisteCarpeta se lanza si ya existe la carpeta
     */
    public static void crearCarpetaUsuario (String usuario) throws YaExisteCarpeta
    {
        File carpetaUsuarioEstado = new File("Data/Usuarios/" + usuario + "/Estado/");
        if (!carpetaUsuarioEstado.mkdirs()) throw new YaExisteCarpeta("La carpeta del usuario " + usuario + " ya existe.");
        File carpetaUsuarioProductos = new File("Data/Usuarios/" + usuario + "/Productos/");
        if (!carpetaUsuarioProductos.mkdirs()) throw new YaExisteCarpeta("La carpeta del usuario " + usuario + " ya existe.");
    }

    /**
     * Se borra la carpeta dentro del proyecto
     * @param usuario nombre del usuario a borrar la carpeta
     * @throws NoExisteCarpeta se lanza si no existe la carpeta
     */
    public static void borrarCarpetaUsuario (String usuario) throws NoExisteCarpeta
    {
        File carpetaUsuario = new File("Data/Usuarios/" + usuario + "/");
        for (String path : emptyIfNullArray(carpetaUsuario.list()))
        {
            System.out.println("patata");
            borrarArchivosUsuarioRecursivo(carpetaUsuario.toString() + "/" + path);
        }
        if (!carpetaUsuario.delete()) throw new NoExisteCarpeta("La carpeta del usuario " + usuario + " no existe.");
    }

    private static void borrarArchivosUsuarioRecursivo (String path)
    {
        File auxFile = new File(path);
        System.out.println(auxFile.toString());
        if (auxFile.isDirectory()) {
            for (String subpath : emptyIfNullArray(auxFile.list())) {
                System.out.println("to");
                borrarArchivosUsuarioRecursivo(path + "/" + subpath);
            }}
        System.out.println("--");
        auxFile.delete();
        System.out.println(".");
    }

    private static String[] emptyIfNullArray( String[] array ) {
        String[] auxArray = {};
        return array == null ? auxArray : array;
    }

    /**
     * Comprueba si existe una carpeta de un usario
     * @param usuario nombre del usuario a comprovar si existe la carpeta
     * @return devuelve cierto si la carpeta existe
     */
    public static boolean existeCarpetaUsuario (String usuario)
    {
        File carpetaUsuario = new File("Data/Usuarios/" + usuario + "/");
        return carpetaUsuario.exists() && carpetaUsuario.isDirectory();
    }

    /**
     * Comprueba si existe un archivo en la carpeta de estados de un usario
     * @param usuario nombre del usuario a borrar la carpeta
     * @return devuelve cierto si la carpeta existe
     */
    public static boolean existeArchivoEstado (String usuario, String nombreArchivo)
    {
        File archivoEstado = new File("Data/Usuarios/" + usuario + "/Estado/" + nombreArchivo + ".json");
        return archivoEstado.exists() && archivoEstado.isFile();
    }

    /**
     * Comprueba si existe un archivo en la carpeta de productos de un usario
     * @param usuario nombre del usuario a borrar la carpeta
     * @return devuelve cierto si la carpeta existe
     */
    public static boolean existeArchivoProductos (String usuario, String nombreArchivo)
    {
        File archivoProductos = new File("Data/Usuarios/" + usuario + "/Productos/" + nombreArchivo + ".json");
        return archivoProductos.exists() && archivoProductos.isFile();
    }

    /**
     * Obtiene la lista de nombres de todas las carpetas
     * @return devuelve la lista de nombres de todas las carpetas
     */
    public static ArrayList<String> obtenerListaCarpetas ()
    {
        ArrayList<String> listaAux = new ArrayList<>();
        File path = new File("Data/Usuarios");

        // Se obtienen todos los paths de los archivos que son directorios del path pasado
        String[] listaNombresCarpetas = path.list((d, s) -> {
            File f = new File(d.getPath() + "/" + s);
            return f.isDirectory();
        });

        // Se obtiene el nombre del ultimo subdirectorio
        for (String s : emptyIfNullArray(listaNombresCarpetas))
        {
            listaAux.add(s.substring(s.lastIndexOf('/') + 1));
        }
        return listaAux;
    }

    public static ArrayList<String> obtenerListaFicherosConjunto (String nombreUsuario)
    {
        String path = "Data/Usuarios/" + nombreUsuario + "/Productos/";
        return obtenerListaFicheros(path);
    }

    public static ArrayList<String> obtenerListaFicherosEstado (String nombreUsuario)
    {
        String path = "Data/Usuarios/" + nombreUsuario + "/Estado/";
        return obtenerListaFicheros(path);
    }

    /**
     * Obtiene la lista de nombres de todas las carpetas
     * @return devuelve la lista de nombres de todas las carpetas
     */
    private static ArrayList<String> obtenerListaFicheros (String nombrePath)
    {
        ArrayList<String> listaAux = new ArrayList<>();
        File path = new File(nombrePath);

        // Se obtienen todos los paths de los archivos que son directorios del path pasado
        String[] listaNombresArchivos = path.list((d, s) -> {
            File f = new File(d.getPath() + "/" + s);
            return f.isFile();
        });

        // Se obtiene el nombre del ultimo subdirectorio
        for (String s : emptyIfNullArray(listaNombresArchivos))
        {
            listaAux.add(s.substring(s.lastIndexOf('/') + 1).split("\\.")[0]);
        }
        return listaAux;
    }
}
