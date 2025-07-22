package Font.datacontrollers;

import Font.Exceptions.CargaArchivoNoValido;
import Font.Exceptions.DiscordanciaEstructuras;
import Font.Exceptions.NoExisteNingunProducto;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Clase que guarda y carga toda la información en archivos formato .json
 */
public class GestorCargaGuardadoJSON
{
    /**
     * Atributo constante para añadir descripciones de errores.
     */
    private static String condicionesNombre = "El nombre de un producto solo debe contener caracteres alfanuméricos, espacios, guiones bajos o guiones.\n" +
        "El nombre no puede contener la secuencia ----.\n" +
        "El nombre no puede terminar con un espacio.";

    /**
     * Llama a una subfunción para guardar los productos con un orden predeterminado, un grado de similitud total relativo al orden, y el conjunto de similitudes de esos productos; en un archivo con un nombre concreto para un usuario en concreto
     * @param productos listado ordenado de nombres de un producto, con posiblemente el nombre de una imagen asociado
     * @param gradoSimilitud valor que indica el grado de similitud total del orden de los productos
     * @param similitudes listado de strings que contienen la información de una similitud, Cada similitud contiene dos nombres de producto y un valor de similitud, El listado puede estar vacia.
     * @param nombreArchivo nombre del archivo al cual se quiere acceder
     * @param usuario nombre del usuario al que se quiere guardar
     * @throws NoExisteNingunProducto se lanza cuando no existe ningun producto a cambiar
     * @throws IOException se lanza cuando ocurre un error de entrada o salida
     * @throws DiscordanciaEstructuras se lanza cuando se sabe que hay discordancia entre el orden de los productos y las similitudes
     */
    public static void guardarSolucionEstanteria(ArrayList<String> productos, Integer gradoSimilitud, ArrayList<String> similitudes, String nombreArchivo, String usuario) throws NoExisteNingunProducto, IOException, DiscordanciaEstructuras {
        if (productos.isEmpty()) throw new NoExisteNingunProducto("No se ha recibido ningun producto para guardarlo");
        if (gradoSimilitud != 0 && similitudes.isEmpty()) throw new DiscordanciaEstructuras("El grado de similitud total del conjunto no concuerda con la falta de el conjunto de similitudes a guardar.");

        String pathname = "Data/Usuarios/" + usuario + "/Estado/" + nombreArchivo + ".json";

        guardarElementosEstanteria(productos, gradoSimilitud, similitudes, pathname);
    }

    /**
     * Llama a una subfunción para guardar los productos y el conjunto de similitudes de esos productos en un archivo con un nombre concreto para un usuario en concreto
     * @param productos listado arbitrario de nombres de un producto, con posiblemente el nombre de una imagen asociado
     * @param similitudes listado de strings que contienen la información de una similitud, Cada similitud contiene dos nombres de producto y un valor de similitud, El listado puede estar vacia
     * @param nombreArchivo nombre del archivo al cual se quiere acceder
     * @param usuario nombre del usuario al que se quiere guardar
     * @throws NoExisteNingunProducto se lanza cuando no existe ningun producto a cambiar
     * @throws IOException se lanza cuando ocurre un error de entrada o salida
     */
    public static void guardarConjuntoProductos (ArrayList<String> productos, ArrayList<String> similitudes, String nombreArchivo, String usuario) throws NoExisteNingunProducto, IOException {
        if (productos.isEmpty()) throw new NoExisteNingunProducto("No se ha recibido ningun producto para guardarlo");

        String pathname = "Data/Usuarios/" + usuario + "/Productos/" + nombreArchivo + ".json";

        guardarElementosEstanteria(productos, -1, similitudes, pathname);
        
    }

