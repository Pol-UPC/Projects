package Font.presentation;

import java.awt.event.*;

/**
 * Clase que implementa MouseMotionListener, MouseListener y MouseWheelListener, solo reimplemnta todas sus funciones y envia datos
 * al Grafo Productos al que está asociada cuando se llama una de sus funciones.
 */
class MiMouseListener implements MouseMotionListener, MouseListener, MouseWheelListener {
    float originX = 0;
    float originY = 0;
    float lastDragX = 0;
    float lastDragY = 0;
    boolean dragging = false;

    GrafoProductos parent;

    public MiMouseListener(GrafoProductos gf) {
        parent = gf;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (dragging) {
            lastDragX = e.getX()-originX;
            lastDragY = e.getY() - originY;
            parent.dragCamara(lastDragX, lastDragY);
            originX = e.getX();
            originY = e.getY();

        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 1)
            parent.clicarCamara(e.getX(), e.getY());
        else if (e.getButton() == 3)
            parent.clicarCamaraDerecho(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 2) {
            dragging = true;
            originY = e.getY();
            originX = e.getX();

        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == 2) {
            parent.soltarCamara(lastDragX, lastDragY);
            lastDragX = 0;
            lastDragY = 0;
            dragging = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        parent.rueditaCamara(e.getWheelRotation());
    }
}