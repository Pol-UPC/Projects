// autor: Christian Conejo Raubiene

package Font.classes;

import java.util.ArrayList;
import java.util.HashMap;

import Font.Exceptions.NoExisteProducto;
import Font.Exceptions.ValorSimilitudFueraRango;

/**
 *Una estantería ordenada es una estructura dedicada a mantener la organización de un conjunto de ids (valores enteros únicos que representan productos), a la vez que el valor del grado de la similitud total con la que se relacionan estas
 *
 * Está constituida principalmente por un ArrayList que contiene las ids, y un HashMap de ids y sus posiciones correspondientes para poder acceder inmediatamente a la id del ArrayList
 *
 * Se puede acceder y eliminar una id de cualquier punto de la estructura mientras exista, pero únicamente se pueden añadir ids al final, También se puede ofrecer un ArrayList y el valor de su grado de similitud total directamente
 *
 * La estructura también cuenta con dos variables auxiliares de optimización, Una es la cantidad de ids de productos existentes, La otra es el valor del grado de la similitud total
 *
 * Al eliminar un producto de la EstanteriaOrdenada, su posición dentro del Array se ve intercambiada por una id "-1", la cual sus funciones internas tienen en cuenta al momento de operar
 */
public class EstanteriaOrdenada
{
    // Estructuras propias
    private ArrayList <Integer> listaOrdenProductos; // Lista con las id de los productos ordenadas de forma concreta
    private HashMap <Integer, Integer> mapaIdPosicion; // Mapa donde por cada id indica la posicion en la lista.
    private Integer cantidadProductosColocados;
    private Integer similitudTotal;

    // Estructuras exteriores
    private GestorSimilitudes gestorSimilitudes;


    /**
     * Constructora de una Estanteria Ordenada
     */
    public EstanteriaOrdenada()
    {
        listaOrdenProductos = new ArrayList<Integer>();
        mapaIdPosicion = new HashMap <Integer, Integer>();
        cantidadProductosColocados = 0;
        similitudTotal = 0;
    }

    /**
     * Enlaza con un gestor de similitudes
     * @param gS el gestor de similitudes
     */
    public void setGestorSimilitudes (GestorSimilitudes gS)
    {
        this.gestorSimilitudes = gS;
    }

    /**
     * Implementa los productos segun el orden dado, junto al grado de la similitud total
     * @param listaEstanteriaOrdenadaAux lista de ids de productos organizada de una cierta forma
     * @param similitudTotalAux valor de la similitud total de la lista
     * @throws ValorSimilitudFueraRango se lanza si la similitud total de la lista es menor a 0.
     */
    public void setEstanteriaOrdenada (ArrayList <Integer> listaEstanteriaOrdenadaAux, Integer similitudTotalAux) throws ValorSimilitudFueraRango
    {
        if (similitudTotalAux < 0) throw new ValorSimilitudFueraRango("El orden de productos pasado a la EstanteriaOrdenada tiene un grado invalido (menor a 0) de similitudes (" + similitudTotalAux + ").");

        listaOrdenProductos = (ArrayList<Integer>)listaEstanteriaOrdenadaAux.clone();
        cantidadProductosColocados = listaOrdenProductos.size();
        similitudTotal = similitudTotalAux;

        mapaIdPosicion.clear();
        for (Integer i = 0; i < cantidadProductosColocados; ++i)
        {
            Integer idAux = listaOrdenProductos.get(i);
            mapaIdPosicion.put(idAux, i);
        }
    }

    /**
     * Consulta si hay un producto colocado o no.
     * @param id id del producto a comprovar
     * @return devuelve cierto si el producto esta en la Estanteria Ordenada.
     */
    public Boolean estaColocado (Integer id)
    {
        return mapaIdPosicion.containsKey(id);
    }

