package Font;
import Font.domaincontrollers.DomainCtrl;
import Font.presentation.ControladorPresentacion;

import java.util.Scanner;
import javax.swing.SwingUtilities;

/**
 * Driver de todos los controladores de dominio
 */
public class MainVisual {
    public static void main(String[] args) {

        // Lector de la entrada
        Scanner scanner = new Scanner(System.in);


        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        ControladorPresentacion ctrlPresentacion = new ControladorPresentacion();
                        ctrlPresentacion.inicializar();
                    }
                }
        );
    }
}