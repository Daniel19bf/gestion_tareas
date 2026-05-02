package infrastructure.persistence;

import application.ports.output.TareaRepository;
import domain.model.Tarea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Adaptador de Salida: Base de Datos en Memoria.
 * Implementa el puerto de salida para guardar las tareas en un HashMap.
 */
public class TareaRepositoryMemory implements TareaRepository {
    private final Map<String, Tarea> db = new HashMap<>();

    @Override
    public void guardar(Tarea tarea) {
        db.put(tarea.getId(), tarea);
    }

    @Override
    public List<Tarea> obtenerTodas() {
        return new ArrayList<>(db.values());
    }

    @Override
    public Optional<Tarea> buscarPorId(String id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public void eliminar(String id) {
        db.remove(id);
    }
}
