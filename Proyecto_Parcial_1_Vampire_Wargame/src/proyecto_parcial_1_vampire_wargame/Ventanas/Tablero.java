/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_parcial_1_vampire_wargame.Ventanas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import proyecto_parcial_1_vampire_wargame.Panel;
import proyecto_parcial_1_vampire_wargame.Fichas.Pieza;
import proyecto_parcial_1_vampire_wargame.Fichas.Vampiro;
import proyecto_parcial_1_vampire_wargame.Fichas.HombreLobo;
import proyecto_parcial_1_vampire_wargame.Fichas.Muerte;
import proyecto_parcial_1_vampire_wargame.Fichas.Zombie;
import proyecto_parcial_1_vampire_wargame.Player;
import proyecto_parcial_1_vampire_wargame.Ruleta;
import java.awt.Point;

public class Tablero extends JFrame {

    private Player jugadorBlanco;
    private Player jugadorNegro;

    public Tablero(Player jugadorBlanco, Player jugadorNegro) {
        this.jugadorBlanco = jugadorBlanco;
        this.jugadorNegro = jugadorNegro;
        initFrame();
    }

    private void initFrame() {
        setTitle("Tablero Visual - Vampire Wargame");
        setSize(1300, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        Panel panelFondo = new Panel("/Images/Fondo4.jpg");
        panelFondo.setOpaque(false);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setOpaque(false);

        TableroPanel tableroPanel = new TableroPanel(jugadorBlanco, jugadorNegro);
        contenedor.add(tableroPanel, BorderLayout.CENTER);

        JPanel placeholderRuleta = tableroPanel.getRuletaPanel();
        placeholderRuleta.setOpaque(false);
        placeholderRuleta.setPreferredSize(new Dimension(260, 0));
        contenedor.add(placeholderRuleta, BorderLayout.WEST);

        JPanel accionesPanel = new JPanel();
        accionesPanel.setOpaque(false);
        accionesPanel.setPreferredSize(new Dimension(220, 0));
        accionesPanel.setLayout(new GridBagLayout());
        tableroPanel.setAccionesPanel(accionesPanel);
        contenedor.add(accionesPanel, BorderLayout.EAST);

        panelFondo.add(contenedor, new GridBagConstraints());
        setContentPane(panelFondo);
    }
}

class TableroPanel extends JPanel {

    private final int tamañoCelda = 110;
    private final int filas = 6;
    private final int columnas = 6;

    private final Image[][] fichas = new Image[filas][columnas];
    private Image imgLoboNegro, imgLoboBlanco, imgVampiroNegro, imgVampiroBlanco,
            imgMuerteNegro, imgMuerteBlanco, imgZombieNegro, imgZombieBlanco;

    private int filaSeleccionada = -1;
    private int columnaSeleccionada = -1;
    private final List<Point> movimientosValidos = new ArrayList<>();
    private final List<Point> ataquesValidos = new ArrayList<>();
    private final boolean[][] posiblesPostCaptura = new boolean[filas][columnas];

    private final Pieza[][] tableroLogico = new Pieza[filas][columnas];

    private JPanel accionesPanel;

    private static final int NONE = 0;
    private static final int ATACAR_NORMAL = 1;
    private static final int CHUPAR = 2;
    private static final int LANZA = 3;
    private static final int CONJURAR_ZOMBIE = 4;
    private static final int ORDER_ZOMBIES = 6;

    private int modoActual = NONE;
    private boolean modoMover2Activo = false;

    private String turnoActual = "Blanco";
    private final Player jugadorBlanco;
    private final Player jugadorNegro;

    private Ruleta ruleta;
    private JPanel ruletaPanelContenedor;
    private JLabel etiquetaTurno;

    private String selectedPieceType = null;
    private String selectedPieceTypeKey = null;

    private boolean gameOver = false;

    public TableroPanel(Player jugadorBlanco, Player jugadorNegro) {
        this.jugadorBlanco = jugadorBlanco;
        this.jugadorNegro = jugadorNegro;

        setPreferredSize(new Dimension(columnas * tamañoCelda, filas * tamañoCelda));
        setOpaque(false);

        cargarRecursos();
        inicializarTableroLogicoYVisual();
        configurarMouse();
        crearPanelRuleta();
        inicioTurno();
    }

    private void cargarRecursos() {
        imgLoboNegro = cargarImagen("/Images/HombreLoboNegro.png");
        imgLoboBlanco = cargarImagen("/Images/HombreLoboBlanco.png");
        imgVampiroNegro = cargarImagen("/Images/VampiroNegro.png");
        imgVampiroBlanco = cargarImagen("/Images/VampiroBlanco.png");
        imgMuerteNegro = cargarImagen("/Images/MuerteNegro.png");
        imgMuerteBlanco = cargarImagen("/Images/MuerteBlanco.png");
        imgZombieNegro = cargarImagen("/Images/ZombieNegro.png");
        imgZombieBlanco = cargarImagen("/Images/ZombieBlanco.png");
    }

