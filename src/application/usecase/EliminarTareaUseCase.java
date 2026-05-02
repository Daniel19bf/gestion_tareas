package application.usecase;

import application.ports.input.EliminarTarea;
import application.ports.output.TareaRepository;

/**
 * Caso de Uso: Eliminar Tarea.
 */
public class EliminarTareaUseCase implements EliminarTarea {
    private final TareaRepository tareaRepository;

    public EliminarTareaUseCase(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    @Override
    public void ejecutar(String id) {
        tareaRepository.eliminar(id);
    }
}
