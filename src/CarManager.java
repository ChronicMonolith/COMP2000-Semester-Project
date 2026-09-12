import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;

public class CarManager {

    private final List<Car> cars = new ArrayList<>();
    public List<Car> getCars() { return cars; }
    public Car getSelectedCar() {
        for (Car c : cars) {
            if (c.isSelected) {
                return c;
            }
        }
        return null;
    }

    private final List<CarController> controllers = new ArrayList<>();
    private final MapPanel panel;
    private final Timer spawnTimer;

    public CarManager(MapPanel panel) {
        this.panel = panel;

        spawnTimer = new Timer(200, e -> spawnCar());
        spawnTimer.start();
    }

    private void spawnCar() {
        if (cars.size() >= 30)
            return;

        int spawnSide = (int) (Math.random() * 3);
        Car newCar = null;

        switch (spawnSide) {
            case 0: // Spawn WEST moving EAST
                Lane topLane = panel.topRoad.lanes.get(0);
                int y1 = topLane.startY + (topLane.width / 2) - 7;
                newCar = new Car(-30, y1, 2.0, Direction.EAST);
                break;

            case 1: // Spawn EAST moving WEST
                Lane bottomLane = panel.bottomRoad.lanes.get(1);
                int y2 = bottomLane.startY + (bottomLane.width / 2) - 7;
                newCar = new Car(1310, y2, 2.0, Direction.WEST);
                break;

            case 2: // Spawn NORTH moving SOUTH
                Lane leftLane = panel.leftRoad.lanes.get(1);
                int x1 = leftLane.startX + (leftLane.width / 2) - 7;
                newCar = new Car(x1, -30, 2.0, Direction.SOUTH);
                break;

        }

        if (newCar != null) {
            if (!isSpawnPointBlocked(newCar)) {
                cars.add(newCar);
                controllers.add(new CarController(newCar, panel, cars));
                // DEBUG: Successfully spawned
                System.out.println("Spawned " + newCar.direction + " car at (" + newCar.x + ", " + newCar.y
                        + ") | Total cars: " + cars.size());
            } else {
                // DEBUG: Blocked spawn
                System.out
                        .println("Spawn BLOCKED for " + newCar.direction + " at (" + newCar.x + ", " + newCar.y + ")");
            }
        }
    }

    private boolean isSpawnPointBlocked(Car newCar) {
        for (Car c : cars) {
            double dist = Math.hypot(c.x - newCar.x, c.y - newCar.y);
            if (dist < 40.0) { // Block spawn if another car is within 40px
                return true;
            }
        }
        return false;
    }

    public void updateAll() {
        for (int i = cars.size() - 1; i >= 0; i--) {
            Car car = cars.get(i);
            CarController controller = controllers.get(i);

            controller.update();
            if (car.x < -100 || car.x > 1380 || car.y < -100 || car.y > 820) {

                cars.remove(i);
                controllers.remove(i);
            }
        }
    }

    public void drawAll(Graphics2D g2) {
        for (Car car : cars) {
            car.draw(g2);
        }
    }
}