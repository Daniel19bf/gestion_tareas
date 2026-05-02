package application.usecase;

import application.ports.input.CompletarTarea;
import application.ports.output.TareaRepository;
import domain.model.Tarea;

/**
 * Caso de Uso: Completar Tarea.
 */
public class CompletarTareaUseCase implements CompletarTarea {
    private final TareaRepository tareaRepository;

    public CompletarTareaUseCase(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    @Override
    public void ejecutar(String id) {
        // 1. Buscar la tarea
        Tarea tarea = tareaRepository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("La tarea con ID " + id + " no existe."));
            
        // 2. Aplicar lógica de negocio en la entidad
        tarea.completar();
        
        // 3. Guardar estado actualizado
        tareaRepository.guardar(tarea);
    }
}
