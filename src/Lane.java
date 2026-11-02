import java.awt.*;

public class Lane {

    public int startX;
    public int startY;
    public int endX;
    public int endY;
    public int width;
    public Color color;
    public Direction direction;

    public double speedLimit;

    public Lane(int startX, int startY, int endX, int endY, int width, Color color, Direction direction,
            double speedLimit) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.width = width;
        this.color = color;
        this.direction = direction;
        this.speedLimit = speedLimit;
    }

    public void draw(Graphics2D g) {
        g.setColor(color);
        int dx = endX - startX;
        int dy = endY - startY;
        if (Math.abs(dx) > Math.abs(dy)) {
            g.fillRect(startX, startY, dx, width);
        } else {
            g.fillRect(startX, startY, width, dy);
        }
    }

    public double getSpeedLimit() {
        return speedLimit;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public boolean contains(double x, double y) {

        if (Math.abs(endX - startX) > Math.abs(endY - startY)) {
            return x >= Math.min(startX, endX)
                    && x <= Math.max(startX, endX)
                    && y >= startY
                    && y <= startY + width;
        } else {
            return x >= startX
                    && x <= startX + width
                    && y >= Math.min(startY, endY)
                    && y <= Math.max(startY, endY);
        }
    }
}