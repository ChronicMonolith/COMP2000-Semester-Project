import java.awt.*;

public class Intersection {
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

    public void draw(Graphics2D g) {
        g.setColor(new Color(51, 51, 51));
        g.fillRect(x, y, width, height);
    }
}
