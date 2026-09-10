import java.awt.*;

public class Car {

    public double x;
    public double y;
    public int diameter = 16;
    public Direction direction;
    public boolean hasTurned = false;
    public boolean stopped = false;

    public double speed;

    public CarState state = CarState.DRIVING;
    public TurnDirection turnDirection = TurnDirection.STRAIGHT;

    public Car(double x, double y, double speed, Direction direction) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.direction = direction;
    }

    public void startTurn(Direction newDirection, int laneCenterX, int laneCenterY) {
        this.direction = newDirection;
        this.x = laneCenterX - (diameter / 2.0);
        this.y = laneCenterY - (diameter / 2.0);
        this.state = CarState.DRIVING;
    }

    public TurnDirection chooseRandomTurn() {
        double r = Math.random();
        if (r < 0.33)
            return TurnDirection.LEFT;
        if (r < 0.66)
            return TurnDirection.RIGHT;
        return TurnDirection.STRAIGHT;
    }

    public void move() {
        if (stopped) {
            System.out.println("Car at (" + x + ", " + y + ") is STOPPED.");
            return;
        }
        x += speed * direction.dx;
        y += speed * direction.dy;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.RED);
        g.fillOval((int) Math.round(x), (int) Math.round(y), diameter, diameter);
    }

    public Direction getDirection() {
        return this.direction;
    }

    public double getCenterX() {
        return x + (diameter / 2.0);
    }

    public double getCenterY() {
        return y + (diameter / 2.0);
    }
}