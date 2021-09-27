import javax.swing.*;

public class MainFrame extends JFrame {
    public static GameField field;
public static final String TITLE = "TIC-TAC-TOE";
    MainFrame(){
        setTitle(TITLE);
        field = new GameField() ;
        add(field);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
