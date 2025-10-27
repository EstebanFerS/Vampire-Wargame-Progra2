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
import proyecto_parcial_1_vampire_wargame.Panel;

public class Tablero extends JFrame {

    public Tablero() {
        setTitle("Tablero Visual - Vampire Wargame");
        setSize(1300, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        Panel panelFondo = new Panel("/Images/Fondo3.jpg");

        panelFondo.setOpaque(false);

        TableroPanel tableroPanel = new TableroPanel();
        panelFondo.add(tableroPanel, new GridBagConstraints());

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

    private int filaSeleccionada = -1;
    private int columnaSeleccionada = -1;

    private java.util.List<Point> movimientosValidos = new ArrayList<>();

    public TableroPanel() {
        setPreferredSize(new Dimension(columnas * tamañoCelda, filas * tamañoCelda));
        setOpaque(false);

        imgLobo = cargarImagen("/Images/HombreLobo.png");
        imgVampiro = cargarImagen("/Images/Vampiro.png");
        imgMuerte = cargarImagen("/Images/Muerte.png");

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
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                manejarClick(e);
            }
        });
    }

    private Image cargarImagen(String ruta) {
        return new ImageIcon(getClass().getResource(ruta)).getImage();
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

        for (Point p : movimientosValidos) {
            if (p.x == fila && p.y == col) {
                moverFicha(filaSeleccionada, columnaSeleccionada, fila, col);
                filaSeleccionada = -1;
                columnaSeleccionada = -1;
                movimientosValidos.clear();
                repaint();
                return;
            }
        }

        if (fichas[fila][col] != null) {
            filaSeleccionada = fila;
            columnaSeleccionada = col;
            calcularMovimientosValidos(fila, col);
        } else {
            filaSeleccionada = -1;
            columnaSeleccionada = -1;
            movimientosValidos.clear();
        }

        repaint();
    }

    private void moverFicha(int filaOrigen, int colOrigen, int filaDestino, int colDestino) {
        fichas[filaDestino][colDestino] = fichas[filaOrigen][colOrigen];
        fichas[filaOrigen][colOrigen] = null;
    }

    private void calcularMovimientosValidos(int fila, int col) {
        movimientosValidos.clear();
        for (int df = -1; df <= 1; df++) {
            for (int dc = -1; dc <= 1; dc++) {
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
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(columnas * tamañoCelda, filas * tamañoCelda);
    }
}
