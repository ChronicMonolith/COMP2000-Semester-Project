import java.awt.*;

public class HUD {
    public int x;
    public int y;

    private static final Color PANEL = new Color(100, 100, 100);
    private static final Font BOLD = new Font("Arial", Font.BOLD, 16);
    private static final Font PLAIN = new Font("Arial", Font.PLAIN, 14);

    public HUD(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics2D g, CarManager carManager) {
        // World Data - numbers come from the per-frame TrafficStats snapshot
        TrafficStats stats = carManager.getStats();

        g.setColor(PANEL);
        g.fillRect(x + 15, y + 15, 270, 125);

        g.setColor(Color.WHITE);

        g.setFont(BOLD);
        g.drawString("Cars: " + stats.total, x + 30, y + 38);

        g.setFont(PLAIN);
        g.drawString("Driving: " + stats.driving + "   Stopped: " + stats.stopped, x + 30, y + 58);
        g.drawString("% Driving: " + (int) stats.drivingPercent() + "%", x + 30, y + 78);
        g.drawString(String.format("Avg speed: %.2f   Top: %.2f", stats.averageSpeed, stats.topSpeed),
                x + 30, y + 98);
        g.drawString("Heading  " + stats.directionSummary(), x + 30, y + 118);

        // Individual Car Data
        Car selectedCar = carManager.getSelectedCar();

        if (selectedCar != null) {
            g.setColor(PANEL);
            g.fillRect(x + 300, y + 15, 250, 100);

            g.setColor(Color.WHITE);

            g.setFont(BOLD);
            g.drawString("Vehicle: Car #" + selectedCar.id, x + 315, y + 40);

            g.setFont(PLAIN);
            g.drawString(String.format("Speed: %.2f", selectedCar.speed), x + 315, y + 60);
            g.drawString("Direction: " + selectedCar.direction, x + 315, y + 80);

            if (selectedCar.stopped)
                g.drawString("State: STOPPED", x + 315, y + 100);
            else
                g.drawString("State: " + selectedCar.state, x + 315, y + 100);
        }
    }
}
