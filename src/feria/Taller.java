package feria;

import java.util.Locale;

/**
 * Base abstracta de un taller. Protege sus reglas: capacidad 1..30, actividad 1..180 minutos,
 * inscritos siempre entre 0 y capacidad. La duracion total la completa cada modalidad
 * sobrescribiendo {@link #minutosAdicionales()} (sin "if" por modalidad).
 */
public abstract class Taller implements Ficha {

    public static final int CAPACIDAD_MIN = 1;
    public static final int CAPACIDAD_MAX = 30;
    public static final int MINUTOS_MIN = 1;
    public static final int MINUTOS_MAX = 180;

    private final String id;
    private final String nombre;
    private final int capacidad;
    private final int minutosActividad;
    private int inscritos;

    protected Taller(String id, String nombre, int capacidad, int minutosActividad) {
        if (!datosValidos(id, nombre, capacidad, minutosActividad)) {
            throw new IllegalArgumentException("Datos de taller invalidos");
        }
        this.id = normalizarId(id);
        this.nombre = nombre.strip();
        this.capacidad = capacidad;
        this.minutosActividad = minutosActividad;
        this.inscritos = 0;
    }

    /** Devuelve el ID normalizado (strip + mayusculas) o null si es nulo/en blanco. */
    public static String normalizarId(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        return id.strip().toUpperCase(Locale.ROOT);
    }

    public static boolean datosValidos(String id, String nombre, int capacidad, int minutosActividad) {
        return normalizarId(id) != null
                && nombre != null && !nombre.isBlank()
                && capacidad >= CAPACIDAD_MIN && capacidad <= CAPACIDAD_MAX
                && minutosActividad >= MINUTOS_MIN && minutosActividad <= MINUTOS_MAX;
    }

    /** Minutos extra propios de la modalidad (preparacion o conexion). Se sobrescribe. */
    public abstract int minutosAdicionales();

    public abstract String modalidad();

    public int duracionTotal() {
        return minutosActividad + minutosAdicionales();
    }

    public int cuposRestantes() {
        return capacidad - inscritos;
    }

    /** Acepta solo cantidades positivas que caben en los cupos restantes. */
    public boolean inscribir(int cantidad) {
        if (cantidad <= 0 || cantidad > cuposRestantes()) {
            return false;
        }
        inscritos += cantidad;
        return true;
    }

    /** Acepta solo cantidades positivas que no superan los inscritos actuales. */
    public boolean cancelar(int cantidad) {
        if (cantidad <= 0 || cantidad > inscritos) {
            return false;
        }
        inscritos -= cantidad;
        return true;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public int getMinutosActividad() {
        return minutosActividad;
    }

    public int getInscritos() {
        return inscritos;
    }

    @Override
    public String generarFicha() {
        return "ID: " + id
                + " | Nombre: " + nombre
                + " | Modalidad: " + modalidad()
                + " | Capacidad: " + capacidad
                + " | Inscritos: " + inscritos
                + " | Duracion total: " + duracionTotal() + " min";
    }
}
