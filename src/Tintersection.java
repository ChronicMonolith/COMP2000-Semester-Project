import java.awt.*;

public class Tintersection extends Intersection {

    public Tintersection(int x, int y, int roadWidth) {
        super(x, y, roadWidth, roadWidth);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(51, 51, 51));

        int hw = width / 2;

        // Horizontal bar
        g.fillRect(x - hw, y - hw, width, width);

        // Vertical stem
        g.fillRect(x - hw, y - width, width, width);
    }
}
