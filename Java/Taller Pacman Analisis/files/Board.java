import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private Dimension d;
    private final Font smallFont = new Font("Helvetica", Font.BOLD, 14);

    private Image ii;
    private final Color dotColor = new Color(192, 192, 0);
    private Color mazeColor;

    private boolean inGame = false;
    private boolean dying = false;

    private final int BLOCK_SIZE = 24; // Tamaño de cada bloque en píxeles
    private final int N_BLOCKS = 15; // Número de bloques en el tablero
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE; // Tamaño total del tablero
    private final int PAC_ANIM_DELAY = 2; // Retardo para la animación de Pac-Man
    private final int PACMAN_ANIM_COUNT = 4; // Número de imágenes en la animación de Pac-Man
    private final int MAX_GHOSTS = 12; // Número máximo de fantasmas en el juego
    private final int PACMAN_SPEED = 6; // Velocidad de movimiento de Pac-Man

    private int pacAnimCount = PAC_ANIM_DELAY; // Contador para la animación de Pac-Man
    private int pacAnimDir = 1; // Dirección de la animación de Pac-Man
    private int pacmanAnimPos = 0; // Posición actual de la animación de Pac-Man
    private int N_GHOSTS = 6; // Número de fantasmas en el juego
    private int pacsLeft, score; // Vidas restantes de Pac-Man y puntuación del jugador
    private int[] dx, dy; // Arreglos para almacenar las coordenadas de desplazamiento
    private int[] ghost_x, ghost_y, ghost_dx, ghost_dy, ghostSpeed; // Información de los fantasmas

    private Image ghost; // Imagen del fantasma
    private Image pacman1, pacman2up, pacman2left, pacman2right, pacman2down; // Imágenes de Pac-Man para diferentes direcciones
    private Image pacman3up, pacman3down, pacman3left, pacman3right;
    private Image pacman4up, pacman4down, pacman4left, pacman4right;

    private int pacman_x, pacman_y, pacmand_x, pacmand_y; // Coordenadas y desplazamiento de Pac-Man
    private int req_dx, req_dy, view_dx, view_dy; // Direcciones solicitadas y actuales de Pac-Man
private final short levelData[] = {
    // Datos del nivel del laberinto
    19, 26, 26, 26, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
    21, 0, 0, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
    21, 0, 0, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
    21, 0, 0, 0, 17, 16, 16, 24, 16, 16, 16, 16, 16, 16, 20,
    17, 18, 18, 18, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 20,
    17, 16, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 16, 24, 20,
    25, 16, 16, 16, 24, 24, 28, 0, 25, 24, 24, 16, 20, 0, 21,
    1, 17, 16, 20, 0, 0, 0, 0, 0, 0, 0, 17, 20, 0, 21,
    1, 17, 16, 16, 18, 18, 22, 0, 19, 18, 18, 16, 20, 0, 21,
    1, 17, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 20, 0, 21,
    1, 17, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 20, 0, 21,
    1, 17, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 20, 0, 21,
    1, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0, 21,
    1, 25, 24, 24, 24, 24, 24, 24, 24, 24, 16, 16, 16, 18, 20,
    9, 8, 8, 8, 8, 8, 8, 8, 8, 8, 25, 24, 24, 24, 28
};

private final int validSpeeds[] = {1, 2, 3, 4, 6, 8};
private final int maxSpeed = 6;

private int currentSpeed = 3;
private short[] screenData;
private Timer timer;

public Board() {
    // Constructor de la clase Board
    
    loadImages(); // Carga las imágenes necesarias
    initVariables(); // Inicializa las variables
    initBoard(); // Inicializa el tablero
}

private void initBoard() {
    // Inicializa el tablero
    
    addKeyListener(new TAdapter()); // Agrega un KeyListener al tablero

    setFocusable(true);

    setBackground(Color.black);
}

