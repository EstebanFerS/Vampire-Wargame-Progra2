package proyecto_parcial_1_vampire_wargame.Ventanas;

import proyecto_parcial_1_vampire_wargame.Panel;
import proyecto_parcial_1_vampire_wargame.Player;
import proyecto_parcial_1_vampire_wargame.GameLog;
import proyecto_parcial_1_vampire_wargame.Almacenamiento.Manager;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Reportes con estética consistente con MiPerfil.
 * Ventana: 1300x850 (como pediste).
 * Tablas con mayor legibilidad: fuente más grande, filas más altas, descripción con wrap.
 */
public class Reportes extends JFrame {

    private final Player jugador;
    private final Manager manager;

    public Reportes(Player jugador, Manager manager) {
        this.jugador = jugador;
        this.manager = manager;
        setTitle("Reportes - Vampire Wargame");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1300, 850); // <- tamaño pedido
        setLocationRelativeTo(null);
        setResizable(false);
        inicializar();
        setVisible(true);
    }

    private void inicializar() {
        Panel panelFondo = new Panel("/Images/Fondo2.jpg");
        panelFondo.setLayout(new GridBagLayout());
        panelFondo.setOpaque(false);

        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblTitulo = new JLabel("REPORTES");
        lblTitulo.setFont(new Font("Arial Black", Font.BOLD, 42));
        lblTitulo.setForeground(new Color(230, 182, 50));
        gbc.gridwidth = 2;
        panelCentral.add(lblTitulo, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        JTabbedPane pestañas = new JTabbedPane();
        pestañas.setFont(new Font("Segoe UI", Font.BOLD, 20));
        pestañas.addTab("Ranking Jugadores", crearPanelRanking());
        pestañas.addTab("Logs del Jugador", crearPanelLogs());
        panelCentral.add(pestañas, gbc);

        gbc.gridy++;
        gbc.weighty = 0;
        gbc.gridwidth = 2;
        JPanel panelBtns = new JPanel();
        panelBtns.setOpaque(false);

        JButton btnVolver = crearBoton("VOLVER");
        btnVolver.addActionListener(e -> {
            dispose();
            try {
                new proyecto_parcial_1_vampire_wargame.Ventanas.MenuPrincipal(jugador).setVisible(true);
            } catch (Exception ex) {
                try {
                    Class<?> cls = Class.forName("proyecto_parcial_1_vampire_wargame.Ventanas.MenuPrincipal");
                    cls.getConstructor(Manager.class, Player.class).newInstance(manager, jugador);
                } catch (Exception ignored) {
                }
            }
        });
        panelBtns.add(btnVolver);

        panelCentral.add(panelBtns, gbc);

        panelFondo.add(panelCentral);
        setContentPane(panelFondo);
        revalidate();
        repaint();
    }

    private JPanel crearPanelRanking() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        List<Player> players;
        try {
            players = manager != null ? manager.getAllPlayers() : Collections.emptyList();
        } catch (Exception ex) {
            players = Collections.emptyList();
        }

        Collections.sort(players, Comparator.comparingInt(Player::getPuntos).reversed());

        String[] columnas = {"Posición", "Jugador", "Puntos", "Fecha ingreso"};
        String[][] datos = new String[players.size()][4];
        for (int i = 0; i < players.size(); i++) {
            Player pl = players.get(i);
            datos[i][0] = String.valueOf(i + 1);
            datos[i][1] = safe(pl != null ? pl.getUsername() : null);
            datos[i][2] = pl != null ? String.valueOf(pl.getPuntos()) : "0";
            datos[i][3] = pl != null && pl.getFechaIngreso() != null ? pl.getFechaIngreso() : "-";
        }

        DefaultTableModel model = new DefaultTableModel(datos, columnas) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tabla = new JTable(model);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        aplicarEstiloTablaRanking(tabla);

        TableColumnModel colModel = tabla.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(90);   // Posición
        colModel.getColumn(1).setPreferredWidth(420);  // Jugador
        colModel.getColumn(2).setPreferredWidth(140);  // Puntos
        colModel.getColumn(3).setPreferredWidth(240);  // Fecha ingreso

        // renderer para colorear filas (oro/plata/bronce)
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private final Color oro = new Color(212, 175, 55);
            private final Color plata = new Color(192, 192, 192);
            private final Color bronce = new Color(205, 127, 50);
            private final Color base = new Color(40, 40, 60, 200);

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (row == 0) {
                    c.setBackground(oro);
                    c.setForeground(Color.BLACK);
                    setFont(getFont().deriveFont(Font.BOLD, 15f));
                } else if (row == 1) {
                    c.setBackground(plata);
                    c.setForeground(Color.BLACK);
                    setFont(getFont().deriveFont(Font.BOLD, 14f));
                } else if (row == 2) {
                    c.setBackground(bronce);
                    c.setForeground(Color.BLACK);
                    setFont(getFont().deriveFont(Font.BOLD, 14f));
                } else {
                    c.setBackground(base);
                    c.setForeground(Color.WHITE);
                    setFont(getFont().deriveFont(Font.PLAIN, 14f));
                }
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                }
                ((JComponent) c).setOpaque(true);
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JPanel crearPanelLogs() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        if (jugador == null) {
            p.add(mensajeCentral("No hay jugador logueado."), BorderLayout.CENTER);
            return p;
        }

        List<GameLog> logs;
        try {
            logs = manager != null ? manager.getAllLogs(jugador.getUsername()) : Collections.emptyList();
        } catch (Exception ex) {
            logs = Collections.emptyList();
        }

        if (logs == null || logs.isEmpty()) {
            p.add(mensajeCentral("No hay registros de partidas para " + jugador.getUsername() + "."), BorderLayout.CENTER);
            return p;
        }

        String[] columnas = {"Descripción", "Fecha", "Oponente", "Resultado"};
        String[][] datos = new String[logs.size()][4];

        for (int i = 0; i < logs.size(); i++) {
            GameLog log = logs.get(i);
            datos[i][0] = log == null ? "" : safe(log.getResultado());
            datos[i][1] = log == null ? "" : safe(log.getFecha());
            datos[i][2] = log == null ? "" : safe(log.getOponente(jugador.getUsername()));
            datos[i][3] = log == null ? "" : safe(log.getResultadoPara(jugador.getUsername()));
        }

        DefaultTableModel model = new DefaultTableModel(datos, columnas) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tabla = new JTable(model);

        // Mejor legibilidad para logs:
        tabla.setRowHeight(50); // fila base elevada
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        TableColumnModel cm = tabla.getColumnModel();
        cm.getColumn(0).setPreferredWidth(600); // Descripción más ancha (legible)
        cm.getColumn(1).setPreferredWidth(200); // Fecha
        cm.getColumn(2).setPreferredWidth(260); // Oponente
        cm.getColumn(3).setPreferredWidth(140); // Resultado

        // renderer de texto multilinea para la columna Descripción (col 0)
        cm.getColumn(0).setCellRenderer(new TextAreaRenderer());

        // Alineación centro para otras columnas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        cm.getColumn(1).setCellRenderer(centerRenderer);
        cm.getColumn(2).setCellRenderer(centerRenderer);
        cm.getColumn(3).setCellRenderer(centerRenderer);

        tabla.setSelectionBackground(new Color(100, 180, 220));
        tabla.setSelectionForeground(Color.BLACK);
        tabla.setShowGrid(false);

        // ajustar altura de filas según contenido de descripción
        ajustarAlturaFilasPorContenido(tabla);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    // renderer que usa JTextArea para permitir wrap y varias líneas
    private static class TextAreaRenderer extends JTextArea implements TableCellRenderer {
        public TextAreaRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.PLAIN, 15));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(new Color(30, 30, 50, 220));
                setForeground(Color.WHITE);
            }
            int prefHeight = getPreferredSize().height + 8;
            if (table.getRowHeight(row) < prefHeight) {
                table.setRowHeight(row, prefHeight);
            }
            return this;
        }
    }

    private void ajustarAlturaFilasPorContenido(JTable table) {
        TableModel model = table.getModel();
        for (int row = 0; row < model.getRowCount(); row++) {
            Object texto = model.getValueAt(row, 0);
            JTextArea temp = new JTextArea(texto == null ? "" : texto.toString());
            temp.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            temp.setLineWrap(true);
            temp.setWrapStyleWord(true);
            temp.setSize(600, Short.MAX_VALUE); // ancho similar a la columna descripción
            int h = temp.getPreferredSize().height + 14;
            table.setRowHeight(row, Math.max(50, h));
        }
    }

    private void aplicarEstiloTablaRanking(JTable tabla) {
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tabla.setRowHeight(42);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabla.setFillsViewportHeight(true);
        tabla.setShowGrid(false);
        tabla.setSelectionBackground(new Color(100, 180, 220));
        tabla.setSelectionForeground(Color.BLACK);
    }

    private Component mensajeCentral(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.ITALIC, 20));
        lbl.setForeground(Color.WHITE);
        return lbl;
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(300, 65));
        btn.setFont(new Font("Arial", Font.BOLD, 22));
        btn.setBackground(new Color(82, 36, 36));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        btn.setOpaque(true);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn.isEnabled()) {
                    btn.setBackground(new Color(140, 50, 50));
                }
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btn.isEnabled()) {
                    btn.setBackground(new Color(82, 36, 36));
                }
            }
        });

        return btn;
    }
}
