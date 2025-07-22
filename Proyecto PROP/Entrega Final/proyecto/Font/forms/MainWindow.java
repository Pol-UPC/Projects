package Font.forms;

import Font.presentation.GrafoProductos;
import Font.presentation.MenuBarra;
import Font.presentation.RoundButtons;
import Font.presentation.VentanaPrincipal;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Esta clase junto a su form muestra una de las posibles vistas principales. La ventana principal que
 * es usada una vez se ha entrado en un usuario, con esta se puede gestionar la configuración más general
 * o configurar y analizar una estantería.
 */
public class MainWindow {
    private JPanel Panel4_Botones;
    private JPanel panel1;
    private JButton crearProductoButton;
    private JPanel panelGrafo;
    private JButton seleccionarProductoButton;
    private JButton verSimilitudesButton;
    private JButton cambiarItemButton;
    private JButton menuButton;
    private JPanel panel2;
    private JPanel panelsuperior;
    private JPanel Panel1_Botones;
    private JPanel Panel2_Botones;
    private JPanel Panel3_Botones;
    private RoundButtons Borr_producto;
    private RoundButtons Cre_producto;
    private RoundButtons Edi_producto;
    private RoundButtons Enc_producto;
    private RoundButtons Int_producto;
    private RoundButtons Ord_estanteria;
    private RoundButtons Ver_similitud;

    protected MenuBarra miMenuBarra;
    protected GrafoProductos gf;
    protected VentanaPrincipal parent;


    ArrayList<String> nombresProductos = new ArrayList<>();

    public MainWindow() {

    }