private void initVariables() {
    // Inicializa las variables del juego

    screenData = new short[N_BLOCKS * N_BLOCKS]; // Inicializa los datos de la pantalla
    mazeColor = new Color(5, 100, 5); // Color del laberinto
    d = new Dimension(400, 400); // Dimensiones del panel
    ghost_x = new int[MAX_GHOSTS]; // Posiciones X de los fantasmas
    ghost_dx = new int[MAX_GHOSTS]; // Velocidades X de los fantasmas
    ghost_y = new int[MAX_GHOSTS]; // Posiciones Y de los fantasmas
    ghost_dy = new int[MAX_GHOSTS]; // Velocidades Y de los fantasmas
    ghostSpeed = new int[MAX_GHOSTS]; // Velocidades de los fantasmas
    dx = new int[4]; // Arreglo de desplazamiento en X para Pac-Man y los fantasmas
    dy = new int[4]; // Arreglo de desplazamiento en Y para Pac-Man y los fantasmas

    timer = new Timer(40, this); // Timer para la actualización del juego
    timer.start();
}
@Override
public void addNotify() {
    // Sobreescribe el método addNotify() de JPanel
    
    super.addNotify(); // Llama al método addNotify() de la clase base JPanel

    initGame(); // Inicializa el juego
}

private void doAnim() {
    // Realiza la animación de Pac-Man

    pacAnimCount--; // Decrementa el contador de animación

    if (pacAnimCount <= 0) {
        pacAnimCount = PAC_ANIM_DELAY;
        pacmanAnimPos = pacmanAnimPos + pacAnimDir;

        if (pacmanAnimPos == (PACMAN_ANIM_COUNT - 1) || pacmanAnimPos == 0) {
            pacAnimDir = -pacAnimDir;
        }
    }
}

private void playGame(Graphics2D g2d) {
    // Juega el juego

    if (dying) {
        // Si Pac-Man está muriendo

        death(); // Realiza las acciones de muerte

    } else {
        // Si Pac-Man está vivo

        movePacman(); // Mueve a Pac-Man
        drawPacman(g2d); // Dibuja a Pac-Man en el tablero
        moveGhosts(g2d); // Mueve a los fantasmas
        checkMaze(); // Verifica si se ha completado el laberinto
    }
}

private void showIntroScreen(Graphics2D g2d) {
    // Muestra la pantalla de introducción

    g2d.setColor(new Color(0, 32, 48));
    g2d.fillRect(50, SCREEN_SIZE / 2 - 30, SCREEN_SIZE - 100, 50);
    g2d.setColor(Color.white);
    g2d.drawRect(50, SCREEN_SIZE / 2 - 30, SCREEN_SIZE - 100, 50);

    String s = "Press s to start."; // Mensaje de inicio
    Font small = new Font("Helvetica", Font.BOLD, 14); // Fuente del texto
    FontMetrics metr = this.getFontMetrics(small); // Métricas de la fuente

    g2d.setColor(Color.white);
    g2d.setFont(small);
    g2d.drawString(s, (SCREEN_SIZE - metr.stringWidth(s)) / 2, SCREEN_SIZE / 2); // Dibuja el mensaje en el centro del tablero
}
private void drawScore(Graphics2D g) {
    // Dibuja el puntaje en el tablero

    int i;
    String s;

    g.setFont(smallFont);
    g.setColor(new Color(96, 128, 255));
    s = "Score: " + score; // Obtiene el puntaje y lo convierte a cadena
    g.drawString(s, SCREEN_SIZE / 2 + 96, SCREEN_SIZE + 16); // Dibuja el puntaje en la posición especificada

    for (i = 0; i < pacsLeft; i++) {
        g.drawImage(pacman3left, i * 28 + 8, SCREEN_SIZE + 1, this); // Dibuja las vidas restantes de Pac-Man en el tablero
    }
}

