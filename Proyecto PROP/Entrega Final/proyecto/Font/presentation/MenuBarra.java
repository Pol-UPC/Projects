package Font.presentation;

import Font.forms.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Clase que gestiona el menú contextual (JPopupMenu) que se muestra en la ventana principal (MainWindow) y
 * la ventana para empleados (MainWindowEmpleado), permitiendo ejecutar diversas acciones basadas en el rol de usuario.
 */
public class MenuBarra {
    JPopupMenu menu;
    MainWindow parent;
    /**
     * Constructor de la clase `MenuBarra`, que crea un menú contextual con opciones diferentes según el rol del usuario.
     *
     * @param parent La ventana principal (MainWindow) que contiene este menú contextual.
     */
    public MenuBarra(MainWindow parent) {
        this.parent = parent;
        menu = new JPopupMenu();

        //Gestor
        JMenuItem itemCargarEstadoGestor= new JMenuItem("Cargar estado");
        JMenuItem itemGuardarEstadoGestor= new JMenuItem("Guardar estado");
        JMenuItem itemCargarProductosGestor= new JMenuItem("Cargar productos");
        JMenuItem itemGuardarProductosGestor = new JMenuItem("Guardar Productos");

        //Empleado
        JMenuItem itemCargarEstadoEmpleado= new JMenuItem("Cargar estado");

        //Ambos
        JMenuItem itemCambiarContra= new JMenuItem("Cambiar contraseña");
        JMenuItem itemBajaUsuario =  new JMenuItem("Dar de baja mi usuario");
        JMenuItem itemSalirEstanteria = new JMenuItem("Salir");

        if(parent.getRol().equals("GESTOR")) {
            menu.add(itemCargarEstadoGestor);
            menu.add(itemGuardarEstadoGestor);
            menu.add(itemCargarProductosGestor);
            menu.add(itemGuardarProductosGestor);
            menu.add(itemCambiarContra);
            menu.add(itemBajaUsuario);
            menu.add(itemSalirEstanteria);

            itemCargarEstadoGestor.addActionListener(e -> cargarEstadoGestor());
            itemGuardarEstadoGestor.addActionListener(e -> guardarEstadoGestor());
            itemCambiarContra.addActionListener(e -> cambiarContra());
            itemBajaUsuario.addActionListener(e -> BajaUsuario());
            itemSalirEstanteria.addActionListener(e -> parent.salir());
            itemCargarProductosGestor.addActionListener(e -> cargarProductosGestor());
            itemGuardarProductosGestor.addActionListener(e -> guardarProductosGestor());

        }
        else{
            menu.add(itemCargarEstadoEmpleado);
            menu.add(itemCambiarContra);
            menu.add(itemBajaUsuario);
            menu.add(itemSalirEstanteria);
            itemCargarEstadoEmpleado.addActionListener(e -> cargarEstadoEmpleado());
            itemCambiarContra.addActionListener(e -> cambiarContra());
            itemBajaUsuario.addActionListener(e -> BajaUsuario());
            itemSalirEstanteria.addActionListener(e -> parent.salir());
        }
    }
    /**
     * Metodo para guardar un grupo de productos a usuario seleccionado por el gestor.
     * El gestor selecciona un archivo de destino y el usuario al que le serán guardados los productos.
     */
    private void guardarProductosGestor() {
        //Selecion de usuarios
        Object usuarioSelecionado = JOptionPane.showInputDialog(
                null,
                "Seleccione un usuario al que guardar los productos",
                "Seleccion de Usuarios",
                JOptionPane.QUESTION_MESSAGE,
                null,
                parent.getusers(),"seleccione");

        String archivo = JOptionPane.showInputDialog("Escoja el nombre del archivo");
        parent.guardarProductos(archivo,usuarioSelecionado.toString());
    }
    /**
     * Metodo para cargar un grupo de productos a usuario seleccionado por el gestor desde un archivo especifico.
     *
     */
    private void cargarProductosGestor() {
        //Selecion de usuarios
        Object usuarioSelecionado = JOptionPane.showInputDialog(
                null,
                "Seleccione un usuario del cual cargar los productos",
                "Seleccion de Usuarios",
                JOptionPane.QUESTION_MESSAGE,
                null,
                parent.getusers(),"seleccione");
        //Selecion de archivos
        Object seleciondearchivos = JOptionPane.showInputDialog(
                null,
                "Seleccione un grupo de productos",
                "Seleccion de estanteria",
                JOptionPane.QUESTION_MESSAGE,
                null,
                parent.getFicheros(usuarioSelecionado,"PROD"),"seleccione");
        if (seleciondearchivos != null) {
            parent.cargarProductos(usuarioSelecionado.toString(), seleciondearchivos.toString());
        }
    }
    /**
     * Metodo para cargar el estado guardado de una estanteria.
     * Este metodo perimite cargar distribuciones de estanterias guardadas en cualquier carpeta de usuario
     */
    private void cargarEstadoGestor() {
        //Selecion de usuarios
        Object usuarioSelecionado = JOptionPane.showInputDialog(
                null,
                "Seleccione un usuario del cual cargar la estanteria",
                "Seleccion de Usuarios",
                JOptionPane.QUESTION_MESSAGE,
                null,
                parent.getusers(),"seleccione");
        //Selecion de archivos
        Object seleciondearchivos = JOptionPane.showInputDialog(
                null,
                "Seleccione una estanteria a cargar",
                "Seleccion de estanteria",
                JOptionPane.QUESTION_MESSAGE,
                null,
                parent.getFicheros(usuarioSelecionado, "EST"),"seleccione");
                if (seleciondearchivos != null) {
                    parent.cargarEstado(seleciondearchivos.toString(), usuarioSelecionado.toString());
                }
    }
    /**
     * Metodo para cargar el estado guardado de una estanteria.
     * Este metodo perimite cargar distribuciones de estanterias guardadas en la propia carpeta del usuario
     */
    private void cargarEstadoEmpleado() {

        //Selecion de archivos
        String aux = parent.getUsuario();
        Object seleciondearchivos = JOptionPane.showInputDialog(
                null,
                "Seleccione una estanteria a cargar",
                "Seleccion de estanteria",
                JOptionPane.QUESTION_MESSAGE,
                null,
                parent.getFicheros(parent.getUsuario(), "EST"),"seleccione");
        if (seleciondearchivos != null) {
            parent.cargarEstado(seleciondearchivos.toString(), aux);
        }
    }
    /**
     * Metodo para guardar el estado guardado de una estanteria.
     * Este metodo perimite guardar distribuciones de estanterias guardadas en cualquier carpeta de usuario
     */
    private void guardarEstadoGestor() {
        //Selecion de usuarios
        Object usuarioSelecionado = JOptionPane.showInputDialog(
                null,
                "Seleccione un usuario al que guardar la estanteria",
                "Seleccion de Usuarios",
                JOptionPane.QUESTION_MESSAGE,
                null,
                parent.getusers(),"seleccione");

        String archivo = JOptionPane.showInputDialog("Escoja el nombre del archivo");
        parent.guardarEstado(archivo,usuarioSelecionado.toString());
    }

    /**
     * Metodo para cambiar la contraseña del usuario actual.
     */
    private void cambiarContra() {

        String OldPassword = JOptionPane.showInputDialog("Ingrese su antigua Contraseña");
        String NewPassword = JOptionPane.showInputDialog("Ingrese su nueva Contraseña");
        parent.cambiarContra(NewPassword,OldPassword);
    }
    /**
     * Metodo para dar de baja al usuario actual, pidiendo su contraseña para confirmar la acción.
     */
    private void BajaUsuario(){
        String Password = JOptionPane.showInputDialog("Ingrese su contraseña para proceder");
            boolean correcto = parent.EliminarUsuario(Password);
            if(correcto){
                JOptionPane.showMessageDialog(null,"Usuario eliminado Correctamente");
                parent.salir();
            }
    }
    /**
     * Obtiene el menú contextual asociado a esta clase.
     *
     * @return El menú contextual (JPopupMenu) que contiene las opciones para el usuario.
     */
    public JPopupMenu getMenu() {
        return menu;
    }
}