    private void inicializarTableroLogicoYVisual() {
        fichas[5][0] = imgLoboBlanco;
        fichas[5][1] = imgVampiroBlanco;
        fichas[5][2] = imgMuerteBlanco;
        fichas[5][3] = imgMuerteBlanco;
        fichas[5][4] = imgVampiroBlanco;
        fichas[5][5] = imgLoboBlanco;

        fichas[0][0] = imgLoboNegro;
        fichas[0][1] = imgVampiroNegro;
        fichas[0][2] = imgMuerteNegro;
        fichas[0][3] = imgMuerteNegro;
        fichas[0][4] = imgVampiroNegro;
        fichas[0][5] = imgLoboNegro;

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
    }

    private void configurarMouse() {
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

    private void crearPanelRuleta() {
        ruleta = new Ruleta();
        ruleta.setPreferredSize(new Dimension(220, 260));
        ruleta.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        ruleta.setResultListener(ficha -> SwingUtilities.invokeLater(() -> manejarResultadoRuleta(ficha)));

        etiquetaTurno = new JLabel("", SwingConstants.CENTER);
        etiquetaTurno.setFont(new Font("Arial", Font.BOLD, 18));
        etiquetaTurno.setForeground(Color.WHITE);
        etiquetaTurno.setPreferredSize(new Dimension(220, 36));

        ruletaPanelContenedor = new JPanel(new BorderLayout());
        ruletaPanelContenedor.setOpaque(false);
        ruletaPanelContenedor.add(etiquetaTurno, BorderLayout.NORTH);
        ruletaPanelContenedor.add(ruleta, BorderLayout.CENTER);
    }

    public JPanel getRuletaPanel() {
        return ruletaPanelContenedor != null ? ruletaPanelContenedor : new JPanel();
    }

    private void manejarResultadoRuleta(String ficha) {
        if (gameOver) {
            return;
        }

        String fichaKey;
        if (ficha == null) {
            fichaKey = null;
        } else {
            String f = ficha.trim().toLowerCase();
            if (f.equals("hombre lobo") || f.equals("hombrelobo")) {
                fichaKey = "hombrelobo";
            } else if (f.equals("vampiro")) {
                fichaKey = "vampiro";
            } else if (f.equals("muerte")) {
                fichaKey = "muerte";
            } else if (f.equals("zombie")) {
                fichaKey = "zombie";
            } else {
                fichaKey = f.replaceAll("\\s+", "");
            }
        }

        int piezasDisponibles = contarPiezasTipo(turnoActual, fichaKey);

        if (piezasDisponibles == 0) {
            if (ruleta != null && ruleta.getSpinsUsed() == 1) {
                showInfo("La ruleta salió '" + ficha + "' pero no tienes piezas de ese tipo.\nPuedes volver a girar una vez",
                        "Ruleta - re-giro permitido");
                ruleta.enableSpinButton(true);
                ruleta.setSelectedPieceLabel(null);
                return;
            } else {
                if (ruleta != null && ruleta.getSpinsUsed() < ruleta.getSpinsAllowed()) {
                    ruleta.enableSpinButton(true);
                    ruleta.setSelectedPieceLabel(null);
                    showInfo("La ruleta salió '" + ficha + "' pero no tienes piezas de ese tipo.\nIntenta de nuevo.",
                            "Ruleta - sin piezas");
                    return;
                } else {
                    showInfo("La ruleta salió '" + ficha + "' y no obtuviste ninguna ficha válida.\nHas perdido el turno.",
                            "Ruleta - turno perdido");
                    ruleta.setSelectedPieceLabel(null);
                    ruleta.enableSpinButton(false);
                    cambiarTurno();
                    return;
                }
            }
        }

        selectedPieceType = ficha;
        selectedPieceTypeKey = fichaKey;
        if (ruleta != null) {
            ruleta.setSelectedPieceLabel(ficha);
            if (ruleta.getSpinsUsed() >= 1) {
                ruleta.enableSpinButton(false);
            }
        }

        mostrarMensajeAcciones("Puedes mover solo fichas tipo: " + ficha + " (" + turnoActual + ").");
    }

    private void inicioTurno() {
        int spinsAllowed = calcularSpinsPermitidos(turnoActual);
        if (ruleta != null) {
            ruleta.setSpinsAllowed(spinsAllowed);
            ruleta.resetSpins();
            ruleta.setSelectedPieceLabel(null);
            ruleta.enableSpinButton(!gameOver);
        }

        movimientosValidos.clear();
        ataquesValidos.clear();
        resetPosiblesPostCaptura();
        filaSeleccionada = -1;
        columnaSeleccionada = -1;
        selectedPieceType = null;
        selectedPieceTypeKey = null;

        String usernameTurno = turnoActual.equals("Blanco") && jugadorBlanco != null
                ? jugadorBlanco.getUsername()
                : (turnoActual.equals("Negro") && jugadorNegro != null ? jugadorNegro.getUsername() : turnoActual);
        if (etiquetaTurno != null) {
            etiquetaTurno.setText("Turno: " + usernameTurno);
        }

        mostrarMensajeAcciones("Debes girar la ruleta (" + spinsAllowed + " giro(s) permitido(s)) y seleccionar la ficha indicada.");
        repaint();
    }

    private int calcularSpinsPermitidos(String color) {
        final int inicial = 6;
        int actuales = contarPiezas(color);
        int perdidas = inicial - actuales;
        if (perdidas >= 4) {
            return 3;
        }
        if (perdidas >= 2) {
            return 2;
        }
        return 1;
    }

    private int contarPiezas(String color) {
        int cnt = 0;
        for (int r = 0; r < filas; r++) {
            for (int c = 0; c < columnas; c++) {
                Pieza p = tableroLogico[r][c];
                if (p != null && color.equals(p.getColor())) {
                    String tipo = p.getTipo() == null ? "" : p.getTipo().replaceAll("\\s+", "").toLowerCase();
                    if (!"zombie".equals(tipo)) {
                        cnt++;
                    }
                }
            }
        }
        return cnt;
    }

    private int contarPiezasTipo(String color, String tipoKey) {
        if (tipoKey == null) {
            return 0;
        }
        String keyNorm = tipoKey.replaceAll("\\s+", "").toLowerCase();
        int cnt = 0;
        for (int r = 0; r < filas; r++) {
            for (int c = 0; c < columnas; c++) {
                Pieza p = tableroLogico[r][c];
                if (p != null && color.equals(p.getColor())) {
                    String tipoP = p.getTipo() == null ? "" : p.getTipo().replaceAll("\\s+", "").toLowerCase();
                    if (tipoP.equals(keyNorm)) {
                        cnt++;
                    }
                }
            }
        }
        return cnt;
    }

    private void cambiarTurno() {
        turnoActual = turnoActual.equals("Blanco") ? "Negro" : "Blanco";
        inicioTurno();
    }

    public void setAccionesPanel(JPanel panel) {
        this.accionesPanel = panel;
        accionesPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextArea area = new JTextArea("Selecciona una ficha para ver sus habilidades.");
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(false);
        area.setForeground(Color.WHITE);
        area.setFont(new Font("Arial", Font.BOLD, 14));
        area.setBorder(null);

        accionesPanel.setLayout(new GridBagLayout());
        accionesPanel.add(area, gbc);
        gbc.gridy++;

        JButton btnRendirse = new JButton("Rendirse");
        btnRendirse.addActionListener(e -> {
            if (gameOver) {
                showInfo("La partida ya terminó.", "Rendirse");
                return;
            }
            Player perdedor = turnoActual.equals("Blanco") ? jugadorBlanco : jugadorNegro;
            Player ganador = turnoActual.equals("Blanco") ? jugadorNegro : jugadorBlanco;
            handleVictory(ganador, perdedor, "rendición");
        });
        accionesPanel.add(btnRendirse, gbc);
        gbc.gridy++;

        accionesPanel.revalidate();
        accionesPanel.repaint();
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
        gbc.gridy++;

        JButton btnRendirse = new JButton("Rendirse");
        btnRendirse.addActionListener(e -> {
            if (gameOver) {
                showInfo("La partida ya terminó.", "Rendirse");
                return;
            }
            Player perdedor = turnoActual.equals("Blanco") ? jugadorBlanco : jugadorNegro;
            Player ganador = turnoActual.equals("Blanco") ? jugadorNegro : jugadorBlanco;
            handleVictory(ganador, perdedor, "rendición");
        });
        accionesPanel.add(btnRendirse, gbc);
        gbc.gridy++;

        accionesPanel.revalidate();
        accionesPanel.repaint();
    }

    private void manejarClick(MouseEvent e) {
        if (gameOver) {
            return;
        }

        int tableroWidth = columnas * tamañoCelda;
        int tableroHeight = filas * tamañoCelda;

        int startX = (getWidth() - tableroWidth) / 2;
        int startY = (getHeight() - tableroHeight) / 2;

        int col = (e.getX() - startX) / tamañoCelda;
        int fila = (e.getY() - startY) / tamañoCelda;

        if (fila < 0 || col < 0 || fila >= filas || col >= columnas) {
            return;
        }

        if (selectedPieceTypeKey == null) {
            showInfo("Gira la ruleta para seleccionar el tipo de ficha que puedes mover.", "Turno");
            return;
        }

        Pieza piezaEnCasilla = tableroLogico[fila][col];

        if (modoActual == NONE) {
            if (piezaEnCasilla != null) {
                if (!turnoActual.equals(piezaEnCasilla.getColor())) {
                    showInfo("No puedes seleccionar una ficha del oponente.", "Selección");
                    return;
                }
                String tipoFichaEnTableroKey = piezaEnCasilla.getTipo() == null ? "" : piezaEnCasilla.getTipo().replaceAll("\\s+", "").toLowerCase();
                if (!selectedPieceTypeKey.replaceAll("\\s+", "").toLowerCase().equals(tipoFichaEnTableroKey)) {
                    showInfo("Solo puedes seleccionar fichas tipo: " + selectedPieceType, "Selección");
                    return;
                }
            }
        }

        if (modoActual != NONE) {
            boolean accionExitosa = aplicarModoEnCasilla(modoActual, filaSeleccionada, columnaSeleccionada, fila, col);
            modoActual = NONE;
            modoMover2Activo = false;
            movimientosValidos.clear();
            ataquesValidos.clear();
            repaint();
            if (accionExitosa) {
                cambiarTurno();
            }
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
                chequearVictoria();
                cambiarTurno();
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
        chequearVictoria();
    }

    private void calcularMovimientosValidos(int fila, int col) {
        movimientosValidos.clear();
        Pieza p = tableroLogico[fila][col];
        if (p == null) {
            return;
        }
        if ("Zombie".equals(p.getTipo())) {
            return;
        }

        if (p.moverDosCasillas() && modoMover2Activo) {
            int[][] deltas2 = {
                {-2, 0}, {2, 0}, {0, -2}, {0, 2},
                {-2, -2}, {-2, 2}, {2, -2}, {2, 2}
            };
            for (int[] d : deltas2) {
                int nf = fila + d[0], nc = col + d[1];
                if (inBounds(nf, nc) && fichas[nf][nc] == null) {
                    movimientosValidos.add(new Point(nf, nc));
                }
            }
            return;
        }

        int rango = 1;
        boolean[][] visited = new boolean[filas][columnas];
        visited[fila][col] = true;
        calcularMovimientos(fila, col, rango, visited, movimientosValidos);
    }

    //Recursiva Down
    private void calcularMovimientos(int r, int c, int pasosRestantes, boolean[][] visited, List<Point> salida) {
        if (pasosRestantes <= 0) {
            return;
        }
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) {
                    continue;
                }
                int nr = r + dr;
                int nc = c + dc;

                if (!inBounds(nr, nc)) {
                    continue;
                }
                if (visited[nr][nc]) {
                    continue;
                }

                if (fichas[nr][nc] != null) {
                    visited[nr][nc] = true;
                    continue;
                }

                salida.add(new Point(nr, nc));
                visited[nr][nc] = true;
                calcularMovimientos(nr, nc, pasosRestantes - 1, visited, salida);
            }
        }
    }

