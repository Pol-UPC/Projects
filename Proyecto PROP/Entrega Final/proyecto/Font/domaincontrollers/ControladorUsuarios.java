/**
 * Autor: Pol García Parra
 */
package Font.domaincontrollers;

import Font.Exceptions.NoExisteRol;
import Font.Exceptions.PasswordIncorrecta;
import Font.Exceptions.UsuarioIncorrecto;
import Font.Exceptions.YaExisteUsuario;
import Font.Exceptions.NoExisteRol;
import Font.classes.GestorUsuarios;
import Font.classes.Usuario;
import java.util.*;
/**
 * Clase controladora que gestiona las operaciones relacionadas con los usuarios.
 * Proporciona una interfaz para interactuar con el `GestorUsuarios` para realizar operaciones como iniciar sesión,
 * cambiar contraseñas, registrar usuarios y más.
 */
public class ControladorUsuarios {
    
    private static final ControladorUsuarios lock = new ControladorUsuarios();
    private static volatile ControladorUsuarios instance;
    private GestorUsuarios gestorUsuarios;


    /**
     * Devuelve la instancia única de `ControladorUsuarios` aplicando el patrón Singleton con doble verificación de sincronización.
     * Esto asegura que solo se cree una instancia, incluso en un entorno multihilo.
     *
     * @return La instancia única de `ControladorUsuarios`.
     */
    public static ControladorUsuarios getInstance(){
           ControladorUsuarios c = instance;
           if(c == null){
                synchronized (lock) {
                    c = instance;
                    if (c == null) {
                        c = new ControladorUsuarios();
                        instance = c;
                    }
                }
           }
           return c;
    }
    /**
     * Constructor privado que inicializa una instancia de `GestorUsuarios`.
     */
    private  ControladorUsuarios(){
        gestorUsuarios = new GestorUsuarios();
    }
    /**
     * Inicia sesión con el nombre de usuario y la contraseña proporcionados.
     * Llama al metodo de iniciar sesion de gestor de usuarios
     *
     * @param Nombre El nombre de usuario con el que se desea iniciar sesión.
     * @param Password La contraseña del usuario.
     * @return `true` si la autenticación es exitosa, de lo contrario `false`.
     * @throws UsuarioIncorrecto Si el nombre de usuario es incorrecto.
     * @throws PasswordIncorrecta Si la contraseña es incorrecta.
     */
    public Boolean iniciarSession(String Nombre, String Password)throws UsuarioIncorrecto, PasswordIncorrecta {
        return gestorUsuarios.iniciarSession(Nombre,Password);
    }
    /**
     * Cambia la contraseña del usuario actual. Llama al metodo cambiar pasword del gestor de usuarios
     *
     * @param newPassword La nueva contraseña que se desea establecer.
     */
    public void cambiarPassword(String newPassword){
        gestorUsuarios.cambiarPassword(newPassword);
    }
    /**
     * Comprueba si la contraseña proporcionada coincide con la del usuario actual.
     * Llama al metodo `comprobar_user` del `GestorUsuarios`.
     *
     * @param oldPassword La contraseña que se desea comprobar.
     * @return `true` si la contraseña coincide, de lo contrario `false`.
     */
    public boolean comprobar_user(String oldPassword){
        return gestorUsuarios.comprobar_user(oldPassword);
    }
    /** 
    * Funcion encargada de llamar a gestor de usuarios para eliminar el usuario actual
    */
    public void eliminarUsuarioActual() {
        gestorUsuarios.eliminarUsuarioActual();
    }
    /**
     * Da de alta un nuevo usuario. Llama al metodo `altaUsuario` del `GestorUsuarios`.
     *
     * @param Nombre El nombre del usuario que se va a registrar.
     * @param Password La contraseña del nuevo usuario.
     * @param Rol El rol que se asignará al usuario.
     * @return `true` si el usuario fue dado de alta exitosamente, de lo contrario `false`.
     * @throws YaExisteUsuario Si el usuario ya existe en el sistema.
     * @throws NoExisteRol Si el rol proporcionado no existe.
     */
     public boolean  altaUsuario(String Nombre, String Password, String Rol)throws YaExisteUsuario, NoExisteRol {
        return gestorUsuarios.altaUsuario(Nombre,Password,Rol);
     }
    /**
     * Obtiene la información del usuario actual.
     * Llama al metodo `pedirUsuario` del `GestorUsuarios`.
     *
     * @return El objeto `Usuario` correspondiente al usuario actual.
     */
    public Usuario pedirUsuario(){
           return  gestorUsuarios.pedirUsuario();
    }
    /**
     * Obtiene el mapa de usuarios gestionado por `GestorUsuarios`.
     *
     * @return Un `HashMap` que mapea nombres de usuario a objetos `Usuario`.
     */
    public HashMap<String, Usuario> getGestorUsers() {
        return gestorUsuarios.getGestorUsers();
    }
    /**
     * Establece el mapa de usuarios gestionado por `GestorUsuarios`.
     *
     * @param gestUs El mapa de usuarios a establecer.
     */
    public void  setGestorUsers(HashMap<String,Usuario>gestUs){
        gestorUsuarios.setGestorUsers(gestUs);
    }
    /**
     * Obtiene el rol del usuario actual.
     * Llama al metodo `rolusuario` del `GestorUsuarios`.
     *
     * @return Una cadena que representa el rol del usuario actual.
     */
    public String  rolusuario(){
        return gestorUsuarios.rolusuario();
    }
    /**
     * Sugerir un nombre de usuario similar basado en una entrada dada.
     * Llama al metodo `sugerir_usuario` del `GestorUsuarios`.
     *
     * @param us El nombre de usuario para el cual se desea sugerir uno alternativo.
     * @return Una sugerencia de nombre de usuario.
     */
    public String sugerir_usuario(String us){
        return gestorUsuarios.sugerir_usuario(us);
    }
    /**
     * Comprueba si la contraseña del usuario actual ha sido modificada.
     * Llama al metodo `PasswordUsuarioModificada` del `GestorUsuarios`.
     *
     * @return `true` si la contraseña fue modificada, de lo contrario `false`.
     */
    public boolean PasswordUsuarioModificada (){
            return gestorUsuarios.PasswordUsuarioModificada();
    }
    /**
     * Obtiene un usuario basado en el nombre de usuario proporcionado.
     * Llama al metodo `getUser` del `GestorUsuarios`.
     *
     * @param Nombre El nombre del usuario que se desea obtener.
     * @return El objeto `Usuario` correspondiente al nombre proporcionado.
     * @throws UsuarioIncorrecto Si no se encuentra un usuario con el nombre proporcionado.
     */
    public Usuario getUser (String Nombre)throws UsuarioIncorrecto{
         return gestorUsuarios.getUser(Nombre);
    }
}