package feria;

/** Taller presencial: suma 15 minutos de preparacion a la actividad. */
public class TallerPresencial extends Taller {

    private static final int MINUTOS_PREPARACION = 15;

    public TallerPresencial(String id, String nombre, int capacidad, int minutosActividad) {
        super(id, nombre, capacidad, minutosActividad);
    }

    @Override
    public int minutosAdicionales() {
        return MINUTOS_PREPARACION;
    }

    @Override
    public String modalidad() {
        return "Presencial";
    }
}
