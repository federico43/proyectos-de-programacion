import javax.swing.JFrame;

// Definición de la clase Pacman, que extiende JFrame
class Pacman extends JFrame {

    // Constructor de la clase Pacman
    public Pacman() {
        
        initUI();
    }
    
    // Método privado para inicializar la interfaz de usuario
    private void initUI() {
        
        add(new Board()); // Agrega un nuevo objeto Board al JFrame
        
        setTitle("Pacman"); // Establece el título de la ventana
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Establece la operación de cierre al cerrar la ventana
        setSize(380, 420); // Establece el tamaño de la ventana
        setLocationRelativeTo(null); // Establece la ubicación de la ventana en el centro de la pantalla
    }

    // Método main, punto de entrada del programa
    public static void main(String[] args) {

  
    }
}
