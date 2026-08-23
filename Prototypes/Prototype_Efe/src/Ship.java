import java.awt.*;

public class Ship {

    private double x;
    private double speed = 1.5;
    private boolean movingRight;

    public Ship(double startX, boolean movingRight) {
        this.x = startX;
        this.movingRight = movingRight;
    }

    public void update() {
        if (movingRight) {
            x += speed;
        } else {
            x -= speed;
        }
    }

    public void stopAtLight() {

        if (movingRight) {
            x = 240;
        } else {
            x = 630;
        }
    }

    public double getX() {
        return x;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public boolean hasPassedBridge() {

        if (movingRight) {
            return x >= 550;
        } else {
            return x <= 400;
        }
    }

    public boolean hasLeftScreen() {

        if (movingRight) {
            return x > 1100;
        } else {
            return x < -160;
        }
    }

    public void setX(double x) {
    this.x = x;
    }

    public void draw(Graphics g) {

        int y = 332;

        g.setColor(new Color(90, 60, 40));

        int[] xPoints;
        int[] yPoints;

        if (movingRight) {

            xPoints = new int[] {
                (int)x,
                (int)x + 130,
                (int)x + 110,
                (int)x + 20
            };

            yPoints = new int[] {
                y,
                y,
                y + 36,
                y + 36
            };

        } else {

            xPoints = new int[] {
                (int)x + 130,
                (int)x,
                (int)x + 20,
                (int)x + 110
            };

            yPoints = new int[] {
                y,
                y,
                y + 36,
                y + 36
            };
        }

        g.fillPolygon(
            xPoints,
            yPoints,
            4
        );

        // Cabin
        g.setColor(Color.LIGHT_GRAY);

        g.fillRect(
            (int)x + 40,
            y - 22,
            50,
            22
        );

        // Windows
        g.setColor(
            new Color(50, 150, 200)
        );

        g.fillRect(
            (int)x + 50,
            y - 16,
            10,
            10
        );

        g.fillRect(
            (int)x + 70,
            y - 16,
            10,
            10
        );

        // Smoke stack
        g.setColor(Color.DARK_GRAY);

        g.fillRect(
            (int)x + 60,
            y - 35,
            10,
            13
        );
    }
}