package Font.presentation;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import java.awt.*;
import javax.swing.*;

/**
 * Clase personalizada que extiende `JButton` para crear botones con bordes redondeados
 *
 *
 */
public class RoundButtons extends JButton {

    private Color color1 = new Color(0x8E8B78);  // Color marrón claro: #8F745C
    private Color color2 = new Color(0x46443A);  // Color marrón más oscuro: #755B44
    private Color color3 = new Color(0x46443A);
    /**
     * Constructor que inicializa un botón con el texto proporcionado.
     *
     * @param text El texto que se mostrará en el botón.
     */
    public RoundButtons(String text) {
        super(text);
        initializeButton();
    }
    /**
     * Metodo privado que configura las propiedades básicas del botón.
     * Esto incluye hacer el fondo transparente y deshabilitar el borde y el enfoque visual.
     */
    private void initializeButton() {
        setOpaque(false);
        setContentAreaFilled(false);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
    }
    /**
     * Sobrescribe el metodo `paintComponent` para personalizar la apariencia del botón.
     * Dibuja el botón con bordes redondeados y un gradiente dinámico que cambia dependiendo
     * del estado del botón (presionado, habilitado, deshabilitado).
     *
     * @param g El objeto `Graphics` usado para dibujar el componente.
     */
    @Override
    protected void paintComponent(Graphics g) {
        Color c1, c2, c3;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ButtonModel m = getModel();

        Paint oldPaint = g2.getPaint();
        if (m.isArmed()) {
            c2 = color1.darker();
            c1 = color2.darker();
            c3 = color3;
        } else {
            c1 = color1.darker();
            c2 = color2.darker();
            c3 = color3.brighter();
        }
        if (!m.isEnabled()) {
            c2 = color1.brighter();
            c1 = color2.brighter();
            c3 = color3.darker();
        }
        RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight() - 1, 20, 20);
        g2.clip(r2d);
        g2.setPaint(new GradientPaint(0.0f, 0.0f, c1, 0.0f, getHeight(), c2));
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setStroke(new BasicStroke(4f));
        g2.setPaint(new GradientPaint(0.0f, 0.0f, c3, 0.0f, getHeight(), c3));
        g2.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 18, 18);

        g2.setPaint(oldPaint);
        super.paintComponent(g);
    }
    /**
     * Obtiene el primer color usado para el fondo del botón.
     *
     * @return El primer color.
     */
    public Color getColor1() {
        return color1;
    }
    /**
     * Establece el primer color usado para el fondo del botón.
     *
     * @param color1 El nuevo color para el fondo.
     */
    public void setColor1(Color color1) {
        this.color1 = color1;
    }
    /**
     * Obtiene el segundo color usado para el fondo del botón.
     *
     * @return El segundo color.
     */
    public Color getColor2() {
        return color2;
    }
    /**
     * Establece el segundo color usado para el fondo del botón.
     *
     * @param color2 El nuevo color para el fondo.
     */
    public void setColor2(Color color2) {
        this.color2 = color2;
    }
    /**
     * Obtiene el tercer color utilizado para el borde del botón.
     *
     * @return El tercer color.
     */
    public Color getColor3() {
        return color3;
    }
    /**
     * Establece el tercer color utilizado para el borde del botón.
     *
     * @param color3 El nuevo color para el borde.
     */
    public void setColor3(Color color3) {
        this.color3 = color3;
    }

    /**
     * Sobrescribe el metodo `getPreferredSize` para establecer el tamaño preferido del botón
     * en función del texto que contiene, añadiendo espacio adicional para los bordes.
     *
     * @return El tamaño preferido del botón.
     */
    @Override
    public Dimension getPreferredSize() {
        // Obtener el tamaño del texto
        FontMetrics fm = getFontMetrics(getFont());
        int width = fm.stringWidth(getText()) + 40; // Un espacio adicional para los bordes
        int height = fm.getHeight() + 20; // Espacio adicional para bordes

        // Retornar el tamaño preferido
        return new Dimension(width, height);
    }
}
