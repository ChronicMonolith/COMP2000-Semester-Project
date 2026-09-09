import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Roundabout implements CarPositionProvider {

    public int cx, cy;
    public int diameter;
    public int inner;
    public int arcRadius;

    private List<Point> arcPath = null;
    private Direction exitDirection = null;

    public Roundabout(int cx, int cy, int diameter) {
        this.cx = cx;
        this.cy = cy;
        this.diameter = diameter;

        this.inner = diameter - 60;
        this.arcRadius = (inner / 2) + 12;
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(51, 51, 51));
        g.fillOval(cx - diameter / 2, cy - diameter / 2, diameter, diameter);

        g.setColor(new Color(230, 230, 230));
        g.fillOval(cx - inner / 2, cy - inner / 2, inner, inner);

        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(3));
        g.drawOval(cx - inner / 2, cy - inner / 2, inner, inner);
    }

    public List<Point> generateArc(Direction entryDir, TurnDecision decision) {
        double startAngle = getEntryAngle(entryDir);
        double turnSpan = getTurnSpan(decision);

        this.exitDirection = calculateExitDirection(entryDir, decision);

        List<Point> arc = new ArrayList<>();
        int steps = 36;

        for (int i = 0; i <= steps; i++) {
            double progress = (double) i / steps;
            double a = startAngle + (turnSpan * progress);

            int px = (int) (cx + arcRadius * Math.cos(a));
            int py = (int) (cy + arcRadius * Math.sin(a));
            arc.add(new Point(px, py));
        }

        arcPath = arc;
        return arc;
    }

    private double getEntryAngle(Direction dir) {
        return switch (dir) {
            case EAST -> Math.PI / 2;
            case WEST -> 3 * Math.PI / 2;
            case NORTH -> Math.PI;
            case SOUTH -> 0;
        };
    }

    private double getTurnSpan(TurnDecision d) {
        return switch (d) {
            case RIGHT -> Math.PI / 2;
            case STRAIGHT -> Math.PI;
            case LEFT -> 3 * Math.PI / 2;
        };
    }

    private Direction calculateExitDirection(Direction entry, TurnDecision decision) {
        Direction[] dirs = { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };
        int index = 0;
        for (int i = 0; i < dirs.length; i++) {
            if (dirs[i] == entry) {
                index = i;
                break;
            }
        }

        int shift = switch (decision) {
            case RIGHT -> 1;
            case STRAIGHT -> 2;
            case LEFT -> 3;
        };

        return dirs[(index + shift) % 4];
    }

    public Direction getExitDirection() {
        return exitDirection;
    }

    @Override
    public boolean contains(Car car) {
        double dx = car.getCenterX() - cx;
        double dy = car.getCenterY() - cy;
        double radius = (diameter / 2.0);

        return (dx * dx + dy * dy) <= (radius * radius);
    }

    @Override
    public int getX(Car car) {
        return (int) car.x;
    }

    @Override
    public int getY(Car car) {
        return (int) car.y;
    }
}