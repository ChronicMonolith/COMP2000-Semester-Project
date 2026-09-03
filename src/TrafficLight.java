import java.awt.*;

public class TrafficLight {

    // 0 = Red
    // 1 = Yellow
    // 2 = Green

    private int state;

    private boolean isVertical;

    public TrafficLight(boolean isVertical, int initialState) {

        this.isVertical = isVertical;
        this.state = initialState;
    }

    public void setState(int state) {

        this.state = state;
    }

    public int getState() {

        return state;
    }

    public void draw(Graphics g, int x, int y) {

        g.setColor(Color.DARK_GRAY);

        if (isVertical) {

            g.fillRect(x + 3, y - 15, 14, 15);

            g.setColor(Color.BLACK);

            g.fillRect(x, y, 20, 52);

            // RED
            if (state == 0) {
                g.setColor(Color.RED);
            } else {
                g.setColor(new Color(80, 0, 0));
            }

            g.fillOval(x + 5, y + 4, 10, 10);

            // YELLOW
            if (state == 1) {
                g.setColor(Color.YELLOW);
            } else {
                g.setColor(new Color(80, 80, 0));
            }

            g.fillOval(x + 5, y + 21, 10, 10);

            // GREEN
            if (state == 2) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(new Color(0, 80, 0));
            }

            g.fillOval(x + 5, y + 38, 10, 10);

        } else {

            g.fillRect(x + 4, y + 26, 10, 20);

            g.setColor(Color.BLACK);

            g.fillRect(x, y, 18, 52);

            // RED
            if (state == 0) {
                g.setColor(Color.RED);
            } else {
                g.setColor(new Color(80, 0, 0));
            }

            g.fillOval(x + 4, y + 4, 10, 10);

            // YELLOW
            if (state == 1) {
                g.setColor(Color.YELLOW);
            } else {
                g.setColor(new Color(80, 80, 0));
            }

            g.fillOval(x + 4, y + 21, 10, 10);

            // GREEN
            if (state == 2) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(new Color(0, 80, 0));
            }

            g.fillOval(x + 4, y + 38, 10, 10);
        }
    }
}