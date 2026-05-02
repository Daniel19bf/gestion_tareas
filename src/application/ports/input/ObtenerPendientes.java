package application.ports.input;

import domain.model.Tarea;
import java.util.List;

/**
 * Puerto de Entrada para obtener las tareas pendientes.
 */
public interface ObtenerPendientes {
    List<Tarea> ejecutar();
}