    /**
     * Crea un objeto JSONobject con los datos a guardar, y se guardan en la carpeta del usuario con el nombre deseado
     * @param productos listado ordenado de nombres de un producto, con posiblemente el nombre de una imagen asociado
     * @param gradoSimilitud valor que indica el grado de similitud total del orden de los productos, Se usa el valor -1 si no se desea guardar
     * @param similitudes listado de strings que contienen la información de una similitud, Cada similitud contiene dos nombres de producto y un valor de similitud, El listado puede estar vacia.
     * @param pathname string con el path del sitio de donde cargar la informacion
     * @throws IOException se lanza cuando ocurre un error de entrada o salida
     */
    private static void guardarElementosEstanteria(ArrayList<String> productos, Integer gradoSimilitud, ArrayList<String> similitudes, String pathname) throws IOException {
        // Crear objeto JSON
        JSONObject solucionEstanteriaJson = new JSONObject();

        // Añadir los productos al objeto JSON
        JSONArray auxiliarArray = new JSONArray();
        for (String producto : productos)
        {
            JSONObject auxProducto = new JSONObject();
            String[] aux = producto.split("----");
            auxProducto.put("prod", aux[0]);
            if (aux.length == 2) auxProducto.put("imagen", aux[1]);
            auxiliarArray.add(auxProducto);
        }
        solucionEstanteriaJson.put("productos", auxiliarArray);

        // Añadir el grado de similitud total, si existe, al objeto JSON
        if (gradoSimilitud != -1) solucionEstanteriaJson.put("gradoSimilitudTotal", gradoSimilitud);

        // Añadir todas las similitudes al objeto JSON
        auxiliarArray = new JSONArray();
        if (!similitudes.isEmpty()) {
            auxiliarArray.addAll(similitudes);
            solucionEstanteriaJson.put("similitudes", auxiliarArray);
        }

        // Crear el archivo, guardarlo en su path y escribir en el
        File f = new File(pathname);
        FileWriter fw = new FileWriter(f);
        fw.write(solucionEstanteriaJson.toJSONString());
        fw.close();
    }

    /**
     * Parsea un archivo .json para poder cargar toda la información de un estado
     * @param nombreArchivo nombre del archivo al cual se quiere acceder
     * @param usuario nombre del usuario al que se quiere guardar
     * @return un hashmap con los productos en una disposición concreta, el valor de similitud que contienen, si importa
     * @throws IOException se lanza cuando ocurre un error de entrada o salida
     * @throws ParseException se lanza cuando ocurre un error al parsear el producto
     * @throws NumberFormatException se lanza cuando se trata de obtener un valor entero de una string
     * @throws CargaArchivoNoValido se lanza cuando hay un error en la lectura de cualquier parte
     */
    public static HashMap <Integer, ArrayList<String>> cargarSolucionEstanteria(String nombreArchivo, String usuario) throws IOException, ParseException, NumberFormatException, CargaArchivoNoValido
    {
        String pathname = "Data/Usuarios/" + usuario + "/Estado/" + nombreArchivo + ".json";
        return cargarElementosEstanteria(pathname, 1);
    }

    /**
     * Parsea un archivo .json para poder cargar toda la información de un conjunto de productos
     * @param nombreArchivo nombre del archivo al cual se quiere acceder
     * @param usuario nombre del usuario al que se quiere guardar
     * @return un hashmap con los productos en una disposición concreta, el valor de similitud que contienen, si importa
     * @throws IOException se lanza cuando ocurre un error de entrada o salida
     * @throws ParseException se lanza cuando ocurre un error al parsear el producto
     * @throws NumberFormatException se lanza cuando se trata de obtener un valor entero de una string
     * @throws CargaArchivoNoValido se lanza cuando hay un error en la lectura de cualquier parte
     */
    public static HashMap <Integer, ArrayList<String>> cargarConjuntoProductos(String nombreArchivo, String usuario) throws IOException, ParseException, NumberFormatException, CargaArchivoNoValido
    {
        String pathname = "Data/Usuarios/" + usuario + "/Productos/" + nombreArchivo + ".json";
        return cargarElementosEstanteria(pathname, 2);
    }

