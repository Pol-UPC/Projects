// autor: Christian Conejo Raubiene

package Font.classes;

import Font.Exceptions.SimilitudMismoProducto;
import Font.Exceptions.ValorSimilitudFueraRango;

/**
 * Una similitud es una pequeña estructura que contiene tres variables en su interior, Dos representan la id por la cual relaciona esta similitud, y la otra representa el valor del grado de similitud entre estas dos id.
 *
 * Esta estructura existe con el fin de pasar información a otra clase, El gestor de similitudes no la usa internamente.
 *
 * Una similitud siempre tendrá una id pequeña y una id grande con el fin de relacionar y optimizar, El valor del grado de similitud estará siempre entre 0 y 100.
 */
public class Similitud
{
    private int idGrande, idPequena, similitud;


    /**
     * Constructora para crear con los valores correctos una Similitud
     * @param idPAux es la id pequeña por la cual se identifica la Similitud
     * @param idGAux es la id grande por la cual se identifica la Similitud
     * @param similitudAux es el valor identificado por idPAux y idGAux
     */
    public Similitud (int idGAux, int idPAux, Integer similitudAux) throws SimilitudMismoProducto, ValorSimilitudFueraRango
    {
        if (idGAux == idPAux) throw new SimilitudMismoProducto("No puedes crear una similitud con la misma id.");
        if (similitudAux < 0 || similitudAux > 100) throw new ValorSimilitudFueraRango("El valor atribuido a la similitud entre " + idGAux + " y " + idPAux + " (" + similitudAux + ") tiene que estar entre 0 y 100.");
        this.idGrande = Math.max(idGAux, idPAux);
        this.idPequena = Math.min(idGAux, idPAux);
        this.similitud = similitudAux;
    }

    public Similitud() {    }

    /**
     * Obtiene y devuelve la id grande de la similitud
     * @return la id grande de la similitud
     */
    public int getIdGrande() { return idGrande; }

    /**
     * Obtiene y devuelve la id pequeña de la similitud.
     * @return la id pequeña de la similitud
     */
    public int getIdPequena() { return idPequena; }


    /**
     * Obtiene y devuelve el grado de similitud.
     * @return el grado de similitud
     */
    public Integer getSimilitud() { return similitud; }
}
