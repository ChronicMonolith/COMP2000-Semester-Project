import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MapPanel extends JPanel {

    Road topRoad;
    Road bottomRoad;
    Road leftRoad;
    Road centerRoad;
    Road rightRoad;

    Roundabout topRoundabout;
    Roundabout bottomLeftRoundabout;

    Intersection topLeft;
    Intersection topRight;
    Intersection bottomCenter;
    Tintersection bottomRight;

    ArrayList<Road> roads = new ArrayList<>();
    ArrayList<LaneDivider> dividers = new ArrayList<>();

    Car car;
    CarNavigator navigator;
    CarController carController;

    public MapPanel() {

        setPreferredSize(new Dimension(1280, 720));
        setBackground(new Color(230, 230, 230));

        int roadWidth = 60;
        int laneWidth = roadWidth / 2;

        int leftX = 200;
        int centerX = 640;
        int rightX = 1080;

        int topY = 180;
        int bottomY = 520;

        topRoad = new Road();
        topRoad.addLane(new Lane(0, topY - laneWidth, 1280, topY - laneWidth, laneWidth, new Color(60, 60, 60)));
        topRoad.addLane(new Lane(0, topY, 1280, topY, laneWidth, new Color(60, 60, 60)));

        bottomRoad = new Road();
        bottomRoad
                .addLane(new Lane(0, bottomY - laneWidth, 1280, bottomY - laneWidth, laneWidth, new Color(60, 60, 60)));
        bottomRoad.addLane(new Lane(0, bottomY, 1280, bottomY, laneWidth, new Color(60, 60, 60)));

        leftRoad = new Road();
        leftRoad.addLane(new Lane(leftX - laneWidth, 0, leftX - laneWidth, 720, laneWidth, new Color(60, 60, 60)));
        leftRoad.addLane(new Lane(leftX, 0, leftX, 720, laneWidth, new Color(60, 60, 60)));

        centerRoad = new Road();
        centerRoad
                .addLane(new Lane(centerX - laneWidth, 0, centerX - laneWidth, 720, laneWidth, new Color(60, 60, 60)));
        centerRoad.addLane(new Lane(centerX, 0, centerX, 720, laneWidth, new Color(60, 60, 60)));

        rightRoad = new Road();
        rightRoad.addLane(new Lane(rightX - laneWidth, 0, rightX - laneWidth, bottomY + laneWidth, laneWidth,
                new Color(51, 51, 51)));
        rightRoad.addLane(new Lane(rightX, 0, rightX, bottomY + laneWidth, laneWidth, new Color(60, 60, 60)));

        roads.add(topRoad);
        roads.add(bottomRoad);
        roads.add(leftRoad);
        roads.add(centerRoad);
        roads.add(rightRoad);

        dividers.add(new LaneDivider(0, topY, 1280, topY));
        dividers.add(new LaneDivider(0, bottomY, 1280, bottomY));
        dividers.add(new LaneDivider(leftX, 0, leftX, 720));
        dividers.add(new LaneDivider(centerX, 0, centerX, 720));
        dividers.add(new LaneDivider(rightX, 0, rightX, bottomY));

        int islandDiam = 80;
        int outerDiam = islandDiam + roadWidth;

        topRoundabout = new Roundabout(centerX, topY, outerDiam);
        bottomLeftRoundabout = new Roundabout(leftX, bottomY, outerDiam);

        topLeft = new Intersection(leftX - laneWidth, topY - laneWidth, roadWidth, roadWidth);
        topRight = new Intersection(rightX - laneWidth, topY - laneWidth, roadWidth, roadWidth);
        bottomCenter = new Intersection(centerX - laneWidth, bottomY - laneWidth, roadWidth, roadWidth);

        bottomRight = new Tintersection(rightX, bottomY, roadWidth);

        Lane spawnLane = topRoad.lanes.get(0);
        int carY = spawnLane.startY + (spawnLane.width / 2) - 5;
        car = new Car(spawnLane.startX, carY, 2.0, Direction.EAST);

        navigator = new CarNavigator();

        navigator.addProvider(topRoad.lanes.get(0));
        navigator.addProvider(topRoad.lanes.get(1));
        navigator.addProvider(bottomRoad.lanes.get(0));
        navigator.addProvider(bottomRoad.lanes.get(1));
        navigator.addProvider(leftRoad.lanes.get(0));
        navigator.addProvider(leftRoad.lanes.get(1));
        navigator.addProvider(centerRoad.lanes.get(0));
        navigator.addProvider(centerRoad.lanes.get(1));
        navigator.addProvider(rightRoad.lanes.get(0));
        navigator.addProvider(rightRoad.lanes.get(1));

        navigator.addProvider(topRoundabout);
        navigator.addProvider(bottomLeftRoundabout);

        navigator.addProvider(topLeft);
        navigator.addProvider(topRight);
        navigator.addProvider(bottomCenter);
        navigator.addProvider(bottomRight);

        carController = new CarController(car, navigator, this);
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for (Road r : roads) {
            r.draw(g2);
        }

        for (LaneDivider d : dividers) {
            d.draw(g2);
        }

        topRoundabout.draw(g2);
        bottomLeftRoundabout.draw(g2);

        topLeft.draw(g2);
        topRight.draw(g2);
        bottomCenter.draw(g2);
        bottomRight.draw(g2);

        car.draw(g2);
    }
}
