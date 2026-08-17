import java.awt.*;

class Ship {
    // Ship class

    private double x = 0;

    private double speed = 1.5;

    public void update() {

        x += speed;

        // Reset ship after leaving screen
        if (x > 1050) {

            x = -150;
        }
    }

    public double getX() {

        return x;
    }

    public void draw(Graphics g) {

        int y = 430;

        // Ship body
        g.setColor(Color.BLACK);

        int[] xPoints = {
                (int) x,
                (int) x + 100,
                (int) x + 80,
                (int) x + 20
        };

        int[] yPoints = {
                y,
                y,
                y + 30,
                y + 30
        };

        g.fillPolygon(
                xPoints,
                yPoints,
                4
        );

        // Ship cabin
        g.setColor(Color.WHITE);

        g.fillRect(
                (int) x + 35,
                y - 25,
                35,
                25
        );

        // Smoke stack
        g.setColor(Color.GRAY);

        g.fillRect(
                (int) x + 55,
                y - 40,
                10,
                15
        );
    }
}