    public MainWindow(VentanaPrincipal parent) {
        //Inicializaciones;
        this.parent = parent;
        miMenuBarra = new MenuBarra(this);
        gf = new GrafoProductos();
        panelGrafo.add(gf);
        gf.setVisible(true);
        actualizarGrafo();


        //Crear e insertar Botones

        Cre_producto = new RoundButtons("Crear Producto");
        Panel1_Botones.add(Cre_producto);

        Borr_producto = new RoundButtons("Borrar Producto");
        Panel1_Botones.add(Borr_producto);

        Edi_producto = new RoundButtons("Editar Producto");
        Panel2_Botones.add(Edi_producto);

        Int_producto = new RoundButtons("Intercambiar Productos");
        Panel2_Botones.add(Int_producto);

        Enc_producto = new RoundButtons("Encontrar Producto");
        Panel2_Botones.add(Enc_producto);

        Ord_estanteria = new RoundButtons("Ordenar Estanteria");
        Panel3_Botones.add(Ord_estanteria);

        Ver_similitud = new RoundButtons("Ver Similitudes");
        Panel4_Botones.add(Ver_similitud);


        ImageIcon hamIcon = new ImageIcon("Font/presentation/image_src/menu hamburguesa.png");
        menuButton.setIcon(hamIcon);
        ImageIcon logoIcon = new ImageIcon("Font/presentation/image_src/carritoIcon.png");
        Image logoImage = logoIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Redimensionar la imagen
        logoIcon = new ImageIcon(logoImage);
        JLabel logoLabel = new JLabel(logoIcon);
        panel2.add(logoLabel, Component.LEFT_ALIGNMENT);
        //Añadir textos
        logoLabel = new JLabel("Market Planner");
        panel2.add(logoLabel);

        menuButton.setSize(512, 512);


        Cre_producto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.actionCrearProducto();
                actualizarGrafo();
            }
        });

        Borr_producto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gf.getProductosSeleccionados().isEmpty()) return;
                if (gf.getNextDisplay() == 0) {
                    parent.actionBorrarProducto(gf.getProductosSeleccionados().get(0));
                    actualizarGrafo();
                } else if (gf.getNextDisplay() == 1) {
                    if (gf.getProductosSeleccionados().size() > 1) {
                        parent.actionBorrarProducto(gf.getProductosSeleccionados().get(1));
                        actualizarGrafo();
                    }
                }
            }
        });

        Ver_similitud.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gf.getNextDisplay() == 0 && parent.actionGetProductos().isEmpty()) return;
                gf.cambiarModo();
                actualizarGrafo();


                if (gf.getNextDisplay() == 0) {
                    Ver_similitud.setText("Ver Similitudes");
                    Edi_producto.setText("Editar Producto");
                } else {
                    Ver_similitud.setText("Ver Estanteria");
                    Edi_producto.setText("Editar Similitud");
                }
            }
        });
        Edi_producto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gf.getNextDisplay() == 0) {
                    parent.actionAbrirVentanaSecundaria(gf.getProductosSeleccionados(), 0);
                } else {
                    parent.actionAbrirVentanaSecundaria(gf.getProductosSeleccionados(), 1);
                }
            }
        });
        Enc_producto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = JOptionPane.showInputDialog("¿Cual es el nombre del producto?");
                gf.encontrarProducto(s);
            }
        });

        Ord_estanteria.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gf.getNextDisplay() == 1) {
                    gf.cambiarModo();
                    actualizarGrafo();
                    Ver_similitud.setText("Ver Estanteria");
                    Edi_producto.setText("Editar Similitud");
                }
                parent.actionOrdenarEstanteria();
                actualizarGrafo();
            }
        });

        Int_producto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> prods = gf.getProductosSeleccionados();
                if (gf.getNextDisplay() == 0 && !prods.isEmpty() && prods.size() >= 2) {
                    parent.actionIntercambiarProductos(prods.get(0), prods.get(1));
                    gf.desseleccionar();
                    actualizarGrafo();
                }
            }
        });
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu temp = miMenuBarra.getMenu();

                temp.show(menuButton, 0, 0);
            }
        });
    }


    /**
     * Función general usada para actualizar los datos del panel GrafoProductos.
     * Usada cada vez que se cambia algo en la capa de dominio (como un producto) o
     * se cambia a otro tipo de display.
     */
    public void actualizarGrafo() {
        ArrayList<String> prods = parent.actionGetProductos();
        ArrayList<String> imgs = new ArrayList<>();
        for (String p : prods) {
            imgs.add(parent.actionGetImagen(p));
        }
        gf.actualizarProductos(parent.actionGetProductos(), imgs);
        ArrayList<Integer> sims = new ArrayList<>();
        ArrayList<String> combs = gf.getSimilitudesNecesarias();
        for (int i = 0; i < combs.size(); i += 2) {
            sims.add(parent.actionGetSimilitud(combs.get(i), combs.get(i + 1)));
        }

        gf.actualizarSimilitudesUsadas(sims);
    }

    public JPanel getPanel() {
        return panel1;
    }

    public void salir() {
        parent.actionResetearEstanteria();
        parent.actionEntrarMenuInicio();
    }


    public void cambiarContra(String NewPassword, String OldPassword) {
        parent.cambiarContra(NewPassword, OldPassword);
    }

    public boolean EliminarUsuario(String Password) {
        return parent.EliminarUsuario(Password);
    }

    public void guardarEstado(String archivo, String usuario) {
        parent.actionGuardarEstado(archivo, usuario);
    }

    public void cargarEstado(String archivo, String usuario) {
        parent.actionCargarEstado(archivo, usuario);
        parent.actualizarVisuales();
    }

    public Object[] getusers() {
        return parent.getUsers();
    }

    public Object[] getFicheros(Object usuarioSelecionado, String objetivo) {
        return parent.getFicheros(usuarioSelecionado, objetivo);
    }

    public String getRol() {
        return parent.getRol();
    }

    public String getUsuario() {
        return parent.getUsuario();
    }

    public void cargarProductos(String usuario, String archivo) {
        parent.cargarProductos(usuario, archivo);
    }

    public void guardarProductos(String archivo, String usuario) {
        parent.guardarProductos(archivo, usuario);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        panel1.setForeground(new Color(-467261));
        panelGrafo = new JPanel();
        panelGrafo.setLayout(new BorderLayout(0, 0));
        panelGrafo.setBackground(new Color(-2238276));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panelGrafo, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.setBackground(new Color(-2238276));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel3, gbc);
        Panel1_Botones = new JPanel();
        Panel1_Botones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        Panel1_Botones.setBackground(new Color(-2238276));
        panel3.add(Panel1_Botones, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        Panel4_Botones = new JPanel();
        Panel4_Botones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        Panel4_Botones.setBackground(new Color(-2238276));
        panel3.add(Panel4_Botones, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        Panel2_Botones = new JPanel();
        Panel2_Botones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        Panel2_Botones.setBackground(new Color(-2238276));
        panel3.add(Panel2_Botones, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        Panel3_Botones = new JPanel();
        Panel3_Botones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        Panel3_Botones.setBackground(new Color(-2238276));
        panel3.add(Panel3_Botones, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelsuperior = new JPanel();
        panelsuperior.setLayout(new BorderLayout(0, 0));
        panelsuperior.setBackground(new Color(-4869991));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panelsuperior, gbc);
        menuButton = new JButton();
        menuButton.setBackground(new Color(-4869991));
        menuButton.setForeground(new Color(-467261));
        menuButton.setText("");
        panelsuperior.add(menuButton, BorderLayout.EAST);
        panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel2.setBackground(new Color(-4869991));
        panelsuperior.add(panel2, BorderLayout.WEST);
        final Spacer spacer1 = new Spacer();
        panelsuperior.add(spacer1, BorderLayout.CENTER);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
