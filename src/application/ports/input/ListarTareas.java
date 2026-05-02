package application.ports.input;

import domain.model.Tarea;
import java.util.List;

/**
 * Puerto de Entrada para listar todas las tareas.
 */
public interface ListarTareas {
    List<Tarea> ejecutar();
}