    private void calcularAtaquesValidos(int fila, int col, int modo) {
        ataquesValidos.clear();
        if (modo == NONE) {
            return;
        }
        Pieza origen = tableroLogico[fila][col];
        String colorOrigen = origen != null ? origen.getColor() : null;

        for (int df = -2; df <= 2; df++) {
            for (int dc = -2; dc <= 2; dc++) {
                if (df == 0 && dc == 0) {
                    continue;
                }
                int nf = fila + df, nc = col + dc;
                if (!inBounds(nf, nc)) {
                    continue;
                }
                if (fichas[nf][nc] == null) {
                    continue;
                }
                Pieza objetivo = tableroLogico[nf][nc];
                if (objetivo == null) {
                    continue;
                }
                if (colorOrigen != null && colorOrigen.equals(objetivo.getColor())) {
                    continue;
                }

                int dr = Math.abs(df), dcAbs = Math.abs(dc);
                if (modo == ATACAR_NORMAL || modo == CHUPAR) {
                    if (dr <= 1 && dcAbs <= 1) {
                        ataquesValidos.add(new Point(nf, nc));
                    }
                } else if (modo == LANZA) {
                    boolean d1 = (dr == 1 && dcAbs == 0) || (dr == 0 && dcAbs == 1);
                    boolean d2 = (dr == 2 && dcAbs == 0) || (dr == 0 && dcAbs == 2);
                    if (d1 || d2) {
                        ataquesValidos.add(new Point(nf, nc));
                    }
                }
            }
        }
    }

