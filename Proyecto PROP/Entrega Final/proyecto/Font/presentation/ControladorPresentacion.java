package Font.presentation;
import Font.Exceptions.*;
import Font.domaincontrollers.DomainCtrl;
import Font.forms.CambiarProdWindow;
import Font.forms.CambiarSimWindow;
import org.json.simple.parser.ParseException;


import java.io.IOException;
import javax.swing.*;
import java.util.ArrayList;

/**
 * Clase principal controladora de presentación, se encarga de generar e interactuar con las ventanas gráficas
 * y enviar peticiones a la capa de dominio.
 */
public class ControladorPresentacion {
    private DomainCtrl ctrlDominio;
    private VentanaPrincipal ventana;
    private JFrame VentanaSecundaria;

    public ControladorPresentacion() {
        try {
            ctrlDominio = DomainCtrl.getInstance();
        } catch (Exception e) {
            System.out.println("Error al crear dominio");
        }
        ventana = new VentanaPrincipal(this);
    }

    public void inicializar() {
        ventana.inicializar();
    }

    /**
     * Llamada a dominio para iniciar usuario, se usa cuando se está en la pantalla de login.
     * @param usuario El nombre del usuario.
     * @param contra La contraseña de este.
     * @throws UsuarioIncorrecto en caso de que el usuario pasado por parámetro no coincida con el usuario actual de la ejecución
     * @throws PasswordIncorrecta en caso de que la contraseña no coincida con la del usuario actual de la ejecución
     */
    public void dominioIniciarSesion (String usuario, String contra) throws UsuarioIncorrecto, PasswordIncorrecta {
        ctrlDominio.iniciarSession(usuario, contra);
    }

    public String dominioGetRol (String usuario) {
        if (ctrlDominio.usuarioActualEsGestor()){
            return  "GESTOR";
        }
        else return "EMPLEADO";
    }

    /**
     * Llamada a dominio para registrar usuario, se usa cuando se está en la pantalla de register.
     * @param nombre El nombre del usuario.
     * @param password La contraseña de este.
     * @param rol El rol que se le quiere asignar.
     * @throws InputIncorrecto en caso de que el nombre itroducido no cumpla los criterios
     * @throws YaExisteUsuario en caso de que ya exista un Usuario con el nombre
     * @throws IOException en caso de que se falle al acceder al fichero para guardar el nuevo Usuario dado de alta
     */
    public void dominioRegistrarUsuario(String nombre, String password, String rol)throws YaExisteUsuario, IOException ,InputIncorrecto{
        ctrlDominio.altaUsuario(nombre, password, rol);
    }

    /**
     * Llamada a dominio para sugerir usuario.
     * @param usuario El nombre del usuario que se había intentado usar
     * @return El nombre de usuario similar sugerido.
     */
    public String dominioSugerirUsuario(String usuario)  {
        return ctrlDominio.sugerir_usuario(usuario);
    }

    /**
     * Llamada a dominio para crear un producto.
     * @param nombre El nombre del producto que se va a crear.
     * @throws YaExisteProducto en caso de que ya haya una instancia de Producto con el nombre
     * @throws InputIncorrecto en caso de que el nombre itroducido no cumpla los criterios
     * @throws NoExisteNingunProducto en caso de que no exista ningún producto, si no se crea correctamente la instancia de Producto, y no se pueda conseguir su id para crear su similitud
     * @throws NoExisteProducto en caso de que no exista el producto con el nombre, si no se crea correctamente la instancia de Producto, y no se pueda conseguir su id para crear su similitud
     */
    public void dominioCrearProducto(String nombre) throws YaExisteProducto, InputIncorrecto, NoExisteNingunProducto, NoExisteProducto {
        ctrlDominio.crearProducto(nombre);
        ctrlDominio.añadirProductoEstanteria(nombre);
    }

    public void dominioBorrarProducto(String nombre) throws NoExisteProducto, InputIncorrecto, NoExisteNingunProducto {
        cerrarVentanaSecundaria();
        ctrlDominio.eliminarProducto(nombre);
    }

    /**
     * Llamada a dominio para encontrar una similitud, se usa cuando se necesita la información para
     * mostrar en una ventana.
     * @param p1 El nombre del primer producto
     * @param p2 El nombre del segundo producto
     * @return La similitud entre ambos productos
     * @throws NoExisteProducto en caso de que no exista ninguna instancia de Producto que tenga cualquiera de los dos nombres
     * @throws InputIncorrecto en caso de que cualquiera de los dos nombres no cumpla los criterios
     * @throws NoExisteNingunProducto en caso de que no exista ninguna instancia de Producto
     */
    public Integer dominioGetSimilitud(String p1, String p2) throws NoExisteProducto, InputIncorrecto, NoExisteNingunProducto {
        return ctrlDominio.getSimilitud(p1,p2);
    }

