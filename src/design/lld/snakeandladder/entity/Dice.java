package design.lld.snakeandladder.entity;

import java.util.concurrent.ThreadLocalRandom;

public class Dice {
    private final int start;
    private final int end;

    public Dice(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int roll(){
        return ThreadLocalRandom.current().nextInt(start, end+1);
    }
}