private void checkMaze() {
    // Verifica si se ha completado el laberinto

    short i = 0;
    boolean finished = true;

    while (i < N_BLOCKS * N_BLOCKS && finished) {
        // Recorre los bloques del laberinto

        if ((screenData[i] & 48) != 0) {
            finished = false; // Si hay bloques no visitados, indica que el laberinto no está completado
        }

        i++;
    }

    if (finished) {
        // Si el laberinto está completado

        score += 50; // Aumenta el puntaje en 50 puntos

        if (N_GHOSTS < MAX_GHOSTS) {
            N_GHOSTS++; // Aumenta la cantidad de fantasmas si no ha alcanzado el máximo
        }

        if (currentSpeed < maxSpeed) {
            currentSpeed++; // Aumenta la velocidad si no ha alcanzado el máximo
        }

        initLevel(); // Inicializa el siguiente nivel del juego
    }
}

private void death() {
    // Acciones realizadas cuando Pac-Man muere

    pacsLeft--; // Decrementa la cantidad de vidas restantes

    if (pacsLeft == 0) {
        inGame = false; // Si no quedan vidas, el juego termina
    }

    continueLevel(); // Continúa al siguiente nivel o reinicia el nivel actual
}

private void moveGhosts(Graphics2D g2d) {
    // Mueve a los fantasmas en el tablero

    short i;
    int pos;
    int count;

    for (i = 0; i < N_GHOSTS; i++) {
        // Recorre cada fantasma

        if (ghost_x[i] % BLOCK_SIZE == 0 && ghost_y[i] % BLOCK_SIZE == 0) {
            // Si el fantasma está en una posición de bloque

            pos = ghost_x[i] / BLOCK_SIZE + N_BLOCKS * (int) (ghost_y[i] / BLOCK_SIZE);

            count = 0;

            if ((screenData[pos] & 1) == 0 && ghost_dx[i] != 1) {
                dx[count] = -1;
                dy[count] = 0;
                count++;
            }

            if ((screenData[pos] & 2) == 0 && ghost_dy[i] != 1) {
                dx[count] = 0;
                dy[count] = -1;
                count++;
            }

            if ((screenData[pos] & 4) == 0 && ghost_dx[i] != -1) {
                dx[count] = 1;
                dy[count] = 0;
                count++;
            }

            if ((screenData[pos] & 8) == 0 && ghost_dy[i] != -1) {
                dx[count] = 0;
                dy[count] = 1;
                count++;
            }

            if (count == 0) {
                // Si no hay opciones de movimiento disponibles

                if ((screenData[pos] & 15) == 15) {
                    ghost_dx[i] = 0;
                    ghost_dy[i] = 0; // El fantasma está atrapado, se detiene
                } else {
                    ghost_dx[i] = -ghost_dx[i];
                    ghost_dy[i] = -ghost_dy[i]; // El fantasma cambia de dirección
                }

            } else {
                // Si hay opciones de movimiento disponibles

                count = (int) (Math.random() * count); // Selecciona una dirección aleatoria

                if (count > 3) {
                    count = 3; // Limita el valor máximo de count a 3
                }

                ghost_dx[i] = dx[count];
                ghost_dy[i] = dy[count]; // Establece la nueva dirección del fantasma
            }
        }

        ghost_x[i] = ghost_x[i] + (ghost_dx[i] * ghostSpeed[i]);
        ghost_y[i] = ghost_y[i] + (ghost_dy[i] * ghostSpeed[i]); // Mueve el fantasma en la dirección actual
        drawGhost(g2d, ghost_x[i] + 1, ghost_y[i] + 1); // Dibuja el fantasma en el tablero

        if (pacman_x > (ghost_x[i] - 12) && pacman_x < (ghost_x[i] + 12)
                && pacman_y > (ghost_y[i] - 12) && pacman_y < (ghost_y[i] + 12)
                && inGame) {
            // Si Pac-Man entra en contacto con un fantasma

            dying = true; // Pac-Man está muriendo
        }
    }
}
private void drawGhost(Graphics2D g2d, int x, int y) {
    // Dibuja un fantasma en una posición específica en el tablero

    g2d.drawImage(ghost, x, y, this); // Dibuja la imagen del fantasma en las coordenadas especificadas
}

