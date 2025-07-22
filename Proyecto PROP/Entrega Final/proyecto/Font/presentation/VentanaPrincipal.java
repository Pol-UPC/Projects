package Font.presentation;
import Font.Exceptions.*;
import Font.classes.EstanteriaOrdenada;
import Font.forms.*;
import org.json.simple.parser.ParseException;

import javax.imageio.IIOException;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase que genera el JFrame usado para la vista principal, también gestiona sus cambios entre diferentes content panes y redirige
 * llamadas de los content panes a Control Presentación.
 */
public class VentanaPrincipal {
    ControladorPresentacion ctrlPresentacion;
    JFrame mainFrame = new JFrame("Market Planner");
    MainWindow window;


    public VentanaPrincipal(ControladorPresentacion ctrl) {
        ctrlPresentacion = ctrl;

    }

    /**
     * Inicializa todos los valores básicos y genera la ventana con la pantalla de inicio.
     */
    public void inicializar() {
        mainFrame.setContentPane(new LogRegWindow(this).getPanel());

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.setSize(640,480);
        mainFrame.setLocationRelativeTo(null);

    }

    /**
     * Llama al controlador de presentación para que abra la ventana secundaria.
     * @param info Información que se enviará.
     * @param tipo El tipo de ventana que se abrirá.
     */
    public void actionAbrirVentanaSecundaria(ArrayList<String> info, int tipo) {
        if (info.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "No hay productos.");
        } else if (tipo == 1 && info.size() < 2) {
            JOptionPane.showMessageDialog(mainFrame, "No hay similitud.");
        } else {
            ctrlPresentacion.abrirVentanaSecundaria(info,tipo);
        }
    }

    /**
     * Llama al controlador de presentación para saber la similitud entre dos productos.
     * @param p1 Primer producto.
     * @param p2 Segundo producto.
     * @return El valor de similitud.
     */
    public int actionGetSimilitud(String p1, String p2) {
        int ret = 0;
        try {
            ret = ctrlPresentacion.dominioGetSimilitud(p1, p2);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return ret;
    }

    public String actionGetImagen(String p) {
        return ctrlPresentacion.dominioGetImagen(p);
    }

    public void actionSetImagen(String p, String img) {
        ctrlPresentacion.dominioSetImagen(p,img);
    }

    /**
     * Cambia el content pane de la ventana al del Menu de Register.
     */
    public void actionEntrarMenuReg () {
        mainFrame.getContentPane().setVisible(false);
        mainFrame.setContentPane(new RegisterWindow(this).getPanel());
    }

    /**
     * Cambia el content pane de la ventana al del Menu Inicial.
     */
    public void actionEntrarMenuInicio () {
        mainFrame.getContentPane().setVisible(false);
        mainFrame.setContentPane(new LogRegWindow(this).getPanel());
    }

    /**
     * Cambia el content pane de la ventana al del Menu de Login.
     */
    public void actionEntrarMenuLogin () {
        mainFrame.getContentPane().setVisible(false);
        mainFrame.setContentPane(new LoginWindow(this).getPanel());
    }

    /**
     * Llama al controlador de presentación para intercambiar dos productos.
     * @param p1 Primer producto.
     * @param p2 Segundo producto.
     */
    public void actionIntercambiarProductos(String p1, String p2) {
        try {
            ctrlPresentacion.dominioIntercambiarProductos(p1,p2);
        } catch (Exception e) {

        }

    }

    /**
     * Llama al controlador de presentación para crear un producto. El nombre del producto lo saca de un dialog que pregunta al usuario
     * el nombre deseado.
     */
    public void actionCrearProducto() {
        String s = JOptionPane.showInputDialog(mainFrame,"¿Que nombre tendrá el producto?","Crear Producto", JOptionPane.QUESTION_MESSAGE);
        try {
            ctrlPresentacion.dominioCrearProducto(s);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame,e.getMessage());
        }
    }

    public void actionBorrarProducto(String p) {
        int confirmado = JOptionPane.showOptionDialog(mainFrame,"¿Estás seguro de querer borrar " + p + "?","Borrar producto",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,null,
                new String[]{"Si", "No"},0);
        if (confirmado == 0) {
            try {
                ctrlPresentacion.dominioBorrarProducto(p);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(mainFrame, e.getMessage());
            }
        }
    }

    /**
     * Llama al controlador de presentación para ordenar la estantería. El tipo de algoritmo lo saca de un dialog que pregunta al usuario
     * que tipo de algoritmo quiere usar.
     */
    public void actionOrdenarEstanteria() {
        int res = JOptionPane.showOptionDialog(mainFrame,"¿Que algoritmo quieres usar?","Ordenar Estanteria",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,null,
                new String[]{"Fuerza bruta", "Aproximado"},0);
        if (res != -1) {
            try {
                int temp = ctrlPresentacion.dominioOrdenarEstanteria(res);
                JOptionPane.showMessageDialog(mainFrame,"La suma resultante de las similitudes ahora es " + temp);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    /**
     * Actualiza los visuales del Grafo Productos.
     */
    public void actualizarVisuales() {
        window.actualizarGrafo();
    }

    /**
     * Llama al controlador de presentación para recibir los nombres de los productos.
     * @return Lista de productos.
     */
    public ArrayList<String> actionGetProductos() {
        ArrayList<String> ret = new ArrayList<>();
        try {
            ret = ctrlPresentacion.dominioGetProductos();
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(mainFrame,"Error Estanteria");
        }
        return ret;
    }

    /**
     * Llama al controlador de presentación para iniciar sesión como un usuario.
     * @param usuario Nombre del usuario.
     * @param contra Su contraseña.
     */
    public void actionIniciarUsuario (String usuario, String contra) {
        Integer res = 0;
        try {
            ctrlPresentacion.dominioIniciarSesion(usuario, contra);
            String rol = ctrlPresentacion.dominioGetRol(usuario);
            if(rol.equals("GESTOR")) {
                mainFrame.getContentPane().setVisible(false);
                window = new MainWindow(this);
                mainFrame.setContentPane(window.getPanel());
            }
            else {
                mainFrame.getContentPane().setVisible(false);
                window = new MainWindowEmpleado(this);
                mainFrame.setContentPane(window.getPanel());
            }
        }
        catch (UsuarioIncorrecto e){
            JOptionPane.showMessageDialog(mainFrame,"Usuario Incorrecto");
        }
        catch (PasswordIncorrecta e) {
            JOptionPane.showMessageDialog(mainFrame, "Contraseña Incorrecta");
        }

    }

    /**
     * Llama al controlador de presentación para registrar un usuario.
     * @param nombre Nombre deseado.
     * @param password Contraseña deseada.
     * @param rol Rol deseado.
     */
    public String actionRegistrarUsuario(String nombre, String password, String rol){

        String aux1 = "";
        try {
            ctrlPresentacion.dominioRegistrarUsuario(nombre, password, rol);
            String aux2 = ctrlPresentacion.dominioGetRol(nombre);
            System.out.println(aux2);
            if(aux2.equals("GESTOR")) {
                JOptionPane.showMessageDialog(mainFrame, "Usuario registrado con éxito.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                mainFrame.getContentPane().setVisible(false);
                window = new MainWindow(this);
                mainFrame.setContentPane(window.getPanel());
            }
            else{
                JOptionPane.showMessageDialog(mainFrame, "Usuario registrado con éxito.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                mainFrame.getContentPane().setVisible(false);
                window = new MainWindowEmpleado(this);
                mainFrame.setContentPane(window.getPanel());
            }

        }
        catch (YaExisteUsuario e) {
            aux1 = ctrlPresentacion.dominioSugerirUsuario(nombre);
            JOptionPane.showMessageDialog(mainFrame, "El usuario ya existe. Pruebe con " + aux1, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NoExisteRol e) {
            // Mensaje de error si el rol es inválido
            JOptionPane.showMessageDialog(mainFrame, "El rol especificado no es válido. Intente de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (InputIncorrecto e){
            JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException e){
            JOptionPane.showMessageDialog(mainFrame, "Fallo en el acceso a datos", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return  aux1;
    }

    /**
     * Llama al controlador de presentación para cambiar la contraseña del usuario actual.
     * @param newPassword Nueva contraseña a la que cambiar.
     * @param OldPassword Contraseña antigua del usuario.
     */
    public void cambiarContra(String newPassword, String OldPassword) {
        try {
            ctrlPresentacion.dominioCambiarContra(newPassword,OldPassword);
        }
        catch(InputIncorrecto e){
            JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (PasswordIncorrecta e){
            JOptionPane.showMessageDialog(mainFrame, "Password Antigua Incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException e){
            JOptionPane.showMessageDialog(mainFrame, "Error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Llama al controlador de presentación para dar de baja al usuario actual.
     * @param password Contraseña del usuario actual.
     * @return Verdadero si se ha eliminado correctamente o Falso si no lo ha hecho.
     */
    public boolean EliminarUsuario(String password) {
        Boolean correcto = true;
        try {
            ctrlPresentacion.dominioEliminarUsuario(password);
            correcto = true;
        }
        catch(ErrorEliminarArchivoOriginal e) {
            JOptionPane.showMessageDialog(mainFrame, "Error al eliminar el archivo original", "Error", JOptionPane.ERROR_MESSAGE);
            correcto = false;
        }
        catch (UsuarioNoEncontrado e){
            JOptionPane.showMessageDialog(mainFrame, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            correcto = false;
        }
        catch (IOException e){
            JOptionPane.showMessageDialog(mainFrame, "Error", "Error", JOptionPane.ERROR_MESSAGE);
            correcto = false;
        }
        catch (NoExisteCarpeta e){
            JOptionPane.showMessageDialog(mainFrame, "No hay carpeta de usuario a eliminar", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (PasswordIncorrecta e){
            JOptionPane.showMessageDialog(mainFrame, "La contraseña es incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
            correcto = false;
        }
        return  correcto;
    }

    /**
     * Llama al controlador de presentación para guardar la estantería.
     * @param archivo Nombre del archivo.
     * @param usuario Nombre del usuario donde guardar.
     */
    public void actionGuardarEstado(String archivo,  String usuario) {
        try {
            ctrlPresentacion.dominioGuardarEstado(archivo, usuario);
        } catch (NoExisteProducto e) {
            JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (InputIncorrecto e) {
            JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NoExisteNingunProducto e) {
            JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (UsuarioIncorrecto e) {
            JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (UsuarioActualNoEsGestor e) {
            JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (UsuarioObjetivoNoEsEmpleado e) {
            JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Llama al controlador de presentación para cargar una estantería.
     * @param archivo Nombre del archivo.
     * @param usuario Nombre del usuario de donde cargar.
     */
    public void actionCargarEstado(String archivo, String usuario) {
        try {

            ctrlPresentacion.dominioCargarEstado(archivo,usuario);
        }
        catch (NoExisteProducto e) {
            JOptionPane.showMessageDialog(mainFrame, "No tiene productos", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (YaExisteProducto e) {
            JOptionPane.showMessageDialog(mainFrame, "Ya existe producto", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (NoExisteNingunProducto e) {
            JOptionPane.showMessageDialog(mainFrame, "No existe ningun producto", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(mainFrame, "Error IOException", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (ParseException e){
            JOptionPane.showMessageDialog(mainFrame, "Parse exception", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (InputIncorrecto e){
            JOptionPane.showMessageDialog(mainFrame, "Input incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (UsuarioActualNoEsGestor e){
            JOptionPane.showMessageDialog(mainFrame, "El usuario actual no es gestor", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (UsuarioObjetivoNoEsEmpleado e){
            JOptionPane.showMessageDialog(mainFrame, "El usuario selecionado no es empleado", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (UsuarioIncorrecto e){
            JOptionPane.showMessageDialog(mainFrame, "Usuario incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Llama al controlador de presentación para resetear la estantería.
     */
    public void actionResetearEstanteria() {
        ctrlPresentacion.dominioResetearEstanteria();
    }

    /**
     * Llama al controlador de presentación para saber todos los usuarios.
     * @return Nombres de todos los usuarios.
     */
    public Object[] getUsers() {
        return ctrlPresentacion.getUsers();
    }

    /**
     * Llama al controlador de presentación para saber todos los archivos de un usuario.
     * @param usuarioSelecionado Nombre del usuario.
     * @param objetivo Tipo de archivos (Estanteria o Productos).
     * @return Conjunto de nombres de los archivos.
     */
    public Object[] getFicheros(Object usuarioSelecionado,String objetivo) {
        return ctrlPresentacion.getFicheros(usuarioSelecionado,objetivo);
    }

    /**
     * Llama al controlador de presentación para saber el rol del usuario actual.
     * @return Nombre del rol.
     */
    public String getRol() {
        return ctrlPresentacion.dominioGetRol(ctrlPresentacion.dominioGetUser());
    }

    /**
     * Llama al controlador de presentación para saber el nombre del usuario actual.
     * @return Nombre del usuario.
     */
    public String getUsuario() {
        return  ctrlPresentacion.dominioGetUser();
    }

    /**
     * Llama al controlador de presentación para cargar los productos de un usuario.
     * @param usuario Nombre del usuario de donde cargar los productos.
     * @param archivo Nombre del archivo.
     */
    public void cargarProductos(String usuario, String archivo) {
        try {
            ctrlPresentacion.dominioCargarProductos(usuario, archivo);
            actualizarVisuales();
        }
        catch (NoExisteProducto e) {
            JOptionPane.showMessageDialog(mainFrame, "No tiene productos", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (YaExisteProducto e) {
            JOptionPane.showMessageDialog(mainFrame, "Ya existe producto", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (NoExisteNingunProducto e) {
            JOptionPane.showMessageDialog(mainFrame, "No existe ningun producto", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(mainFrame, "Error IOException", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (ParseException e){
            JOptionPane.showMessageDialog(mainFrame, "Parse exception", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (InputIncorrecto e){
            JOptionPane.showMessageDialog(mainFrame, "Input incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (UsuarioActualNoEsGestor e){
            JOptionPane.showMessageDialog(mainFrame, "El usuario actual no es gestor", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (UsuarioObjetivoNoEsEmpleado e){
            JOptionPane.showMessageDialog(mainFrame, "El usuario selecionado no es empleado", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (UsuarioIncorrecto e){
            JOptionPane.showMessageDialog(mainFrame, "Usuario incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Llama al controlador de presentación para guardar productos en un usuario.
     * @param archivo Nombre del archivo.
     * @param usuario Nombre del usuario donde guardar.
     */
    public void guardarProductos(String archivo, String usuario) {
        try {
            ctrlPresentacion.guardarProductos(archivo, usuario);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
