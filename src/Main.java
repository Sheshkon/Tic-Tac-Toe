import javax.swing.*;

public class Main {
    public static MainFrame game;

    public static void main(String[] args) {
        game = new MainFrame();
        game.setIconImage(new ImageIcon(Main.class.getResource("Resources/tic-tac-toe.png")).getImage());
        game.setVisible(true);
    }
}
