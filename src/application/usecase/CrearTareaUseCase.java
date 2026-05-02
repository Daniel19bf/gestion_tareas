package application.usecase;

import application.ports.input.CrearTarea;
import application.ports.output.TareaRepository;
import domain.model.Tarea;

/**
 * Caso de Uso: Crear Tarea.
 * Implementa el puerto de entrada y se comunica con el puerto de salida.
 */
public class CrearTareaUseCase implements CrearTarea {
    private final TareaRepository tareaRepository;

    // Inyección de dependencias mediante el constructor
    public CrearTareaUseCase(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    @Override
    public void ejecutar(String id, String titulo) {
        // Se crea la entidad de dominio. Sus propias reglas validan los datos.
        Tarea tarea = new Tarea(id, titulo);
        
        // El repositorio guarda la entidad usando el puerto de salida.
        tareaRepository.guardar(tarea);
    }
}
