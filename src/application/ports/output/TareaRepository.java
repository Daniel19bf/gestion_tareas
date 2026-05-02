package application.ports.output;

import domain.model.Tarea;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de Salida.
 * Define el contrato que debe cumplir cualquier mecanismo de persistencia.
 * La capa de aplicación (y dominio) no sabe cómo se guardan los datos, solo usa esta interfaz.
 */
public interface TareaRepository {
    void guardar(Tarea tarea);
    List<Tarea> obtenerTodas();
    Optional<Tarea> buscarPorId(String id);
    void eliminar(String id);
}
