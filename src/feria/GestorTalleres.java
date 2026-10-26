package feria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Registro de talleres. Programa contra List<T> con ArrayList: la cantidad es variable,
 * se agrega al final (orden de registro) y se recorre por posicion; no hay inserciones
 * ni borrados en el medio que justifiquen LinkedList.
 */
public class GestorTalleres {

    private final List<Taller> talleres = new ArrayList<>();

    public boolean registrarPresencial(String id, String nombre, int capacidad, int minutos) {
        if (!puedeRegistrar(id, nombre, capacidad, minutos)) {
            return false;
        }
        return talleres.add(new TallerPresencial(id, nombre, capacidad, minutos));
    }

    public boolean registrarVirtual(String id, String nombre, int capacidad, int minutos) {
        if (!puedeRegistrar(id, nombre, capacidad, minutos)) {
            return false;
        }
        return talleres.add(new TallerVirtual(id, nombre, capacidad, minutos));
    }

    private boolean puedeRegistrar(String id, String nombre, int capacidad, int minutos) {
        return Taller.datosValidos(id, nombre, capacidad, minutos) && buscar(id) == null;
    }

    /** Busca con un ciclo y equals sobre el ID normalizado. Devuelve null si no existe. */
    public Taller buscar(String id) {
        String buscado = Taller.normalizarId(id);
        if (buscado == null) {
            return null;
        }
        for (int i = 0; i < talleres.size(); i++) {
            if (talleres.get(i).getId().equals(buscado)) {
                return talleres.get(i);
            }
        }
        return null;
    }

    public String consultarFicha(String id) {
        Taller taller = buscar(id);
        if (taller == null) {
            return "No encontrado";
        }
        return taller.generarFicha();
    }

    public boolean inscribir(String id, int cantidad) {
        Taller taller = buscar(id);
        if (taller == null) {
            return false;
        }
        return taller.inscribir(cantidad);
    }

    public boolean cancelar(String id, int cantidad) {
        Taller taller = buscar(id);
        if (taller == null) {
            return false;
        }
        return taller.cancelar(cantidad);
    }

    /** Copia de solo lectura, en orden de registro, expuesta como contrato Ficha. */
    public List<Ficha> listarFichas() {
        List<Ficha> fichas = new ArrayList<>();
        for (int i = 0; i < talleres.size(); i++) {
            fichas.add(talleres.get(i));
        }
        return Collections.unmodifiableList(fichas);
    }

    public int cantidadTalleres() {
        return talleres.size();
    }

    public int totalInscritos() {
        int suma = 0;
        for (int i = 0; i < talleres.size(); i++) {
            suma += talleres.get(i).getInscritos();
        }
        return suma;
    }

    public String promedioInscritos() {
        if (talleres.isEmpty()) {
            return "Sin datos";
        }
        return String.valueOf((double) totalInscritos() / talleres.size());
    }

    /** Taller con mas inscritos; en empates, el primero registrado. */
    public String liderInscritos() {
        if (talleres.isEmpty()) {
            return "Sin datos";
        }
        Taller lider = talleres.get(0);
        for (int i = 1; i < talleres.size(); i++) {
            if (talleres.get(i).getInscritos() > lider.getInscritos()) {
                lider = talleres.get(i);
            }
        }
        return lider.getId() + " (" + lider.getInscritos() + " inscritos)";
    }
}
