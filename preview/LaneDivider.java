import java.awt.*;

public class LaneDivider {

    int x1, y1, x2, y2;
    Stroke stroke;
    Color color;

    public LaneDivider(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

        float[] dash = { 15f, 15f };
        this.stroke = new BasicStroke(
                2,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10f,
                dash,
                0f);

        this.color = Color.WHITE;
    }

    public void draw(Graphics2D g) {
        g.setColor(color);
        g.setStroke(stroke);
        g.drawLine(x1, y1, x2, y2);
    }
}
