import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class CarFactory {

    private static final int OFF_MAP = 30;

    private final SimulationConfig cfg = SimulationConfig.getInstance();
    private final List<SpawnPoint> spawnPoints;

    private final Supplier<TurnStrategy> driverStyle = () -> ThreadLocalRandom.current().nextDouble() < 0.7
            ? TurnStrategy.preferStraight(0.6)
            : TurnStrategy.RANDOM;

    public CarFactory(MapPanel panel) {
        this.spawnPoints = Collections.unmodifiableList(buildSpawnPoints(panel));
    }

    public List<SpawnPoint> getSpawnPoints() {
        return spawnPoints;
    }

    public Car create(SpawnPoint sp) {
        double speed = cfg.minSpeed + ThreadLocalRandom.current().nextDouble() * (cfg.maxSpeed - cfg.minSpeed);
        Car car = new Car(0, 0, speed, sp.direction);
        car.x = sp.x - car.diameter / 2.0;
        car.y = sp.y - car.diameter / 2.0;
        car.turnStrategy = driverStyle.get();
        return car;
    }

    private List<SpawnPoint> buildSpawnPoints(MapPanel p) {
        List<SpawnPoint> list = new ArrayList<>();
        int w = cfg.mapWidth;
        int h = cfg.mapHeight;

        // Horizontal roads: lane 0 flows EAST (enters on the left), lane 1 flows WEST (enters on the right)
        for (Road road : new Road[] { p.topRoad, p.bottomRoad }) {
            list.add(new SpawnPoint(-OFF_MAP, laneCentreY(road.lanes.get(0)), Direction.EAST));
            list.add(new SpawnPoint(w + OFF_MAP, laneCentreY(road.lanes.get(1)), Direction.WEST));
        }

        // Full-height vertical roads: lane 0 flows NORTH (enters at the bottom), lane 1 flows SOUTH (enters at the top)
        for (Road road : new Road[] { p.leftRoad, p.centerRoad }) {
            list.add(new SpawnPoint(laneCentreX(road.lanes.get(1)), -OFF_MAP, Direction.SOUTH));
            list.add(new SpawnPoint(laneCentreX(road.lanes.get(0)), h + OFF_MAP, Direction.NORTH));
        }

        // Right road ends at the T-junction, so only its SOUTH lane starts at the map edge
        list.add(new SpawnPoint(laneCentreX(p.rightRoad.lanes.get(1)), -OFF_MAP, Direction.SOUTH));

        return list;
    }

    private static double laneCentreX(Lane lane) {
        return lane.startX + lane.width / 2.0;
    }

    private static double laneCentreY(Lane lane) {
        return lane.startY + lane.width / 2.0;
    }
}
