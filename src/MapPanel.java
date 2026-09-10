import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MapPanel extends JPanel implements ActionListener {

        Road topRoad;
        Road bottomRoad;
        Road leftRoad;
        Road centerRoad;
        Road rightRoad;

        Intersection topCenter;
        Intersection bottomLeft;

        Intersection topLeft;
        Intersection topRight;
        Intersection bottomCenter;
        Tintersection bottomRight;

        private TrafficLightController junction1Lights;
        private TrafficLightController junction2Lights;
        private TrafficLightController junction3Lights;
        private TrafficLightController junction4Lights;

        ArrayList<Road> roads = new ArrayList<>();
        ArrayList<LaneDivider> dividers = new ArrayList<>();

        CarManager carManager;

        Direction direction;

        private Timer timer;

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
                topRoad.addLane(new Lane(0, topY - laneWidth, 1280, topY - laneWidth, laneWidth, new Color(60, 60, 60),
                                Direction.EAST));
                topRoad.addLane(new Lane(0, topY, 1280, topY, laneWidth, new Color(60, 60, 60), Direction.WEST));

                bottomRoad = new Road();
                bottomRoad
                                .addLane(new Lane(0, bottomY - laneWidth, 1280, bottomY - laneWidth, laneWidth,
                                                new Color(60, 60, 60),
                                                Direction.EAST));
                bottomRoad.addLane(
                                new Lane(0, bottomY, 1280, bottomY, laneWidth, new Color(60, 60, 60), Direction.WEST));

                leftRoad = new Road();
                leftRoad.addLane(
                                new Lane(leftX - laneWidth, 0, leftX - laneWidth, 720, laneWidth, new Color(60, 60, 60),
                                                Direction.NORTH));
                leftRoad.addLane(new Lane(leftX, 0, leftX, 720, laneWidth, new Color(60, 60, 60), Direction.SOUTH));

                centerRoad = new Road();
                centerRoad
                                .addLane(new Lane(centerX - laneWidth, 0, centerX - laneWidth, 720, laneWidth,
                                                new Color(60, 60, 60),
                                                Direction.NORTH));
                centerRoad.addLane(
                                new Lane(centerX, 0, centerX, 720, laneWidth, new Color(60, 60, 60), Direction.SOUTH));

                rightRoad = new Road();
                rightRoad.addLane(new Lane(rightX - laneWidth, 0, rightX - laneWidth, bottomY + laneWidth, laneWidth,
                                new Color(51, 51, 51), Direction.NORTH));
                rightRoad.addLane(
                                new Lane(rightX, 0, rightX, bottomY + laneWidth, laneWidth, new Color(60, 60, 60),
                                                Direction.SOUTH));

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

                topCenter = new Intersection(centerX - laneWidth, topY - laneWidth, roadWidth, roadWidth);
                bottomLeft = new Intersection(leftX - laneWidth, bottomY - laneWidth, roadWidth, roadWidth);

                topLeft = new Intersection(leftX - laneWidth, topY - laneWidth, roadWidth, roadWidth);
                topRight = new Intersection(rightX - laneWidth, topY - laneWidth, roadWidth, roadWidth);
                bottomCenter = new Intersection(centerX - laneWidth, bottomY - laneWidth, roadWidth, roadWidth);

                bottomRight = new Tintersection(rightX, bottomY, roadWidth);

                junction1Lights = new TrafficLightController();
                junction2Lights = new TrafficLightController();
                junction3Lights = new TrafficLightController();
                junction4Lights = new TrafficLightController();

                carManager = new CarManager(this);

                timer = new Timer(16, this);
                timer.start();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
                carManager.updateAll();
                repaint();
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

                topCenter.draw(g2);
                bottomLeft.draw(g2);

                topLeft.draw(g2);
                topRight.draw(g2);
                bottomCenter.draw(g2);
                bottomRight.draw(g2);
                carManager.drawAll(g2);

                drawTrafficLights(g2);
        }

        private void drawTrafficLights(Graphics2D g2) {

                int x1 = 200;
                int y1 = 180;
                junction1Lights.drawVertical(g2, x1 - 35, y1 + 55, junction1Lights.getSouthLight());
                junction1Lights.drawVertical(g2, x1 + 15, y1 - 125, junction1Lights.getNorthLight());
                junction1Lights.drawHorizontal(g2, x1 - 125, y1 - 35, junction1Lights.getWestLight());
                junction1Lights.drawHorizontal(g2, x1 + 55, y1 + 15, junction1Lights.getEastLight());

                int x2 = 640;
                int y2 = 520;
                junction2Lights.drawVertical(g2, x2 - 35, y2 + 55, junction2Lights.getSouthLight());
                junction2Lights.drawVertical(g2, x2 + 15, y2 - 125, junction2Lights.getNorthLight());
                junction2Lights.drawHorizontal(g2, x2 - 125, y2 - 35, junction2Lights.getWestLight());
                junction2Lights.drawHorizontal(g2, x2 + 55, y2 + 15, junction2Lights.getEastLight());

                int x3 = 1080;
                int y3 = 520;
                junction3Lights.drawVertical(g2, x3 + 15, y3 - 125, junction3Lights.getNorthLight());
                junction3Lights.drawHorizontal(g2, x3 - 125, y3 - 35, junction3Lights.getWestLight());
                junction3Lights.drawHorizontal(g2, x3 + 55, y3 + 15, junction3Lights.getEastLight());

                int x4 = 1080;
                int y4 = 180;
                junction4Lights.drawVertical(g2, x4 - 35, y4 + 55, junction4Lights.getSouthLight());
                junction4Lights.drawVertical(g2, x4 + 15, y4 - 125, junction4Lights.getNorthLight());
                junction4Lights.drawHorizontal(g2, x4 - 125, y4 - 35, junction4Lights.getWestLight());
                junction4Lights.drawHorizontal(g2, x4 + 55, y4 + 15, junction4Lights.getEastLight());
        }

        public TrafficLightController getJunction1Lights() {
                return junction1Lights;
        }

        public TrafficLightController getJunction2Lights() {
                return junction2Lights;
        }

        public TrafficLightController getJunction3Lights() {
                return junction3Lights;
        }

        public TrafficLightController getJunction4Lights() {
                return junction4Lights;
        }
}