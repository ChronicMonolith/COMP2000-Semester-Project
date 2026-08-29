import java.awt.*;

public class Car implements Interactable {
    private int x;
    private int y;

    private int width = 100;
    private int height = 50;

    private double speed;

    public boolean isSelected = false;

    public Car(int x, int y) {
        this.x = x;
        this.y = y;
        speed = 2;
    }

    public boolean isInside(int mX, int mY) {
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

    public void move() {
        x += speed;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(200, 20, 20));
        g.fillRect(x, y, width, height);
    }
}
