import java.awt.*;

public class Bridge {

    private int raiseAmount = 0;

    private final int maxHeight = 80;

    public void raise() {

        if (raiseAmount < maxHeight) {

            raiseAmount += 2;

            if (raiseAmount > maxHeight) {
                raiseAmount = maxHeight;
            }
        }
    }

    public void lower() {

        if (raiseAmount > 0) {

            raiseAmount -= 2;

            if (raiseAmount < 0) {
                raiseAmount = 0;
            }
        }
    }

    public boolean isOpen() {

        return raiseAmount >= maxHeight;
    }

    public boolean isClosed() {

        return raiseAmount <= 0;
    }

    public int getRaiseAmount() {

        return raiseAmount;
    }

    public void draw(Graphics g) {

        // Road
        g.setColor(new Color(66, 66, 66));

        g.fillRect(460, 0, 80, 300);

        g.fillRect(460, 400, 80, 300);

        // Road markings
        g.setColor(Color.YELLOW);

        for (int y = 15; y < 290; y += 30) {

            g.fillRect(498, y, 4, 16);
        }

        for (int y = 415; y < 690; y += 30) {

            g.fillRect(498, y, 4, 16);
        }

        // Closed bridge
        if (raiseAmount == 0) {

            g.setColor(new Color(66, 66, 66));

            g.fillRect(460, 300, 80, 100);

            g.setColor(Color.YELLOW);

            g.fillRect(498, 300, 4, 100);

        } else {

            // Raised bridge sections
            g.setColor(new Color(120, 120, 120));

            g.fillRect(
                455,
                300 - raiseAmount,
                20,
                100
            );

            g.fillRect(
                525,
                300 - raiseAmount,
                20,
                100
            );
        }
    }
}