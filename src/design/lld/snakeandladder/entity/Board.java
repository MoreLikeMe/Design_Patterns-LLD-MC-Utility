package design.lld.snakeandladder.entity;

import java.util.Map;

public class Board {
    private final int row;
    private final int col;
    private final int maxCell;
    private final Map<Integer, Snake> snakes;
    private final Map<Integer, Ladder> ladders;

    public Board(int row, int col, Map<Integer, Snake> snakes, Map<Integer, Ladder> ladders) {
        this.row = row;
        this.col = col;
        this.maxCell = row*col;
        /**
         * Validate all the snakes and ladders
         * that they are inside the boundary of the board
         */
        this.snakes = snakes;
        this.ladders = ladders;
    }

    public int calculatePositionWithSnakesAndLadders(int cellValue){
        if(snakes.containsKey(cellValue)){
            return snakes.get(cellValue).getEnd();
        }
        if(ladders.containsKey(cellValue)){
            return ladders.get(cellValue).getEnd();
        }
        return cellValue;
    }

    public int getMaxCell() {
        return maxCell;
    }
}