    /**
     * Llamada a dominio para cambiar una similitud.
     * @param p1 Nombre del primer producto
     * @param p2 Nombre del segundo producto
     * @param s La nueva similitud deseada entre ambos productos
     */
    public void dominioCambiarSimilitud(String p1, String p2, Integer s) {
        try {
            ctrlDominio.cambiarSimilitudes(p1,p2,s);
        } catch (Exception e) {

        }
    }

    /**
     * Función simple que llama actualizar visuales de la ventana principal.
     */
    public void actualizarVisuales() {
        ventana.actualizarVisuales();
    }

    /**
     * Llamada a dominio para ver los nombres de todos los productos.
     * @return Una lista con todos los nombres.
     * @throws NoExisteNingunProducto en caso de que no exista ninguna instancia de Producto en la distribución de la EstanteriaOrdenada
     */
    public ArrayList<String> dominioGetProductos() throws NoExisteNingunProducto{
        return ctrlDominio.getDistribucionEstanteriaOrdenada();
    }

    /**
     * Llamada a dominio para cambiar el nombre de un producto.
     * @param producto Nombre actual del producto.
     * @param nombreNuevo Nombre nuevo al que se quiere cambiar el producto.
     * @throws MismoNombre en caso de que el nombre actual y nuevo coincidan
     * @throws InputIncorrecto en caso de que cualquiera de los dos nombres itroducidos no cumpla los criterios
     * @throws YaExisteProducto en caso de que ya haya una instancia de Producto con el nombre newNombre
     * @throws NoExisteNingunProducto en caso de que no haya ninguna instancia de Producto
     * @throws NoExisteProducto en caso de que no haya ninguna instancia de Producto con el nombre
     */
    public void dominioCambiarNombreProducto(String producto, String nombreNuevo) throws NoExisteProducto, InputIncorrecto, YaExisteProducto, MismoNombre, NoExisteNingunProducto {
        ctrlDominio.setNombreProducto(producto,nombreNuevo);
    }

    /**
     * Llamada a dominio para intercambiar la posición de dos productos.
     * @param p1 Nombre del primer producto.
     * @param p2 Nombre del segundo producto.
     * @throws NoExisteProducto en caso de que no haya ninguna instancia de Producto con cualquiera de los dos nombres en la
     * EstanteriaOrdenada
     * @throws MismoNombre en caso de que el nombre de las dos instancias de Producto coincidan
     * @throws InputIncorrecto en caso de que cualquiera de los dos nombres no cumpla los criterios
     */
    public void dominioIntercambiarProductos(String p1, String p2) throws NoExisteProducto, InputIncorrecto, MismoNombre {
        ctrlDominio.intercambiarProductos(p1,p2);
    }

    /**
     * Llamada a dominio para ordenar la estantería segun un algoritmo concreto.
     * @param tipoAlgoritmo El tipo del algoritmo que se usará.
     * @return La similitud total resultante de la nueva ordenación.
     * @throws NoExisteNingunProducto en caso de que en la estanteria no haya ninguna instancia de Producto
     * @throws NoExisteAlgoritmo en caso de que el entero tipoAlgoritmo no corresponda a ningun algoritmo de ordenación ofrecido
    */
    public int dominioOrdenarEstanteria(int tipoAlgoritmo) throws NoExisteNingunProducto, NoExisteAlgoritmo {
        ctrlDominio.ordenarProductos(tipoAlgoritmo);
        return ctrlDominio.getSimilitudTotal();
    }

    /**
     * Función que abre una ventana secundaria de un tipo particular, o pondrá en focus
     * la ventana secundaria abierta si existe una ya.
     * @param info La información que recibirá la ventana secundaria.
     * @param tipo El tipo de ventana secundaria (el content pane) que se quiere abrir.
     */
    public void abrirVentanaSecundaria(ArrayList<String> info, int tipo) {
        if (VentanaSecundaria == null || !VentanaSecundaria.isDisplayable()) {
            VentanaSecundaria = new JFrame();
            if (tipo == 0) {
                VentanaSecundaria.setContentPane(new CambiarProdWindow(info,this).getPanel());
                VentanaSecundaria.setTitle("Editar Producto");
            } else {
                VentanaSecundaria.setContentPane(new CambiarSimWindow(info,this).getPanel());
                VentanaSecundaria.setTitle("Editar Similitud");
            }

            VentanaSecundaria.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            VentanaSecundaria.pack();
            VentanaSecundaria.setVisible(true);
            VentanaSecundaria.setSize(250, 200);
            VentanaSecundaria.setLocationRelativeTo(null);
        } else {
            VentanaSecundaria.requestFocus();
        }
    }

