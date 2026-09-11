import java.awt.*;
import java.util.ArrayList;

public class Road {

    public ArrayList<Lane> lanes = new ArrayList<>();

    public <T extends Lane> void addLane(T lane) {
        lanes.add(lane);
    }

    public void draw(Graphics2D g) {
        for (Lane lane : lanes) {
            lane.draw(g);
        }
    }
}
