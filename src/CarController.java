import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CarController {

    private static final double LOOK_AHEAD = 70;
    private static final double STOP_LINE_OFFSET = 14; // car centre stops this far before the junction box
    private static final int PATIENCE_FRAMES = 600; // ~10 s

    private final Car car;
    private final MapPanel panel;
    private final List<Car> allCars;
    private final SimulationConfig cfg = SimulationConfig.getInstance();

    public CarController(Car car, MapPanel panel, List<Car> allCars) {
        this.car = car;
        this.panel = panel;
        this.allCars = allCars;
    }

    public Car getCar() {
        return car;
    }

    public void update() {
        double allowed = car.maxSpeed;

        if (car.state == CarState.DRIVING) {

            Lane currentLane = panel.getLaneAt(
                    car.getCenterX(),
                    car.getCenterY(),
                    car.getDirection());

            if (currentLane != null) {
                allowed = Math.min(allowed, currentLane.getSpeedLimit());
            }

            if (!car.emergency) {                       // sirens ignore red lights
                allowed = Math.min(allowed, speedLimitFromSignal());
            }

            allowed = Math.min(allowed, speedLimitFromSignal());
        }

        double carLimit = speedLimitFromCarAhead();
        allowed = Math.min(allowed, carLimit);

        car.adjustSpeed(allowed, cfg.brakeDeceleration);
        car.stopped = car.speed < 0.01;

        car.blockedFrames = (car.stopped && carLimit < 0.05) ? car.blockedFrames + 1 : 0;

        if (!car.stopped) {
            handleIntersection();
        }
        car.move();
    }

    // lights

    private double speedLimitFromSignal() {
        double cx = car.getCenterX();
        double cy = car.getCenterY();

        return panel.getIntersections().stream()
                .filter(box -> box.getLight() != null)
                .mapToDouble(box -> {
                    double distance = box.distanceToEdge(
                            car.direction, cx, cy);

                    if (distance <= 0 || distance > LOOK_AHEAD) {
                        return Double.MAX_VALUE;
                    }

                    TrafficLightController.LightState state = box.getLight().getLight(car.direction);

                    if (state == TrafficLightController.LightState.GREEN) {
                        return isClearingCrossTraffic(box)
                            ? brakingSpeed(distance - STOP_LINE_OFFSET, 0)
                            : Double.MAX_VALUE;
                    }

                    double gap = distance - STOP_LINE_OFFSET;

                    if (state == TrafficLightController.LightState.YELLOW
                            && !canStopComfortably(gap)) {
                        return Double.MAX_VALUE;
                    }

                    return brakingSpeed(gap, 0);
                })
                .min()
                .orElse(Double.MAX_VALUE);
    }

    private boolean isClearingCrossTraffic(Intersection box) {
        return allCars.stream().anyMatch(other -> other != car
                && other.direction != car.direction
                && box.contains((int) other.getCenterX(), (int) other.getCenterY()));
    }

    private boolean canStopComfortably(double gap) {
        if (gap <= 0) {
            return false;
        }
        double neededDeceleration = (car.speed * car.speed) / (2 * gap);
        return neededDeceleration <= cfg.brakeDeceleration * 1.5;
    }

    private double brakingSpeed(double gap, double leaderSpeed) {
        if (gap < 0) {
            return Math.max(0, leaderSpeed + gap * 0.03); // too close: open the gap back up
        }
        return Math.sqrt(leaderSpeed * leaderSpeed + 2 * cfg.brakeDeceleration * gap);
    }

    // other cars

    private double speedLimitFromCarAhead() {
        double hx = car.getHeadingX();
        double hy = car.getHeadingY();

        return allCars.stream()
                .filter(other -> other != car)
                .filter(other -> {
                    double rx = other.getCenterX() - car.getCenterX();
                    double ry = other.getCenterY() - car.getCenterY();

                    double ahead = rx * hx + ry * hy;
                    double side = Math.abs(rx * hy - ry * hx);

                    boolean sameWay = hx * other.getHeadingX()
                            + hy * other.getHeadingY() > 0.5;

                    return ahead > 0
                            && ahead <= LOOK_AHEAD
                            && side <= car.diameter * 0.75
                            && (sameWay || shouldYieldTo(other));
                })
                .mapToDouble(other -> {
                    double rx = other.getCenterX() - car.getCenterX();
                    double ry = other.getCenterY() - car.getCenterY();

                    double ahead = rx * hx + ry * hy;

                    return brakingSpeed(
                            ahead - cfg.safeGap,
                            other.speed);
                })
                .min()
                .orElse(Double.MAX_VALUE);
    }

    /**
     * Cars on crossing paths only give way one at a time (older car has priority),
     * otherwise two cars could each wait for the other forever.
     */
    private boolean shouldYieldTo(Car other) {
        if (car.blockedFrames > PATIENCE_FRAMES) {
            return false; // fail-safe: never wait forever
        }
        if (other.emergency != car.emergency) {
            return other.emergency; // everyone gives way to the siren; it gives way to no one
        }
        return other.id < car.id;
    }   

    // junctions

    private void handleIntersection() {
        if (car.state == CarState.TURNING) {
            return;
        }

        int cx = (int) car.getCenterX();
        int cy = (int) car.getCenterY();

        Intersection box = panel.getIntersections().stream()
                .filter(i -> i.contains(cx, cy))
                .findFirst()
                .orElse(null);

        if (box == null) {
            car.hasTurned = false;
            return;
        }
        if (car.hasTurned) {
            return;
        }
        car.hasTurned = true;

        TurnDirection turn = chooseTurn(box);
        car.turnDirection = turn;

        if (turn != TurnDirection.STRAIGHT) {
            Direction exit = car.direction.after(turn);
            car.startTurn(exit, box.laneExit(exit));
        }
    }

    /**
     * Only offers turns whose exit road exists at this junction, then lets the
     * car's strategy pick.
     */
    private TurnDirection chooseTurn(Intersection box) {
        List<TurnDirection> options = Arrays.stream(TurnDirection.values())
                .filter(t -> box.hasExit(car.direction.after(t)))
                .collect(Collectors.toList());

        if (options.isEmpty()) {
            return TurnDirection.STRAIGHT;
        }
        return car.turnStrategy.choose(car, options);
    }
}