    /**
     * Llamada a dominio para cambiar la constraseña.
     * @param newPassword La nueva contraseña que se quiere poner.
     * @param oldPassword La antigua contraseña del usuario.
     * @throws InputIncorrecto en caso de que la contraseña introducida no cumpla los criterios.
     * @throws IOException en caso de que se falle al acceder al fichero para actualizar la contraseña.
     * @throws PasswordIncorrecta en caso de que la contraseña no sea la correcta.
     */
    public void dominioCambiarContra(String newPassword, String oldPassword) throws InputIncorrecto , PasswordIncorrecta, IOException {

        ctrlDominio.cambiarPassword(oldPassword, newPassword);
    }

    /**
     * Llamada a dominio para eliminar un usuario.
     * @param password La contraseña del usuario.
     * @throws ErrorEliminarArchivoOriginal si se produce un error al eliminar el archivo original.
     * @throws UsuarioNoEncontrado si no se encuentra el usuario en el archivo de registro.
     * @throws IOException en caso de que se falle al acceder al fichero para eliminar el usuario actual de la ejecución.
     * @throws NoExisteCarpeta en caso de que no exista la carpeta con el registro de usuarios.
     * @throws PasswordIncorrecta en caso de que la contraseña no sea la correcta.
     */
    public void dominioEliminarUsuario(String password) throws ErrorEliminarArchivoOriginal, UsuarioNoEncontrado, IOException, NoExisteCarpeta, PasswordIncorrecta {
        ctrlDominio.eliminarUsuarioActual(password);
    }

    /**
     * Cierra la ventana secundaria si hay una abierta.
     */
    public void cerrarVentanaSecundaria() {
        if (!(VentanaSecundaria == null || !VentanaSecundaria.isDisplayable()))
            VentanaSecundaria.dispose();
    }

    /**
     * Llamada a dominio para resetear la estanteria.
     */
    public void dominioResetearEstanteria() {
        ctrlDominio.resetearProductos();
    }

    /**
     * Llamada a dominio para guardar el estado.
     * @param archivo Nombre del archivo donde se quiere guardar.
     * @param usuario El usuario donde se van a guardar los datos.
     * @throws NoExisteProducto No existe un producto.
     * @throws UsuarioIncorrecto El usuario no existe.
     * @throws UsuarioActualNoEsGestor El usuario que quiere guardar no es Gestor.
     * @throws UsuarioObjetivoNoEsEmpleado El usuario a donde se quiere guardar no es Empleado.
     * @throws InputIncorrecto El input tiene un formato incorrecto.
     * @throws NoExisteNingunProducto se lanza cuando no existe ningun producto a cambiar.
     * @throws IOException se lanza cuando ocurre un error de entrada o salida.
     */
    public void dominioGuardarEstado(String archivo,String usuario) throws NoExisteProducto, UsuarioIncorrecto,UsuarioActualNoEsGestor, UsuarioObjetivoNoEsEmpleado, InputIncorrecto, NoExisteNingunProducto, IOException {

        if(usuario.equals(ctrlDominio.getNombreUsuarioActual())) ctrlDominio.guardarEstadoPropio(archivo);
        else ctrlDominio.guardarEstadoAEmpleado(archivo,usuario);
    }

    /**
     * Llamada a dominio para cargar el estado
     * @param archivo Nombre del archivo que se quiere cargar.
     * @param usuario El usuario de donde se van a cargar los datos.
     * @throws UsuarioIncorrecto el usuario no existe.
     * @throws UsuarioActualNoEsGestor el usuario que quiere cargar no es Gestor.
     * @throws UsuarioObjetivoNoEsEmpleado el usuario de donde se quiere cargar no es Empleado.
     * @throws NoExisteProducto no existe un producto.
     * @throws InputIncorrecto el input tiene un formato incorrecto.
     * @throws YaExisteProducto ya existe un producto con el mismo nombre que uno del fichero.
     * @throws NoExisteNingunProducto no existe ningun producto.
     * @throws IOException se lanza cuando ocurre un error de entrada o salida.
     * @throws ParseException hay un error al intentar cargar el formato del json.
     */
    public void dominioCargarEstado(String archivo,String usuario) throws UsuarioIncorrecto, UsuarioActualNoEsGestor, UsuarioObjetivoNoEsEmpleado, NoExisteProducto, InputIncorrecto, YaExisteProducto, NoExisteNingunProducto, IOException, ParseException {
        if(usuario.equals(ctrlDominio.getNombreUsuarioActual())) {
            ctrlDominio.cargarEstadoPropio(archivo);
        }
        else ctrlDominio.cargarEstadoDeEmpleado(archivo,usuario);
    }

