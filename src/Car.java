import java.awt.*;

public class Car {

    public double x;
    public double y;
    public int diameter = 16; // Circle size

    public double speed;
    public Direction direction;

    public Car(double x, double y, double speed, Direction direction) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.direction = direction;
    }

    public void move() {
        x += speed * direction.dx;
        y += speed * direction.dy;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.RED);
        g.fillOval((int) x, (int) y, diameter, diameter);

        // Dark border for clean visual
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1));
        g.drawOval((int) x, (int) y, diameter, diameter);
    }

    public double getCenterX() {
        return x + (diameter / 2.0);
    }

    public double getCenterY() {
        return y + (diameter / 2.0);
    }
}