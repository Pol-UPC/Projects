package Font.presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Clase generica usada para cada submenú del Grafo Productos, gestiona sus datos, y como los muestra por
 * la pantalla. Tiene algunas funciones genericas implementadas, pero otras solo són implementadas por
 * sus subclases.
 */
public class MenuGraficoGenerico {
    GrafoProductos parent;

    float camX = 0;
    float camY = 0;
    float jump = 0.3f;

    float origenX = 0;
    float origenY = 0;
    float objetivoX = 0;
    float objetivoY = 0;
    float tiempoMovimiento = 0;
    float limiteX = 0;
    float limiteY = 0;

    float tiempoEntrada = -1;
    float tiempoSalida = -1;


    BufferedImage imgDefault = null;
    BufferedImage imgEasterEgg = null;

    Dimension medida;
    int medidaCamara = 235;

    Timer movimientoTimer;

    Timer entradaTimer;
    Timer salidaTimer;

    ArrayList<Float> poligonosPuntosX = new ArrayList<>();
    ArrayList<Float> poligonosPuntosY = new ArrayList<>();
    ArrayList<Float> poligonosPuntosZ = new ArrayList<>();
    ArrayList<Color> poligonosColor = new ArrayList<>();

    /**
     * Función usada para preparar la transición de entrada del nuevo submenú.
     */
    public void entrar() {
        parent.setInteractuable(true);
        parent.repaint();
    };

    /**
     * Función usada para recibir los datos necesarios de un nuevo submenú.
     * @param info
     */
    public void preparar(ArrayList<String> info) {

    }

    /**
     * Función usada para preparar la transición de salida del submenú actual.
     */
    public void salir() {
        parent.terminarTransicion();
    };

    /**
     * Generica que pinta toda la pantalla, la versión generica siempre se llama primero (prepara información
     * básica y pinta el color de fondo), y luego la reimplementación de cada submenú pinta por encima.
     * @param g Los gráficos sobre los que se tiene que pintar.
     * @param medida Tamaño del panel sobre el que se pintará.
     */
    public void pintar(Graphics g, Dimension medida) {
        this.medida = medida;

        if (tiempoEntrada == -1 && tiempoSalida == -1) {
            if (camX < -0.5f) camX = -0.5f;
            if (camY < -0.5f) camY = -0.5f;
            if (camX > limiteX + 0.5f) camX = limiteX + 0.5f;
            if (camY > limiteY + 0.5f) camY = limiteY + 0.5f;
        }
        Graphics2D graphic2d = (Graphics2D) g;
        graphic2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphic2d.setColor(new Color(0.8666666f,0.84705882352f,0.73725490196f));
        graphic2d.fillRect(0, 0, medida.width, medida.height);
    }

    public void pulsarTecla(int key) {}

    /**
     * Define el comportamiento al arrastrar el ratón.
     * @param x Movimiento horizontal del ratón.
     * @param y Movimiento vertical del ratón.
     */
    public void dragCamara(float x, float y) {}

    /**
     * Define el comportamiento al hacer click izquierdo.
     * @param x Posición X del ratón.
     * @param y Posición Y del ratón.
     */
    public void clicarCamara(float x, float y) {}

    /**
     * Define el comportamiento al hacer click derecho.
     * @param x Posición X del ratón.
     * @param y Posición Y del ratón.
     */
    public void clicarCamaraDerecho(float x, float y) {}

    /**
     * Define el como desseleccionar el elemento secundario.
     */
    public void desseleccionar() {}

    /**
     * Define el comportamiento al mover la ruedita.
     * @param mov Cantidad de movimiento.
     */
    public void rueditaCamara(float mov) {}

    /**
     * Define el comportamiento al soltar el drag.
     * @param x Movimiento horizontal del ratón.
     * @param y Movimiento vertical del ratón.
     */
    public void soltarCamara(float x, float y) {}

    /**
     * Define como encontrar un producto en el submenú actual.
     * @param s El nombre del producto que se quiere encontrar.
     */
    public void encontrarProducto(String s) {}



    /**
     * Define que responder cuando se le pide al submenú actual que similitudes necesitará.
     */
    public ArrayList<String> getSimilitudesUsadas() { return new ArrayList<>();}



    /**
     * Define que devolverá cuando se le piden que productos están seleccionados.
     * @return Lista de nombres de productos seleccionados.
     */
    public ArrayList<String> getProductosSeleccionados() {
        return new ArrayList<String>();
    }

    /**
     * El movimento que hará la cámara aunque no esté siendo movida manualmente por una animación.
     */
    protected void animacionMoverCamara() {
        float tiempoEsperado = 5;
        camX = origenX + (objetivoX - origenX) * (tiempoMovimiento/tiempoEsperado);
        camY = origenY + (objetivoY - origenY) * (tiempoMovimiento/tiempoEsperado);

        tiempoMovimiento ++;
        if (tiempoMovimiento > tiempoEsperado) {
            tiempoMovimiento = 0;
            parent.setInteractuable(true);
            movimientoTimer.stop();
        }

        parent.repaint();
    }

