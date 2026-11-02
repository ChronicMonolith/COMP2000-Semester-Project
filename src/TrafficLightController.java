import java.awt.*;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class TrafficLightController {

    public enum LightState {
        RED,
        YELLOW,
        GREEN
    }

    private static final Direction[] ORDER = { Direction.SOUTH, Direction.NORTH, Direction.EAST, Direction.WEST };

    private final Map<Direction, LightState> lights = new EnumMap<>(Direction.class);
    private final Set<Direction> approaches;

    private SignalPhase phase;
    private int remainingMs;

    public TrafficLightController() {
        this(EnumSet.allOf(Direction.class), 0);
    }

    /**
     * @param approaches    headings that actually arrive at this junction (a T-junction has 3)
     * @param startOffsetMs fast-forwards the cycle so neighbouring junctions are not in sync
     */
    public TrafficLightController(Set<Direction> approaches, int startOffsetMs) {
        this.approaches = EnumSet.copyOf(approaches);
        setAllRed();
        phase = new SignalPhase.Green(firstApproach());
        phase.enter(this);
        remainingMs = phase.durationMs();
        update(startOffsetMs);
    }

    public final void update(int dtMs) {
        remainingMs -= dtMs;
        while (remainingMs <= 0) {
            phase = phase.next(this);
            phase.enter(this);
            remainingMs += phase.durationMs();
        }
    }

    //used by SignalPhase

    void setAllRed() {
        for (Direction d : Direction.values()) {
            lights.put(d, LightState.RED);
        }
    }

    void setLight(Direction d, LightState state) {
        lights.put(d, state);
    }

    Direction nextApproach(Direction current) {
        int i = indexOf(current);
        for (int step = 1; step <= ORDER.length; step++) {
            Direction candidate = ORDER[(i + step) % ORDER.length];
            if (approaches.contains(candidate)) {
                return candidate;
            }
        }
        return current;
    }

    private Direction firstApproach() {
        for (Direction d : ORDER) {
            if (approaches.contains(d)) {
                return d;
            }
        }
        throw new IllegalArgumentException("A traffic light needs at least one approach");
    }

    private static int indexOf(Direction d) {
        for (int i = 0; i < ORDER.length; i++) {
            if (ORDER[i] == d) {
                return i;
            }
        }
        return 0;
    }

    //queries

    public LightState getLight(Direction heading) {
        return lights.get(heading);
    }

    public boolean canMove(Direction heading) {
        return lights.get(heading) == LightState.GREEN;
    }

    public LightState getNorthLight() {
        return getLight(Direction.NORTH);
    }

    public void setNorthLight(LightState northLight) {
        setLight(Direction.NORTH, northLight);
    }

    public LightState getEastLight() {
        return getLight(Direction.EAST);
    }

    public void setEastLight(LightState eastLight) {
        setLight(Direction.EAST, eastLight);
    }

    public LightState getSouthLight() {
        return getLight(Direction.SOUTH);
    }

    public void setSouthLight(LightState southLight) {
        setLight(Direction.SOUTH, southLight);
    }

    public LightState getWestLight() {
        return getLight(Direction.WEST);
    }

    public void setWestLight(LightState westLight) {
        setLight(Direction.WEST, westLight);
    }

    public boolean canNorthMove() {
        return canMove(Direction.NORTH);
    }

    public boolean canEastMove() {
        return canMove(Direction.EAST);
    }

    public boolean canSouthMove() {
        return canMove(Direction.SOUTH);
    }

    public boolean canWestMove() {
        return canMove(Direction.WEST);
    }

    //drawing

    public void drawVertical(
            Graphics2D g2,
            int x,
            int y,
            LightState state) {
        g2.setColor(Color.BLACK);

        g2.fillRoundRect(
                x,
                y,
                24,
                70,
                8,
                8);

        if (state == LightState.RED) {
            g2.setColor(Color.RED);
        } else {
            g2.setColor(new Color(80, 0, 0));
        }

        g2.fillOval(
                x + 5,
                y + 5,
                14,
                14);

        if (state == LightState.YELLOW) {
            g2.setColor(Color.YELLOW);
        } else {
            g2.setColor(new Color(80, 80, 0));
        }

        g2.fillOval(
                x + 5,
                y + 28,
                14,
                14);

        if (state == LightState.GREEN) {
            g2.setColor(Color.GREEN);
        } else {
            g2.setColor(new Color(0, 80, 0));
        }

        g2.fillOval(
                x + 5,
                y + 51,
                14,
                14);
    }

    public void drawHorizontal(
            Graphics2D g2,
            int x,
            int y,
            LightState state) {
        g2.setColor(Color.BLACK);

        g2.fillRoundRect(
                x,
                y,
                70,
                24,
                8,
                8);

        if (state == LightState.RED) {
            g2.setColor(Color.RED);
        } else {
            g2.setColor(new Color(80, 0, 0));
        }

        g2.fillOval(
                x + 5,
                y + 5,
                14,
                14);

        if (state == LightState.YELLOW) {
            g2.setColor(Color.YELLOW);
        } else {
            g2.setColor(new Color(80, 80, 0));
        }

        g2.fillOval(
                x + 28,
                y + 5,
                14,
                14);

        if (state == LightState.GREEN) {
            g2.setColor(Color.GREEN);
        } else {
            g2.setColor(new Color(0, 80, 0));
        }

        g2.fillOval(
                x + 51,
                y + 5,
                14,
                14);
    }
}