    private boolean aplicarModoEnCasilla(int modo, int origenFila, int origenCol, int destFila, int destCol) {
        if (gameOver) {
            return false;
        }
        if (origenFila < 0 || origenCol < 0) {
            showInfo("No hay pieza seleccionada para aplicar la accion.", "Accion");
            return false;
        }
        Pieza origen = tableroLogico[origenFila][origenCol];
        if (origen == null) {
            showInfo("La pieza seleccionada no tiene logica (imposible aplicar).", "Accion");
            return false;
        }
        if (!turnoActual.equals(origen.getColor())) {
            showInfo("No es tu turno para usar esa ficha.", "Error");
            return false;
        }

        Pieza objetivo = inBounds(destFila, destCol) ? tableroLogico[destFila][destCol] : null;
        switch (modo) {
            case ATACAR_NORMAL:
                return handleAtacarNormal(origen, objetivo, origenFila, origenCol, destFila, destCol);
            case CHUPAR:
                return handleChupar(origen, objetivo, origenFila, origenCol, destFila, destCol);
            case LANZA:
                return handleLanza(origen, objetivo, origenFila, origenCol, destFila, destCol);
            case CONJURAR_ZOMBIE:
                return handleConjurar(origen, objetivo, destFila, destCol);
            case ORDER_ZOMBIES:
                return handleOrderZombies(origen, objetivo, destFila, destCol);
            default:
                showInfo("Modo no soportado.", "Acción");
                return false;
        }
    }

