import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class StraightRoadMapViewer extends JPanel {

    public StraightRoadMapViewer() {
        setPreferredSize(new Dimension(1280, 720));
        setBackground(new Color(230, 230, 230)); // Ground background
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Coordinates for straight layout
        int leftX = 200;      // Left vertical road X
        int centerX = 640;     // Center vertical road X
        int rightX = 1080;    // Right vertical road X

        int topY = 180;       // Top horizontal road Y
        int bottomY = 520;    // Bottom horizontal road Y

        int roadWidth = 60;   // 60px wide total (30px per lane)
        int hw = roadWidth / 2;
        int laneOffset = 15;

        // --- 1. Combine All Asphalt into One Seamless Shape ---
        Area roadArea = new Area();

        // Add Horizontal Roads
        roadArea.add(new Area(new Rectangle2D.Double(0, topY - hw, 1280, roadWidth)));
        roadArea.add(new Area(new Rectangle2D.Double(0, bottomY - hw, 1280, roadWidth)));

        // Add Vertical Roads
        roadArea.add(new Area(new Rectangle2D.Double(leftX - hw, 0, roadWidth, 720)));
        roadArea.add(new Area(new Rectangle2D.Double(centerX - hw, 0, roadWidth, 720)));
        roadArea.add(new Area(new Rectangle2D.Double(rightX - hw, 0, roadWidth, bottomY + hw)));

        // Add Roundabout Outer Rings
        int islandDiam = 80;
        int outerDiam = islandDiam + roadWidth;
        roadArea.add(new Area(new Ellipse2D.Double(centerX - outerDiam/2.0, topY - outerDiam/2.0, outerDiam, outerDiam)));
        roadArea.add(new Area(new Ellipse2D.Double(leftX - outerDiam/2.0, bottomY - outerDiam/2.0, outerDiam, outerDiam)));

        // Draw Unified Asphalt Base (Fills Intersections Smoothly)
        g2.setColor(new Color(60, 60, 60));
        g2.fill(roadArea);

        // Draw Continuous Outer White Shoulders
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.draw(roadArea);

        // --- 2. Roundabout Central Islands ---
        drawRoundaboutIsland(g2, centerX, topY, islandDiam);
        drawRoundaboutIsland(g2, leftX, bottomY, islandDiam);

        // --- 3. Dashed Center Lane Dividers ---
        float[] dashPattern = {15.0f, 15.0f};
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0.0f));

        // Horizontal Lanes
        g2.drawLine(0, topY, 1280, topY);
        g2.drawLine(0, bottomY, 1280, bottomY);

        // Vertical Lanes
        g2.drawLine(leftX, 0, leftX, 720);
        g2.drawLine(centerX, 0, centerX, 720);
        g2.drawLine(rightX, 0, rightX, bottomY);

        // Re-cover roundabout island centers to clean center dashes inside islands
        drawRoundaboutIsland(g2, centerX, topY, islandDiam);
        drawRoundaboutIsland(g2, leftX, bottomY, islandDiam);

        // --- 4. Lane Direction Arrows ---
        g2.setColor(Color.YELLOW);
        g2.setStroke(new BasicStroke(3));

        // Left Vertical Road
        drawArrow(g2, leftX - laneOffset, 380, leftX - laneOffset, 320); // Northbound
        drawArrow(g2, leftX + laneOffset, 320, leftX + laneOffset, 380); // Southbound

        // Top Horizontal Road
        drawArrow(g2, 400, topY - laneOffset, 460, topY - laneOffset);   // Eastbound
        drawArrow(g2, 460, topY + laneOffset, 400, topY + laneOffset);   // Westbound

        // Center Vertical Road
        drawArrow(g2, centerX - laneOffset, 320, centerX - laneOffset, 380); // Southbound
        drawArrow(g2, centerX + laneOffset, 380, centerX + laneOffset, 320); // Northbound

        // Bottom Horizontal Road
        drawArrow(g2, 400, bottomY - laneOffset, 460, bottomY - laneOffset); // Eastbound
        drawArrow(g2, 460, bottomY + laneOffset, 400, bottomY + laneOffset); // Westbound
        drawArrow(g2, 850, bottomY - laneOffset, 910, bottomY - laneOffset); // Eastbound continuation
    }

    private void drawRoundaboutIsland(Graphics2D g2, int x, int y, int diameter) {
        int radius = diameter / 2;

        // Inner terrain circle
        g2.setColor(new Color(230, 230, 230));
        g2.fillOval(x - radius, y - radius, diameter, diameter);

        // Island white border
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawOval(x - radius, y - radius, diameter, diameter);
    }

    private void drawArrow(Graphics2D g2, int x1, int y1, int x2, int y2) {
        g2.drawLine(x1, y1, x2, y2);

        double angle = Math.atan2(y2 - y1, x2 - x1);
        int arrowSize = 8;

        int x3 = (int) (x2 - arrowSize * Math.cos(angle - Math.PI / 6));
        int y3 = (int) (y2 - arrowSize * Math.sin(angle - Math.PI / 6));
        int x4 = (int) (x2 - arrowSize * Math.cos(angle + Math.PI / 6));
        int y4 = (int) (y2 - arrowSize * Math.sin(angle + Math.PI / 6));

        Polygon head = new Polygon();
        head.addPoint(x2, y2);
        head.addPoint(x3, y3);
        head.addPoint(x4, y4);
        g2.fill(head);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Seamless Intersections Map (1280x720)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(new StraightRoadMapViewer());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}