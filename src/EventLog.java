import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;
 
/** An Observer that remembers the latest few events and draws them as a small panel. */
public class EventLog implements SimulationListener {
 
    private static final int MAX_LINES = 6;
 
    private final Deque<String> lines = new ArrayDeque<>();
 
    @Override
    public void onEvent(SimulationEvent event) {
        lines.addFirst(event.message);
        while (lines.size() > MAX_LINES) {
            lines.removeLast();
        }
    }
 
    public void draw(Graphics2D g, int x, int y) {
        g.setColor(new Color(100, 100, 100, 220));
        g.fillRect(x, y, 310, 30 + MAX_LINES * 18);
 
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Event log", x + 12, y + 18);
 
        g.setFont(new Font("Arial", Font.PLAIN, 13));
        int row = 0;
        for (String line : lines) {
            g.drawString(line, x + 12, y + 38 + row * 18);
            row++;
        }
    }
}
 