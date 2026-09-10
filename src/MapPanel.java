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
        }
}