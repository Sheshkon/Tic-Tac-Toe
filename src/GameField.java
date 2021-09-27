import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameField extends JPanel {
    private StartPanel start_panel = new StartPanel(this);
    public static final int CELL_SIZE = 300;
    public int player_move;
    public boolean game_is_over;
    private Cell first_win_cell;
    private Cell second_win_cell;
    final static int DRAW = 0,
            WIN = 1,
            LOSE = -1,
            NO_ONE =2;

    Bot bot = new Bot() ;
    public int level = 0;
    private Cell[][] field;
    public int bot_wins = 0;
    public int player_wins = 0;

    GameField() {
        init();
        add(start_panel);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(game_is_over) {
                    Main.game.setTitle(MainFrame.TITLE);
                    init();
                    repaint();
                }
            }
        });
        setPreferredSize(new Dimension(CELL_SIZE * 3, CELL_SIZE * 3));
        setBackground(Color.BLACK);
    }

    private void init() {
        game_is_over = true;
        field = new Cell[3][3];
        first_win_cell = null;
        second_win_cell = null;
        start_panel.setVisible(true);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = new Cell(j, i);
            }
        }
    }


    private void clicked(MouseEvent e) {
        if (game_is_over)
            return;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (e.getX() < field[i][j].x + CELL_SIZE && e.getX() > field[i][j].x) {
                    if (e.getY() < field[i][j].y + CELL_SIZE && e.getY() > field[i][j].y) {
                        if (field[i][j].state == 0) {
                                field[i][j].state = player_move;
                                botMakeMove();
                        }
                    }
                }
            }
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if(start_panel.isVisible())
            return;
        g2d.setStroke(new BasicStroke(CELL_SIZE / 10));
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                g2d.drawRect(field[i][j].x, field[i][j].y, CELL_SIZE, CELL_SIZE);
                if (field[i][j].state == Cell.CROSS)
                    paintCross(g2d, field[i][j]);
                else if (field[i][j].state == Cell.ROUND)
                    paintRound(g2d, field[i][j]);
            }
        }
        if (game_is_over) {
            paintWinLine(g2d);
            Main.game.setTitle("Press SPACE to restart");
        }
    }

    private void paintCross(Graphics2D g2d, Cell cell) {
        Color prefColor = g2d.getColor();
        Stroke prefStroke = g2d.getStroke();
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(CELL_SIZE/ 6));
        g2d.drawLine(cell.x + CELL_SIZE * 2 / 10, cell.y + CELL_SIZE * 2 / 10, cell.x + CELL_SIZE * 8 / 10, cell.y + CELL_SIZE * 8 / 10);
        g2d.drawLine(cell.x + CELL_SIZE * 2 / 10, cell.y + CELL_SIZE * 8 / 10, cell.x + CELL_SIZE * 8 / 10, cell.y + CELL_SIZE * 2 / 10);
        g2d.setColor(prefColor);
        g2d.setStroke(prefStroke);
    }

    private void paintRound(Graphics2D g2d, Cell cell) {
        Color prev_color = g2d.getColor();
        Stroke prev_stroke = g2d.getStroke();
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(CELL_SIZE / 6));
        g2d.drawOval(cell.x + CELL_SIZE * 2 / 10, cell.y + CELL_SIZE * 2 / 10, CELL_SIZE * 6 / 10, CELL_SIZE * 6 / 10);
        g2d.setColor(prev_color);
        g2d.setStroke(prev_stroke);
    }


    public int check() {
        Cell cell;
        for (int i = 0; i < 3; i++) {
            cell = field[0][i];
            if (cell.isEqual(field[1][i]) && cell.isEqual(field[2][i])) {
               setWinLine(cell, field[2][i]);
                return cell.state;
            }
            cell = field[i][0];
            if (cell.isEqual(field[i][1]) && cell.isEqual(field[i][2])) {
                setWinLine(cell, field[i][2]);
                return cell.state;
            }
            cell = field[0][0];
            if (cell.isEqual(field[1][1]) && cell.isEqual(field[2][2])) {
               setWinLine(cell, field[2][2]);
                return cell.state;
            }
            cell = field[2][0];
            if (cell.isEqual(field[1][1]) && cell.isEqual(field[0][2])) {
               setWinLine(cell, field[0][2]);
                return cell.state;
            }

        }

        if (noEmptyCells(field)) {
            game_is_over = true;
            return DRAW;
        }

        return NO_ONE;
    }

    public static boolean noEmptyCells(Cell[][] field){
        for(int i = 0;i< 3;i++){
            for(int j = 0; j < 3;j++){
                if(field[i][j].state == Cell.EMPTY)
                    return false;
            }
        }
        return true;
    }

    void setWinLine(Cell cell1, Cell cell2) {
        game_is_over = true;
        first_win_cell = new Cell(cell1);
        second_win_cell = new Cell(cell2);
    }

    private void paintWinLine(Graphics2D g2d) {
        if (first_win_cell == null || second_win_cell == null)
            return;

        Color prev_color = g2d.getColor();
        Stroke prev_stroke = g2d.getStroke();

        if(player_move == first_win_cell.state) {
            g2d.setColor(Color.GREEN);
        }
        else {
            g2d.setColor(Color.RED);
        }
        g2d.drawLine(first_win_cell.x + CELL_SIZE/2, first_win_cell.y + CELL_SIZE/2,
                second_win_cell.x + CELL_SIZE/2, second_win_cell.y+CELL_SIZE/2);
        g2d.setColor(prev_color);
        g2d.setStroke(prev_stroke);
    }


    static void changeButtonStyle(Component component, Color color, int size) {
        component.setForeground(color);
        component.setFont(new Font("", Font.BOLD, size));
    }

    public void addMouseAdapter(){
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                clicked(e);
            }
        });
    }

    void botMakeMove() {
        repaint();
        int check = check();
        printResult(check);
        if (check == NO_ONE) {
            bot.makeMove(field,-player_move,level);
            printResult(check());
            repaint();
        }
    }

    private void printResult(int check){
        if(check == Cell.CROSS) {
            if (player_move == Cell.CROSS) {
                player_wins++;
                start_panel.player_wins.setText(String.valueOf(player_wins));
            }
             else {
                bot_wins++;
                start_panel.bot_wins.setText(String.valueOf(bot_wins));
            }
            System.out.println("X win");
        }
        else if(check == Cell.ROUND) {
            if (player_move == Cell.ROUND) {
                player_wins++;
                start_panel.player_wins.setText(String.valueOf(player_wins));
            }
            else {
                bot_wins++;
                start_panel.bot_wins.setText(String.valueOf(bot_wins));
            }
            System.out.println("O win");
        }
        else if(check == DRAW)
            System.out.println("DRAW");
    }

}
