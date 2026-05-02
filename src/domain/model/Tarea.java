package domain.model;

/**
 * Entidad principal del dominio.
 * Encapsula su estado y valida sus propias reglas de negocio.
 */
public class Tarea {
    private final String id;
    private final String titulo;
    private boolean completada;

    public Tarea(String id, String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID no puede estar vacío");
        }
        this.id = id;
        this.titulo = titulo;
        this.completada = false;
    }

    // Regla de negocio: La tarea maneja su propio estado
    public void completar() {
        this.completada = true;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public boolean isCompletada() {
        return completada;
    }

    @Override
    public String toString() {
        return "Tarea [" + id + "] " + titulo + " - " + (completada ? "COMPLETADA" : "PENDIENTE");
    }
}
