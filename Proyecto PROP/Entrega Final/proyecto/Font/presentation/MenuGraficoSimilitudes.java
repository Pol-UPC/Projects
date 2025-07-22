package Font.presentation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MenuGraficoSimilitudes extends MenuGraficoGenerico {
    String productoPrincipal;

    Timer flechitaTimer;
    float selectorRotacion = 0;


    public MenuGraficoSimilitudes (GrafoProductos p) {
        try {
            ImageIcon iconoDefault = new ImageIcon("Font/presentation/image_src/cesta.png");
            imgDefault = new BufferedImage(
                    iconoDefault.getIconWidth(),
                    iconoDefault.getIconHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics g = imgDefault.createGraphics();
            iconoDefault.paintIcon(null, g, 0,0);
            g.dispose();
            ImageIcon iconoEgg = new ImageIcon("Font/presentation/image_src/Jose.png");
            imgEasterEgg = new BufferedImage(
                    iconoEgg.getIconWidth(),
                    iconoEgg.getIconHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            g = imgEasterEgg.createGraphics();
            iconoEgg.paintIcon(null, g, 0,0);
            g.dispose();
        } catch (Exception e) {
        }
        flechitaTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotarFlechita();
            }
        });
        flechitaTimer.start();
        parent = p;
    }

    private void rotarFlechita() {
        selectorRotacion += 0.1f;
        if (selectorRotacion >= Math.PI*2) selectorRotacion -= (float) Math.PI*2;
        parent.repaint();
    }

    /**
     * Override de la función Preparar genérica. Recibe la primera pieza
     * de información de la lista y la usa como producto seleccionado.
     * @param info
     */
    @Override
    public void preparar(ArrayList<String> info) {
        productoPrincipal = info.getFirst();
    }

    /**
     * Override de la función Entrar genérica. Prepara la transición de entrada y su timer asociado.
     */
    @Override
    public void entrar() {
        camX = 0.5f;
        camY = -1;
        flechitaTimer.start();
        entradaTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animacionEntrar();
            }
        });
        entradaTimer.start();
        animacionEntrar();
    }

    /**
     * Override de la función Salir genérica. Prepara la transición de salida y su timer asociado.
     */
    @Override
    public void salir() {
        flechitaTimer.stop();
        salidaTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animacionSalir();
            }
        });
        salidaTimer.start();
    }

    /**
     * Gestiona la animación de entrada.
     */
    private void animacionEntrar() {
        tiempoEntrada++;
        camY = -1 + (tiempoEntrada/10);
        parent.repaint();
        if (tiempoEntrada >= 10) {
            parent.setInteractuable(true);
            tiempoEntrada = -1;
            entradaTimer.stop();
        }
    }

    /**
     * Gestiona la animación de salida.
     */
    private void animacionSalir() {
        tiempoSalida++;
        camX = 0.25f + (tiempoSalida/2);
        parent.repaint();
        if (tiempoSalida >= 10) {
            parent.terminarTransicion();
            tiempoSalida = -1;
            salidaTimer.stop();
        }
    }

    /**
     * Override de la función Encontrar Producto genérica. Comprueba que el producto exista y mueve
     * prepara la animación apra mover la cámara a ese producto.
     * @param s El nombre del producto que se quiere encontrar.
     */
    @Override
    public void encontrarProducto(String s) {
        if (s != productoPrincipal && parent.productosList.contains(s)) {
            int y = parent.productosList.indexOf(s);
            if (y > parent.productosList.indexOf(productoPrincipal)) y --;
            moverCamaraAProducto(y);
        }
    }

    /**
     * Si se mueve la ruedita, se mueve la cámara verticalmente.
     * @param mov Cantidad de movimiento.
     */
    @Override
    public void rueditaCamara(float mov) {
        camY += mov/5;
        parent.repaint();
    }

    /**
     * Override de la función Pintar genérica. A la derecha pinta el nombre y gráficos del
     * producto principal seleccionado. A la izquierda una lista en vertical del resto de productos y
     * sus similitudes con el producto seleccionado.
     * @param g Los gráficos sobre los que se tiene que pintar.
     * @param medida Tamaño del panel sobre el que se pintará.
     */
    @Override
    public void pintar(Graphics g, Dimension medida) {
        super.pintar(g,medida);

        limiteY = parent.productosList.size() * jump - jump;
        Graphics2D graphic2d = (Graphics2D) g;

        BufferedImage image = parent.imagenesProds.get(parent.productosList.indexOf(productoPrincipal));
        if (productoPrincipal.equals("JoseMiguel")) {
            image = imgEasterEgg;
        } else if(parent.imagenesProds.get(parent.productosList.indexOf(productoPrincipal)) == null) {
            image = imgDefault;
        }

        if (tiempoEntrada != -1) {
            float x = (tiempoEntrada * (0.5f/10));
            crearBaseProductoPrincipal(x);
            dibujarImagen(x,-jump,image,graphic2d);
           centrarTexto(x, 0, productoPrincipal, graphic2d, 1);
        } else if (tiempoSalida != -1) {
            float x = (0.5f - tiempoSalida * (0.5f/10));
            crearBaseProductoPrincipal(x);
            dibujarImagen(x,-jump,image,graphic2d);
            centrarTexto(x, 0, productoPrincipal, graphic2d, 1);
        } else {
            if (camY < 0) camY = 0;
            if (camY > limiteY) camY = limiteY;
            float px = 0.5f;
            crearBaseProductoPrincipal(px);
            dibujarImagen(px,-jump,image,graphic2d);
            centrarTexto(0.5f, 0f, productoPrincipal, graphic2d, 1);
            float x = -0.8f;
            float y = 0;
            float[] xs = {x-0.1f,x-0.2f,x-0.2f};
            float[] ys = {y,y -(0.05f*(float) Math.cos(selectorRotacion)),y +(0.05f*(float) Math.cos(selectorRotacion))};
            float[] zs = {1,1-(0.05f*(float) Math.sin(selectorRotacion)),1+(0.05f*(float) Math.sin(selectorRotacion))};
            crearPoligono(xs,ys,zs,new Color(0.8f, 0f, 0.8f));
        }

        dibujarColumna(graphic2d);
        dibujarPoligonos(graphic2d);
    }

    /**
     * Prepara los poligonos debajo del producto principal.
     * @param x La x donde se dibujará.
     */
    private void crearBaseProductoPrincipal(float x) {
        Color c = new Color(1.0f,1.0f,1.0f);
        float[] xs = {x-jump/2, x+jump/2, x-jump/2};
        float[] ys = {jump/2,jump/2,jump/2};
        float[] zs = {1.1f,1.1f,0.9f};
        crearPoligono(xs,ys,zs,c);
        xs = new float[]{x-jump/2,x+jump/2,x+jump/2};
        ys = new float[]{jump/2,jump/2,jump/2};
        zs = new float[]{0.9f,0.9f,1.1f};
        crearPoligono(xs,ys,zs,c);
    }

    /**
     * Dibuja todos los nombres de los productos que no són el seleccionado principal, y sus similitudes
     * con este.
     * @param g Graphics2D sobre el que se dibujará.
     */
    private void dibujarColumna(Graphics2D g) {
        float posY = - camY;
        int i2 = 0;
        float posX = - camX;
        float j = jump;
        for (String s : parent.productosList) {
            if (!s.equals(productoPrincipal)) {
                centrarTexto(posX, posY, s, g, 2);
                if (!parent.similitudes.isEmpty())
                    centrarTexto(posX + 0.05f, posY, parent.similitudes.get(i2).toString(), g, 0);
                posY += j;
                i2++;
            }
        }
    }

    /**
     * Si se dragea el ratón, la columna de productos se moverá (no hay movimiento horizontal).
     * @param x Movimiento horizontal del ratón.
     * @param y Movimiento vertical del ratón.
     */
    @Override
    public void dragCamara(float x, float y) {
        y /= medida.height;
        camY -= y;
    }

    /**
     * Si se clica con el botón izquierdo en un punto específico, se seleccionará el producto de la columna
     * más cercano.
     * @param x Posición X del ratón.
     * @param y Posición Y del ratón.
     */
    @Override
    public void clicarCamara(float x, float y) {
        y = screenToWindowY(y);
        y = getProductoSeleccionadoID(y);
        if (y > parent.productosList.indexOf(productoPrincipal)) y --;
        moverCamaraAProducto((int) y);
    }



    /**
     * Override de la función genérica Get Similitudes Usadas, devuelve todas las similitudes que necesitará el
     * submenú. En particular siempre necesita las similitudes entre el producto seleccionado principal y el resto
     * de productos.
     * @return
     */
    @Override
    public ArrayList<String> getSimilitudesUsadas() {
        ArrayList<String> ret = new ArrayList<>();
        for (String p: parent.productosList) {
            if (!p.equals(productoPrincipal)) {
                ret.add(productoPrincipal);
                ret.add(p);
            }
        }
        return ret;
    }

    /**
     * Función que prepara el timer y los datos necesarios para hacer una animación lineal moviendo la
     * cámara a un nuevo producto (solo verticalmente).
     * @param y Indice del producto al que se quiere mover.
     */
    private void moverCamaraAProducto(int y) {
        if (true) {
            parent.setInteractuable(false);

            origenX = camX;
            origenY = camY;
            objetivoY = y * jump;
            objetivoX = camX;
            movimientoTimer = new Timer(10, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    animacionMoverCamara();
                }
            });
            movimientoTimer.start();

        }
    }

    /**
     * Devuelve el indice del producto seleccionado en el centro de la pantalla.
     * @return Número de índice del producto en la lista de productos.
     */
    private int getProductoSeleccionadoID() {
        return getProductoSeleccionadoID(0);
    }

    /**
     * Devuelve el indice del producto seleccionado en el centro de la pantalla más una posición x.
     * (Normalmente utilizado para cuando se clica un producto).
     * @return Número de índice del producto en la lista de productos.
     */
    private int getProductoSeleccionadoID(float offsetY) {
        float index = (camY +offsetY+ (jump/2)) / jump;
        if (index < 0) index = 0;
        if (index > parent.productosList.size() - 2) index = parent.productosList.size() - 2;
        if (index >= parent.productosList.indexOf(productoPrincipal)) index++;
        return (int) index;
    }

    /**
     * Override del Get Productos Seleccionados genérico, devuelve siempre dos productos, el principal y el que está
     * en el centro de la columna (secundario).
     * @return Lista de nombres de productos seleccionados.
     */
    @Override
    public ArrayList<String> getProductosSeleccionados() {
        int index = getProductoSeleccionadoID();

        ArrayList<String> ret = new ArrayList<>();
        if (!parent.productosList.isEmpty()) ret.add(productoPrincipal);
        if (parent.productosList.size() > 1) ret.add(parent.productosList.get(index));
        return ret;
    }
}
