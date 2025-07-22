package Font.classes;

import java.util.ArrayList;

public class ResultadoAlgoritmo {
    ArrayList<Integer> productos;
    int sumaTotal;

    public ResultadoAlgoritmo () {
        productos = new ArrayList<>();
        sumaTotal = 0;
    }

    public ArrayList<Integer> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<Integer> productos) {
        this.productos = productos;
    }

    public int getSumaTotal() {
        return sumaTotal;
    }

    public void setSumaTotal(int sumaTotal) {
        this.sumaTotal = sumaTotal;
    }
}