    private boolean handleAtacarNormal(Pieza origen, Pieza objetivo, int of, int oc, int df, int dc) {
        if (objetivo == null) {
            showInfo("Selecciona una casilla con objetivo para atacar.", "Atacar");
            return false;
        }
        if (origen.getColor().equals(objetivo.getColor())) {
            showInfo("No puedes atacar a tus propias fichas.", "Atacar");
            return false;
        }
        if (Math.abs(df - of) <= 1 && Math.abs(dc - oc) <= 1 && !(df == of && dc == oc)) {
            objetivo.recibirDanio(origen.getAtaque());
            showInfo(origen.getTipo() + " atacó a " + objetivo.getTipo() + ".", "Atacar");
            boolean huboMuerte = chequearMuerteYQuitar(df, dc);
            if (huboMuerte) {
                actualizarPosiblesPostCaptura(turnoActual);
            }
            mostrarResumenAtaque(objetivo, df, dc);
            chequearVictoria();
            return true;
        } else {
            showInfo("Objetivo fuera de rango (debe ser adyacente).", "Atacar");
            return false;
        }
    }

    private boolean handleChupar(Pieza origen, Pieza objetivo, int of, int oc, int df, int dc) {
        if (!(origen instanceof Vampiro)) {
            showInfo("Solo Vampiro puede chupar sangre.", "Chupar");
            return false;
        }
        if (objetivo == null) {
            showInfo("Selecciona una casilla con objetivo para chupar.", "Chupar");
            return false;
        }
        if (origen.getColor().equals(objetivo.getColor())) {
            showInfo("No puedes chupar a tus propias fichas.", "Chupar");
            return false;
        }
        if (Math.abs(df - of) <= 1 && Math.abs(dc - oc) <= 1 && !(df == of && dc == oc)) {
            ((Vampiro) origen).chuparSangre(objetivo);
            showInfo("Vampiro chupó sangre a " + objetivo.getTipo() + ".", "Chupar");
            boolean huboMuerte = chequearMuerteYQuitar(df, dc);
            if (huboMuerte) {
                actualizarPosiblesPostCaptura(turnoActual);
            }
            mostrarResumenAtaque(objetivo, df, dc);
            chequearVictoria();
            return true;
        } else {
            showInfo("Objetivo fuera de rango (debe ser adyacente).", "Chupar");
            return false;
        }
    }

    private boolean handleLanza(Pieza origen, Pieza objetivo, int of, int oc, int df, int dc) {
        if (!(origen instanceof Muerte)) {
            showInfo("Solo Muerte puede lanzar lanza.", "Lanza");
            return false;
        }
        if (objetivo == null) {
            showInfo("Selecciona una casilla con objetivo para la lanza.", "Lanza");
            return false;
        }
        if (origen.getColor().equals(objetivo.getColor())) {
            showInfo("No puedes lanzar la lanza a tus propias fichas.", "Lanza");
            return false;
        }

        int dr = Math.abs(df - of), dcAbs = Math.abs(dc - oc);
        boolean es1 = (dr == 1 && dcAbs == 0) || (dr == 0 && dcAbs == 1);
        boolean es2 = (dr == 2 && dcAbs == 0) || (dr == 0 && dcAbs == 2);

        if (es1 || es2) {
            ((Muerte) origen).lanzarLanza(objetivo);
            showInfo("Muerte lanzó lanza a " + objetivo.getTipo() + ".", "Lanza");

            boolean huboMuerte = chequearMuerteYQuitar(df, dc);
            if (huboMuerte) {
                resetPosiblesPostCaptura();
                actualizarPosiblesPostCaptura(turnoActual);
            }
            mostrarResumenAtaque(objetivo, df, dc);
            chequearVictoryAfterAction();
            return true;
        } else {
            showInfo("La lanza alcanza 1 casilla adyacente o exactamente 2 casillas en línea recta.", "Lanza");
            return false;
        }
    }

    private boolean handleConjurar(Pieza origen, Pieza objetivo, int df, int dc) {
        if (!(origen instanceof Muerte)) {
            showInfo("Solo Muerte puede conjurar zombies.", "Conjurar");
            return false;
        }
        if (objetivo != null) {
            showInfo("Selecciona una casilla vacía para colocar el zombie.", "Conjurar");
            return false;
        }
        Zombie z = ((Muerte) origen).conjurarZombie();
        if (z == null) {
            showInfo("No se pudo conjurar más zombies (límite alcanzado).", "Conjurar");
            return false;
        }
        tableroLogico[df][dc] = z;
        fichas[df][dc] = origen.getColor().equals("Blanco") ? imgZombieBlanco : imgZombieNegro;
        showInfo("Zombie conjurado y colocado.", "Conjurar");
        actualizarPosiblesPostCaptura(turnoActual);
        chequearVictoryAfterAction();
        return true;
    }

