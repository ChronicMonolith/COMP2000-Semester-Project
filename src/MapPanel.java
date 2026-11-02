import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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

        /** Every junction on the map; each one owns its traffic light. */
        private final List<Intersection> intersections = new ArrayList<>();

        HUD HUD;

        CarManager carManager;

        private final SimulationConfig cfg = SimulationConfig.getInstance();

        private Timer timer;

        public MapPanel() {

                setPreferredSize(new Dimension(cfg.mapWidth, cfg.mapHeight));
                setBackground(new Color(230, 230, 230));

                int roadWidth = 60;
                int laneWidth = roadWidth / 2;

                int leftX = 200;
                int centerX = 640;
                int rightX = 1080;

                int topY = 180;
                int bottomY = 520;

                try {
                        initializeLayout(roadWidth, laneWidth, leftX, centerX, rightX, topY, bottomY);
                } catch (IllegalArgumentException ex) {
                        System.err.println("Failed to initialize map dimensions: " + ex.getMessage());
                } catch (Exception ex) {
                        System.err.println("Unexpected error initializing map elements: " + ex.getMessage());
                }

                carManager = new CarManager(this);

                HUD = new HUD(700, 565);

                this.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent me) {
                                try {
                                        selectCarAt(me.getX(), me.getY());
                                } catch (Exception ex) {
                                        System.err.println("Error handling mouse click event: " + ex.getMessage());
                                }
                        }
                });

                timer = new Timer(cfg.frameDelayMs, this);
                timer.start();
        }

        /**
         * Selects the closest car under the click (if any) and deselects all the
         * others.
         */
        private void selectCarAt(int mx, int my) {
                Car clicked = carManager.select(c -> c.isClickable(mx, my)).stream()
                                .min(Comparator.comparingDouble(
                                                c -> Math.hypot(mx - c.getCenterX(), my - c.getCenterY())))
                                .orElse(null);

                carManager.forEachCar(c -> {
                        if (c == clicked)
                                c.onClick();
                        else
                                c.onDeselect();
                });
        }

        /**
         * Initializes the layout of roads, lanes, intersections, and traffic lights.
         * 
         * @throws IllegalArgumentException
         */
        private void initializeLayout(int roadWidth, int laneWidth, int leftX, int centerX, int rightX, int topY,
                        int bottomY)
                        throws IllegalArgumentException {

                if (roadWidth <= 0 || laneWidth <= 0) {
                        throw new IllegalArgumentException("Road and lane widths must be greater than zero.");
                }

                topRoad = new Road();
                topRoad.addLane(new Lane(0, topY - laneWidth, 1280, topY - laneWidth, laneWidth, new Color(60, 60, 60),
                                Direction.EAST, 2.5));
                topRoad.addLane(new Lane(0, topY, 1280, topY, laneWidth, new Color(60, 60, 60), Direction.WEST, 4));

                bottomRoad = new Road();
                bottomRoad.addLane(new Lane(0, bottomY - laneWidth, 1280, bottomY - laneWidth, laneWidth,
                                new Color(60, 60, 60), Direction.EAST, 4));
                bottomRoad.addLane(
                                new Lane(0, bottomY, 1280, bottomY, laneWidth, new Color(60, 60, 60), Direction.WEST,
                                                1.5));

                leftRoad = new Road();
                leftRoad.addLane(
                                new Lane(leftX - laneWidth, 0, leftX - laneWidth, 720, laneWidth, new Color(60, 60, 60),
                                                Direction.NORTH, 2.5));
                leftRoad.addLane(
                                new Lane(leftX, 0, leftX, 720, laneWidth, new Color(60, 60, 60), Direction.SOUTH, 1.2));

                centerRoad = new Road();
                centerRoad.addLane(new Lane(centerX - laneWidth, 0, centerX - laneWidth, 720, laneWidth,
                                new Color(60, 60, 60), Direction.NORTH, 2.6));
                centerRoad.addLane(
                                new Lane(centerX, 0, centerX, 720, laneWidth, new Color(60, 60, 60), Direction.SOUTH,
                                                1.4));

                rightRoad = new Road();
                rightRoad.addLane(new Lane(rightX - laneWidth, 0, rightX - laneWidth, bottomY + laneWidth, laneWidth,
                                new Color(60, 60, 60), Direction.NORTH, 2.9));
                rightRoad.addLane(new Lane(rightX, 0, rightX, bottomY + laneWidth, laneWidth, new Color(60, 60, 60),
                                Direction.SOUTH, 1.6));

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

                intersections.addAll(
                                Arrays.asList(topLeft, topCenter, topRight, bottomLeft, bottomCenter, bottomRight));

                // Every junction gets its own light, started at a different point in the cycle
                // so they do not all turn green together.
                int cycleStep = 4500;
                for (int i = 0; i < intersections.size(); i++) {
                        Intersection junction = intersections.get(i);
                        junction.setLight(new TrafficLightController(junction.getApproaches(), i * cycleStep));
                }
        }

        public List<Intersection> getIntersections() {
                return intersections;
        }

        public Lane getLaneAt(double x, double y, Direction direction) {

                return roads.stream()
                                .flatMap(road -> road.lanes.stream())
                                .filter(lane -> lane.contains(x, y))
                                .filter(lane -> lane.getDirection() == direction)
                                .findFirst()
                                .orElse(null);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
                try {
                        intersections.forEach(i -> i.update(cfg.frameDelayMs));
                        carManager.updateAll();
                } catch (Exception ex) {
                        System.err.println("Error updating simulation state: " + ex);
                        ex.printStackTrace();
                }
                repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {

                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                roads.stream().forEach(r -> r.draw(g2));

                dividers.stream().forEach(d -> d.draw(g2));

                intersections.forEach(i -> i.draw(g2));

                carManager.drawAll(g2);

                intersections.forEach(i -> i.drawSignals(g2));

                HUD.draw(g2, carManager);
        }
}
