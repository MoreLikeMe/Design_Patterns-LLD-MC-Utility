package design.lld.snakeandladder.entity;

public class Player {
    private final String assignedColor;
    private int position;

    public Player(String assignedColor) {
        this.assignedColor = assignedColor;
    }

    public String getAssignedColor() {
        return assignedColor;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Player{" +
                "assignedColor='" + assignedColor + '\'' +
                ", position=" + position +
                '}';
    }
}