    private boolean handleOrderZombies(Pieza origen, Pieza objetivo, int df, int dc) {
        if (!(origen instanceof Muerte)) {
            showInfo("Solo Muerte puede ordenar zombies.", "Ordenar Zombies");
            return false;
        }
        if (objetivo == null) {
            showInfo("Selecciona una pieza enemiga para que tus zombies la ataquen.", "Ordenar Zombies");
            return false;
        }
        if (origen.getColor().equals(objetivo.getColor())) {
            showInfo("No puedes ordenar a tus zombies atacar a tus propias fichas.", "Ordenar Zombies");
            return false;
        }
        List<Point> zombiesAdj = getAdjacentZombiesOfColor(df, dc, origen.getColor());
        if (zombiesAdj.isEmpty()) {
            showInfo("No hay zombies aliados adyacentes a ese objetivo.", "Ordenar Zombies");
            return false;
        }
        for (Point zpos : zombiesAdj) {
            Pieza zz = tableroLogico[zpos.x][zpos.y];
            if (zz instanceof Zombie) {
                ((Zombie) zz).ataqueOrdenado(objetivo);
            }
        }
        boolean huboMuerte2 = chequearMuerteYQuitar(df, dc);
        mostrarResumenAtaque(objetivo, df, dc);
        if (huboMuerte2) {
            actualizarPosiblesPostCaptura(turnoActual);
        }
        chequearVictoryAfterAction();
        return true;
    }

    private boolean chequearMuerteYQuitar(int fila, int col) {
        Pieza p = tableroLogico[fila][col];
        if (p != null && !p.isVivo()) {
            tableroLogico[fila][col] = null;
            fichas[fila][col] = null;
            showInfo(p.getTipo() + " ha muerto y fue removido del tablero.", "Muerto");

            movimientosValidos.clear();
            ataquesValidos.clear();
            resetPosiblesPostCaptura();
            actualizarPosiblesPostCaptura(turnoActual);

            if (filaSeleccionada == fila && columnaSeleccionada == col) {
                filaSeleccionada = -1;
                columnaSeleccionada = -1;
            }

            repaint();
            return true;
        }
        return false;
    }

    private void chequearVictoria() {
        if (gameOver) {
            return;
        }

        int blancas = contarPiezas(0, 0, "Blanco");
        int negras = contarPiezas(0, 0, "Negro");

        if (negras == 0) {
            handleVictory(jugadorBlanco, jugadorNegro, "eliminación");
        } else if (blancas == 0) {
            handleVictory(jugadorNegro, jugadorBlanco, "eliminación");
        }
    }

    private void chequearVictoryAfterAction() {
        chequearVictoria();
        if (gameOver && ruleta != null) {
            ruleta.enableSpinButton(false);
        }
    }

    private void handleVictory(Player ganador, Player perdedor, String motivo) {
        if (gameOver) {
            return;
        }
        gameOver = true;

        String nombreGanador = ganador != null ? ganador.getUsername() : "Desconocido";
        String nombrePerdedor = perdedor != null ? perdedor.getUsername() : "Desconocido";

        if (ganador != null) {
            try {
                ganador.agregarPuntos(3);
            } catch (Exception ignored) {
            }
        }

        try {
            proyecto_parcial_1_vampire_wargame.Almacenamiento.Manager mgr = proyecto_parcial_1_vampire_wargame.Almacenamiento.Manager.getInstance();
            if (ganador != null) {
                mgr.actualizarPlayer(ganador);
            }
            if (perdedor != null) {
                mgr.actualizarPlayer(perdedor);
            }

            String resultadoTexto;
            if (motivo != null && motivo.toLowerCase().contains("rend")) {
                resultadoTexto = nombreGanador + " venció por rendición a " + nombrePerdedor;
            } else if (motivo != null && motivo.toLowerCase().contains("elimin")) {
                resultadoTexto = nombreGanador + " venció por eliminación de todas las piezas a " + nombrePerdedor;
            } else {
                resultadoTexto = nombreGanador + " venció a " + nombrePerdedor + " (" + motivo + ")";
            }

            proyecto_parcial_1_vampire_wargame.GameLog log = new proyecto_parcial_1_vampire_wargame.GameLog(
                    nombreGanador.equals("Desconocido") ? null : nombreGanador,
                    nombrePerdedor.equals("Desconocido") ? null : nombrePerdedor,
                    resultadoTexto
            );

            mgr.agregarLog(log);
        } catch (Exception ex) {
            System.err.println("Warning: no se pudo guardar o actualizar en Manager: " + ex.getMessage());
        }

        showInfo("Victoria de " + nombreGanador + " por " + motivo + ".\n\n" + nombreGanador + " recibió 3 puntos.", "Partida finalizada");

        if (ruleta != null) {
            ruleta.enableSpinButton(false);
        }
        mostrarMensajeAcciones("Partida terminada. Ganador: " + nombreGanador + ".");

        try {
            Component root = SwingUtilities.getRoot(this);
            if (root instanceof JFrame) {
                ((JFrame) root).dispose();
            }
        } catch (Exception ex) {
            System.err.println("No se pudo cerrar la ventana del tablero: " + ex.getMessage());
        }

        try {
            if (jugadorBlanco != null) {
                new proyecto_parcial_1_vampire_wargame.Ventanas.MenuPrincipal(jugadorBlanco).setVisible(true);
            } else if (jugadorNegro != null) {
                new proyecto_parcial_1_vampire_wargame.Ventanas.MenuPrincipal(jugadorNegro).setVisible(true);
            }
        } catch (Exception ex) {
            System.err.println("Error al abrir MenuPrincipal: " + ex.getMessage());
        }
    }

