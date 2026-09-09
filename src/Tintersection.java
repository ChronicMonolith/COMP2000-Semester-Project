import java.awt.*;

public class Tintersection implements CarPositionProvider {

    public int x;
    public int y;
    public int roadWidth;

    public Tintersection(int x, int y, int roadWidth) {
        this.x = x;
        this.y = y;
        this.roadWidth = roadWidth;
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(51, 51, 51));
        int hw = roadWidth / 2;

        g.fillRect(x - hw, y - hw, roadWidth, roadWidth);
        g.fillRect(x - hw, y - roadWidth, roadWidth, roadWidth);
    }

    @Override
    public boolean contains(Car car) {
        double cx = car.getCenterX();
        double cy = car.getCenterY();
        int hw = roadWidth / 2;

        boolean h = cx >= x - hw && cx <= x + hw && cy >= y - hw && cy <= y + hw;
        boolean v = cx >= x - hw && cx <= x + hw && cy >= y - roadWidth && cy <= y;
        return h || v;
    }

    @Override
    public int getX(Car car) {
        return (int) car.x;
    }

    @Override
    public int getY(Car car) {
        return y - (car.diameter / 2);
    }
}