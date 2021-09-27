public class Cell {
    public static final int
            CROSS = 100,
            ROUND = -100,
            EMPTY = 0;

    int x,y;
    int j,i;
    int state;

    Cell(){
        state = EMPTY;
    }
    Cell(int j,int i){
        this.j = j;
        this.i = i;
        this.x = j*GameField.CELL_SIZE;
        this.y = i*GameField.CELL_SIZE;
        state = EMPTY;
    }

    Cell(Cell cell){
        j = cell.j;
        i = cell.i;
        x = j*GameField.CELL_SIZE;
        y = i*GameField.CELL_SIZE;
        state = cell.state;
    }

    boolean isEqual(Cell cell){
        return cell.state != 0 && cell.state == this.state;
    }

}
