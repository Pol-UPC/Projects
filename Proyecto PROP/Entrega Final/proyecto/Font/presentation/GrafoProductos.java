package Font.presentation;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * GrafoProductos es una subclase de JPanel que se usa para poder tener los efectos
 * y movimientos que se muestran en la pantalla principal al ver los productos.
 * Contiene múltiples instancias de menu entre las que va cambiando según el momento.
 */
public class GrafoProductos extends JPanel {

    ArrayList<MenuGraficoGenerico> menus;
    public ArrayList<ArrayList<String>> productosMat = new ArrayList<>();
    public ArrayList<String> productosList = new ArrayList<>();
    public ArrayList<Integer> similitudes = new ArrayList<>();
    public ArrayList<BufferedImage> imagenesProds = new ArrayList<>();

    Graphics2D graphic2d;

    boolean interactuable = true;


    int displayMode = 0; //0 = estanteria normal 1 = Similitudes de un producto
    int nextDisplay = 0;

    ArrayList<String> info = new ArrayList<>();

    public GrafoProductos() {
        menus = new ArrayList<>();
        menus.add(new MenuGraficoEstanteria(this));
        menus.add(new MenuGraficoSimilitudes(this));
        MiMouseListener miMouseListener = new MiMouseListener(this);
        addMouseMotionListener(miMouseListener);
        addMouseListener(miMouseListener);
        addMouseWheelListener(miMouseListener);

    }

    /**
     * Llamada al menu actual para seleccionar el producto que recibe según la string.
     * @param s El nombre del producto que se quiere seleccionar.
     */
    public void encontrarProducto(String s) {
        menus.get(displayMode).encontrarProducto(s);
    }


    /**
     * Llamada generica que pinta la pantalla, cada menu pinta cosas diferentes así
     * que se le delega la tarea.
     * @param g  El contexto de graficos con el que se pinta.
     */
    @Override
    public void paint(Graphics g) {
        menus.get(displayMode).pintar(g, this.getParent().getSize());
    }

    /**
     * Esta función es llamada cada vez que se arrastra el ratón, redirige la información al menu
     * activo y repinta la escena.
     * @param x Movimiento horizontal del ratón.
     * @param y Movimiento vertical del ratón.
     */
    public void dragCamara(float x, float y) {
        if (interactuable) {
            menus.get(displayMode).dragCamara(x,y);
            repaint();
        }
    }



    /**
     * Esta función es llamada cada vez que se clica con el botón izquierdo, redirige la información al menu
     * activo y repinta la escena.
     * @param x Posición X del ratón.
     * @param y Posición Y del ratón.
     */
    public void clicarCamara(float x, float y) {
        if (interactuable) {
            menus.get(displayMode).clicarCamara(x,y);
        }
    }

    /**
     * Esta función es llamada cada vez que se clica con botón derecho, redirige la información al menu
     * activo y repinta la escena.
     * @param x Posición X del ratón.
     * @param y Posición Y del ratón.
     */
    public void clicarCamaraDerecho (float x, float y) {
        if (interactuable) {
            menus.get(displayMode).clicarCamaraDerecho(x,y);
            repaint();
        }
    }

    /**
     * Esta función es llamada cada vez que se mueve la ruedita, redirige la información al menu
     * activo y repinta la escena.
     * @param mov Cantidad de movimiento.
     */
    public void rueditaCamara(float mov) {
        if (interactuable) {
            menus.get(displayMode).rueditaCamara(mov);
            repaint();
        }
    }

    /**
     * Esta función es llamada cada vez que se suelta el drag, redirige la información al menu
     * activo y repinta la escena.
     * @param x Movimiento horizontal del ratón.
     * @param y Movimiento vertical del ratón.
     */
    public void soltarCamara(float x, float y) {
        if (interactuable) {
            menus.get(displayMode).soltarCamara(x,y);
            repaint();
        }
    }

    /**
     * Actualiza la lista de productos guardada.
     * @param ps La nueva lista de productos.
     * @param imgs La nueva lista de imagenes.
     */
    public void actualizarProductos(ArrayList<String> ps, ArrayList<String> imgs) {
        productosMat = new ArrayList<>();
        productosList = new ArrayList<>();
        imagenesProds = new ArrayList<>();
        String path = System.getProperty("user.dir");
        path += "/Imagenes";
        File carpeta = new File(path);
        if (!carpeta.exists()) carpeta.mkdir();
        for (int y = 0; y < ps.size(); y+= 10) {
            productosMat.add(new ArrayList<>());
            for (int x = 0; x < 10; x++) {
                if (x + y < ps.size()) {
                    productosMat.get(y/10).add(ps.get(x + y));
                    productosList.add(ps.get(x+y));

                    File file = new File(path + "/" +  imgs.get(x+y) + ".png");
                    try {
                        BufferedImage image = ImageIO.read(file);
                        imagenesProds.add(image);
                    } catch (Exception e) {
                        imagenesProds.add(null);
                    }
                }
            }
        }


        repaint();
    }

    /**
     * Actualiza la lista de similitudes guardada.
     * @param sims La nueva lista de similitudes.
     */
    public void actualizarSimilitudesUsadas(ArrayList<Integer> sims) {
        similitudes = sims;
    }

    /**
     * Gestiona la primera parte de cambiar entre distintos tipos de display (menus),
     * preparación de datos y transición de salida del menú actual.
     */
    public void cambiarModo() {
        if (interactuable) {
            nextDisplay = displayMode == 0? 1 : 0;
            interactuable = false;
            info = menus.get(displayMode).getProductosSeleccionados();
            menus.get(displayMode).salir();
            menus.get(nextDisplay).preparar(info);
        }
    }

    public int getNextDisplay(){
        return nextDisplay;
    }

    /**
     * Gestiona la segunda parte de cambiar entre distintos tipos de display (menus), la transición
     * de entrada al nuevo menu y seteo del nuevo display.
     */
    public void terminarTransicion() {
        displayMode = nextDisplay;
        menus.get(nextDisplay).entrar();
    }

    /**
     * Devuelve las similitudes que el menú actual necesita para mostrarse correctamente.
     * @return Una lista de strings de dos en dos, cada dos strings es una similitud que se quiere saber entre esos dos productos.
     */
    public ArrayList<String> getSimilitudesNecesarias() {
        return menus.get(nextDisplay).getSimilitudesUsadas();
    }

    public void setInteractuable(boolean b) {interactuable = b; }

    public boolean getInteractuable() { return  interactuable; }

    /**
     * Le pregunta al menu activo que productos tiene seleccionados.
     * @return Conjunto de productos.
     */
    public ArrayList<String> getProductosSeleccionados() {
        return menus.get(displayMode).getProductosSeleccionados();
    }

    /**
     * Función que llama a desseleccionar del menú activo.
     */
    public void desseleccionar() {
        menus.get(displayMode).desseleccionar();
    }

}


