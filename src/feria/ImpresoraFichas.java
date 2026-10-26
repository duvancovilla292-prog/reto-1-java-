package feria;

import java.util.List;

/** Cliente del contrato Ficha: imprime cualquier ficha sin conocer su tipo concreto. */
public class ImpresoraFichas {

    public void mostrar(Ficha ficha) {
        System.out.println("  " + ficha.generarFicha());
    }

    public void mostrarTodas(List<? extends Ficha> fichas) {
        if (fichas.isEmpty()) {
            System.out.println("  Sin datos");
            return;
        }
        for (int i = 0; i < fichas.size(); i++) {
            mostrar(fichas.get(i));
        }
    }
}
