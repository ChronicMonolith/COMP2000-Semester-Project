import java.awt.*;

public class Car implements Interactable {

    private double x;
    private double y;

    private int width = 20;
    private int height = 32;

    private boolean movingDown;
    public boolean isMovingDown() { return this.movingDown; }

    private double speed = 2.0;
    public double getSpeed() { return this.speed; }

    private String type = "4 Seater";
    public String getType() { return  this.type; }

    public boolean isSelected = false;

    /*
     * TOP CAR
     * The top traffic light is around y = 230.
     * Cars stop around y = 190,
     */

    private static final double TOP_STOP = 190;

    /*
     * BOTTOM CAR
     * The bottom traffic light is around y = 425.
     * Cars stop around y = 490,
     */

    private static final double BOTTOM_STOP = 490;

    /*
     * Space between cars.
     */

    private static final double CAR_GAP = 70;

    public Car(
        double x,
        double y,
        boolean movingDown
    ) {

        this.x = x;

        this.y = y;

        this.movingDown = movingDown;
    }

    public boolean isClickable(int mX, int mY) { // Input is mouse position
        if (mX < x)
            return false;
        if (mY < y)
            return false;
        if (mX > x + width)
            return false;
        if (mY > y + height)
            return false;

        return true;
    }

    public void onClick() {
        System.out.printf("Speed: " + Double.toString(speed) + "\n");
    }

    public void update(
        boolean stopSignal,
        double frontCarY
    ) {

        // ============================================
        // TOP → BOTTOM
        // ============================================

        if (movingDown) {

            /*
             * RED/YELLOW LIGHT
             * Car has NOT reached the stop line
             * Move toward the light.
             */

            if (stopSignal && y < TOP_STOP) {

                y += speed;

                if (y > TOP_STOP) {

                    y = TOP_STOP;
                }

                return;
            }

            /*
             * If there is a car ahead,
             * maintain the safety gap.
             */

            if (
                frontCarY != -1 &&
                frontCarY > y &&
                frontCarY - y < CAR_GAP
            ) {

                return;
            }

            /*
             * GREEN
             * OR
             * Car has already passed
             * the stop line.
             */

            y += speed;
        }

        // ============================================
        // BOTTOM → TOP
        // ============================================

        else {

            /*
             * RED/YELLOW LIGHT
             * Move toward the stop line.
             */

            if (stopSignal && y > BOTTOM_STOP) {

                y -= speed;

                if (y < BOTTOM_STOP) {

                    y = BOTTOM_STOP;
                }

                return;
            }

            /*
             * Safety gap.
             */

            if (
                frontCarY != -1 &&
                frontCarY < y &&
                y - frontCarY < CAR_GAP
            ) {

                return;
            }

            /*
             * GREEN
             * OR
             * Already past the light.
             */

            y -= speed;
        }
    }

    public void draw(Graphics g) {

        // Car colour

        if (movingDown) {

            g.setColor(
                new Color(220, 50, 50)
            );

        } else {

            g.setColor(
                new Color(50, 100, 220)
            );
        }

        // Body

        g.fillRect(
            (int) x,
            (int) y,
            width,
            height
        );

        // Windows

        g.setColor(
            new Color(200, 220, 240)
        );

        if (movingDown) {

            g.fillRect(
                (int) x + 3,
                (int) y + 6,
                14,
                8
            );

        } else {

            g.fillRect(
                (int) x + 3,
                (int) y + 18,
                14,
                8
            );
        }

        // Wheels

        g.setColor(Color.BLACK);

        g.fillRect(
            (int) x - 3,
            (int) y + 5,
            4,
            7
        );

        g.fillRect(
            (int) x + 19,
            (int) y + 5,
            4,
            7
        );

        g.fillRect(
            (int) x - 3,
            (int) y + 20,
            4,
            7
        );

        g.fillRect(
            (int) x + 19,
            (int) y + 20,
            4,
            7
        );
    }

    public double getY() {

        return y;
    }
}