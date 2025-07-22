package Font.presentation;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Objects;

public class MenuGraficoEstanteria extends MenuGraficoGenerico {
    float vel = 0;
    float angle = 0;
    float desacc2 = 0.005f;
    float desacc = 0.0f;
    float minVel = 0.005f;
    Timer desaceleracionTimer;
    Timer seleccionSecundariaTimer;
    float selectorRotacion = 0;

    boolean saliendo = false;
    int semiseleccionadoX = -1;
    int semiseleccionadoY = -1;


    public MenuGraficoEstanteria (GrafoProductos p) {
        parent = p;
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
        desaceleracionTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                desacelerarCamara();
            }
        });
        seleccionSecundariaTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionSecundariaAnim();
            }
        });
    }

    /**
     * La desaceleración que hace la cámara cuando se suelta un drag con mucha velocidad.
     * Se llama a través de un timer.
     */
    private void desacelerarCamara () {
        desacc += desacc2;
        vel -= desacc2 * Math.signum(vel);
        camX += vel * Math.cos(angle);
        camY += vel * Math.sin(angle);

        if (vel < minVel && vel > -minVel) {
            stopDesaceleracion();
            moverCamaraAProducto(getProductoSeleccionadoIDX(),getProductoSeleccionadoIDY() );

        }
        parent.repaint();
    }

    private void seleccionSecundariaAnim() {

        selectorRotacion += 0.1f;
        if (selectorRotacion >= Math.PI*2) selectorRotacion -= (float) Math.PI*2;
        parent.repaint();
    }

    /**
     * Override del pintar generico, también dibuja todos los elementos gráficos de la estantería.
     * @param g Los gráficos sobre los que se tiene que pintar.
     * @param medida Tamaño del panel sobre el que se pintará.
     */
    @Override
    public void pintar(Graphics g, Dimension medida) {
        super.pintar(g,medida);
        limiteX = 10 * jump - jump;
        limiteY = parent.productosMat.size() * jump*3;
        Graphics2D graphic2d = (Graphics2D) g;


        int seleccionadoX = getProductoSeleccionadoIDX();
        int seleccionadoY = getProductoSeleccionadoIDY();

        float posY = - camY;
        float j = jump;
        for (int y = 0; y < parent.productosMat.size(); y++) {
            float posX = - camX;
            for (int x = 0; x < parent.productosMat.get(y).size(); x++) {

                centrarTexto(posX, posY, parent.productosMat.get(y).get(x), graphic2d, 1);

                BufferedImage image = parent.imagenesProds.get(x+y*10);
                if (parent.productosMat.get(y).get(x).equals("JoseMiguel")) {
                    image = imgEasterEgg;
                } else if(parent.imagenesProds.get(x+y*10) == null) {
                    image = imgDefault;
                }
                dibujarImagen(posX, posY - j, image, graphic2d);



                Color c = new Color(0);
                if ((x+y) % 2 == 0) c = new Color(0.2f, 0.2f, 0.2f);
                else c = new Color(0.5f, 0.5f, 0.5f);
                if (seleccionadoX == x && seleccionadoY == y) c = new Color(1.0f, 1.0f, 1.0f);

                float[] xs = {posX - j / 2, posX + j / 2, posX - j / 2};
                float[] ys = {posY + j / 2, posY + j / 2, posY + j / 2};
                float[] zs = {1.1f, 1.1f, 0.9f};
                crearPoligono(xs, ys, zs, c);

                xs = new float[]{posX - j / 2, posX + j / 2, posX + j / 2};
                ys = new float[]{posY + j / 2, posY + j / 2, posY + j / 2};
                zs = new float[]{0.9f, 0.9f, 1.1f};
                crearPoligono(xs, ys, zs, c);
                posX += j;
            }
            posY += j*3;
        }
        if (semiseleccionadoX != -1) {
            float x = semiseleccionadoX * jump - camX;
            float y = semiseleccionadoY * jump*3 - camY;
            float[] xs = {x,x-(0.1f*(float) Math.cos(selectorRotacion)),x+(0.1f*(float) Math.cos(selectorRotacion))};
            float[] ys = {y-jump-0.2f,y-jump-0.4f,y-jump-0.4f};
            float[] zs = {1,1-(0.1f*(float) Math.sin(selectorRotacion)),1+(0.1f*(float) Math.sin(selectorRotacion))};
            crearPoligono(xs,ys,zs,new Color(0.8f, 0f, 0.8f));

            float x2 = seleccionadoX * jump - camX;
            float y2 = seleccionadoY * jump*3 - camY;
            float[] xs2 = {x2,x2-(0.1f*(float) Math.cos(selectorRotacion)),x2+(0.1f*(float) Math.cos(selectorRotacion))};
            float[] ys2 = {y2-jump-0.2f,y2-jump-0.4f,y2-jump-0.4f};
            float[] zs2 = {1,1-(0.1f*(float) Math.sin(selectorRotacion)),1+(0.1f*(float) Math.sin(selectorRotacion))};
            crearPoligono(xs2,ys2,zs2,new Color(0.8f, 0f, 0.8f));
        }
        dibujarPoligonos(graphic2d);
    }

    /**
     * Para el timer de desaceleración y pone todos sus parametros a 0.
     * Usada por Desaceleración Camara.
     */
    private void stopDesaceleracion() {
        angle = 0;
        vel = 0;
        desacc = 0;
        desaceleracionTimer.stop();
    }

    /**
     * Override del mover cámara genérico añade un condicional en caso de que se esté haciendo una transición
     * de salida del menú.
     */
    @Override
    protected void animacionMoverCamara() {
        super.animacionMoverCamara();
        if (saliendo && tiempoMovimiento == 0) {
            saliendo = false;
            parent.terminarTransicion();
            parent.interactuable = false;
        }
    }

    /**
     * Override del Get Productos Seleccionados genérico, suele devolver un solo producto (el seleccionado
     * en blanco en el centro de la pantalla), pero puede devolver dos si se ha seleccionado otro con
     * click izquierdo.
     * @return Lista de nombres de productos seleccionados.
     */
    @Override
    public ArrayList<String> getProductosSeleccionados() {
        int indexX = getProductoSeleccionadoIDX();
        int indexY = getProductoSeleccionadoIDY();

        ArrayList<String> ret = new ArrayList<>();
        if (!parent.productosMat.isEmpty()) {
            ret.add(parent.productosMat.get(indexY).get(indexX));
            if (semiseleccionadoX != -1) ret.add(parent.productosMat.get(semiseleccionadoY).get(semiseleccionadoX));
        }
        return ret;
    }

    /**
     * Override de la función de desseleccionar generica, simplemente setea el elemento secundario
     * seleccionado a ninguno.
     */
    @Override
    public void desseleccionar() {
        semiseleccionadoX = -1;
        semiseleccionadoY = -1;
    }

    /**
     * Devuelve el indice del producto seleccionado en el centro de la pantalla.
     * @return Número de índice del producto en la lista de productos.
     */
    private Integer getProductoSeleccionadoIDX() {
        return getProductoSeleccionadoIDX(0);
    }

    private Integer getProductoSeleccionadoIDY() {
        return getProductoSeleccionadoIDY(0);
    }

    /**
     * Devuelve el indice del producto seleccionado en el centro de la pantalla más una posición x.
     * (Normalmente utilizado para cuando se clica un producto).
     * @return Número de índice del producto en la lista de productos.
     */
    private int getProductoSeleccionadoIDX(float offsetX) {
        return getProductoSeleccionadoIDX(offsetX,0);
    }

    private int getProductoSeleccionadoIDX(float offsetX, float offsetY) {
        int indexX = (int)((camX + offsetX + (jump/2)) / jump);
        if (!parent.productosMat.isEmpty()) {
            int temp = parent.productosMat.get(getProductoSeleccionadoIDY(offsetY)).size() - 1;
            if (indexX > temp) indexX = temp;
        }
        if (indexX < 0) indexX = 0;
        return indexX;
    }

    private int getProductoSeleccionadoIDY(float offsetY) {
        int indexY = (int)((camY + offsetY + jump)/(jump*3));
        if (indexY > parent.productosMat.size() - 1) indexY = parent.productosMat.size() - 1;
        if (indexY < 0) indexY = 0;
        return indexY;
    }

    /**
     * Override de la función Encontrar Producto genérica. Encuentra el producto y comienza a mover la cámara
     * hacia él.
     * @param s El nombre del producto que se quiere encontrar.
     */
    @Override
    public void encontrarProducto(String s) {
        int indexX = 0;
        int indexY = 0;
        for (int y = 0; y < parent.productosMat.size(); y++) {
            for (int x = 0; x < parent.productosMat.get(y).size(); x++) {
                if (parent.productosMat.get(y).get(x).equals(s)) {
                    indexX = x;
                    indexY = y;
                }
            }
        }
        moverCamaraAProducto(indexX,indexY);
    }

    /**
     * Si se arrastra el ratón, se mueve la cámara y se para cualquier desaceleración previa.
     * @param x Movimiento horizontal del ratón.
     * @param y Movimiento vertical del ratón.
     */
    @Override
    public void dragCamara(float x, float y) {
        camX -= screenToWindowX(x + medida.width/2f);
        camY -= screenToWindowY(y + medida.height/2f);
        stopDesaceleracion();
    }

    /**
     * Si se suelta la cámara con velocidad, se activa una desaceleración de la cámara.
     * @param x Movimiento horizontal del ratón.
     * @param y Movimiento vertical del ratón.
     */
    @Override
    public void soltarCamara(float x, float y) {
        if (x != 0 || y != 0) {
            x = screenToWindowX(x + medida.width / 2f);
            y = screenToWindowY(y + medida.height / 2f);
            vel = (float) Math.sqrt((x * x) + (y * y));
            angle = (float) Math.atan(y / x);

            if (x > 0) angle += (float) Math.PI;

            desaceleracionTimer.start();
        } else {
            stopDesaceleracion();
            moverCamaraAProducto(getProductoSeleccionadoIDX(),getProductoSeleccionadoIDY() );
        }
    }

    @Override
    public void pulsarTecla(int key) {
    }

    /**
     * Si se clica en un punto específico, se selecciona el producto más cercano.
     * @param x Posición X del ratón.
     * @param y Posición Y del ratón.
     */
    @Override
    public void clicarCamara(float x, float y) {
        x = screenToWindowX(x);
        y = screenToWindowY(y);
        moverCamaraAProducto(getProductoSeleccionadoIDX(x,y), getProductoSeleccionadoIDY(y));
    }

    /**
     * Si se clica con botón derecho en un punto específico, el producto más cercano se hace la selección
     * secundaria.
     * @param x Posición X del ratón.
     * @param y Posición Y del ratón.
     */
    @Override
    public void clicarCamaraDerecho(float x, float y) {
        x = screenToWindowX(x);
        y = screenToWindowY(y);
        x = getProductoSeleccionadoIDX(x);
        y = getProductoSeleccionadoIDY(y);
        if ((semiseleccionadoX == x  && semiseleccionadoY == y) || (getProductoSeleccionadoIDX() == x && getProductoSeleccionadoIDY() == y) ) {
            desseleccionarSecundario();
        } else {
            semiseleccionadoX = (int) x;
            semiseleccionadoY = (int) y;
            seleccionSecundariaTimer.start();
        }
    }

    private void desseleccionarSecundario() {
        semiseleccionadoY = -1;
        semiseleccionadoX = -1;
        seleccionSecundariaTimer.stop();
    }



    /**
     * Override de la función Salir genérica. Se para cualquier desaceleración y se mueve la cámara al
     * producto más cercano.
     */
    @Override
    public void salir() {
        stopDesaceleracion();
        moverCamaraAProducto(getProductoSeleccionadoIDX(),getProductoSeleccionadoIDY());
        semiseleccionadoX = -1;
        semiseleccionadoY = -1;
        saliendo = true;
    }

    /**
     * Función que prepara el timer y los datos necesarios para hacer una animación lineal moviendo la
     * cámara a un nuevo producto.
     * @param x Indice del producto al que se quiere mover.
     */
    private void moverCamaraAProducto(int x, int y) {
        if (true) {
            parent.setInteractuable(false);
            if (x < 0) x = 0;
            if (x > 9) x = 9;
            if (y < 0) y = 0;
            if (y > parent.productosMat.size() - 1) y = parent.productosMat.size()-1;

            origenX = camX;
            origenY = camY;
            objetivoY = y * jump*3;
            objetivoX = x * jump;
            movimientoTimer = new Timer(10, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    animacionMoverCamara();
                }
            });
            movimientoTimer.start();

        }
    }
}
