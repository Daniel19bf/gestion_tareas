package infrastructure.ui;

import application.ports.input.*;
import domain.model.Tarea;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * Adaptador de Entrada: Interfaz de Usuario UI "Ultra Pro".
 * Utiliza Gráficos 2D avanzados y personalización extrema de Swing 
 * para lograr un aspecto de IDE moderno (Dark Mode) sin usar frameworks.
 */
public class TareaSwingUI extends JFrame {

    private final CrearTarea crearTarea;
    private final ListarTareas listarTareas;
    private final CompletarTarea completarTarea;
    private final ObtenerPendientes obtenerPendientes;
    private final EliminarTarea eliminarTarea;

    // Paleta de Colores - Tema Oscuro Estilo "Dracula" / Modern IDE
    private final Color BG_MAIN = new Color(30, 30, 46);
    private final Color BG_PANEL = new Color(40, 42, 54);
    private final Color FG_TEXT = new Color(248, 248, 242);
    private final Color FG_MUTED = new Color(150, 150, 170);
    private final Color ACCENT_COLOR = new Color(189, 147, 249); // Morado
    private final Color SUCCESS_COLOR = new Color(80, 250, 123); // Verde Neón
    private final Color DANGER_COLOR = new Color(255, 85, 85); // Rojo Neón
    private final Color WARNING_COLOR = new Color(255, 184, 108); // Naranja
    private final Color INPUT_BG = new Color(50, 52, 65);
    private final Color BORDER_COLOR = new Color(68, 71, 90);

    private JTextField txtId;
    private JTextField txtTitulo;
    private DefaultTableModel tableModel;
    private JTable tablaTareas;

    public TareaSwingUI(CrearTarea crearTarea, ListarTareas listarTareas, CompletarTarea completarTarea,
                        ObtenerPendientes obtenerPendientes, EliminarTarea eliminarTarea) {
        this.crearTarea = crearTarea;
        this.listarTareas = listarTareas;
        this.completarTarea = completarTarea;
        this.obtenerPendientes = obtenerPendientes;
        this.eliminarTarea = eliminarTarea;

        configurarVentana();
        inicializarComponentes();
        refrescarTabla(listarTareas.ejecutar());
    }

    private void configurarVentana() {
        setTitle("✨ TaskManager - Ultra Pro Edition");
        setSize(1050, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_MAIN);
    }

    private void inicializarComponentes() {
        // --- PANEL LATERAL (SIDEBAR) ---
        JPanel panelLateral = new JPanel(new GridBagLayout());
        panelLateral.setBackground(BG_PANEL);
        panelLateral.setPreferredSize(new Dimension(320, 0));
        panelLateral.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR),
            new EmptyBorder(35, 25, 35, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Título Logo
        JLabel lblLogo = new JLabel("✦ TaskManager");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblLogo.setForeground(ACCENT_COLOR);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 40, 0);
        panelLateral.add(lblLogo, gbc);

