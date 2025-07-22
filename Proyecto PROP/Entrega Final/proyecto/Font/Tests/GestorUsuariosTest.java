/**
 * Autor: Pol García Parra
 */
package Font.Tests;

import Font.Exceptions.PasswordIncorrecta;
import Font.Exceptions.UsuarioIncorrecto;
import Font.classes.GestorUsuarios;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.HashMap;


public class GestorUsuariosTest {

    private GestorUsuarios gestorUsuarios = new GestorUsuarios();

    @Test
    public void testIniciarSession_Correcto() {
        try {
            boolean resultado = gestorUsuarios.iniciarSession("Juan", "Juan1234");
            assertTrue("El inicio de sesión debería ser exitoso", resultado);
        } catch (Exception e) {
            fail("No se esperaba ninguna excepción: " + e.getMessage());
        }
    }

    @Test
    public void testIniciarSession_ContraseñaIncorrecta() {
        try {
            gestorUsuarios.iniciarSession("Juan", "NotJuan1234");
            fail("Debería lanzarse una excepción PasswordIncorrecta");
        } catch (PasswordIncorrecta e) {
            assertEquals("Contraseña incorrecta. Intente de nuevo.\n", e.getMessage());
        } catch (Exception e) {
            fail("Se esperaba PasswordIncorrecta, pero se lanzó: " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testIniciarSession_UsuarioInexistente() {
        try {
            gestorUsuarios.iniciarSession("NoExiste", "password123");
            fail("Debería lanzarse una excepción UsuarioIncorrecto");
        } catch (UsuarioIncorrecto e) {
            assertEquals("Usuario incorrecto.Intente de nuevo.\n", e.getMessage());
        } catch (Exception e) {
            fail("Se esperaba UsuarioIncorrecto, pero se lanzó: " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testIniciarSession_UsuarioEnBlanco() {
        try {
            gestorUsuarios.iniciarSession("", "Juan1234");
            fail("Debería lanzarse una excepción UsuarioIncorrecto");
        } catch (UsuarioIncorrecto e) {
            assertEquals("Usuario incorrecto.Intente de nuevo.\n", e.getMessage());
        } catch (Exception e) {
            fail("Se esperaba UsuarioIncorrecto, pero se lanzó: " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testIniciarSession_PasswordEnBlanco() {
        try {
            gestorUsuarios.iniciarSession("Juan", "");
            fail("Debería lanzarse una excepción PasswordIncorrecta");
        } catch (PasswordIncorrecta e) {
            assertEquals("Contraseña incorrecta. Intente de nuevo.\n", e.getMessage());
        } catch (Exception e) {
            fail("Se esperaba PasswordIncorrecta, pero se lanzó: " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testIniciarSession_UsuarioNulo() {
        try {
            gestorUsuarios.iniciarSession(null, "Juan1234");
            fail("Debería lanzarse una excepción UsuarioIncorrecto");
        } catch (UsuarioIncorrecto e) {
            assertEquals("Usuario incorrecto.Intente de nuevo.\n", e.getMessage());
        } catch (Exception e) {
            fail("Se esperaba UsuarioIncorrecto, pero se lanzó: " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testIniciarSession_PasswordNula() {
        try {
            gestorUsuarios.iniciarSession("Juan", null);
            fail("Debería lanzarse una excepción PasswordIncorrecta");
        } catch (PasswordIncorrecta e) {
            assertEquals("Contraseña incorrecta. Intente de nuevo.\n", e.getMessage());
        } catch (Exception e) {
            fail("Se esperaba PasswordIncorrecta, pero se lanzó: " + e.getClass().getSimpleName());
        }
    }

    // Tests de comprobar usuario
    @Test
    public void testComprobarUser_ContraseñaCorrecta() {
        try {
            gestorUsuarios.iniciarSession("Ana", "Ana1234");
            boolean esValido = gestorUsuarios.comprobar_user("Ana1234");
            assertTrue("La contraseña debería coincidir", esValido);
        } catch (Exception e) {
            fail("No se esperaba ninguna excepción: " + e.getMessage());
        }
    }

    @Test
    public void testComprobarUser_ContraseñaIncorrecta() {
        try {
            gestorUsuarios.iniciarSession("Ana", "Ana1234");
            boolean esValido = gestorUsuarios.comprobar_user("NotAna1234");
            assertFalse("La contraseña no debería coincidir", esValido);
        } catch (Exception e) {
            fail("No se esperaba ninguna excepción: " + e.getMessage());
        }
    }

    // Test de cambio de contraseña
    @Test
    public void testCambiarPassword() {
        try {
            gestorUsuarios.iniciarSession("Ana", "Ana1234");
            gestorUsuarios.cambiarPassword("newPassword");
            boolean esValido = gestorUsuarios.comprobar_user("newPassword");
            assertTrue("La contraseña debería haberse actualizado", esValido);
        } catch (Exception e) {
            fail("No se esperaba ninguna excepción: " + e.getMessage());
        }
    }

    // Test de eliminar usuario
    @Test
    public void testEliminarUsuarioActual() {
        try {
            gestorUsuarios.iniciarSession("Ana", "Ana1234");
            gestorUsuarios.eliminarUsuarioActual();
            gestorUsuarios.iniciarSession("Ana", "Ana1234");
            fail("Debería lanzarse una excepción UsuarioIncorrecto tras eliminar el usuario");
        } catch (UsuarioIncorrecto e) {
            assertEquals("Usuario incorrecto.Intente de nuevo.\n", e.getMessage());
        } catch (Exception e) {
            fail("Se esperaba UsuarioIncorrecto, pero se lanzó: " + e.getClass().getSimpleName());
        }
    }
}
