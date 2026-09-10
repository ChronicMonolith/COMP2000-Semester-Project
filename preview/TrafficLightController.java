import javax.swing.*;
import java.awt.*;

public class TrafficLightController {

    public enum LightState {
        RED,
        YELLOW,
        GREEN
    }

    private LightState northLight = LightState.RED;
    private LightState eastLight  = LightState.RED;
    private LightState southLight = LightState.GREEN;
    private LightState westLight  = LightState.RED;

    private Timer timer;

    // ---- Tunable durations (milliseconds) ----
    private final int greenTime  = 8000;   // green duration
    private final int yellowTime = 2000;   // yellow duration

    private int phase = 0;

    public TrafficLightController() {
        start();
    }

    public void start() {
        int initialDelay = (phase == 0) ? greenTime : yellowTime;
        timer = new Timer(initialDelay, e -> nextPhase());
        timer.setInitialDelay(initialDelay);
        timer.setDelay(initialDelay);
        timer.start();
    }

    private void nextPhase() {
        phase = (phase + 1) % 8;
        applyPhase();
    }

    /**
     * Applies the state for the current phase and restarts the timer with
     * the correct delay. Restarting is important: calling setDelay() alone
     * does not affect the currently-scheduled tick.
     */
    private void applyPhase() {
        int delay;

        switch (phase) {
            case 0:
                southLight = LightState.GREEN;
                delay = greenTime;
                break;
            case 1:
                southLight = LightState.YELLOW;
                delay = yellowTime;
                break;
            case 2:
                southLight = LightState.RED;
                northLight = LightState.GREEN;
                delay = greenTime;
                break;
            case 3:
                northLight = LightState.YELLOW;
                delay = yellowTime;
                break;
            case 4:
                northLight = LightState.RED;
                eastLight  = LightState.GREEN;
                delay = greenTime;
                break;
            case 5:
                eastLight  = LightState.YELLOW;
                delay = yellowTime;
                break;
            case 6:
                eastLight  = LightState.RED;
                westLight  = LightState.GREEN;
                delay = greenTime;
                break;
            case 7:
                westLight  = LightState.YELLOW;
                delay = yellowTime;
                break;
            default:
                delay = greenTime;
        }

        timer.setDelay(delay);
        timer.setInitialDelay(delay);
        timer.restart();
    }

    // ------------------------------------------------------------------
    // Getters / setters
    // ------------------------------------------------------------------

    public LightState getNorthLight() { return northLight; }
    public void setNorthLight(LightState s) { this.northLight = s; }

    public LightState getEastLight() { return eastLight; }
    public void setEastLight(LightState s) { this.eastLight = s; }

    public LightState getSouthLight() { return southLight; }
    public void setSouthLight(LightState s) { this.southLight = s; }

    public LightState getWestLight() { return westLight; }
    public void setWestLight(LightState s) { this.westLight = s; }

    public boolean canNorthMove() { return northLight == LightState.GREEN; }
    public boolean canEastMove()  { return eastLight  == LightState.GREEN; }
    public boolean canSouthMove() { return southLight == LightState.GREEN; }
    public boolean canWestMove()  { return westLight  == LightState.GREEN; }

    // ------------------------------------------------------------------
    // Drawing
    // ------------------------------------------------------------------

    public void drawVertical(Graphics2D g2, int x, int y, LightState state) {
        g2.setColor(Color.BLACK);
        g2.fillRoundRect(x, y, 24, 70, 8, 8);

        g2.setColor(state == LightState.RED ? Color.RED : new Color(80, 0, 0));
        g2.fillOval(x + 5, y + 5, 14, 14);

        g2.setColor(state == LightState.YELLOW ? Color.YELLOW : new Color(80, 80, 0));
        g2.fillOval(x + 5, y + 28, 14, 14);

        g2.setColor(state == LightState.GREEN ? Color.GREEN : new Color(0, 80, 0));
        g2.fillOval(x + 5, y + 51, 14, 14);
    }

    public void drawHorizontal(Graphics2D g2, int x, int y, LightState state) {
        g2.setColor(Color.BLACK);
        g2.fillRoundRect(x, y, 70, 24, 8, 8);

        g2.setColor(state == LightState.RED ? Color.RED : new Color(80, 0, 0));
        g2.fillOval(x + 5, y + 5, 14, 14);

        g2.setColor(state == LightState.YELLOW ? Color.YELLOW : new Color(80, 80, 0));
        g2.fillOval(x + 28, y + 5, 14, 14);

        g2.setColor(state == LightState.GREEN ? Color.GREEN : new Color(0, 80, 0));
        g2.fillOval(x + 51, y + 5, 14, 14);
    }

    public void stop() {
        if (timer != null) timer.stop();
    }
}