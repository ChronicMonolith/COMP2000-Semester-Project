import java.awt.*;

public class Lane {

    public int startX;
    public int startY;
    public int endX;
    public int endY;
    public int width;
    public Color color;
    public Direction direction;

    public Lane(int startX, int startY, int endX, int endY, int width, Color color, Direction direction) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.width = width;
        this.color = color;
        this.direction = direction;
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

    public Direction getDirection() {
        return this.direction;
    }
}