    /**
     * Llamada a dominio para saber todos los usuarios.
     * @return Conjunto de usuarios.
     */
    public Object[] getUsers() {
        return ctrlDominio.getNombresUsuarios().toArray(new String[0]);
    }

    /**
     * Llamada a dominio para saber todos los archivos de un usuario.
     * @param usuarioSelecionado Nombre del usuario.
     * @param objetivo Tipo de archivos (Estanterias o productos).
     * @return Conjunto de nombres de archivos.
     */
    public Object[] getFicheros(Object usuarioSelecionado, String objetivo) {

        if(objetivo.equals("EST"))return ctrlDominio.getArchivosGuardadoEstado(usuarioSelecionado.toString()).toArray();
        else return ctrlDominio.getArchivosGuardadoProductos(usuarioSelecionado.toString()).toArray();
    }

    /**
     * Llamada a dominio para saber el usuario actual
     * @return Nombre del usuario actual.
     */
    public String dominioGetUser() {
        return ctrlDominio.getNombreUsuarioActual();
    }

    /**
     * Llamada a dominio para saber el nombre de la imagen de un producto.
     * @param p Nombre del producto.
     * @return Nombre de la imagen.
     */
    public String dominioGetImagen(String p) {
        String img = ctrlDominio.getImagenProducto(p);
        if (img == null) return "";
        else return img;
    }

    /**
     * Llamada a dominio para asignar una imagen a un producto.
     * @param p Nombre del producto.
     * @param img Nombre de la imagen.
     */
    public void dominioSetImagen(String p, String img) { ctrlDominio.setImagenProducto(p,img);}

    /**
     * Llamada a dominio para cargar productos de un archivo.
     * @param usuario El usuario de donde se van a cargar los datos.
     * @param archivo Nombre del archivo que se quiere cargar.
     * @throws UsuarioIncorrecto el usuario no existe.
     * @throws UsuarioActualNoEsGestor el usuario que quiere cargar no es Gestor.
     * @throws UsuarioObjetivoNoEsEmpleado el usuario de donde se quiere cargar no es Empleado.
     * @throws NoExisteProducto no existe un producto.
     * @throws InputIncorrecto el input tiene un formato incorrecto.
     * @throws YaExisteProducto ya existe un producto con el mismo nombre que uno del fichero.
     * @throws NoExisteNingunProducto no existe ningun producto.
     * @throws IOException se lanza cuando ocurre un error de entrada o salida.
     * @throws ParseException hay un error al intentar cargar el formato del json.
     * @throws NumberFormatException Hay un error en el formateado de números.
     */
    public void dominioCargarProductos(String usuario, String archivo)throws IOException,UsuarioIncorrecto, UsuarioObjetivoNoEsEmpleado, ParseException, NumberFormatException, NoExisteProducto, YaExisteProducto, NoExisteNingunProducto, InputIncorrecto, UsuarioActualNoEsGestor {
        if(usuario.equals(ctrlDominio.getNombreUsuarioActual())) {
            ctrlDominio.cargarYañadirProductosPropios(archivo);
        }
        else ctrlDominio.cargarEstadoDeEmpleado(archivo,usuario);
    }

    /**
     * Llamada a dominio para guardar productos en un archivo.
     * @param archivo
     * @param usuario El usuario de donde se van a cargar los datos.
     * @throws UsuarioIncorrecto el usuario no existe.
     * @throws UsuarioActualNoEsGestor el usuario que quiere guardar no es Gestor.
     * @throws UsuarioObjetivoNoEsEmpleado el usuario a donde se quiere guardar no es Empleado.
     * @throws NoExisteProducto no existe un producto.
     * @throws YaExisteProducto ya existe un producto con el mismo nombre.
     * @throws NoExisteNingunProducto no existe ningun producto.
     * @throws IOException se lanza cuando ocurre un error de entrada o salida.
     * @throws ParseException hay un error al intentar cargar el formato del json.
     * @throws InputIncorrecto el input tiene un formato incorrecto.
     */
    public void guardarProductos(String archivo, String usuario)throws UsuarioIncorrecto, UsuarioActualNoEsGestor, UsuarioObjetivoNoEsEmpleado, NoExisteProducto, YaExisteProducto, NoExisteNingunProducto, IOException, ParseException, InputIncorrecto {
        if(usuario.equals(ctrlDominio.getNombreUsuarioActual())) ctrlDominio.guardarProductosPropios(archivo);
        else ctrlDominio.guardarProductosDeEmpleado(archivo,usuario);
    }
}