private void movePacman() {
    // Mueve a Pac-Man en el tablero

    int pos;
    short ch;

    if (req_dx == -pacmand_x && req_dy == -pacmand_y) {
        pacmand_x = req_dx;
        pacmand_y = req_dy;
        view_dx = pacmand_x;
        view_dy = pacmand_y;
    }

    if (pacman_x % BLOCK_SIZE == 0 && pacman_y % BLOCK_SIZE == 0) {
        // Si Pac-Man está en una posición de bloque

        pos = pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE);
        ch = screenData[pos];

        if ((ch & 16) != 0) {
            screenData[pos] = (short) (ch & 15);
            score++; // Si Pac-Man come un punto, aumenta el puntaje
        }

        if (req_dx != 0 || req_dy != 0) {
            // Si se ha solicitado un cambio de dirección

            if (!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0)
                    || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
                    || (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
                    || (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))) {
                // Si el cambio de dirección no está bloqueado

                pacmand_x = req_dx;
                pacmand_y = req_dy;
                view_dx = pacmand_x;
                view_dy = pacmand_y; // Actualiza la dirección de movimiento de Pac-Man
            }
        }

        // Verifica si Pac-Man está detenido en su posición actual
        if ((pacmand_x == -1 && pacmand_y == 0 && (ch & 1) != 0)
                || (pacmand_x == 1 && pacmand_y == 0 && (ch & 4) != 0)
                || (pacmand_x == 0 && pacmand_y == -1 && (ch & 2) != 0)
                || (pacmand_x == 0 && pacmand_y == 1 && (ch & 8) != 0)) {
            pacmand_x = 0;
            pacmand_y = 0; // Pac-Man se detiene
        }
    }

    pacman_x = pacman_x + PACMAN_SPEED * pacmand_x;
    pacman_y = pacman_y + PACMAN_SPEED * pacmand_y; // Mueve a Pac-Man en la dirección actual
}

private void drawPacman(Graphics2D g2d) {
    // Dibuja a Pac-Man en el tablero

    if (view_dx == -1) {
        drawPacnanLeft(g2d);
    } else if (view_dx == 1) {
        drawPacmanRight(g2d);
    } else if (view_dy == -1) {
        drawPacmanUp(g2d);
    } else {
        drawPacmanDown(g2d);
    }
}

private void drawPacmanUp(Graphics2D g2d) {
    // Dibuja a Pac-Man mirando hacia arriba

    switch (pacmanAnimPos) {
        case 1:
            g2d.drawImage(pacman2up, pacman_x + 1, pacman_y + 1, this);
            break;
        case 2:
            g2d.drawImage(pacman3up, pacman_x + 1, pacman_y + 1, this);
            break;
        case 3:
            g2d.drawImage(pacman4up, pacman_x + 1, pacman_y + 1, this);
            break;
        default:
            g2d.drawImage(pacman1, pacman_x + 1, pacman_y + 1, this);
            break;
    }
}
private void drawPacmanDown(Graphics2D g2d) {
    // Dibuja a Pac-Man mirando hacia abajo

    switch (pacmanAnimPos) {
        case 1:
            g2d.drawImage(pacman2down, pacman_x + 1, pacman_y + 1, this);
            break;
        case 2:
            g2d.drawImage(pacman3down, pacman_x + 1, pacman_y + 1, this);
            break;
        case 3:
            g2d.drawImage(pacman4down, pacman_x + 1, pacman_y + 1, this);
            break;
        default:
            g2d.drawImage(pacman1, pacman_x + 1, pacman_y + 1, this);
            break;
    }
}

private void drawPacnanLeft(Graphics2D g2d) {
    // Dibuja a Pac-Man mirando hacia la izquierda

    switch (pacmanAnimPos) {
        case 1:
            g2d.drawImage(pacman2left, pacman_x + 1, pacman_y + 1, this);
            break;
        case 2:
            g2d.drawImage(pacman3left, pacman_x + 1, pacman_y + 1, this);
            break;
        case 3:
            g2d.drawImage(pacman4left, pacman_x + 1, pacman_y + 1, this);
            break;
        default:
            g2d.drawImage(pacman1, pacman_x + 1, pacman_y + 1, this);
            break;
    }
}

