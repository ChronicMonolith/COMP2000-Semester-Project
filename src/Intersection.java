import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Intersection {
    public int x;
    public int y;
    public int width;
    public int height;

    private final Set<Direction> exits;
    private TrafficLightController light;

    // A normal four-way junction.
    public Intersection(int x, int y, int width, int height) {
        this(x, y, width, height, EnumSet.allOf(Direction.class));
    }

    public Intersection(int x, int y, int width, int height, Set<Direction> exits) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.exits = EnumSet.copyOf(exits);
    }

    public boolean contains(int cx, int cy) {
        return cx >= x && cx <= x + width &&
                cy >= y && cy <= y + height;
    }

    public int centerX() {
        return x + width / 2;
    }

    public int centerY() {
        return y + height / 2;
    }

    // roads attached to this junction

    public boolean hasExit(Direction heading) {
        return exits.contains(heading);
    }

    public boolean hasApproach(Direction heading) {
        return exits.contains(heading.opposite());
    }

    public Set<Direction> getApproaches() {
        return Arrays.stream(Direction.values())
                .parallel()
                .filter(this::hasApproach)
                .collect(Collectors.toCollection(
                        () -> EnumSet.noneOf(Direction.class)));
    }

    // lane geometry
    public Point2D.Double laneExit(Direction heading) {
        switch (heading) {
            case EAST:
                return new Point2D.Double(x + width, y + height / 4.0);
            case WEST:
                return new Point2D.Double(x, y + 3 * height / 4.0);
            case SOUTH:
                return new Point2D.Double(x + 3 * width / 4.0, y + height);
            default: // NORTH
                return new Point2D.Double(x + width / 4.0, y);
        }
    }

    public double distanceToEdge(Direction heading, double cx, double cy) {
        switch (heading) {
            case EAST:
                return (cy >= y && cy <= y + height) ? x - cx : Double.POSITIVE_INFINITY;
            case WEST:
                return (cy >= y && cy <= y + height) ? cx - (x + width) : Double.POSITIVE_INFINITY;
            case SOUTH:
                return (cx >= x && cx <= x + width) ? y - cy : Double.POSITIVE_INFINITY;
            default: // NORTH
                return (cx >= x && cx <= x + width) ? cy - (y + height) : Double.POSITIVE_INFINITY;
        }
    }

    // traffic light

    public void setLight(TrafficLightController light) {
        this.light = light;
    }

    public TrafficLightController getLight() {
        return light;
    }

    public void update(int dtMs) {
        if (light != null) {
            light.update(dtMs);
        }
    }

    // drawing

    public void draw(Graphics2D g) {
        g.setColor(new Color(51, 51, 51));
        g.fillRect(x, y, width, height);
    }

    public void drawSignals(Graphics2D g) {
        if (light == null) {
            return;
        }
        for (Direction d : getApproaches()) {
            TrafficLightController.LightState state = light.getLight(d);
            switch (d) {
                case SOUTH:
                    light.drawVertical(g, x + width + 4, y - 74, state);
                    break;
                case NORTH:
                    light.drawVertical(g, x - 28, y + height + 4, state);
                    break;
                case EAST:
                    light.drawHorizontal(g, x - 74, y - 28, state);
                    break;
                default: // WEST
                    light.drawHorizontal(g, x + width + 4, y + height + 4, state);
                    break;
            }
        }
    }
}
