import javax.swing.*;
import java.awt.*;

public class TrafficLightController {

    public enum LightState {
        RED,
        YELLOW,
        GREEN
    }

    // Four directions
    private LightState northLight = LightState.RED;
    private LightState eastLight = LightState.RED;
    private LightState southLight = LightState.GREEN;
    private LightState westLight = LightState.RED;

    private Timer timer;

    private final int greenTime = 5000;
    private final int yellowTime = 2000;

    private int phase = 0;

    public TrafficLightController() {
        start();
    }

    // =========================================================
    // START TRAFFIC LIGHT
    // =========================================================

    public void start() {

        timer = new Timer(
                greenTime,
                e -> nextPhase()
        );

        timer.start();
    }

    // =========================================================
    // CHANGE TRAFFIC LIGHT PHASE
    // =========================================================

    private void nextPhase() {

        phase++;

        switch (phase) {

            case 1:

                southLight = LightState.YELLOW;

                timer.setDelay(yellowTime);

                break;

            case 2:

                southLight = LightState.RED;
                northLight = LightState.GREEN;

                timer.setDelay(greenTime);

                break;

            case 3:

                northLight = LightState.YELLOW;

                timer.setDelay(yellowTime);

                break;

            case 4:

                northLight = LightState.RED;
                eastLight = LightState.GREEN;

                timer.setDelay(greenTime);

                break;

            case 5:

                eastLight = LightState.YELLOW;

                timer.setDelay(yellowTime);

                break;

            case 6:

                eastLight = LightState.RED;
                westLight = LightState.GREEN;

                timer.setDelay(greenTime);

                break;

            case 7:

                westLight = LightState.YELLOW;

                timer.setDelay(yellowTime);

                break;

            case 8:

                westLight = LightState.RED;
                southLight = LightState.GREEN;

                phase = 0;

                timer.setDelay(greenTime);

                break;
        }
    }

    // =========================================================
    // GET LIGHT STATES
    // =========================================================

    public LightState getNorthLight() {
        return northLight;
    }

    public LightState getEastLight() {
        return eastLight;
    }

    public LightState getSouthLight() {
        return southLight;
    }

    public LightState getWestLight() {
        return westLight;
    }

    // =========================================================
    // VEHICLE MOVEMENT CHECKS
    // =========================================================

    public boolean canNorthMove() {
        return northLight == LightState.GREEN;
    }

    public boolean canEastMove() {
        return eastLight == LightState.GREEN;
    }

    public boolean canSouthMove() {
        return southLight == LightState.GREEN;
    }

    public boolean canWestMove() {
        return westLight == LightState.GREEN;
    }

    // =========================================================
    // VERTICAL TRAFFIC LIGHT
    // =========================================================

    public void drawVertical(
            Graphics2D g2,
            int x,
            int y,
            LightState state
    ) {

        // Housing
        g2.setColor(Color.BLACK);

        g2.fillRoundRect(
                x,
                y,
                24,
                70,
                8,
                8
        );

        // RED
        if (state == LightState.RED) {
            g2.setColor(Color.RED);
        } else {
            g2.setColor(new Color(80, 0, 0));
        }

        g2.fillOval(
                x + 5,
                y + 5,
                14,
                14
        );

        // YELLOW
        if (state == LightState.YELLOW) {
            g2.setColor(Color.YELLOW);
        } else {
            g2.setColor(new Color(80, 80, 0));
        }

        g2.fillOval(
                x + 5,
                y + 28,
                14,
                14
        );

        // GREEN
        if (state == LightState.GREEN) {
            g2.setColor(Color.GREEN);
        } else {
            g2.setColor(new Color(0, 80, 0));
        }

        g2.fillOval(
                x + 5,
                y + 51,
                14,
                14
        );
    }

    // =========================================================
    // HORIZONTAL TRAFFIC LIGHT
    // =========================================================

    public void drawHorizontal(
            Graphics2D g2,
            int x,
            int y,
            LightState state
    ) {

        // Housing
        g2.setColor(Color.BLACK);

        g2.fillRoundRect(
                x,
                y,
                70,
                24,
                8,
                8
        );

        // RED
        if (state == LightState.RED) {
            g2.setColor(Color.RED);
        } else {
            g2.setColor(new Color(80, 0, 0));
        }

        g2.fillOval(
                x + 5,
                y + 5,
                14,
                14
        );

        // YELLOW
        if (state == LightState.YELLOW) {
            g2.setColor(Color.YELLOW);
        } else {
            g2.setColor(new Color(80, 80, 0));
        }

        g2.fillOval(
                x + 28,
                y + 5,
                14,
                14
        );

        // GREEN
        if (state == LightState.GREEN) {
            g2.setColor(Color.GREEN);
        } else {
            g2.setColor(new Color(0, 80, 0));
        }

        g2.fillOval(
                x + 51,
                y + 5,
                14,
                14
        );
    }

    // =========================================================
    // STOP CONTROLLER
    // =========================================================

    public void stop() {

        if (timer != null) {
            timer.stop();
        }
    }
}
