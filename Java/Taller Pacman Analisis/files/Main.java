import java.awt.EventQueue; // Importa las clases necesarias de java.awt para trabajar con la interfaz gráfica de usuario
import javax.swing.JFrame; // Importa las clases necesarias de javax.swing para crear la ventana del juego

public class Main {
  public static void main(String[] args) {

      // Asegurarse de que la creación de la ventana se realiza en el hilo de eventos de la interfaz de usuario
      EventQueue.invokeLater(() -> {
            // Crear una instancia de la clase Pacman
            Pacman ex = new Pacman();
            // Hacer visible la ventana de juego
            ex.setVisible(true);
        });
  }
}

