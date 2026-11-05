import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CarManager {

    private final List<Car> cars = new ArrayList<>();
    private final List<CarController> controllers = new ArrayList<>();

    private final MapPanel panel;
    private final CarFactory factory; // Week 7: Factory
    private final SimulationConfig cfg = SimulationConfig.getInstance(); // Week 7: Singleton

    private int frame = 0;
    private TrafficStats stats = TrafficStats.EMPTY;

    public CarManager(MapPanel panel) {
        this.panel = panel;
        this.factory = new CarFactory(panel);
    }

    // queries

    public List<Car> getCars() {
        return cars.stream().collect(Collectors.toList());
    }

    public Car getSelectedCar() {
        return cars.stream()
                .filter(c -> c.isSelected)
                .findFirst()
                .orElse(null);
    }

    public long count(Predicate<Car> condition) {
        return cars.parallelStream()
                .filter(condition)
                .count();
    }

    public List<Car> select(Predicate<Car> condition) {
        return cars.stream().filter(condition).collect(Collectors.toList());
    }

    public void forEachCar(Consumer<Car> action) {
        cars.forEach(action);
    }

    public long countDrivingCars() {
        return count(c -> !c.stopped);
    }

    public TrafficStats getStats() {
        return stats;
    }

    // spawning

    private void trySpawn() {
        if (cars.size() >= cfg.maxCars) {
            return;
        }

        List<SpawnPoint> free = factory.getSpawnPoints().stream()
                .filter(this::hasRoom)
                .collect(Collectors.toList());

        if (free.isEmpty()) {
            return;
        }

        SpawnPoint where = free.get(ThreadLocalRandom.current().nextInt(free.size()));
        Car car = factory.create(where);
        cars.add(car);
        controllers.add(new CarController(car, panel, cars));
    }

    private boolean hasRoom(SpawnPoint sp) {
        return cars.stream()
                .noneMatch(c -> Math.hypot(c.getCenterX() - sp.x, c.getCenterY() - sp.y) < cfg.spawnClearance);
    }

    // per frame

    public void updateAll() {
        frame++;
        if (frame % cfg.spawnIntervalFrames == 0) {
            trySpawn();
        }

        controllers.forEach(CarController::update);

        removeCarsThatLeftTheMap();

        stats = TrafficStats.compute(getCars());
    }

    private void removeCarsThatLeftTheMap() {
        Set<Car> gone = cars.stream()
                .filter(c -> c.x < -100 || c.x > cfg.mapWidth + 100 || c.y < -100 || c.y > cfg.mapHeight + 100)
                .collect(Collectors.toSet());

        if (!gone.isEmpty()) {
            cars.removeAll(gone);
            controllers.removeIf(ctrl -> gone.contains(ctrl.getCar()));
        }
    }

    public void drawAll(Graphics2D g2) {
        cars.forEach(c -> c.draw(g2));
    }
}