private void drawPacmanRight(Graphics2D g2d) {
    // Dibuja a Pac-Man mirando hacia la derecha

    switch (pacmanAnimPos) {
        case 1:
            g2d.drawImage(pacman2right, pacman_x + 1, pacman_y + 1, this);
            break;
        case 2:
            g2d.drawImage(pacman3right, pacman_x + 1, pacman_y + 1, this);
            break;
        case 3:
            g2d.drawImage(pacman4right, pacman_x + 1, pacman_y + 1, this);
            break;
        default:
            g2d.drawImage(pacman1, pacman_x + 1, pacman_y + 1, this);
            break;
    }
}

private void drawMaze(Graphics2D g2d) {
    // Dibuja el laberinto en el tablero

    short i = 0;
    int x, y;

    for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
        for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {

            g2d.setColor(mazeColor);
            g2d.setStroke(new BasicStroke(2));

            if ((screenData[i] & 1) != 0) {
                g2d.drawLine(x, y, x, y + BLOCK_SIZE - 1);
            }

            if ((screenData[i] & 2) != 0) {
                g2d.drawLine(x, y, x + BLOCK_SIZE - 1, y);
            }

            if ((screenData[i] & 4) != 0) {
                g2d.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1,
                        y + BLOCK_SIZE - 1);
            }

            if ((screenData[i] & 8) != 0) {
                g2d.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1,
                        y + BLOCK_SIZE - 1);
            }

            if ((screenData[i] & 16) != 0) {
                g2d.setColor(dotColor);
                g2d.fillRect(x + 11, y + 11, 2, 2);
            }

            i++;
        }
    }
}
private void initGame() {
    // Inicializa el juego

    pacsLeft = 3; // Número de vidas de Pac-Man
    score = 0; // Puntuación inicial
    initLevel(); // Inicializa el nivel
    N_GHOSTS = 6; // Número de fantasmas
    currentSpeed = 3; // Velocidad actual del juego
}

private void initLevel() {
    // Inicializa el nivel

    int i;
    for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
        screenData[i] = levelData[i]; // Copia los datos del nivel al arreglo screenData
    }

    continueLevel(); // Continúa el nivel
}
private void continueLevel() {
    // Continúa el nivel

    short i;
    int dx = 1;
    int random;

    for (i = 0; i < N_GHOSTS; i++) {
        // Configura las propiedades de cada fantasma

        ghost_y[i] = 4 * BLOCK_SIZE; // Posición inicial en el eje y del fantasma
        ghost_x[i] = 4 * BLOCK_SIZE; // Posición inicial en el eje x del fantasma
        ghost_dy[i] = 0; // Velocidad en el eje y del fantasma
        ghost_dx[i] = dx; // Velocidad en el eje x del fantasma
        dx = -dx; // Invierte la dirección del movimiento en el siguiente fantasma

        random = (int) (Math.random() * (currentSpeed + 1)); // Genera una velocidad aleatoria para el fantasma

        if (random > currentSpeed) {
            random = currentSpeed;
        }

        ghostSpeed[i] = validSpeeds[random]; // Asigna la velocidad al fantasma
    }

    pacman_x = 7 * BLOCK_SIZE; // Posición inicial en el eje x de Pac-Man
    pacman_y = 11 * BLOCK_SIZE; // Posición inicial en el eje y de Pac-Man
    pacmand_x = 0; // Velocidad en el eje x de Pac-Man
    pacmand_y = 0; // Velocidad en el eje y de Pac-Man
    req_dx = 0; // Dirección solicitada en el eje x de Pac-Man
    req_dy = 0; // Dirección solicitada en el eje y de Pac-Man
    view_dx = -1; // Dirección de vista en el eje x de Pac-Man
    view_dy = 0; // Dirección de vista en el eje y de Pac-Man
    dying = false; // Bandera para indicar si Pac-Man está muriendo
}

