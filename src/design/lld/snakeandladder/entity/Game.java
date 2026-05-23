package design.lld.snakeandladder.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Game {
    private final Board board;
    private final Dice dice;
    private final List<Player> playersList;
    private final Queue<Player> players;
    private Player currentPlayer;

    public Game(Board board, Dice dice, List<Player> players) {
        this.board = board;
        this.dice = dice;
        this.players = new LinkedList<>();
        this.playersList = players;
    }

    public void start(){
        System.out.println("Starting the Game...");
        playersList.forEach(player -> {
            player.setPosition(1);
            this.players.add(player);
        });
        this.currentPlayer = players.poll();
        displayCurrentPlayer();
    }

    public boolean rollDice(){
        if(players.isEmpty()) {
            System.out.println("Game Not Started Yet...");
            return false;
        }

        int diceValue = dice.roll();
        System.out.println("Dice Value: " + diceValue);
        int newPosition = currentPlayer.getPosition() + diceValue;
        if(newPosition>board.getMaxCell()){
            proceedWithNextPlayer();
            return true;
        }
        newPosition = board.calculatePositionWithSnakesAndLadders(newPosition);
        currentPlayer.setPosition(newPosition);
        System.out.println("New Position: " + newPosition);

        //Winner checking
        if(newPosition==board.getMaxCell()){
            System.out.println("Player " + currentPlayer.getAssignedColor() + " is the Winner");
            endGame();
            return false;
        }

        proceedWithNextPlayer();
        return true;
    }

    private void proceedWithNextPlayer() {
        this.players.add(currentPlayer);
        currentPlayer = this.players.poll();
        displayCurrentPlayer();
    }

    public void displayCurrentPlayer(){
        System.out.println("Current Player: " + currentPlayer);
    }

    private void endGame() {
        players.clear();
        currentPlayer = null;
        System.out.println("Game Over");
    }
}
