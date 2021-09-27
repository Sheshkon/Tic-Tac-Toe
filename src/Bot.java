import java.util.Random;
import java.util.Vector;

public class Bot {
    int move;
    int level;
    final static Random random = new Random();

    Bot() {

    }

    private static class Pair{
        Cell cell;
        int score;
        Pair(Cell cell, int score){
            this.cell = cell;
            this.score = score;
        }
    }

    public static void easyLVL(Cell[][] field,int move ) {
        Vector<Cell> empty_cells = new Vector<>();
        for (Cell[] cells : field)
            for (int j = 0; j < field.length; j++) {
                if (cells[j].state == Cell.EMPTY)
                    empty_cells.add(cells[j]);
            }

       int number_for_move =  random.nextInt(empty_cells.size());
        Cell cell = empty_cells.elementAt(number_for_move);

            field[cell.i][cell.j].state = move;

        System.out.println("bot make a move: "+ cell.j + " " + cell.i);
    }

    public void makeMove(Cell[][] field, int move, int level){
        this.level = level;

        if(level == 0)
        easyLVL(field,move);
        else
            hardLVL(field,move);
    }

    public void hardLVL(Cell[][] field, int move){
        this.move = move;
       Vector<Pair> moves = new Vector<>();
       Vector<Cell> best_moves = new Vector<>();
        Cell bestMove;
        int score;
        int bestScore = -Integer.MAX_VALUE;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                if(field[i][j].state == 0){
                    field[i][j].state = move;
                    score = minimax(field,0,false);
                    moves.add(new Pair(new Cell(j,i), score));
                    field[i][j].state = Cell.EMPTY;

                    if(score > bestScore){
                        bestScore = score;
                    }
                }
            }
        }
        for(Pair pair: moves){
            if(pair.score == bestScore)
                best_moves.add(pair.cell);
        }
        if(level == 1 && random.nextInt(5) == 0)
            bestMove = moves.elementAt(random.nextInt(moves.size())).cell;
        else
            bestMove = best_moves.elementAt(random.nextInt(best_moves.size()));
        field[bestMove.i][bestMove.j].state = move;
        System.out.println("bot make a move: "+ bestMove.i + " " + bestMove.j);
    }

    private int minimax(Cell[][] field, int depth, boolean isBotMove) {
        int result = whoIsWon(field);
        if(result != GameField.NO_ONE)
           return result;

        int best;

        if (isBotMove) {
            best = Integer.MIN_VALUE;
            for (Cell[] cells : field) {
                for (int j = 0; j < field.length; j++) {
                    if (cells[j].state == Cell.EMPTY) {
                        cells[j].state = move;
                        best = Math.max(best, minimax(field, depth + 1, false));
                        cells[j].state = Cell.EMPTY;
                    }
                }
            }
        } else {
            best = Integer.MAX_VALUE;
            for (Cell[] cells : field) {
                for (int j = 0; j < field.length; j++) {
                    if (cells[j].state == Cell.EMPTY) {
                        cells[j].state = -move;
                        best = Math.min(best, minimax(field, depth + 1, true));
                        cells[j].state = Cell.EMPTY;
                    }
                }
            }
        }
        return best;
    }


    public Cell check(Cell[][] field) {
        Cell cell;
        for (int i = 0; i < 3; i++) {
            cell = field[0][i];
            if (cell.isEqual(field[1][i]) && cell.isEqual(field[2][i])) {
                return cell;
            }
            cell = field[i][0];
            if (cell.isEqual(field[i][1]) && cell.isEqual(field[i][2])) {
                return cell;
            }
            cell = field[0][0];
            if (cell.isEqual(field[1][1]) && cell.isEqual(field[2][2])) {
                return cell;
            }
            cell = field[2][0];
            if (cell.isEqual(field[1][1]) && cell.isEqual(field[0][2])) {
                return cell;
            }

        }
       if (GameField.noEmptyCells(field)){
           return new Cell();
        }

        return  null;
    }


    public int whoIsWon(Cell[][] field){
        Cell cell = check(field);
        if(cell == null)
            return GameField.NO_ONE;
        else if(cell.state == -move)
            return GameField.LOSE;
       else if(cell.state == move)
            return GameField.WIN;
       else
           return GameField.DRAW;
    }
}
