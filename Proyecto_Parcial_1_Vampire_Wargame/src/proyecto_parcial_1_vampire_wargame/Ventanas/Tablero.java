/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_parcial_1_vampire_wargame.Ventanas;
/**
* 
* @author esteb
*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import proyecto_parcial_1_vampire_wargame.Panel;
import proyecto_parcial_1_vampire_wargame.Fichas.Pieza;
import proyecto_parcial_1_vampire_wargame.Fichas.Vampiro;
import proyecto_parcial_1_vampire_wargame.Fichas.HombreLobo;
import proyecto_parcial_1_vampire_wargame.Fichas.Muerte;
import proyecto_parcial_1_vampire_wargame.Fichas.Zombie;
import proyecto_parcial_1_vampire_wargame.Player;
import proyecto_parcial_1_vampire_wargame.Ruleta;

public class Tablero extends JFrame {
    private Player jugadorBlanco;
    private Player jugadorNegro;

    public Tablero() {
        this.jugadorBlanco = jugadorBlanco;
        this.jugadorNegro = jugadorNegro;
        setTitle("Tablero Visual - Vampire Wargame");
        setSize(1300, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        Panel panelFondo = new Panel("/Images/Fondo3.jpg");
        panelFondo.setOpaque(false);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setOpaque(false);

        TableroPanel tableroPanel = new TableroPanel();
        contenedor.add(tableroPanel, BorderLayout.CENTER);

        JPanel placeholderRuleta = new JPanel(new BorderLayout());
        placeholderRuleta.setOpaque(false);
        placeholderRuleta.setPreferredSize(new Dimension(220, 0));

        JPanel wrapperRuleta = new JPanel();
        wrapperRuleta.setOpaque(false);
        wrapperRuleta.setLayout(new BorderLayout());

        Ruleta ruleta = new Ruleta();
        ruleta.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        ruleta.setResultListener(ficha -> {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "Ruleta dijo: " + ficha, "Ruleta", JOptionPane.INFORMATION_MESSAGE);

            });
        });
        wrapperRuleta.add(ruleta, BorderLayout.NORTH);

        JButton btnGirar = new JButton("Girar ruleta");
        btnGirar.addActionListener(ev -> ruleta.girarAleatorio());

        JPanel topBox = new JPanel(new BorderLayout());
        topBox.setOpaque(false);
        topBox.add(ruleta, BorderLayout.NORTH);

        JPanel botonBox = new JPanel();
        botonBox.setOpaque(false);
        botonBox.add(btnGirar);

        wrapperRuleta.add(topBox, BorderLayout.NORTH);
        wrapperRuleta.add(botonBox, BorderLayout.CENTER);

        placeholderRuleta.add(wrapperRuleta, BorderLayout.NORTH);
        contenedor.add(placeholderRuleta, BorderLayout.WEST);

        JPanel accionesPanel = new JPanel();
        accionesPanel.setOpaque(false);
        accionesPanel.setPreferredSize(new Dimension(200, 0));
        accionesPanel.setLayout(new GridBagLayout());
        tableroPanel.setAccionesPanel(accionesPanel);
        contenedor.add(accionesPanel, BorderLayout.EAST);

        panelFondo.add(contenedor, new GridBagConstraints());

        setContentPane(panelFondo);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Tablero().setVisible(true));
    }
}

class TableroPanel extends JPanel {

    private int tamañoCelda = 110;
    private int filas = 6;
    private int columnas = 6;

    private Image[][] fichas = new Image[filas][columnas];

    private Image imgLobo;
    private Image imgVampiro;
    private Image imgMuerte;
    private Image imgZombie;

    private int filaSeleccionada = -1;
    private int columnaSeleccionada = -1;
    private java.util.List<Point> movimientosValidos = new ArrayList<>();
    private java.util.List<Point> ataquesValidos = new ArrayList<>();

    private Pieza[][] tableroLogico = new Pieza[filas][columnas];

    private JPanel accionesPanel;

    private static final int NONE = 0;
    private static final int ATACAR_NORMAL = 1;
    private static final int CHUPAR = 2;
    private static final int LANZA = 3;
    private static final int CONJURAR_ZOMBIE = 4;
    private static final int MOVER_DOS = 5;

    private int modoActual = NONE;
    private boolean modoMover2Activo = false;

    public TableroPanel() {
        setPreferredSize(new Dimension(columnas * tamañoCelda, filas * tamañoCelda));
        setOpaque(false);

        imgLobo = cargarImagen("/Images/4.png");
        imgVampiro = cargarImagen("/Images/3.png");
        imgMuerte = cargarImagen("/Images/1.png");
        imgZombie = cargarImagen("/Images/2.png");

        fichas[0][0] = imgLobo;
        fichas[0][1] = imgVampiro;
        fichas[0][2] = imgMuerte;
        fichas[0][3] = imgMuerte;
        fichas[0][4] = imgVampiro;
        fichas[0][5] = imgLobo;

        fichas[5][0] = imgLobo;
        fichas[5][1] = imgVampiro;
        fichas[5][2] = imgMuerte;
        fichas[5][3] = imgMuerte;
        fichas[5][4] = imgVampiro;
        fichas[5][5] = imgLobo;

        tableroLogico[5][0] = new HombreLobo("Blanco");
        tableroLogico[5][1] = new Vampiro("Blanco");
        tableroLogico[5][2] = new Muerte("Blanco");
        tableroLogico[5][3] = new Muerte("Blanco");
        tableroLogico[5][4] = new Vampiro("Blanco");
        tableroLogico[5][5] = new HombreLobo("Blanco");

        tableroLogico[0][0] = new HombreLobo("Negro");
        tableroLogico[0][1] = new Vampiro("Negro");
        tableroLogico[0][2] = new Muerte("Negro");
        tableroLogico[0][3] = new Muerte("Negro");
        tableroLogico[0][4] = new Vampiro("Negro");
        tableroLogico[0][5] = new HombreLobo("Negro");

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                manejarClick(e);
            }
        });
    }

    private Image cargarImagen(String ruta) {
        try {
            return new ImageIcon(getClass().getResource(ruta)).getImage();
        } catch (Exception ex) {
            return null;
        }
    }

    public void setAccionesPanel(JPanel panel) {
        this.accionesPanel = panel;
        mostrarMensajeAcciones("Selecciona una ficha para ver sus habilidades.");
    }

    private void mostrarMensajeAcciones(String texto) {
        if (accionesPanel == null) {
            return;
        }
        accionesPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextArea area = new JTextArea(texto);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(false);
        area.setForeground(Color.WHITE);
        area.setFont(new Font("Arial", Font.BOLD, 14));
        area.setBorder(null);

        accionesPanel.setLayout(new GridBagLayout());
        accionesPanel.add(area, gbc);
        accionesPanel.revalidate();
        accionesPanel.repaint();
    }

    private void manejarClick(MouseEvent e) {
        int tableroWidth = columnas * tamañoCelda;
        int tableroHeight = filas * tamañoCelda;

        int startX = (getWidth() - tableroWidth) / 2;
        int startY = (getHeight() - tableroHeight) / 2;

        int col = (e.getX() - startX) / tamañoCelda;
        int fila = (e.getY() - startY) / tamañoCelda;

        if (fila < 0 || col < 0 || fila >= filas || col >= columnas) {
            return;
        }

        if (modoActual != NONE) {
            aplicarModoEnCasilla(modoActual, filaSeleccionada, columnaSeleccionada, fila, col);
            modoActual = NONE;
            modoMover2Activo = false;
            movimientosValidos.clear();
            ataquesValidos.clear();
            repaint();
            return;
        }

        for (Point p : movimientosValidos) {
            if (p.x == fila && p.y == col) {
                moverFicha(filaSeleccionada, columnaSeleccionada, fila, col);
                filaSeleccionada = -1;
                columnaSeleccionada = -1;
                movimientosValidos.clear();
                ataquesValidos.clear();
                repaint();
                return;
            }
        }

        if (fila == filaSeleccionada && col == columnaSeleccionada) {
            filaSeleccionada = -1;
            columnaSeleccionada = -1;
            movimientosValidos.clear();
            ataquesValidos.clear();
            mostrarMensajeAcciones("Selecciona una ficha para ver sus habilidades.");
            repaint();
            return;
        }

        if (fichas[fila][col] != null) {
            filaSeleccionada = fila;
            columnaSeleccionada = col;
            calcularMovimientosValidos(fila, col);
            ataquesValidos.clear();
            Pieza p = tableroLogico[fila][col];
            if (p != null) {
                mostrarBotonesPara(p);
            }
            repaint();
            return;
        }

        filaSeleccionada = -1;
        columnaSeleccionada = -1;
        movimientosValidos.clear();
        ataquesValidos.clear();
        mostrarMensajeAcciones("Selecciona una ficha para ver sus habilidades.");
        repaint();
    }

    private void moverFicha(int filaOrigen, int colOrigen, int filaDestino, int colDestino) {
        fichas[filaDestino][colDestino] = fichas[filaOrigen][colOrigen];
        fichas[filaOrigen][colOrigen] = null;
        if (tableroLogico[filaOrigen][colOrigen] != null) {
            tableroLogico[filaDestino][colDestino] = tableroLogico[filaOrigen][colOrigen];
            tableroLogico[filaOrigen][colOrigen] = null;
        }
    }

    private void calcularMovimientosValidos(int fila, int col) {
        movimientosValidos.clear();
        Pieza p = tableroLogico[fila][col];
        int rango = (p != null && p.moverDosCasillas() && modoMover2Activo) ? 2 : 1;
        for (int df = -rango; df <= rango; df++) {
            for (int dc = -rango; dc <= rango; dc++) {
                if (df == 0 && dc == 0) {
                    continue;
                }
                int nf = fila + df;
                int nc = col + dc;
                if (nf >= 0 && nf < filas && nc >= 0 && nc < columnas && fichas[nf][nc] == null) {
                    movimientosValidos.add(new Point(nf, nc));
                }
            }
        }
    }

    private void calcularAtaquesValidos(int fila, int col, int modo) {
        ataquesValidos.clear();
        if (modo == NONE) {
            return;
        }
        for (int df = -2; df <= 2; df++) {
            for (int dc = -2; dc <= 2; dc++) {
                if (df == 0 && dc == 0) {
                    continue;
                }
                int nf = fila + df;
                int nc = col + dc;
                if (nf < 0 || nf >= filas || nc < 0 || nc >= columnas) {
                    continue;
                }
                if (fichas[nf][nc] == null) {
                    continue;
                }
                int dr = Math.abs(df);
                int dcAbs = Math.abs(dc);
                if (modo == ATACAR_NORMAL || modo == CHUPAR) {
                    if (dr <= 1 && dcAbs <= 1) {
                        ataquesValidos.add(new Point(nf, nc));
                    }
                } else if (modo == LANZA) {
                    boolean distancia1Recta = (dr == 1 && dcAbs == 0) || (dr == 0 && dcAbs == 1);
                    boolean distancia2Recta = (dr == 2 && dcAbs == 0) || (dr == 0 && dcAbs == 2);
                    if (distancia1Recta || distancia2Recta) {
                        ataquesValidos.add(new Point(nf, nc));
                    }
                }
            }
        }
    }

    private void aplicarModoEnCasilla(int modo, int origenFila, int origenCol, int destFila, int destCol) {
        if (origenFila < 0 || origenCol < 0) {
            JOptionPane.showMessageDialog(this, "No hay pieza seleccionada para aplicar la accion.", "Accion", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Pieza origen = tableroLogico[origenFila][origenCol];
        if (origen == null) {
            JOptionPane.showMessageDialog(this, "La pieza seleccionada no tiene logica (imposible aplicar).", "Accion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Pieza objetivo = tableroLogico[destFila][destCol];

        switch (modo) {
            case ATACAR_NORMAL:
                if (objetivo == null) {
                    JOptionPane.showMessageDialog(this, "Selecciona una casilla con objetivo para atacar.", "Atacar", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                if (Math.abs(destFila - origenFila) <= 1 && Math.abs(destCol - origenCol) <= 1 && !(destFila == origenFila && destCol == origenCol)) {
                    objetivo.recibirDanio(origen.getAtaque());
                    JOptionPane.showMessageDialog(this, origen.getTipo() + " atacó a " + objetivo.getTipo() + ".", "Atacar", JOptionPane.INFORMATION_MESSAGE);
                    chequearMuerteYQuitar(destFila, destCol);
                } else {
                    JOptionPane.showMessageDialog(this, "Objetivo fuera de rango (debe ser adyacente).", "Atacar", JOptionPane.WARNING_MESSAGE);
                }
                break;

            case CHUPAR:
                if (!(origen instanceof Vampiro)) {
                    JOptionPane.showMessageDialog(this, "Solo Vampiro puede chupar sangre.", "Chupar", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (objetivo == null) {
                    JOptionPane.showMessageDialog(this, "Selecciona una casilla con objetivo para chupar.", "Chupar", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                if (Math.abs(destFila - origenFila) <= 1 && Math.abs(destCol - origenCol) <= 1 && !(destFila == origenFila && destCol == origenCol)) {
                    objetivo.recibirDanio(1);
                    ((Vampiro) origen).chuparSangre(objetivo);
                    JOptionPane.showMessageDialog(this, "Vampiro chupó sangre a " + objetivo.getTipo() + ".", "Chupar", JOptionPane.INFORMATION_MESSAGE);
                    chequearMuerteYQuitar(destFila, destCol);
                } else {
                    JOptionPane.showMessageDialog(this, "Objetivo fuera de rango (debe ser adyacente).", "Chupar", JOptionPane.WARNING_MESSAGE);
                }
                break;

            case LANZA:
                if (!(origen instanceof Muerte)) {
                    JOptionPane.showMessageDialog(this, "Solo Muerte puede lanzar lanza.", "Lanza", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (objetivo == null) {
                    JOptionPane.showMessageDialog(this, "Selecciona una casilla con objetivo para la lanza.", "Lanza", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                int dr = Math.abs(destFila - origenFila);
                int dc = Math.abs(destCol - origenCol);
                boolean esDistancia1Recta = (dr == 1 && dc == 0) || (dr == 0 && dc == 1);
                boolean esDistancia2Recta = (dr == 2 && dc == 0) || (dr == 0 && dc == 2);
                if (esDistancia1Recta || esDistancia2Recta) {
                    ((Muerte) origen).lanzarLanza(objetivo);
                    JOptionPane.showMessageDialog(this, "Muerte lanzó lanza a " + objetivo.getTipo() + ".", "Lanza", JOptionPane.INFORMATION_MESSAGE);
                    chequearMuerteYQuitar(destFila, destCol);
                } else {
                    JOptionPane.showMessageDialog(this, "La lanza alcanza 1 casilla adyacente o exactamente 2 casillas en línea recta.", "Lanza", JOptionPane.WARNING_MESSAGE);
                }
                break;

            case CONJURAR_ZOMBIE:
                if (!(origen instanceof Muerte)) {
                    JOptionPane.showMessageDialog(this, "Solo Muerte puede conjurar zombies.", "Conjurar", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (objetivo != null) {
                    JOptionPane.showMessageDialog(this, "Selecciona una casilla vacía para colocar el zombie.", "Conjurar", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                Zombie z = ((Muerte) origen).conjurarZombie();
                if (z == null) {
                    JOptionPane.showMessageDialog(this, "No se pudo conjurar más zombies (límite alcanzado).", "Conjurar", JOptionPane.WARNING_MESSAGE);
                } else {
                    tableroLogico[destFila][destCol] = z;
                    fichas[destFila][destCol] = imgZombie;
                    JOptionPane.showMessageDialog(this, "Zombie conjurado y colocado.", "Conjurar", JOptionPane.INFORMATION_MESSAGE);
                }
                break;

            default:
                JOptionPane.showMessageDialog(this, "Modo no soportado.", "Acción", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void chequearMuerteYQuitar(int fila, int col) {
        Pieza p = tableroLogico[fila][col];
        if (p != null && !p.isVivo()) {
            tableroLogico[fila][col] = null;
            fichas[fila][col] = null;
            JOptionPane.showMessageDialog(this, p.getTipo() + " ha muerto y fue removido del tablero.", "Muerto", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void mostrarBotonesPara(Pieza pieza) {
        if (accionesPanel == null) {
            return;
        }
        accionesPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;

        JLabel titulo = new JLabel("Ficha: " + pieza.getTipo() + " (" + pieza.getColor() + ")");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        accionesPanel.setLayout(new GridBagLayout());
        accionesPanel.add(titulo, gbc);
        gbc.gridy++;

        JButton btnEstado = new JButton("Ver estado");
        btnEstado.addActionListener(e -> JOptionPane.showMessageDialog(this, pieza.toString(), "Estado", JOptionPane.INFORMATION_MESSAGE));
        accionesPanel.add(btnEstado, gbc);
        gbc.gridy++;

        JButton btnAtq = new JButton("Atacar normal");
        btnAtq.addActionListener(e -> {
            modoActual = ATACAR_NORMAL;
            calcularAtaquesValidos(filaSeleccionada, columnaSeleccionada, modoActual);
            JOptionPane.showMessageDialog(this, "Modo: Atacar activado. Haz click en el objetivo adyacente.", "Atacar", JOptionPane.INFORMATION_MESSAGE);
            repaint();
        });
        accionesPanel.add(btnAtq, gbc);
        gbc.gridy++;

        String tipo = pieza.getTipo();
        if ("Vampiro".equals(tipo)) {
            JButton btnChupar = new JButton("Chupar sangre");
            btnChupar.addActionListener(e -> {
                modoActual = CHUPAR;
                calcularAtaquesValidos(filaSeleccionada, columnaSeleccionada, modoActual);
                JOptionPane.showMessageDialog(this, "Modo: Chupar activado. Haz click en el objetivo adyacente.", "Chupar", JOptionPane.INFORMATION_MESSAGE);
                repaint();
            });
            accionesPanel.add(btnChupar, gbc);
            gbc.gridy++;
        } else if ("HombreLobo".equals(tipo)) {
            JButton btnMover2 = new JButton(modoMover2Activo ? "Mover 2 (ON)" : "Mover 2 (OFF)");
            btnMover2.addActionListener(e -> {
                modoMover2Activo = !modoMover2Activo;
                btnMover2.setText(modoMover2Activo ? "Mover 2 (ON)" : "Mover 2 (OFF)");
                if (filaSeleccionada != -1 && columnaSeleccionada != -1) {
                    calcularMovimientosValidos(filaSeleccionada, columnaSeleccionada);
                }
                JOptionPane.showMessageDialog(this, "Modo Mover 2 " + (modoMover2Activo ? "activado" : "desactivado") + ". Haz click en destino vacío.", "Mover 2", JOptionPane.INFORMATION_MESSAGE);
                repaint();
            });
            accionesPanel.add(btnMover2, gbc);
            gbc.gridy++;
        } else if ("Muerte".equals(tipo)) {
            JButton btnLanza = new JButton("Lanzar lanza");
            btnLanza.addActionListener(e -> {
                modoActual = LANZA;
                calcularAtaquesValidos(filaSeleccionada, columnaSeleccionada, modoActual);
                JOptionPane.showMessageDialog(this, "Modo: Lanza activado. Haz click en objetivo a 1 o 2 casillas en línea recta.", "Lanza", JOptionPane.INFORMATION_MESSAGE);
                repaint();
            });
            accionesPanel.add(btnLanza, gbc);
            gbc.gridy++;

            JButton btnConj = new JButton("Conjurar Zombie");
            btnConj.addActionListener(e -> {
                modoActual = CONJURAR_ZOMBIE;
                JOptionPane.showMessageDialog(this, "Modo: Conjurar activado. Haz click en casilla vacía para colocar zombie.", "Conjurar", JOptionPane.INFORMATION_MESSAGE);
            });
            accionesPanel.add(btnConj, gbc);
            gbc.gridy++;
        } else if ("Zombie".equals(tipo)) {
            JButton btnInfo = new JButton("Info Zombie");
            btnInfo.addActionListener(e -> JOptionPane.showMessageDialog(this, "Zombie: vida 1. Solo ataca por orden de Muerte.", "Zombie", JOptionPane.INFORMATION_MESSAGE));
            accionesPanel.add(btnInfo, gbc);
            gbc.gridy++;
        }

        JButton btnCancelar = new JButton("Cancelar modo");
        btnCancelar.addActionListener(e -> {
            modoActual = NONE;
            modoMover2Activo = false;
            movimientosValidos.clear();
            ataquesValidos.clear();
            if (filaSeleccionada != -1) {
                calcularMovimientosValidos(filaSeleccionada, columnaSeleccionada);
            }
            JOptionPane.showMessageDialog(this, "Modos cancelados.", "Cancelar", JOptionPane.INFORMATION_MESSAGE);
            repaint();
        });
        accionesPanel.add(btnCancelar, gbc);
        gbc.gridy++;

        accionesPanel.revalidate();
        accionesPanel.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int tableroWidth = columnas * tamañoCelda;
        int tableroHeight = filas * tamañoCelda;

        int startX = (getWidth() - tableroWidth) / 2;
        int startY = (getHeight() - tableroHeight) / 2;

        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                int x = startX + col * tamañoCelda;
                int y = startY + fila * tamañoCelda;

                g2.setColor((fila + col) % 2 == 0 ? new Color(60, 60, 90) : new Color(40, 40, 70));
                g2.fillRect(x, y, tamañoCelda, tamañoCelda);

                for (Point p : movimientosValidos) {
                    if (p.x == fila && p.y == col) {
                        g2.setColor(new Color(0, 255, 0, 100));
                        g2.fillRect(x, y, tamañoCelda, tamañoCelda);
                    }
                }

                for (Point p : ataquesValidos) {
                    if (p.x == fila && p.y == col) {
                        g2.setColor(new Color(255, 0, 0, 100));
                        g2.fillRect(x, y, tamañoCelda, tamañoCelda);
                    }
                }

                if (fila == filaSeleccionada && col == columnaSeleccionada) {
                    g2.setColor(new Color(255, 255, 0, 100));
                    g2.fillRect(x, y, tamañoCelda, tamañoCelda);
                }

                g2.setColor(new Color(120, 120, 180));
                g2.setStroke(new BasicStroke(2));
                g2.drawRect(x, y, tamañoCelda, tamañoCelda);

                if (fichas[fila][col] != null) {
                    g2.drawImage(
                            fichas[fila][col],
                            x + 10,
                            y + 10,
                            tamañoCelda - 20,
                            tamañoCelda - 20,
                            this
                    );
                } else {
                    Pieza p = tableroLogico[fila][col];
                    if (p != null && imgZombie != null && p.getTipo().equals("Zombie")) {
                        g2.drawImage(imgZombie, x + 10, y + 10, tamañoCelda - 20, tamañoCelda - 20, this);
                    }
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(columnas * tamañoCelda, filas * tamañoCelda);
    }
}