    private void actualizarPosiblesPostCaptura(String color) {
        resetPosiblesPostCaptura();
        for (int r = 0; r < filas; r++) {
            for (int c = 0; c < columnas; c++) {
                Pieza pi = tableroLogico[r][c];
                if (pi != null && color.equals(pi.getColor())) {
                    for (int dr = -1; dr <= 1; dr++) {
                        for (int dc = -1; dc <= 1; dc++) {
                            if (dr == 0 && dc == 0) {
                                continue;
                            }
                            int nr = r + dr, nc = c + dc;
                            if (inBounds(nr, nc) && fichas[nr][nc] == null) {
                                posiblesPostCaptura[nr][nc] = true;
                            }
                        }
                    }
                }
            }
        }
    }

    private void resetPosiblesPostCaptura() {
        for (int r = 0; r < filas; r++) {
            for (int c = 0; c < columnas; c++) {
                posiblesPostCaptura[r][c] = false;
            }
        }
    }

    private boolean inBounds(int r, int c) {
        return r >= 0 && r < filas && c >= 0 && c < columnas;
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
            showInfo("Modo: Atacar activado. Haz click en el objetivo adyacente (solo enemigos).", "Atacar");
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
                showInfo("Modo: Chupar activado. Haz click en el objetivo adyacente (solo enemigos).", "Chupar");
                repaint();
            });
            accionesPanel.add(btnChupar, gbc);
            gbc.gridy++;
        } else if ("HombreLobo".equals(tipo) || "Hombre Lobo".equals(tipo) || "Hombrelobo".equals(tipo)) {
            JButton btnMover2 = new JButton(modoMover2Activo ? "Mover 2 (ON)" : "Mover 2 (OFF)");
            btnMover2.addActionListener(e -> {
                modoMover2Activo = !modoMover2Activo;
                btnMover2.setText(modoMover2Activo ? "Mover 2 (ON)" : "Mover 2 (OFF)");
                if (filaSeleccionada != -1 && columnaSeleccionada != -1) {
                    calcularMovimientosValidos(filaSeleccionada, columnaSeleccionada);
                }
                showInfo("Modo Mover 2 " + (modoMover2Activo ? "activado" : "desactivado") + ". Haz click en destino vacío.", "Mover 2");
                repaint();
            });
            accionesPanel.add(btnMover2, gbc);
            gbc.gridy++;
        } else if ("Muerte".equals(tipo) || "Death".equals(tipo)) {
            JButton btnLanza = new JButton("Lanzar lanza");
            btnLanza.addActionListener(e -> {
                modoActual = LANZA;
                calcularAtaquesValidos(filaSeleccionada, columnaSeleccionada, modoActual);
                showInfo("Modo: Lanza activado. Haz click en objetivo a 1 o 2 casillas en línea recta (solo enemigos).", "Lanza");
                repaint();
            });
            accionesPanel.add(btnLanza, gbc);
            gbc.gridy++;

            JButton btnConj = new JButton("Conjurar Zombie");
            btnConj.addActionListener(e -> {
                modoActual = CONJURAR_ZOMBIE;
                showInfo("Modo: Conjurar activado. Haz click en casilla vacía para colocar zombie.", "Conjurar");
            });
            accionesPanel.add(btnConj, gbc);
            gbc.gridy++;

            JButton btnOrdenarZombies = new JButton("Ordenar Zombies");
            btnOrdenarZombies.addActionListener(e -> {
                modoActual = ORDER_ZOMBIES;
                ataquesValidos.clear();
                String colorOrigen = pieza.getColor();
                List<Point> posibles = computeTargetsForZombieOrder(colorOrigen);
                if (posibles.isEmpty()) {
                    showInfo("No hay objetivos adyacentes a tus zombies para ordenarles atacar.", "Ordenar Zombies");
                    modoActual = NONE;
                    return;
                }
                ataquesValidos.addAll(posibles);
                showInfo("Modo: Ordenar Zombies activado. Haz click en una pieza enemiga que tenga zombies aliados adyacentes.", "Ordenar Zombies");
                repaint();
            });
            accionesPanel.add(btnOrdenarZombies, gbc);
            gbc.gridy++;
        } else if ("Zombie".equals(tipo)) {
            JButton btnInfo = new JButton("Info Zombie");
            btnInfo.addActionListener(e -> showInfo("Zombie: vida 1. Solo ataca por orden de Muerte.", "Zombie"));
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
            showInfo("Modos cancelados.", "Cancelar");
            repaint();
        });
        accionesPanel.add(btnCancelar, gbc);
        gbc.gridy++;

        JButton btnRendirse = new JButton("Rendirse");
        btnRendirse.addActionListener(e -> {
            if (gameOver) {
                showInfo("La partida ya terminó.", "Rendirse");
                return;
            }
            Player perdedor = turnoActual.equals("Blanco") ? jugadorBlanco : jugadorNegro;
            Player ganador = turnoActual.equals("Blanco") ? jugadorNegro : jugadorBlanco;
            handleVictory(ganador, perdedor, "rendición");
        });
        accionesPanel.add(btnRendirse, gbc);
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
                        g2.setColor(new Color(255, 0, 0, 130));
                        g2.fillRect(x, y, tamañoCelda, tamañoCelda);
                    }
                }

                if (posiblesPostCaptura[fila][col]) {
                    g2.setColor(new Color(0, 120, 255, 80));
                    g2.fillRect(x, y, tamañoCelda, tamañoCelda);
                }

                if (fila == filaSeleccionada && col == columnaSeleccionada) {
                    g2.setColor(new Color(255, 255, 0, 100));
                    g2.fillRect(x, y, tamañoCelda, tamañoCelda);
                }

                g2.setColor(new Color(120, 120, 180));
                g2.setStroke(new BasicStroke(2));
                g2.drawRect(x, y, tamañoCelda, tamañoCelda);

                if (fichas[fila][col] != null) {
                    g2.drawImage(fichas[fila][col], x + 10, y + 10, tamañoCelda - 20, tamañoCelda - 20, this);
                } else {
                    Pieza p = tableroLogico[fila][col];
                    if (p != null && "Zombie".equals(p.getTipo())) {
                        Image img = p.getColor().equals("Blanco") ? imgZombieBlanco : imgZombieNegro;
                        if (img != null) {
                            g2.drawImage(img, x + 10, y + 10, tamañoCelda - 20, tamañoCelda - 20, this);
                        }
                    }
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(columnas * tamañoCelda, filas * tamañoCelda);
    }

    private List<Point> getAdjacentZombiesOfColor(int fila, int col, String color) {
        List<Point> zombies = new ArrayList<>();
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) {
                    continue;
                }
                int nr = fila + dr, nc = col + dc;
                if (!inBounds(nr, nc)) {
                    continue;
                }
                Pieza p = tableroLogico[nr][nc];
                if (p != null && "Zombie".equals(p.getTipo()) && color.equals(p.getColor())) {
                    zombies.add(new Point(nr, nc));
                }
            }
        }
        return zombies;
    }

    private List<Point> computeTargetsForZombieOrder(String colorOrigen) {
        List<Point> targets = new ArrayList<>();
        for (int r = 0; r < filas; r++) {
            for (int c = 0; c < columnas; c++) {
                Pieza objetivo = tableroLogico[r][c];
                if (objetivo == null) {
                    continue;
                }
                if (colorOrigen != null && colorOrigen.equals(objetivo.getColor())) {
                    continue;
                }
                if (!getAdjacentZombiesOfColor(r, c, colorOrigen).isEmpty()) {
                    targets.add(new Point(r, c));
                }
            }
        }
        return targets;
    }

    //Recursiva Up
    private int contarPiezas(int fila, int col, String color) {
        if (fila >= filas) {
            return 0;
        }
        int siguienteFila = fila;
        int siguienteCol = col + 1;
        if (siguienteCol >= columnas) {
            siguienteCol = 0;
            siguienteFila = fila + 1;
        }
        int cuenta = 0;
        Pieza p = tableroLogico[fila][col];
        if (p != null && color != null && color.equals(p.getColor())) {
            cuenta = 1;
        }
        return cuenta + contarPiezas(siguienteFila, siguienteCol, color);
    }

    private void mostrarResumenAtaque(Pieza objetivo, int filaObj, int colObj) {
        boolean destruido = (filaObj >= 0 && colObj >= 0 && tableroLogico[filaObj][colObj] == null);
        if (destruido) {
            String tipo = objetivo != null ? objetivo.getTipo() : "pieza";
            String color = objetivo != null ? objetivo.getColor() : "";
            showInfo("SE DESTRUYÓ LA PIEZA " + tipo + (color.isEmpty() ? "" : " (" + color + ")") + ".", "Resultado del ataque");
            return;
        }
        if (objetivo == null) {
            return;
        }
        int piezasRestantes = contarPiezas(objetivo.getColor());
        String msg = "Resultado del ataque:\n" + objetivo.getTipo() + " (" + objetivo.getColor() + ")\n"
                + "Vida: " + objetivo.getVida() + "\n"
                + "Escudo: " + objetivo.getEscudo() + "\n"
                + "Piezas restantes para " + objetivo.getColor() + ": " + piezasRestantes;
        showInfo(msg, "Resultado del ataque");
    }

    private void showInfo(String msg, String title) {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
