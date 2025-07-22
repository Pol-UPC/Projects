/**
 * Autor: Pol García Parra
 */
package Font.classes;
import Font.Exceptions.PasswordIncorrecta;
import Font.Exceptions.UsuarioIncorrecto;
import Font.Exceptions.YaExisteUsuario;
import Font.Exceptions.NoExisteRol;
import Font.classes.Usuario.Tusuario;
import java.util.*;

/**
 * Clase dedicada a la Gestion de los Usuarios y sus operaciones. Esta tiene un Hash Map de todos los usuarios registrados en el sistema
 *  y un usuario actual que es el usuario que se encuentra dentro del sistema en ese momento.
 */
public class GestorUsuarios {

        /** Atributos **/
       private HashMap <String, Usuario> gestorUsers;
       Usuario actual;
       String caracteresPermitidos = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
       boolean flagUserPassword;
        /**
        * Constructora para crear un GestorUsuarios.
        */
        public GestorUsuarios() {
            gestorUsers = new HashMap<>();
            flagUserPassword = false;         
	    }
        
        
        /**Metodos **/

        //Pre: La funcion recibe un Nombre de Usuario y una Contraseña no nulos
        //Post: Devuelve true si el inicio de sessión es exitoso. De cualquier otra manera sale con excepcion. 
         /**
         * La función inicia sessión en el sistema 
         * @param Nombre es un nombre de usuarios
         * @param Password es una contraseña de usuario
         * @return Devuelve true si el inicio de sessión es exitoso. De cualquier otra manera sale con excepcion.
         */
        public Boolean iniciarSession(String Nombre, String Password)throws UsuarioIncorrecto, PasswordIncorrecta { 

            if(this.gestorUsers.containsKey(Nombre)){
                actual = gestorUsers.get(Nombre); //Añadimos el usuario actual del sistema
                if (actual.getPassword().equals(Password))return true;
            
                else {
                    actual = null;
                    throw new PasswordIncorrecta("Contraseña incorrecta. Intente de nuevo.\n");  
                }     
            }
            else{
                    throw new UsuarioIncorrecto("Usuario incorrecto.Intente de nuevo.\n"); 
            }
        }
        
        //Pre: La funcion recibe una contraseña no nula.
        //Post: El sistema devuelve true si la contraseña coincide con la contraseña de el Usuario actual
         /**
         * La función determina si la contraseña coincide con la del usuario actualmente registrado
         * @param oldPassword es una contraseña
         * @return devuelve un bool true si coincide con la contraseña del usuario actualmente registrado y false si no coincide
         */
        public boolean comprobar_user(String oldPassword){ 
            return actual.getPassword().equals(oldPassword);
        }
        
        //Pre:La funcion recibe una contraseña no nula.
        //Post: Actual.Password pasara a tener la nueva contraseña 
         /**
         * La funcion realiza un cambio de contraseña al usuario actualmente registrado
         * @param newPassword es la nueva contraseña a la que se quiere cambiar
         */
        public void cambiarPassword(String newPassword) { 
            actual.setPassword(newPassword);
            flagUserPassword = true;
        }
        
        //Pre:
        //Post: El sistema retorna el Usuario actual del sistema 
         /**
         * La funcion devuelve el usuario actual del sistema
         * @return devuelve el usuario actual
         */
        public Usuario pedirUsuario(){
            return actual;
        }
    
        //Pre: 
        //Post: El usuario actual se elimina del gestor de usuarios. 
        /**
         * La función elimina el usuario del gestor de usuarios
         */
        public void eliminarUsuarioActual()  {
            
            gestorUsers.remove(actual.getNombreUsuario());
            actual = null;
        }
        
        //Pre: Recibe un Nombre no nulo, una contraseña no nula y un Rol válido (EMPLEADO o GESTOR)
        //Post Se da de alta a un nuevo usuario al sistema
        /**
         * La función da de alta a un nuevo usuario al sistema
         * @param Nombre Es un nombre de usuario no nulo
         * @param Password Es una contraseña de usuario no nula
         * @param Rol Es un rol de usuario no nulo y con valor EMPLEADO o GESTOR
         * @return devuelve true si el inicio de sessión ha sido exitoso. De otra forma sale con excepcion.
         */
        public boolean altaUsuario(String Nombre, String Password, String Rol) throws YaExisteUsuario, NoExisteRol{
            boolean correct = false;
            Tusuario rolEnum;          

                if (this.gestorUsers.containsKey(Nombre)) {
                    throw new YaExisteUsuario("El usuario ya existe. Intente con otro nombre:\n"); 
                } 
                else{
                    correct = true;
                    if (!"Gestor".equals(Rol) && !"Empleado".equals(Rol)) {
                        throw new NoExisteRol("El rol no es correcto. Intente con: Gestor o Empleado.");
                    }
                    rolEnum = Tusuario.valueOf(Rol.toUpperCase()); // Convierte la cadena a Enum
                    actual = new Usuario(Nombre, Password, rolEnum); // Asigna el nuevo usuario al actual
                    gestorUsers.put(Nombre, actual);// Añadir el nuevo usuario al HashMap
                }
            return  correct;
        }
          
        //Pre: 
        //Post: Devuelve el map de usuarios 
        /**
         * La función devuelve el map de gestion de usuarios
         * @return devuelve el HashMap de Usuarios
         */
        public HashMap<String, Usuario> getGestorUsers() {
            return gestorUsers;
        }
        //Pre: 
        //Post: Hace un set de el hash map de los usuarios del sistema
        /**
         * Hace un set de el hash map de los usuarios del sistema
         * @param gestUs Es un hashMap con los usuarios del sistema. Ordenados por Nombre como clave primaria.
         */
        public void  setGestorUsers(HashMap<String,Usuario>gestUs){
            gestorUsers = gestUs;
        }
        //Pre: 
        //Post: La funcion devuelve un rol de usuario

        public String  rolusuario(){
            return actual.getRol().toString();
        }
        
        //Pre: La funcion recibe un nombre de usuario ya en el sistema
        //Post: El sistema devuelve un nombre de usuario que si sea válido para dar-se de alta
        public String sugerir_usuario(String us){
            StringBuilder aux = new StringBuilder();
            aux.append("_");
            int indiceAleatorio;
            Random random = new Random();
            for(int i = 0; i < 5;++i){
                indiceAleatorio = random.nextInt(caracteresPermitidos.length());
                aux.append(caracteresPermitidos.charAt(indiceAleatorio));
            }
            return us+aux.toString();
        } 

        public boolean PasswordUsuarioModificada (){
            return flagUserPassword;
        }

        public Usuario getUser(String Nombre)throws  UsuarioIncorrecto{
            if(this.gestorUsers.containsKey(Nombre)){
                return gestorUsers.get(Nombre);
            }
            else  throw new UsuarioIncorrecto("Usuario incorrecto.Intente de nuevo.\n");
        }
}