    /**
     * Dibujar todos los poligonos guardados en las listas de poligonos en un Graphics2D y resetea
     * la lista.
     * @param g Graphics2D sobre el que se dibujarán los poligonos.
     */
    protected void dibujarPoligonos(Graphics2D g) {
        float ratio = (float)Math.min(medida.height,medida.width)/medidaCamara;
        for (int i = 0; i < poligonosPuntosX.size(); i+=3) {
            int[] xs = new int[3];
            int[] ys = new int[3];
            for (int j = 0; j < 3; j++) {
                float zdivide = poligonosPuntosZ.get(i+j) + ratio - 1;
                xs[j] = (int) windowToScreenX((poligonosPuntosX.get(i+j) * ratio)/zdivide);
                ys[j] = (int) windowToScreenY((poligonosPuntosY.get(i+j) * ratio)/zdivide);
            }

            g.setColor(poligonosColor.get(i/3));
            g.fillPolygon(xs,ys,3);

        }
        poligonosPuntosX = new ArrayList<>();
        poligonosPuntosY = new ArrayList<>();
        poligonosPuntosZ = new ArrayList<>();
        poligonosColor = new ArrayList<>();
    }

    /**
     * Crea un nuevo triangulo que guarda para pintar a futuro.
     * @param xs Posiciones x de cada vertice.
     * @param ys Posiciones y de cada vertice.
     * @param zs Posiciones z de cada vertice.
     * @param c Color del triangulo.
     */
    protected void crearPoligono(float[] xs,float[] ys,float[] zs,Color c) {
        for (int i = 0; i < 3; i++) {
            poligonosPuntosX.add(xs[i]);
            poligonosPuntosY.add(ys[i]);
            poligonosPuntosZ.add(zs[i]);
        }
        poligonosColor.add(c);
    }

    /**
     * Dibuja un texto en el elemento gráfico con una alineación particular.
     * @param xrel X del texto relativo al espacio de cámara.
     * @param yrel Y del texto relativo al espacio de cámara.
     * @param s El texto que se querrá poner.
     * @param g Graphics2D sobre el que se dibujará.
     * @param alineacion La alineación puede ser: 0, izquierda, 1, centrado o 2, derecha.
     */
    protected void centrarTexto(float xrel, float yrel, String s, Graphics2D g, int alineacion) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("TimesRoman", Font.PLAIN, (int) ((15 * medidaCamara) / 235f)));
        if (s.length() > 10) {
            s = s.substring(0,7);
            s = s+"...";
        }

        int x = (int) windowToScreenX(xrel);
        int y = (int) windowToScreenY(yrel);

        Dimension ret = new Dimension();
        Rectangle2D r2D = g.getFontMetrics().getStringBounds(s,g);
        int rWidth = (int) Math.round(r2D.getWidth());
        int rHeight = (int) Math.round(r2D.getHeight());

        if (alineacion == 0) {
            ret.width =  x;
        } else if (alineacion == 1) {
            ret.width =  x - (rWidth / 2);
        } else if (alineacion == 2) {
            ret.width =  x - (rWidth);
        }
        ret.height =  y + (rHeight / 2);
        g.drawString(s, ret.width, ret.height);
    }

    /**
     * Dibuja una imagen en el elemento gráfico.
     * @param xrel X de la imagen relativo al espacio de cámara.
     * @param yrel Y de la imagen relativo al espacio de cámara.
     * @param img La imagen que se quiere dibujar.
     * @param g Graphics2D sobre el que se dibujará la imagen.
     */
    protected void dibujarImagen(float xrel, float yrel, Image img, Graphics2D g) {

        float scale = medidaCamara/235f;
        g.drawImage(img,(int)(windowToScreenX(xrel)-25*scale),(int)(windowToScreenY(yrel) - 25*scale),(int) (50*scale),(int) (50*scale), null);
    }

    /**
     * Función generica que pasa una posición X del espacio de pantalla a espacio de cámara.
     * @param x Antigua X
     * @return Nueva X
     */
    protected float screenToWindowX(float x) {
        x /= medida.width;
        x -= 0.5f;
        x *= medida.width/(float) medidaCamara;
        return x;
    }

    /**
     * Función generica que pasa una posición Y del espacio de pantalla a espacio de cámara.
     * @param y Antigua Y
     * @return Nueva Y
     */
    protected float screenToWindowY(float y) {
        y /= medida.height;
        y -= 0.5f;
        y *= medida.height/(float) medidaCamara;
        return y;
    }

    /**
     * Función generica que pasa una posición X del espacio de cámara a espacio de pantalla.
     * @param x Antigua X
     * @return Nueva X
     */
    protected float windowToScreenX(float x) {
        x = (x*medidaCamara) + medida.width/2f;
        return x;
    }

    /**
     * Función generica que pasa una posición Y del espacio de cámara a espacio de pantalla.
     * @param y Antigua Y
     * @return Nueva Y
     */
    protected float windowToScreenY(float y) {
        y = (y*medidaCamara) + medida.height/2f;
        return y;
    }

}

