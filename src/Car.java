import java.awt.*;
import java.awt.geom.Point2D;

public class Car implements Interactable {
    private static int nextId = 1;

    public final int id = nextId++;

    public double x;
    public double y;
    public int diameter = 16;
    public Direction direction;
    public boolean hasTurned = false;
    public boolean stopped = false;

    public double acceleration;

    public double speed;
    public double maxSpeed;

    public CarState state = CarState.DRIVING;
    public TurnDirection turnDirection = TurnDirection.STRAIGHT;
    public TurnStrategy turnStrategy = TurnStrategy.RANDOM;

    public boolean isSelected;

    /** Frames spent stopped because another car is in the way. */
    public int blockedFrames = 0;

    // which way the car is actually pointing
    private double headingX;
    private double headingY;

    private Point2D.Double p0;
    private Point2D.Double p1;
    private Point2D.Double p2;
    private double t;
    private Direction exitDirection;

    public Car(double x, double y, double speed, Direction direction, double acceleration) {
        this.x = x;

        this.y = y;
        this.speed = speed;
        this.maxSpeed = speed;
        this.direction = direction;
        this.headingX = direction.dx;
        this.headingY = direction.dy;
        this.acceleration = acceleration;

        isSelected = false;
    }

    public boolean isClickable(int mX, int mY) {
        return Math.hypot(mX - getCenterX(), mY - getCenterY()) <= diameter;
    }

    public void onClick() {
        isSelected = true;
    }

    public void onDeselect() {
        isSelected = false;
    }

    public void startTurn(Direction newDirection, Point2D.Double exitPoint) {
        p0 = new Point2D.Double(getCenterX(), getCenterY());
        p2 = exitPoint;
        // control point is where the entry lane line meets the exit lane line
        p1 = (direction.dx != 0)
                ? new Point2D.Double(p2.x, p0.y)
                : new Point2D.Double(p0.x, p2.y);
        this.exitDirection = newDirection;
        this.t = 0;
        this.state = CarState.TURNING;
    }

    public TurnDirection chooseRandomTurn() {
        double r = Math.random();
        if (r < 0.33)
            return TurnDirection.LEFT;
        if (r < 0.66)
            return TurnDirection.RIGHT;
        return TurnDirection.STRAIGHT;
    }

    public void adjustSpeed(double allowedSpeed, double brakeDeceleration) {
        if (speed > allowedSpeed) {
            speed = Math.max(allowedSpeed, speed - brakeDeceleration * 3); // up to 3x brake in an emergency
        } else {
            speed = Math.min(allowedSpeed, speed + acceleration);
        }
    }

    public void move() {
        if (state == CarState.TURNING) {
            advanceTurn();
            return;
        }
        x += speed * direction.dx;
        y += speed * direction.dy;
        headingX = direction.dx;
        headingY = direction.dy;
    }

    private void advanceTurn() {
        double dxdt = 2 * (1 - t) * (p1.x - p0.x) + 2 * t * (p2.x - p1.x);
        double dydt = 2 * (1 - t) * (p1.y - p0.y) + 2 * t * (p2.y - p1.y);
        double len = Math.max(Math.hypot(dxdt, dydt), 1e-6);

        t += speed / len; // step a fixed distance along the curve

        if (t >= 1) {
            direction = exitDirection;
            x = p2.x - diameter / 2.0;
            y = p2.y - diameter / 2.0;
            headingX = direction.dx;
            headingY = direction.dy;
            state = CarState.DRIVING;
            return;
        }

        double u = 1 - t;
        double px = u * u * p0.x + 2 * u * t * p1.x + t * t * p2.x;
        double py = u * u * p0.y + 2 * u * t * p1.y + t * t * p2.y;
        x = px - diameter / 2.0;
        y = py - diameter / 2.0;

        double hx = 2 * u * (p1.x - p0.x) + 2 * t * (p2.x - p1.x);
        double hy = 2 * u * (p1.y - p0.y) + 2 * t * (p2.y - p1.y);
        double hl = Math.max(Math.hypot(hx, hy), 1e-6);
        headingX = hx / hl;
        headingY = hy / hl;
    }

    public void draw(Graphics2D g) {
        if (isSelected)
            g.setColor(Color.GREEN);
        else
            g.setColor(Color.RED);

        g.fillOval((int) Math.round(x), (int) Math.round(y), diameter, diameter);

        // small dark dot on the front of the car so you can see which way it is facing
        g.setColor(Color.BLACK);
        int dot = 5;
        g.fillOval((int) Math.round(getCenterX() + headingX * 5 - dot / 2.0),
                (int) Math.round(getCenterY() + headingY * 5 - dot / 2.0), dot, dot);
    }

    public Direction getDirection() {
        return this.direction;
    }

    public double getHeadingX() {
        return headingX;
    }

    public double getHeadingY() {
        return headingY;
    }

    public double getCenterX() {
        return x + (diameter / 2.0);
    }

    public double getCenterY() {
        return y + (diameter / 2.0);
    }
}
