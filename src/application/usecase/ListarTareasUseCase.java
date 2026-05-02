package application.usecase;

import application.ports.input.ListarTareas;
import application.ports.output.TareaRepository;
import domain.model.Tarea;
import java.util.List;

/**
 * Caso de Uso: Listar Tareas.
 */
public class ListarTareasUseCase implements ListarTareas {
    private final TareaRepository tareaRepository;

    public ListarTareasUseCase(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    @Override
    public List<Tarea> ejecutar() {
        return tareaRepository.obtenerTodas();
    }
}
