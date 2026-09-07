import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class StraightRoadMapViewer extends JPanel {

    private TrafficLightController junction1Lights;
    private TrafficLightController junction2Lights;
    private TrafficLightController junction3Lights;
    private TrafficLightController junction4Lights;

    public StraightRoadMapViewer() {

        setPreferredSize(new Dimension(1280, 720));
        setBackground(new Color(230, 230, 230));

        // One controller for each junction
        junction1Lights = new TrafficLightController();
        junction2Lights = new TrafficLightController();
        junction3Lights = new TrafficLightController();
        junction4Lights = new TrafficLightController();

        Timer repaintTimer = new Timer(100, e -> repaint());
        repaintTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        int leftX = 200;
        int centerX = 640;
        int rightX = 1080;

        int topY = 180;
        int bottomY = 520;

        int roadWidth = 60;
        int hw = roadWidth / 2;
        int laneOffset = 15;

        // =====================================================
        // 1. ROADS
        // =====================================================

        Area roadArea = new Area();

        // Top horizontal road
        roadArea.add(new Area(
                new Rectangle2D.Double(
                        0,
                        topY - hw,
                        1280,
                        roadWidth
                )
        ));

        // Bottom horizontal road
        roadArea.add(new Area(
                new Rectangle2D.Double(
                        0,
                        bottomY - hw,
                        1280,
                        roadWidth
                )
        ));

        // Left vertical road
        roadArea.add(new Area(
                new Rectangle2D.Double(
                        leftX - hw,
                        0,
                        roadWidth,
                        720
                )
        ));

        // Centre vertical road
        roadArea.add(new Area(
                new Rectangle2D.Double(
                        centerX - hw,
                        0,
                        roadWidth,
                        720
                )
        ));

        // Right vertical road
        roadArea.add(new Area(
                new Rectangle2D.Double(
                        rightX - hw,
                        0,
                        roadWidth,
                        bottomY + hw
                )
        ));

        // =====================================================
        // 2. ROUNDABOUT ROADS
        // =====================================================

        int islandDiam = 80;
        int outerDiam = islandDiam + roadWidth;

        // Top roundabout
        roadArea.add(new Area(
                new Ellipse2D.Double(
                        centerX - outerDiam / 2.0,
                        topY - outerDiam / 2.0,
                        outerDiam,
                        outerDiam
                )
        ));

        // Bottom-left roundabout
        roadArea.add(new Area(
                new Ellipse2D.Double(
                        leftX - outerDiam / 2.0,
                        bottomY - outerDiam / 2.0,
                        outerDiam,
                        outerDiam
                )
        ));

        // Asphalt
        g2.setColor(new Color(60, 60, 60));
        g2.fill(roadArea);

        // White road border
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.draw(roadArea);

        // =====================================================
        // 3. ROUNDABOUT ISLANDS
        // =====================================================

        drawRoundaboutIsland(
                g2,
                centerX,
                topY,
                islandDiam
        );

        drawRoundaboutIsland(
                g2,
                leftX,
                bottomY,
                islandDiam
        );

        // =====================================================
        // 4. ROAD CENTER LINES
        // =====================================================

        float[] dashPattern = {
                15.0f,
                15.0f
        };

        g2.setStroke(
                new BasicStroke(
                        2,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        10.0f,
                        dashPattern,
                        0.0f
                )
        );

        // Horizontal
        g2.drawLine(
                0,
                topY,
                1280,
                topY
        );

        g2.drawLine(
                0,
                bottomY,
                1280,
                bottomY
        );

        // Vertical
        g2.drawLine(
                leftX,
                0,
                leftX,
                720
        );

        g2.drawLine(
                centerX,
                0,
                centerX,
                720
        );

        g2.drawLine(
                rightX,
                0,
                rightX,
                bottomY
        );

        // Draw islands over centre lines
        drawRoundaboutIsland(
                g2,
                centerX,
                topY,
                islandDiam
        );

        drawRoundaboutIsland(
                g2,
                leftX,
                bottomY,
                islandDiam
        );

        // =====================================================
        // 5. DIRECTION ARROWS
        // =====================================================

        g2.setColor(Color.YELLOW);
        g2.setStroke(new BasicStroke(3));

        // -----------------------------------------------------
        // LEFT VERTICAL ROAD
        // -----------------------------------------------------

        // Northbound
        drawArrow(
                g2,
                leftX - laneOffset,
                380,
                leftX - laneOffset,
                320
        );

        // Southbound
        drawArrow(
                g2,
                leftX + laneOffset,
                320,
                leftX + laneOffset,
                380
        );

        // -----------------------------------------------------
        // TOP HORIZONTAL ROAD
        // -----------------------------------------------------

        // Eastbound
        drawArrow(
                g2,
                400,
                topY - laneOffset,
                460,
                topY - laneOffset
        );

        // Westbound
        drawArrow(
                g2,
                460,
                topY + laneOffset,
                400,
                topY + laneOffset
        );

        // -----------------------------------------------------
        // CENTER VERTICAL ROAD
        // -----------------------------------------------------

        // Southbound
        drawArrow(
                g2,
                centerX - laneOffset,
                320,
                centerX - laneOffset,
                380
        );

        // Northbound
        drawArrow(
                g2,
                centerX + laneOffset,
                380,
                centerX + laneOffset,
                320
        );

        // -----------------------------------------------------
        // BOTTOM HORIZONTAL ROAD
        // -----------------------------------------------------

        // Eastbound
        drawArrow(
                g2,
                400,
                bottomY - laneOffset,
                460,
                bottomY - laneOffset
        );

        // Westbound
        drawArrow(
                g2,
                460,
                bottomY + laneOffset,
                400,
                bottomY + laneOffset
        );

        // Eastbound continuation
        drawArrow(
                g2,
                850,
                bottomY - laneOffset,
                910,
                bottomY - laneOffset
        );

        // =====================================================
        // 6. TRAFFIC LIGHTS
        // =====================================================

        drawTrafficLights(g2);
    }

    // =========================================================
    // TRAFFIC LIGHT POSITIONS
    // =========================================================

    private void drawTrafficLights(Graphics2D g2) {

        // =====================================================
        // JUNCTION 1 - TOP LEFT 4-WAY
        // =====================================================

        int x = 200;
        int y = 180;

        /*
         * Vertical lights:
         *
         * LEFT vertical light = BELOW intersection
         * RIGHT vertical light = ABOVE intersection
         */

        // Southbound traffic light
        junction1Lights.drawVertical(
                g2,
                x - 35,
                y + 55,
                junction1Lights.getSouthLight()
        );

        // Northbound traffic light
        junction1Lights.drawVertical(
                g2,
                x + 15,
                y - 125,
                junction1Lights.getNorthLight()
        );

        // West traffic light
        junction1Lights.drawHorizontal(
                g2,
                x - 125,
                y - 35,
                junction1Lights.getWestLight()
        );

        // East traffic light
        junction1Lights.drawHorizontal(
                g2,
                x + 55,
                y + 15,
                junction1Lights.getEastLight()
        );


        // =====================================================
        // JUNCTION 2 - TOP RIGHT 4-WAY
        // =====================================================

        x = 1080;
        y = 180;

        // Southbound traffic light - below intersection
        junction4Lights.drawVertical(
                g2,
                x - 35,
                y + 55,
                junction4Lights.getSouthLight()
        );

        // Northbound traffic light - above intersection
        junction4Lights.drawVertical(
                g2,
                x + 15,
                y - 125,
                junction4Lights.getNorthLight()
        );

        // West traffic light
        junction4Lights.drawHorizontal(
                g2,
                x - 125,
                y - 35,
                junction4Lights.getWestLight()
        );

        // East traffic light
        junction4Lights.drawHorizontal(
                g2,
                x + 55,
                y + 15,
                junction4Lights.getEastLight()
        );


        // =====================================================
        // JUNCTION 3 - BOTTOM CENTRE 4-WAY
        // =====================================================

        x = 640;
        y = 520;

        // Southbound traffic light - below intersection
        junction2Lights.drawVertical(
                g2,
                x - 35,
                y + 55,
                junction2Lights.getSouthLight()
        );

        // Northbound traffic light - above intersection
        junction2Lights.drawVertical(
                g2,
                x + 15,
                y - 125,
                junction2Lights.getNorthLight()
        );

        // West traffic light
        junction2Lights.drawHorizontal(
                g2,
                x - 125,
                y - 35,
                junction2Lights.getWestLight()
        );

        // East traffic light
        junction2Lights.drawHorizontal(
                g2,
                x + 55,
                y + 15,
                junction2Lights.getEastLight()
        );


        // =====================================================
        // JUNCTION 4 - BOTTOM RIGHT T-JUNCTION
        // =====================================================

        x = 1080;
        y = 520;

        /*
         * T-junction:
         *
         * There is NO south road.
         *
         * Therefore:
         * - North light
         * - West light
         * - East light
         */

        // North traffic light - above intersection
        junction3Lights.drawVertical(
                g2,
                x + 15,
                y - 125,
                junction3Lights.getNorthLight()
        );

        // West traffic light
        junction3Lights.drawHorizontal(
                g2,
                x - 125,
                y - 35,
                junction3Lights.getWestLight()
        );

        // East traffic light
        junction3Lights.drawHorizontal(
                g2,
                x + 55,
                y + 15,
                junction3Lights.getEastLight()
        );
    }

    // =========================================================
    // ROUNDABOUT ISLAND
    // =========================================================

    private void drawRoundaboutIsland(
            Graphics2D g2,
            int x,
            int y,
            int diameter
    ) {

        int radius = diameter / 2;

        // Inner island
        g2.setColor(new Color(230, 230, 230));

        g2.fillOval(
                x - radius,
                y - radius,
                diameter,
                diameter
        );

        // White border
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));

        g2.drawOval(
                x - radius,
                y - radius,
                diameter,
                diameter
        );
    }

    // =========================================================
    // ARROW
    // =========================================================

    private void drawArrow(
            Graphics2D g2,
            int x1,
            int y1,
            int x2,
            int y2
    ) {

        g2.drawLine(
                x1,
                y1,
                x2,
                y2
        );

        double angle = Math.atan2(
                y2 - y1,
                x2 - x1
        );

        int arrowSize = 8;

        int x3 = (int) (
                x2 -
                arrowSize *
                Math.cos(angle - Math.PI / 6)
        );

        int y3 = (int) (
                y2 -
                arrowSize *
                Math.sin(angle - Math.PI / 6)
        );

        int x4 = (int) (
                x2 -
                arrowSize *
                Math.cos(angle + Math.PI / 6)
        );

        int y4 = (int) (
                y2 -
                arrowSize *
                Math.sin(angle + Math.PI / 6)
        );

        Polygon head = new Polygon();

        head.addPoint(x2, y2);
        head.addPoint(x3, y3);
        head.addPoint(x4, y4);

        g2.fill(head);
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame(
                    "Traffic Light Junction Simulation"
            );

            frame.setDefaultCloseOperation(
                    JFrame.EXIT_ON_CLOSE
            );

            frame.setResizable(false);

            frame.add(
                    new StraightRoadMapViewer()
            );

            frame.pack();

            frame.setLocationRelativeTo(null);

            frame.setVisible(true);
        });
    }
}
