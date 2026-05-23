package design.lld.snakeandladder;

import design.lld.snakeandladder.entity.*;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Snake snake1 = new Snake(14, 4);
        Snake snake2 = new Snake(10, 2);
        Map<Integer, Snake> snakes = Map.of(snake1.getStart(), snake1, snake2.getStart(), snake2);

        Ladder ladder1 = new Ladder(3,12);
        Ladder ladder2 = new Ladder(6,15);
        Map<Integer, Ladder> ladders = Map.of(ladder1.getStart(), ladder1, ladder2.getStart(), ladder2);

        Board board = new Board(4,4,snakes,ladders);
        Dice dice = new Dice(1,6);
        List<Player> players = List.of(new Player("RED"), new Player("BLUE"), new Player("GREEN"));

        Game game = new Game(board, dice, players);
        game.start();

        while(game.rollDice()){
            System.out.println("Game On...");
        }
    }
}
