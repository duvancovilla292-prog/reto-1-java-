package feria;

/** Taller virtual: suma 5 minutos de conexion a la actividad. */
public class TallerVirtual extends Taller {

    private static final int MINUTOS_CONEXION = 5;

    public TallerVirtual(String id, String nombre, int capacidad, int minutosActividad) {
        super(id, nombre, capacidad, minutosActividad);
    }

    @Override
    public int minutosAdicionales() {
        return MINUTOS_CONEXION;
    }

    @Override
    public String modalidad() {
        return "Virtual";
    }
}