        // Formulario
        JLabel lblId = new JLabel("Identificador");
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblId.setForeground(FG_MUTED);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 5, 0);
        panelLateral.add(lblId, gbc);

        txtId = crearInputPro();
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        panelLateral.add(txtId, gbc);

        JLabel lblTitulo = new JLabel("Descripción de la Tarea");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitulo.setForeground(FG_MUTED);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 5, 0);
        panelLateral.add(lblTitulo, gbc);

        txtTitulo = crearInputPro();
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 30, 0);
        panelLateral.add(txtTitulo, gbc);

        // Botón Principal
        JButton btnCrear = crearBotonPro("Agregar Tarea", ACCENT_COLOR, BG_MAIN);
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 0, 0);
        panelLateral.add(btnCrear, gbc);

        // Espacio Flexible
        gbc.gridy = 6;
        gbc.weighty = 1.0;
        panelLateral.add(Box.createVerticalGlue(), gbc);
        gbc.weighty = 0.0;

        // Acciones Secundarias (Requieren selección de tabla)
        JLabel lblAcciones = new JLabel("ACCIONES DE SELECCIÓN");
        lblAcciones.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblAcciones.setForeground(FG_MUTED);
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 10, 0);
        panelLateral.add(lblAcciones, gbc);

        JButton btnCompletar = crearBotonPro("✓ Marcar Lista", SUCCESS_COLOR, BG_MAIN);
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, 10, 0);
        panelLateral.add(btnCompletar, gbc);

        JButton btnEliminar = crearBotonPro("🗑 Eliminar", DANGER_COLOR, Color.WHITE);
        gbc.gridy = 9;
        gbc.insets = new Insets(0, 0, 0, 0);
        panelLateral.add(btnEliminar, gbc);

        add(panelLateral, BorderLayout.WEST);

        // --- PANEL CENTRAL (TABLA DE DATOS) ---
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(BG_MAIN);
        panelCentral.setBorder(new EmptyBorder(35, 40, 35, 40));

        // Cabecera superior con filtros
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        panelFiltros.setBackground(BG_MAIN);
        panelFiltros.setBorder(new EmptyBorder(0, 0, 25, 0));

        JLabel lblVista = new JLabel("Vistas:");
        lblVista.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblVista.setForeground(FG_MUTED);
        panelFiltros.add(lblVista);

        JButton btnTodas = crearBotonTab("Todas las Tareas", true);
        JButton btnPendientes = crearBotonTab("Solo Pendientes", false);
        panelFiltros.add(btnTodas);
        panelFiltros.add(btnPendientes);

        panelCentral.add(panelFiltros, BorderLayout.NORTH);

        // Tabla estéticamente impecable
        String[] columnas = {"ID", "Descripción", "Estado"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tablaTareas = new JTable(tableModel);
        tablaTareas.setBackground(BG_PANEL);
        tablaTareas.setForeground(FG_TEXT);
        tablaTareas.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tablaTareas.setRowHeight(50);
        tablaTareas.setIntercellSpacing(new Dimension(0, 0)); // Quita bordes de celdas
        tablaTareas.setShowGrid(false); // Sin líneas
        tablaTareas.setSelectionBackground(BORDER_COLOR);
        tablaTareas.setSelectionForeground(Color.WHITE);
        tablaTareas.setFocusable(false); // Quitar el feo borde azul de selección

        // Cabecera de la tabla customizada
        JTableHeader header = tablaTareas.getTableHeader();
        header.setBackground(BG_PANEL);
        header.setForeground(ACCENT_COLOR);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(100, 45));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        
        // Alinear a la izquierda y dar padding a la cabecera
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
        
        // Renderizado personalizado de filas para colores y padding
        tablaTareas.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBorder(new EmptyBorder(0, 20, 0, 20)); // Padding horizontal
                
                if (column == 2) {
                    label.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    if ("COMPLETADA".equals(value)) {
                        label.setForeground(SUCCESS_COLOR);
                    } else {
                        label.setForeground(WARNING_COLOR);
                    }
                } else if (!isSelected) {
                    label.setForeground(FG_TEXT);
                }
                return label;
            }
        });

        // Evento de selección de fila
        tablaTareas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaTareas.getSelectedRow() != -1) {
                txtId.setText(tablaTareas.getValueAt(tablaTareas.getSelectedRow(), 0).toString());
                txtTitulo.setText(tablaTareas.getValueAt(tablaTareas.getSelectedRow(), 1).toString());
            }
        });

        // Contenedor de la tabla (JScrollPane modificado)
        JScrollPane scrollTabla = new JScrollPane(tablaTareas);
        scrollTabla.setBackground(BG_PANEL);
        scrollTabla.getViewport().setBackground(BG_PANEL);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder()); // Sin bordes
        
        // Scrollbar personalizado e invisible (como MacOS/Modern web)
        scrollTabla.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = BORDER_COLOR;
                this.trackColor = BG_PANEL;
            }
            @Override
            protected JButton createDecreaseButton(int orientation) { return crearBotonVacio(); }
            @Override
            protected JButton createIncreaseButton(int orientation) { return crearBotonVacio(); }
        });

        // Panel curvo de fondo para la tabla
        JPanel panelContenedorTabla = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_PANEL);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18); // Esquinas redondeadas
                g2.dispose();
            }
        };
        panelContenedorTabla.setOpaque(false); // Deja ver el fondo oscuro real
        panelContenedorTabla.setBorder(new EmptyBorder(10, 10, 10, 10)); // Margen interior del cuadro
        panelContenedorTabla.add(scrollTabla, BorderLayout.CENTER);

        panelCentral.add(panelContenedorTabla, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

        // --- ASIGNAR EVENTOS ---
        btnCrear.addActionListener(e -> crear());
        btnCompletar.addActionListener(e -> completar());
        btnEliminar.addActionListener(e -> eliminar());
        
        btnTodas.addActionListener(e -> {
            btnTodas.setForeground(ACCENT_COLOR);
            btnPendientes.setForeground(FG_MUTED);
            refrescarTabla(listarTareas.ejecutar());
        });
        
        btnPendientes.addActionListener(e -> {
            btnPendientes.setForeground(ACCENT_COLOR);
            btnTodas.setForeground(FG_MUTED);
            refrescarTabla(obtenerPendientes.ejecutar());
        });
    }

    // ====================================================================
    // MÉTODOS DE RENDERIZADO PERSONALIZADO 100% (Sin librerías externas)
    // ====================================================================

    private JTextField crearInputPro() {
        JTextField txt = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(INPUT_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        txt.setOpaque(false); // Fondo transparente para que sirva el paintComponent
        txt.setForeground(FG_TEXT);
        txt.setCaretColor(ACCENT_COLOR);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR), // Borde fino
            new EmptyBorder(12, 15, 12, 15) // Relleno interior
        ));
        return txt;
    }

    private JButton crearBotonPro(String texto, Color bg, Color fg) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Efecto Hover y Click básico (Dibuja el fondo del color)
                if (getModel().isPressed()) g2.setColor(bg.darker().darker());
                else if (getModel().isRollover()) g2.setColor(bg.brighter());
                else g2.setColor(bg);
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(fg);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(14, 20, 14, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton crearBotonTab(String texto, boolean activo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(activo ? ACCENT_COLOR : FG_MUTED);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(5, 10, 5, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton crearBotonVacio() {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(0, 0));
        return btn;
    }

    // ====================================================================
    // LÓGICA DE EVENTOS HACIA CASOS DE USO (Arquitectura Hexagonal)
    // ====================================================================

    private void refrescarTabla(List<Tarea> tareas) {
        tableModel.setRowCount(0);
        for (Tarea t : tareas) {
            tableModel.addRow(new Object[]{
                t.getId(),
                t.getTitulo(),
                t.isCompletada() ? "COMPLETADA" : "PENDIENTE"
            });
        }
    }

    private void crear() {
        try {
            crearTarea.ejecutar(txtId.getText(), txtTitulo.getText());
            refrescarTabla(listarTareas.ejecutar());
            limpiarCampos();
        } catch (Exception ex) {
            mostrarError("Error al crear: " + ex.getMessage());
        }
    }

    private void completar() {
        try {
            if (txtId.getText().isEmpty()) throw new IllegalArgumentException("Por favor, selecciona una tarea de la tabla.");
            completarTarea.ejecutar(txtId.getText());
            refrescarTabla(listarTareas.ejecutar());
            limpiarCampos();
        } catch (Exception ex) {
            mostrarError("Error al completar: " + ex.getMessage());
        }
    }

    private void eliminar() {
        try {
            if (txtId.getText().isEmpty()) throw new IllegalArgumentException("Por favor, selecciona una tarea de la tabla.");
            int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar permanentemente la tarea seleccionada?", 
                    "Confirmación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (conf == JOptionPane.YES_OPTION) {
                eliminarTarea.ejecutar(txtId.getText());
                refrescarTabla(listarTareas.ejecutar());
                limpiarCampos();
            }
        } catch (Exception ex) {
            mostrarError("Error al eliminar: " + ex.getMessage());
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtTitulo.setText("");
        tablaTareas.clearSelection();
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Notificación", JOptionPane.ERROR_MESSAGE);
    }
}
