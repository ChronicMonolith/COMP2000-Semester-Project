import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Intersection implements CarPositionProvider {

    public int x;
    public int y;
    public int width;
    public int height;

    public Intersection(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(51, 51, 51));
        g.fillRect(x, y, width, height);
    }

    public List<Point> generateLeftTurnArc(Direction currentDir) {
        List<Point> arc = new ArrayList<>();
        int steps = 20;

        double cx = x + (width / 2.0);
        double cy = y + (height / 2.0);

        double startX = cx, startY = cy;
        double endX = cx, endY = cy;

        switch (currentDir) {
            case EAST -> {
                startX = x;
                startY = y + (height * 0.75);
                endX = x + (width * 0.25);
                endY = y;
            }
            case WEST -> {
                startX = x + width;
                startY = y + (height * 0.25);
                endX = x + (width * 0.75);
                endY = y + height;
            }
            case NORTH -> {
                startX = x + (width * 0.75);
                startY = y + height;
                endX = x;
                endY = y + (height * 0.75);
            }
            case SOUTH -> {
                startX = x + (width * 0.25);
                startY = y;
                endX = x + width;
                endY = y + (height * 0.25);
            }
        }

        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps;
            double px = (1 - t) * (1 - t) * startX + 2 * (1 - t) * t * cx + t * t * endX;
            double py = (1 - t) * (1 - t) * startY + 2 * (1 - t) * t * cy + t * t * endY;
            arc.add(new Point((int) px, (int) py));
        }

        return arc;
    }

    public Direction getLeftTurnExitDirection(Direction currentDir) {
        return switch (currentDir) {
            case EAST -> Direction.NORTH;
            case WEST -> Direction.SOUTH;
            case NORTH -> Direction.WEST;
            case SOUTH -> Direction.EAST;
        };
    }

    @Override
    public boolean contains(Car car) {
        double cx = car.getCenterX();
        double cy = car.getCenterY();
        return cx >= x && cx <= x + width && cy >= y && cy <= y + height;
    }

    @Override
    public int getX(Car car) {
        return (int) car.x;
    }

    @Override
    public int getY(Car car) {
        return y + (height / 2) - (car.diameter / 2);
    }
}