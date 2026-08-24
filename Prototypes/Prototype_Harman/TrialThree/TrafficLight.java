package TrialThree;

public class TrafficLight {
    public enum State {
        RED, YELLOW, GREEN
    }

    private int x;
    private int y;
    private State state;
    private long lastChangeTime;
    private int greenDuration = 1200;
    private int yellowDuration = 500;
    private int redDuration = 1200;

    public TrafficLight(int x, int y) {
        this.x = x;
        this.y = y;
        this.state = State.RED;
        this.lastChangeTime = System.currentTimeMillis();
    }

    public void update() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastChangeTime;

        switch (state) {
            case GREEN:
                if (elapsed > greenDuration) {
                    state = State.YELLOW;
                    lastChangeTime = now;
                }
                break;

            case YELLOW:
                if (elapsed > yellowDuration) {
                    state = State.RED;
                    lastChangeTime = now;
                }
                break;

            case RED:
                if (elapsed > redDuration) {
                    state = State.GREEN;
                    lastChangeTime = now;
                }
                break;
        }
    }

    public State getState() {
        return state;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
