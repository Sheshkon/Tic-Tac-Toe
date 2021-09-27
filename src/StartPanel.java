import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StartPanel extends JPanel implements ActionListener {
    private static final String[] LVL ={
            "Easy",
            "Medium",
            "Hard"
    };
    GameField parent;
    JPanel radioPanel;
    JLabel player_wins;
    JLabel bot_wins;
    JLabel lvlLabel;

    StartPanel(GameField parent) {
        this.parent = parent;
        player_wins = new JLabel("0");
        bot_wins = new JLabel("0");
        lvlLabel = new JLabel("level");
        setPreferredSize(new Dimension(GameField.CELL_SIZE * 3, GameField.CELL_SIZE * 3));
        setBackground(Color.BLACK);
        init();
        setVisible(true);
    }

    private void init() {
        JLabel cross_button = new JLabel("X");
        GameField.changeButtonStyle(cross_button, Color.WHITE, GameField.CELL_SIZE + GameField.CELL_SIZE / 2);
        cross_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                parent.player_move = Cell.CROSS;
                hideStartPanel();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                cross_button.setForeground(Color.GREEN);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                cross_button.setForeground(Color.WHITE);
            }

        });
        JLabel round_button = new JLabel("O");
        GameField.changeButtonStyle(round_button, Color.WHITE, GameField.CELL_SIZE + GameField.CELL_SIZE / 2);
        round_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                parent.player_move = Cell.ROUND;
                hideStartPanel();
                parent.botMakeMove();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                round_button.setForeground(Color.GREEN);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                round_button.setForeground(Color.WHITE);
            }
        });

        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.add(round_button);
        panel.add(Box.createHorizontalStrut(GameField.CELL_SIZE / 5));
        panel.add(cross_button);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JComboBox comboBox = new JComboBox(LVL);
        comboBox.setForeground(Color.WHITE);
        comboBox.setBackground(Color.BLACK);
        comboBox.setFont(new Font("", Font.BOLD, GameField.CELL_SIZE / 5));
        createLevelPanel();
        add(Box.createVerticalGlue());
        add(panel);
        add(Box.createVerticalGlue());
    }

   private void createLevelPanel(){
       ButtonGroup group = new ButtonGroup();
       JRadioButton[] levelsButtons = new JRadioButton[3];
       for(int i = 0; i< 3;i++){
          levelsButtons[i] = new JRadioButton(LVL[i]);
          levelsButtons[i].setFont(new Font("",Font.BOLD,GameField.CELL_SIZE/10));
          levelsButtons[i].setActionCommand(String.valueOf(i));
          levelsButtons[i].addActionListener(this);
          group.add(levelsButtons[i]);
       }


       lvlLabel.setText(LVL[parent.level]);
       lvlLabel.setForeground(Color.WHITE);
       lvlLabel.setFont(new Font("",Font.BOLD,GameField.CELL_SIZE/3));
       lvlLabel.setAlignmentX(0.5f);
       lvlLabel.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               super.mouseClicked(e);
               if(radioPanel.isVisible())
                   radioPanel.setVisible(false);

               else {
                   levelsButtons[parent.level].setSelected(true);
                   radioPanel.setVisible(true);
               }
           }

           @Override
           public void mouseEntered(MouseEvent e) {
               super.mouseEntered(e);
               lvlLabel.setForeground(Color.GREEN);
           }

           @Override
           public void mouseExited(MouseEvent e) {
               super.mouseExited(e);
               lvlLabel.setForeground(Color.WHITE);
           }
       });
       add(new JLabel("    "));
       add(lvlLabel);
       radioPanel = new JPanel(new GridLayout(0, 1));
       radioPanel.setBackground(Color.BLACK);
       radioPanel.setAlignmentX(0.5f);
       for(int i= 0;i<3;i++) {
           radioPanel.add(levelsButtons[i]);
       }
       radioPanel.setVisible(false);
       add(radioPanel);
        JPanel container = new JPanel();
        container.setBackground(Color.BLACK);
        player_wins.setFont(new Font("",Font.BOLD,GameField.CELL_SIZE/10));
        player_wins.setForeground(Color.WHITE);
        bot_wins.setFont(new Font("",Font.BOLD,GameField.CELL_SIZE/10));
        bot_wins.setForeground(Color.WHITE);
       JLabel player_wins_label = new JLabel("Player wins:  ");
       player_wins_label.setForeground(Color.WHITE);
       player_wins_label.setFont(new Font("",Font.BOLD,GameField.CELL_SIZE/10));
       container.add(player_wins_label);
       container.add(player_wins);
      JLabel bot_wins_label =  new JLabel("   Bot wins:  ");
       bot_wins_label.setForeground(Color.WHITE);
       bot_wins_label.setFont(new Font("",Font.BOLD,GameField.CELL_SIZE/10));
       container.add(bot_wins_label);
       container.add(bot_wins);
       add(container);

   }

    @Override
    public void actionPerformed(ActionEvent e) {
      parent.level = Integer.valueOf(e.getActionCommand());
      lvlLabel.setText(LVL[Integer.valueOf(e.getActionCommand())]);
    }


    private void hideStartPanel(){
        radioPanel.setVisible(false);
        parent.addMouseAdapter();
        parent.repaint();
        setVisible(false);
        parent.game_is_over = false;
    }
}
