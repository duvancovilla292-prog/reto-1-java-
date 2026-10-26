package feria;

/**
 * Clase generica propia: guarda una descripcion y un contenido ya inicializado.
 * Se usa con Integer (total semanal) y con String (conclusion), sin raw types ni casts.
 */
public class Evidencia<T> {

    private final String descripcion;
    private final T contenido;

    public Evidencia(String descripcion, T contenido) {
        if (descripcion == null || descripcion.isBlank() || contenido == null) {
            throw new IllegalArgumentException("Evidencia incompleta");
        }
        this.descripcion = descripcion.strip();
        this.contenido = contenido;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public T getContenido() {
        return contenido;
    }

    public String mostrar() {
        return descripcion + ": " + contenido;
    }
}
