package application.ports.input;

/**
 * Puerto de Entrada para crear una tarea.
 */
public interface CrearTarea {
    void ejecutar(String id, String titulo);
}