        /**
         * Parsea un archivo .json para poder cargar toda su información
         * @param pathname string con el path del sitio de donde cargar la informacion
         * @param tipo el tipo de archivo que se busca cargar
         * @return un hashmap con los productos en una disposición concreta, el valor del grado total de similitud que contienen y las similitudes si contiene
         * @throws IOException se lanza cuando ocurre un error de entrada o salida
         * @throws ParseException se lanza cuando ocurre un error al parsear el producto
         * @throws NumberFormatException se lanza cuando se trata de obtener un valor entero de una string
         * @throws CargaArchivoNoValido se lanza cuando hay un error en la lectura de cualquier parte
         */
    private static HashMap <Integer, ArrayList<String>> cargarElementosEstanteria(String pathname, int tipo) throws IOException, ParseException, NumberFormatException, CargaArchivoNoValido {

        // Obtener el archivo y coger su lector
        File f = new File(pathname);
        FileReader fr = new FileReader(f);

        // Crear el parser de JSON y obtener el objeto leido
        JSONParser jsonParser = new JSONParser();
        JSONObject solucionEstanteriaOrdenada = (JSONObject) jsonParser.parse(fr);

        // Obtener el grado de similitud total y lanzar una excepción si se detecta que no es acorde al formato
        Object auxiliarObject = solucionEstanteriaOrdenada.get("gradoSimilitudTotal");
        if (auxiliarObject == null && tipo == 1) throw new CargaArchivoNoValido("El archivo no se puede leer por su tipo.");
        else if (auxiliarObject != null && tipo == 2) throw new CargaArchivoNoValido("El archivo no se puede leer por su tipo.");
        Integer gradoSimilitud = -1;
        if (tipo == 1)
        {
            try {
                gradoSimilitud = Integer.valueOf(auxiliarObject.toString());
                if (gradoSimilitud < 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                throw new CargaArchivoNoValido("El archivo no se puede leer por el grado de similitud total.");
            }
        }

        // Obtener el conjunto de productos a crear
        auxiliarObject = solucionEstanteriaOrdenada.get("productos");
        if (auxiliarObject == null) throw new CargaArchivoNoValido("No existe ningun tipo de producto en el archivo.");
        JSONArray auxiliarArray = (JSONArray) auxiliarObject;
        ArrayList<String> productos = new ArrayList<String>();
        for (Object aux : auxiliarArray)
        {
            // Para cada producto comprobamos la valideza de su nombre
            JSONObject auxiliarJSONObject = (JSONObject) aux;
            String nombreProducto = auxiliarJSONObject.get("prod").toString();

            if (nombreProductoIncorrecto(nombreProducto)){
                throw new CargaArchivoNoValido("El archivo no se puede leer por un producto.\n" + condicionesNombre);
            }

            // Para cada producto comprobamos si tiene imagen, y si esta es valida
            Object imagenAux = auxiliarJSONObject.get("imagen");
            if (imagenAux != null)
            {
                String imagen = imagenAux.toString();
                if (imagen.split("/").length > 1) {
                    throw new CargaArchivoNoValido("El archivo no se puede leer por una imagen de producto.");
                }
                nombreProducto = nombreProducto + "----" + imagen;
            }

            productos.add(nombreProducto);
        }

        // Obtener el conjunto de similitudes a crear
        auxiliarArray = (JSONArray) solucionEstanteriaOrdenada.get("similitudes");
        ArrayList<String> similitudes = new ArrayList<String>();
        if (auxiliarArray != null) for (Object aux : auxiliarArray)
        {
            String similitud = aux.toString();
            String[] partesSimilitud = similitud.split("----");
            if ( partesSimilitud.length != 3 || nombreProductoIncorrecto(partesSimilitud[0]) || nombreProductoIncorrecto(partesSimilitud[1]))
                throw new CargaArchivoNoValido("El archivo no se puede leer por una similitud.\n" + condicionesNombre);
            try {
                int g = Integer.parseInt(partesSimilitud[2]);
                if (g < 0 || g > 100) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                throw new CargaArchivoNoValido("El archivo no se puede leer por una similitud.");
            }
            similitudes.add(similitud);
        }

        // Almacenar todos los datos a pasar en una estructura que permite distintos tipos
        HashMap <Integer, ArrayList<String>> tupla = new HashMap<Integer, ArrayList<String>>();
        tupla.put(gradoSimilitud, productos);
        tupla.put(gradoSimilitud+1, similitudes);

        return tupla;
    }

    /**
     * Comprueba que el nombre de un producto sea valido
     * @param nombreProducto nombre a observar la valideza
     * @return devuelve cierto si el nombre es erroneo.
     */
    private static boolean nombreProductoIncorrecto(String nombreProducto)
    {
        return (!nombreProducto.matches("^(?!\\s+)(?!.*-{4,})(?!.*\\s+$)[a-zA-Z0-9 _-]+$"));
    }
}