    /**
     * Elimina un producto de la EStanteria Ordenada
     * @param id la id del producto que quieres eliminar.
     * @throws NoExisteProducto se lanza cuando el producto no existe.
     */
    public void eliminarProducto(Integer id) throws NoExisteProducto {
        Integer pos = mapaIdPosicion.get(id);

        // Si solo hay dos productos, al eliminar uno la similitud total sera 0
        // Si hay mas de dos productos hace falta comprovar la similitud entre los productos que estan de lado.
        if (cantidadProductosColocados == 2) similitudTotal = 0;
        else if (cantidadProductosColocados != 1)
        {
            Integer posAnt = lastValidPos(mapaIdPosicion.get(id));
            Integer posPost = nextValidPos(mapaIdPosicion.get(id));

            Integer idAnt = listaOrdenProductos.get(posAnt);
            Integer idPost = listaOrdenProductos.get(posPost);
            similitudTotal = similitudTotal - gestorSimilitudes.obtenerSimilitud(id, idAnt) - gestorSimilitudes.obtenerSimilitud(id, idPost) + gestorSimilitudes.obtenerSimilitud(idAnt, idPost); // ESTO DE AQUI DEPENDE DE QUE HACEMOS AL ELIMINAR
        }

        mapaIdPosicion.remove(id);
        listaOrdenProductos.set(pos, -1);
        cantidadProductosColocados--;
        if (listaOrdenProductos.size() * 35 / 100 < listaOrdenProductos.size() - cantidadProductosColocados) cleanListaProductos();
        while (!listaOrdenProductos.isEmpty() && listaOrdenProductos.getLast() == -1) listaOrdenProductos.removeLast();
    }

    /**
     * Añade un producto a la estructura de productos.
     * @param id id del producto que quieres añadir.
     * @throws NoExisteProducto se lanza cuando el producto no existe.
     */
    public void añadirProducto (Integer id) throws NoExisteProducto// Se añade un producto al final de la lista
    {
        listaOrdenProductos.add(id);
        mapaIdPosicion.put(id, listaOrdenProductos.size() - 1);
        cantidadProductosColocados++;

        // Si solo hay un producto no hace falta comparar similitudes
        // Si solo hay 2 productos la similitud total es la de entre los productos dos veces
        // Si hay mas de 2 productos hace falta comprovar que productos dejan de estar de lado y cuales ahora lo estaran.
        if (cantidadProductosColocados == 1) similitudTotal = 0;
        else if (cantidadProductosColocados == 2)
        {
            Integer id0 = listaOrdenProductos.get(lastValidPos(listaOrdenProductos.size() - 1));
            similitudTotal = 2 * gestorSimilitudes.obtenerSimilitud(id0, id);
        }
        else
        {
            Integer idAnt = listaOrdenProductos.get(lastValidPos(listaOrdenProductos.size() - 1));
            Integer idPost = listaOrdenProductos.get(nextValidPos(listaOrdenProductos.size() - 1));
            similitudTotal = similitudTotal - gestorSimilitudes.obtenerSimilitud(idAnt, idPost) + gestorSimilitudes.obtenerSimilitud(idAnt, id) + gestorSimilitudes.obtenerSimilitud(idPost, id);
        }

    }

    /**
     * Intercambia de posicion en la Estanteria Ordenada dos productos.
     * @param id1 es la id del primer producto que quieres intercambiar.
     * @param id2 es es id del segon producto que quieres intercambiar
     * @throws NoExisteProducto se lanza cuando uno de los dos productos no existe.
     */
    public void intercambiarProductos (Integer id1, Integer id2) throws NoExisteProducto {
        Integer pos1 = mapaIdPosicion.get(id1);
        Integer pos2 = mapaIdPosicion.get(id2);

        // Si hay mas de tres productos que se intercambian de posicion, hace falta comprovar las similitudes entre los productos posiciones en nuevas posiciones
        if (cantidadProductosColocados > 3)
        {
            Integer idAnt1 = listaOrdenProductos.get(lastValidPos(pos1));
            Integer idPost1 = listaOrdenProductos.get(nextValidPos(pos1));
            Integer idAnt2 = listaOrdenProductos.get(lastValidPos(pos2));
            Integer idPost2 = listaOrdenProductos.get(nextValidPos(pos2));

            Integer perdida1 = gestorSimilitudes.obtenerSimilitud(idAnt1, id1) + gestorSimilitudes.obtenerSimilitud(id1, idPost1);
            Integer perdida2 = gestorSimilitudes.obtenerSimilitud(idAnt2, id2) + gestorSimilitudes.obtenerSimilitud(id2, idPost2);
            Integer ganancia1 = gestorSimilitudes.obtenerSimilitud(idAnt1, id2) + gestorSimilitudes.obtenerSimilitud(id2, idPost1);
            Integer ganancia2 = gestorSimilitudes.obtenerSimilitud(idAnt2, id1) + gestorSimilitudes.obtenerSimilitud(id1, idPost2);
            similitudTotal = similitudTotal - (perdida1 + perdida2) + (ganancia1 + ganancia2);
        }
        listaOrdenProductos.set(pos1, id2);
        listaOrdenProductos.set(pos2, id1);
        mapaIdPosicion.put(id1, pos2);
        mapaIdPosicion.put(id2, pos1);
    }


