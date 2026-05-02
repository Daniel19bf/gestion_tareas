package main;

import application.ports.output.TareaRepository;
import application.usecase.CompletarTareaUseCase;
import application.usecase.CrearTareaUseCase;
import application.usecase.EliminarTareaUseCase;
import application.usecase.ListarTareasUseCase;
import application.usecase.ObtenerPendientesUseCase;
import infrastructure.persistence.TareaRepositoryMemory;
import infrastructure.ui.TareaSwingUI;

import javax.swing.*;
import java.awt.Color;

public class Main {
    public static void main(String[] args) {
        
        // Quitar los molestos bordes punteados de focus de Swing por defecto
        UIManager.put("Button.focus", new Color(0, 0, 0, 0));

        // 1. Inicializar adaptador de salida
        TareaRepository repositorio = new TareaRepositoryMemory();

        // 2. Inicializar Casos de Uso
        CrearTareaUseCase crearTarea = new CrearTareaUseCase(repositorio);
        ListarTareasUseCase listarTareas = new ListarTareasUseCase(repositorio);
        CompletarTareaUseCase completarTarea = new CompletarTareaUseCase(repositorio);
        ObtenerPendientesUseCase obtenerPendientes = new ObtenerPendientesUseCase(repositorio);
        EliminarTareaUseCase eliminarTarea = new EliminarTareaUseCase(repositorio);

        // 3. Inicializar adaptador de entrada (UI)
        SwingUtilities.invokeLater(() -> {
            TareaSwingUI ui = new TareaSwingUI(
                    crearTarea,
                    listarTareas,
                    completarTarea,
                    obtenerPendientes,
                    eliminarTarea
            );
            ui.setVisible(true);
        });
    }
}
