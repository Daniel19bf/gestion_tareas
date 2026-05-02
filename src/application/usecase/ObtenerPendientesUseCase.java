package application.usecase;

import application.ports.input.ObtenerPendientes;
import application.ports.output.TareaRepository;
import domain.model.Tarea;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Caso de Uso: Obtener Pendientes.
 */
public class ObtenerPendientesUseCase implements ObtenerPendientes {
    private final TareaRepository tareaRepository;

    public ObtenerPendientesUseCase(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    @Override
    public List<Tarea> ejecutar() {
        return tareaRepository.obtenerTodas().stream()
                .filter(tarea -> !tarea.isCompletada())
                .collect(Collectors.toList());
    }
}