    /**
     * Obtiene la lista ordenada de ids de productos.
     * @return devuelve la lista ordenada de ids de productos.
     */
    public ArrayList <Integer> getListaOrdenProductos()
    {
        return listaOrdenProductos;
    }

    /**
     * Consigue el valor del grado de la similitud total
     * @return devuelve el valor del grado de la similitud total
     */
    public Integer getSimilitudTotal ()
    {
        return similitudTotal;
    }

    /**
     * Cambia el valor de grado de la similitud total, si hiciera falta, tras el cambio de similitudes.
     * @param id1 id del primer producto a cambiar similitud
     * @param id2 id del segundo producto a cambiar similitud
     * @param similitud valor de la similitud a cambiar
     * @throws NoExisteProducto se lanza cuando uno de los dos productos no existe.
     */
    public void cambiarSimilitud(Integer id1, Integer id2, Integer similitud) throws NoExisteProducto, ValorSimilitudFueraRango {
        Integer posId1 = mapaIdPosicion.get(id1);
        Integer posId2 = mapaIdPosicion.get(id2);
        Integer posAlta = Math.max(posId1, posId2);
        Integer posBaja = Math.min(posId1, posId2);
        if (similitud < 0 || similitud > 100) throw new ValorSimilitudFueraRango("El valor atribuido a la similitud entre " + id1 + " y " + id2 + " (" + similitud + ") tiene que estar entre 0 y 100.");

        if (nextValidPos(posBaja) == posAlta) similitudTotal = similitudTotal - gestorSimilitudes.obtenerSimilitud(id1, id2) + similitud;
        if (nextValidPos(posAlta) == posBaja) similitudTotal = similitudTotal - gestorSimilitudes.obtenerSimilitud(id1, id2) + similitud;
    }

    // FUNCIONES PRIVADAS

    /**
     * Comprueba que una posicion este dentro del rango que tenemos.
     * @param pos es la posicion que queremos comprovar.
     * @return la nueva posición en caso de que tuvieses que recalcularse.
     */
    private Integer inBounds (Integer pos)
    {
        Integer aux = pos % listaOrdenProductos.size();
        if (aux < 0) aux += listaOrdenProductos.size();
        return aux;
    }

    /**
     * Comprueba que una posicion no este vacia con un producto -1 si vemos el posterior.
     * @param pos es la posicion nueva que queremos comprovar.
     * @return la nueva posible posicion
     */
    private Integer nextValidPos (Integer pos)
    {
        Integer nextPos = inBounds(pos+1);
        if (listaOrdenProductos.get(nextPos) < 0) return nextValidPos(nextPos);
        return nextPos;
    }

    /**
     * Comprueba que una posicion no este vacia con un producto -1 si vemos el anterior.
     * @param pos es la posicion nueva que queremos comprovar.
     * @return la nueva posible posicion
     */
    private Integer lastValidPos (Integer pos)
    {
        Integer lastPos = inBounds(pos-1);
        if (listaOrdenProductos.get(lastPos) < 0) return lastValidPos(lastPos);
        return lastPos;
    }

    private void cleanListaProductos()
    {
        // Releemos todas las posiciones de los productos.
        ArrayList<Integer> nuevaListaProductos = new ArrayList<>();
        HashMap<Integer,Integer> nuevoMapaIdPosicion = new HashMap<Integer, Integer>();
        int newPos = 0;
        for (int i = 0; i < listaOrdenProductos.size(); i++)
        {
            Integer idProd = listaOrdenProductos.get(i);
            if (idProd != -1)
            {
                nuevaListaProductos.add(idProd);
                nuevoMapaIdPosicion.put(idProd, newPos);
                ++newPos;
            }
        }
        listaOrdenProductos = nuevaListaProductos;
        mapaIdPosicion = nuevoMapaIdPosicion;
    }
}
