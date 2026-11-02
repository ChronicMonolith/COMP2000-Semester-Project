import java.awt.*;
import java.util.EnumSet;


public class Tintersection extends Intersection {

    /**
     * @param centerX x of the point where the road centre lines meet
     * @param centerY y of that point
     */
    public Tintersection(int centerX, int centerY, int roadWidth) {
        super(centerX - roadWidth / 2, centerY - roadWidth / 2, roadWidth, roadWidth,
                EnumSet.of(Direction.NORTH, Direction.EAST, Direction.WEST));
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(51, 51, 51));
        g.fillRect(x, y, width, height);
    }
}
