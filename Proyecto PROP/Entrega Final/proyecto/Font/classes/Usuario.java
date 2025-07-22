/**
 * Autor: Pol García Parra
 */
package Font.classes;

/**
 * Clase dedidcada a los Usuarios los cuales tienen Nombre, Contraseña y un Rol (EMPLEADO o GESTOR)
 * 
 */
public class Usuario {
	
	/** Tipos de Usuarios (Rol) **/
    public enum Tusuario {
        EMPLEADO, GESTOR 
    }

    /** Atributos **/
	private String nombre; //El nombre es único 
    private String Password;
    private Tusuario rol ;

	/** Constructora **/
	
	public Usuario(String nombre, String Password, Tusuario rol) {
		this.nombre = nombre;
        this.Password = Password;
        this.rol = rol;
	}

	/** Métodos públicos **/ 

    /** Gets y Sets **/

    /**
     * La funcion obtiene el Nombre de Usuario
     * @return Nombre del Usuario
     */
    public String getNombreUsuario(){
        return nombre;
    }
    /**
     * La funcion obtiene la Password del Usuario
     * @return Pasword del Usuario
     */
    public String getPassword(){
        return Password;
    }
    /**
     * La funcion obtiene el rol del usuario
     *  @return El rol del Usuario
     */
    public Tusuario getRol(){
        return rol;
    }
    /**
     * La función determina el nombre del usuario
     * @param nombre es un nombre de usuario
     */
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    /**
     * La funcion determina la contraseña del usuario
     * @param Password es una contraseña 
     */
    public void setPassword(String Password){
        this.Password = Password;
    }
    /**
     * La funcion determina el rol del usuario
     * @param rol Es un rol de usuario: EMPLEADO o GESTOR
     */
    public void setRol(Tusuario rol){
        this.rol = rol;
    }

 
}