import java.awt.*;

class Car {
    // Vehicle class

    private double x;
    private double y;

    private boolean movingRight;

    private double speed = 2.5;

    public Car(double x, double y, boolean movingRight) {

        this.x = x;
        this.y = y;

        this.movingRight = movingRight;
    }

    public void update(boolean bridgeOpen) {

        // Stop when bridge is open
        if (bridgeOpen) {

            if (movingRight && x < 390) {

                x += speed;

            } else if (!movingRight && x > 610) {

                x -= speed;
            }

        } else {

            if (movingRight) {

                x += speed;

            } else {

                x -= speed;
            }
        }
    }

    public void draw(Graphics g) {

        g.setColor(Color.RED);

        g.fillRect(
                (int) x,
                (int) y,
                35,
                20
        );

        // Wheels
        g.setColor(Color.BLACK);

        g.fillOval(
                (int) x + 5,
                (int) y + 15,
                8,
                8
        );

        g.fillOval(
                (int) x + 23,
                (int) y + 15,
                8,
                8
        );
    }

    public double getX() {

        return x;
    }
}
