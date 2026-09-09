import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class CarNavigator {

    private ArrayList<CarPositionProvider> providers = new ArrayList<>();
    private CarPositionProvider active;

    private List<Point> arcPath = null;
    private int arcIndex = 0;

    public void addProvider(CarPositionProvider provider) {
        providers.add(provider);
    }

    public CarPositionProvider getActiveProvider(Car car) {
        // First pass: Check junction providers (Roundabouts / Intersections)
        for (CarPositionProvider p : providers) {
            if ((p instanceof Roundabout || p instanceof Intersection || p instanceof Tintersection)
                    && p.contains(car)) {
                active = p;
                return p;
            }
        }

        // Second pass: Fall back to normal lanes/roads
        for (CarPositionProvider p : providers) {
            if (p.contains(car)) {
                active = p;
                return p;
            }
        }

        return null;
    }

    public void update(Car car) {
        // Stop default lane updates if following an arc path
        if (followingArc()) {
            return;
        }

        CarPositionProvider p = getActiveProvider(car);

        if (p != null) {
            // Move forward based on direction vector
            car.x += car.speed * car.direction.dx;
            car.y += car.speed * car.direction.dy;

            // Do NOT snap X and Y if the provider is a Roundabout
            if (!(p instanceof Roundabout)) {
                if (car.direction == Direction.EAST || car.direction == Direction.WEST) {
                    car.y = p.getY(car);
                } else {
                    car.x = p.getX(car);
                }
            }
        } else {
            // Default linear movement if outside known providers
            car.x += car.speed * car.direction.dx;
            car.y += car.speed * car.direction.dy;
        }
    }

    // ARC MODE
    public void setArc(List<Point> arc) {
        this.arcPath = arc;
        this.arcIndex = 0;
    }

    public boolean followingArc() {
        return arcPath != null && arcIndex < arcPath.size();
    }

    public void updateArc(Car car) {
        if (!followingArc())
            return;

        Point target = arcPath.get(arcIndex);

        double dx = target.x - car.getCenterX();
        double dy = target.y - car.getCenterY();
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist <= Math.max(car.speed * 1.5, 4.0)) {
            arcIndex++;

            if (arcIndex >= arcPath.size()) {
                // Apply final exit direction when leaving the junction
                if (active instanceof Roundabout roundabout) {
                    Direction exitDir = roundabout.getExitDirection();
                    if (exitDir != null)
                        car.direction = exitDir;
                } else if (active instanceof Intersection intersection) {
                    car.direction = intersection.getLeftTurnExitDirection(car.direction);
                }

                arcPath = null; // Exit arc mode cleanly
            }
            return;
        }

        car.x += (dx / dist) * car.speed;
        car.y += (dy / dist) * car.speed;

        updateDirection(car, dx, dy);
    }

    private void updateDirection(Car car, double dx, double dy) {
        if (Math.abs(dx) > Math.abs(dy)) {
            car.direction = dx > 0 ? Direction.EAST : Direction.WEST;
        } else {
            car.direction = dy > 0 ? Direction.SOUTH : Direction.NORTH;
        }
    }
}