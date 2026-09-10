import java.awt.*;

public class Roundabout {

    public int x, y;
    public int diameter;
    public int inner;
    public int arcRadius;

    public Roundabout(int x, int y, int diameter) {
        this.x = x;
        this.y = y;
        this.diameter = diameter;

        this.inner = diameter - 60;
        this.arcRadius = (inner / 2) + 12;
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(51, 51, 51));
        g.fillOval(x - diameter / 2, y - diameter / 2, diameter, diameter);

        g.setColor(new Color(230, 230, 230));
        g.fillOval(x - inner / 2, y - inner / 2, inner, inner);

        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(3));
        g.drawOval(x - inner / 2, y - inner / 2, inner, inner);
    }
}