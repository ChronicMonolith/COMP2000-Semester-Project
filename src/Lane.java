import java.awt.*;

public class Lane implements CarPositionProvider {

    public int startX;
    public int startY;
    public int endX;
    public int endY;
    public int width;
    public Color color;

    public Lane(int startX, int startY, int endX, int endY, int width, Color color) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.width = width;
        this.color = color;
    }

    public void draw(Graphics2D g) {
        int dx = endX - startX;
        int dy = endY - startY;
        if (Math.abs(dx) > Math.abs(dy)) {
            g.fillRect(startX, startY, dx, width);
        } else {
            g.fillRect(startX, startY, width, dy);
        }
    }

    @Override
    public boolean contains(Car car) {
        double cx = car.getCenterX();
        double cy = car.getCenterY();

        int minX = Math.min(startX, endX);
        int maxX = Math.max(startX, endX) + (startX == endX ? width : 0);
        int minY = Math.min(startY, endY);
        int maxY = Math.max(startY, endY) + (startY == endY ? width : 0);

        return cx >= minX && cx <= maxX && cy >= minY && cy <= maxY;
    }

    @Override
    public int getX(Car car) {
        int dx = endX - startX;
        int dy = endY - startY;

        if (Math.abs(dy) > Math.abs(dx)) {
            return startX + (width / 2) - (car.diameter / 2);
        }
        return (int) car.x;
    }

    @Override
    public int getY(Car car) {
        int dx = endX - startX;
        int dy = endY - startY;

        if (Math.abs(dx) >= Math.abs(dy)) {
            return startY + (width / 2) - (car.diameter / 2);
        }
        return (int) car.y;
    }
}