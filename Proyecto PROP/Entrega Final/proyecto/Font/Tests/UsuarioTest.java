/**
 * Autor: Pol García Parra
 */
package Font.Tests;

import Font.classes.Usuario;
import Font.classes.Usuario.Tusuario;
import org.junit.*;
import static org.junit.Assert.*;

public class UsuarioTest {

    private Usuario usuario;

    @Before
    public void setUp() {
        usuario = new Usuario("Juan", "Juan1234", Tusuario.EMPLEADO);
    }

    @After
    public void tearDown() {
        // Limpiar la instancia después de cada test
        usuario = null;
    }

    /** Tests del Constructor **/
    @Test
    public void testConstructor_Correcto() {
        Usuario nuevoUsuario = new Usuario("Ana", "Ana1234", Tusuario.GESTOR);
        assertEquals("El nombre debería ser 'Ana'", "Ana", nuevoUsuario.getNombreUsuario());
        assertEquals("La contraseña debería ser 'Ana1234'", "Ana1234", nuevoUsuario.getPassword());
        assertEquals("El rol debería ser GESTOR", Tusuario.GESTOR, nuevoUsuario.getRol());
    }

    /** Tests de Getters **/
    @Test
    public void testGetNombreUsuario() {
        assertEquals("El nombre debería ser 'Juan'", "Juan", usuario.getNombreUsuario());
    }

    @Test
    public void testGetPassword() {
        assertEquals("La contraseña debería ser 'Juan1234'", "Juan1234", usuario.getPassword());
    }

    @Test
    public void testGetRol() {
        assertEquals("El rol debería ser EMPLEADO", Tusuario.EMPLEADO, usuario.getRol());
    }

    /** Tests de Setters **/
    @Test
    public void testSetNombreUsuario() {
        usuario.setNombre("Pedro");
        assertEquals("El nombre debería ser 'Pedro'", "Pedro", usuario.getNombreUsuario());
    }

    @Test
    public void testSetPassword() {
        usuario.setPassword("NuevaPass123");
        assertEquals("La contraseña debería ser 'NuevaPass123'", "NuevaPass123", usuario.getPassword());
    }

    @Test
    public void testSetRol() {
        usuario.setRol(Tusuario.GESTOR);
        assertEquals("El rol debería ser GESTOR", Tusuario.GESTOR, usuario.getRol());
    }

    /** Tests de Casos Extremos **/
    @Test
    public void testNombreUsuarioVacio() {
        usuario.setNombre("");
        assertEquals("El nombre debería ser una cadena vacía", "", usuario.getNombreUsuario());
    }

    @Test
    public void testPasswordVacia() {
        usuario.setPassword("");
        assertEquals("La contraseña debería ser una cadena vacía", "", usuario.getPassword());
    }

    @Test
    public void testRolNulo() {
        usuario.setRol(null);
        assertNull("El rol debería ser null", usuario.getRol());
    }

    /** Cambiando valores**/
    @Test
    public void testCambiarValores() {
        usuario.setNombre("Maria");
        usuario.setPassword("Maria1234");
        usuario.setRol(Tusuario.GESTOR);

        assertEquals("El nombre debería ser 'Maria'", "Maria", usuario.getNombreUsuario());
        assertEquals("La contraseña debería ser 'Maria1234'", "Maria1234", usuario.getPassword());
        assertEquals("El rol debería ser GESTOR", Tusuario.GESTOR, usuario.getRol());
    }
}