private void loadImages() {
    // Carga las imágenes del juego

    ghost = new ImageIcon("images/ghost.gif").getImage();
    pacman1 = new ImageIcon("images/pacman.png").getImage();
    pacman2up = new ImageIcon("images/up1.png").getImage();
    pacman3up = new ImageIcon("images/up2.png").getImage();
    pacman4up = new ImageIcon("images/up3.png").getImage();
    pacman2down = new ImageIcon("images/down1.png").getImage();
    pacman3down = new ImageIcon("images/down2.png").getImage();
    pacman4down = new ImageIcon("images/down3.png").getImage();
    pacman2left = new ImageIcon("images/left1.png").getImage();
    pacman3left = new ImageIcon("images/left2.png").getImage();
    pacman4left = new ImageIcon("images/left3.png").getImage();
    pacman2right = new ImageIcon("images/right1.png").getImage();
    pacman3right = new ImageIcon("images/right2.png").getImage();
    pacman4right = new ImageIcon("images/right3.png").getImage();
}

@Override
public void paintComponent(Graphics g) {
    super.paintComponent(g);

    doDrawing(g);
}

private void doDrawing(Graphics g) {
    // Realiza el dibujo del juego

    Graphics2D g2d = (Graphics2D) g;

    g2d.setColor(Color.black);
    g2d.fillRect(0, 0, d.width, d.height);

    drawMaze(g2d); // Dibuja el laberinto
    drawScore(g2d); // Dibuja la puntuación

    doAnim(); // Realiza la animación

    if (inGame) {
        playGame(g2d); // Juega el juego
    } else {
        showIntroScreen(g2d); // Muestra la pantalla de introducción
    }

    g2d.drawImage(ii, 5, 5, this);
    Toolkit.getDefaultToolkit().sync();
    g2d.dispose();
}
class TAdapter extends KeyAdapter {
    // Clase interna que extiende KeyAdapter para manejar eventos de teclado

    @Override
    public void keyPressed(KeyEvent e) {
        // Método llamado cuando se presiona una tecla

        int key = e.getKeyCode(); // Obtiene el código de la tecla presionada

        if (inGame) {
            // Si el juego está en curso

            if (key == KeyEvent.VK_LEFT) {
                // Si se presiona la tecla de flecha izquierda
                req_dx = -1; // Solicita mover a la izquierda en el eje x
                req_dy = 0; // No se solicita movimiento en el eje y
            } else if (key == KeyEvent.VK_RIGHT) {
                // Si se presiona la tecla de flecha derecha
                req_dx = 1; // Solicita mover a la derecha en el eje x
                req_dy = 0; // No se solicita movimiento en el eje y
            } else if (key == KeyEvent.VK_UP) {
                // Si se presiona la tecla de flecha arriba
                req_dx = 0; // No se solicita movimiento en el eje x
                req_dy = -1; // Solicita mover hacia arriba en el eje y
            } else if (key == KeyEvent.VK_DOWN) {
                // Si se presiona la tecla de flecha abajo
                req_dx = 0; // No se solicita movimiento en el eje x
                req_dy = 1; // Solicita mover hacia abajo en el eje y
            } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
                // Si se presiona la tecla de escape y el temporizador está en ejecución
                inGame = false; // Finaliza el juego
            } else if (key == KeyEvent.VK_PAUSE) {
                // Si se presiona la tecla de pausa
                if (timer.isRunning()) {
                    timer.stop(); // Detiene el temporizador
                } else {
                    timer.start(); // Inicia el temporizador
                }
            }
        } else {
            // Si el juego no está en curso

            if (key == 's' || key == 'S') {
                // Si se presiona la tecla 's' o 'S'
                inGame = true; // Inicia el juego
                initGame(); // Inicializa el juego
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Método llamado cuando se suelta una tecla

        int key = e.getKeyCode(); // Obtiene el código de la tecla soltada

        if (key == Event.LEFT || key == Event.RIGHT
                || key == Event.UP || key == Event.DOWN) {
            // Si se suelta una tecla de dirección

            req_dx = 0; // No se solicita movimiento en el eje x
            req_dy = 0; // No se solicita movimiento en el eje y
        }
    }
}

@Override
public void actionPerformed(ActionEvent e) {
    // Método llamado cuando se produce un evento de acción

    repaint(); // Vuelve a pintar el componente
}
