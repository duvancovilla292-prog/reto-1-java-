package feria;

/**
 * Asistencias de cinco dias (dia 1 = indice 0 ... dia 5 = indice 4), cada una de 0 a 200.
 * El arreglo es privado, de tamano fijo y nunca se entrega ni se ordena.
 */
public class AsistenciaSemanal implements Ficha {

    public static final int DIAS = 5;
    public static final int ASISTENCIA_MIN = 0;
    public static final int ASISTENCIA_MAX = 200;

    private final int[] asistencias = new int[DIAS];

    public boolean esDiaValido(int dia) {
        return dia >= 1 && dia <= DIAS;
    }

    /** Registra o corrige el dia indicado. Rechaza dia fuera de 1..5 o valor fuera de 0..200. */
    public boolean registrarDia(int dia, int asistentes) {
        if (!esDiaValido(dia) || asistentes < ASISTENCIA_MIN || asistentes > ASISTENCIA_MAX) {
            return false;
        }
        asistencias[dia - 1] = asistentes;
        return true;
    }

    public boolean corregirDia(int dia, int asistentes) {
        return registrarDia(dia, asistentes);
    }

    public String consultarDia(int dia) {
        if (!esDiaValido(dia)) {
            return "Posicion inexistente";
        }
        return "Dia " + dia + ": " + asistencias[dia - 1];
    }

    public int total() {
        int suma = 0;
        for (int i = 0; i < asistencias.length; i++) {
            suma += asistencias[i];
        }
        return suma;
    }

    /** Siempre hay cinco posiciones, por lo que el divisor nunca es cero. */
    public double promedio() {
        return (double) total() / DIAS;
    }

    public int maximo() {
        int max = asistencias[0];
        for (int i = 1; i < asistencias.length; i++) {
            if (asistencias[i] > max) {
                max = asistencias[i];
            }
        }
        return max;
    }

    /** Primer dia (1..5) donde aparece el maximo; en empates gana el primero. */
    public int primerDiaMaximo() {
        int max = maximo();
        for (int i = 0; i < asistencias.length; i++) {
            if (asistencias[i] == max) {
                return i + 1;
            }
        }
        return 1;
    }

    public Evidencia<Integer> crearEvidenciaTotal() {
        return new Evidencia<Integer>("Total semanal de asistencia", total());
    }

    public Evidencia<String> crearEvidenciaConclusion() {
        String texto = "Pico de " + maximo() + " asistentes el dia " + primerDiaMaximo()
                + "; promedio " + promedio() + " por dia";
        return new Evidencia<String>("Conclusion de la semana", texto);
    }

    @Override
    public String generarFicha() {
        return "Asistencia semanal | Total: " + total()
                + " | Promedio: " + promedio()
                + " | Maximo: " + maximo()
                + " | Primer dia del maximo: " + primerDiaMaximo();
    }
}
