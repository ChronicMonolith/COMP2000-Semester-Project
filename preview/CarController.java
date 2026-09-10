import java.util.ArrayList;
import java.util.List;

public class CarController {

    private final Car car;
    private final MapPanel panel;
    private final List<Car> allCars;

    // Tracks whether the car has already been cleared to cross the current
    // controlled intersection, so it doesn't stop mid-crossing.
    private boolean clearedLightCheck = false;

    public CarController(Car car, MapPanel panel, List<Car> allCars) {
        this.car = car;
        this.panel = panel;
        this.allCars = allCars;
    }

    // ------------------------------------------------------------------
    // Collision avoidance
    // ------------------------------------------------------------------

    private boolean checkCollisionAhead() {
        double safetyDistance = 40.0;

        for (Car other : allCars) {
            if (other == this.car)
                continue;

            double dx = other.getCenterX() - car.getCenterX();
            double dy = other.getCenterY() - car.getCenterY();
            double distance = Math.hypot(dx, dy);

            if (distance < 15.0) {
                continue;
            }

            switch (car.getDirection()) {
                case EAST:
                    if (dx > 0 && dx <= safetyDistance && Math.abs(dy) < 15) {
                        return true;
                    }
                    break;

                case WEST:
                    if (dx < 0 && Math.abs(dx) <= safetyDistance && Math.abs(dy) < 15) {
                        return true;
                    }
                    break;

                case SOUTH:
                    if (dy > 0 && dy <= safetyDistance && Math.abs(dx) < 15) {
                        return true;
                    }
                    break;

                case NORTH:
                    if (dy < 0 && Math.abs(dy) <= safetyDistance && Math.abs(dx) < 15) {
                        return true;
                    }
                    break;
            }
        }

        return false;
    }

    // ------------------------------------------------------------------
    // Intersection helpers
    // ------------------------------------------------------------------

    private List<Intersection> getAllIntersections() {
        List<Intersection> list = new ArrayList<>();
        list.add(panel.topLeft);
        list.add(panel.topCenter);
        list.add(panel.topRight);
        list.add(panel.bottomLeft);
        list.add(panel.bottomCenter);
        list.add(panel.bottomRight);
        return list;
    }

    private Intersection getCurrentIntersection() {
        double cx = car.getCenterX();
        double cy = car.getCenterY();
        for (Intersection inter : getAllIntersections()) {
            if (inter.contains((int) cx, (int) cy)) {
                return inter;
            }
        }
        return null;
    }

    private Intersection getApproachingIntersection() {
        double lookAhead = 40.0;
        double checkX = car.getCenterX() + car.getDirection().dx * lookAhead;
        double checkY = car.getCenterY() + car.getDirection().dy * lookAhead;

        for (Intersection inter : getAllIntersections()) {
            if (inter.contains((int) checkX, (int) checkY)) {
                return inter;
            }
        }
        return null;
    }

    /**
     * True if the car is approaching or inside an uncontrolled intersection
     * (i.e. one with no traffic lights). Used to skip light logic entirely.
     */
    private boolean isAtUncontrolledIntersection() {
        Intersection approaching = getApproachingIntersection();
        Intersection current = getCurrentIntersection();

        if (approaching != null && !panel.hasTrafficLights(approaching)) {
            return true;
        }
        if (current != null && !panel.hasTrafficLights(current)) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if the car should stop because the traffic light for its
     * direction is not green at the intersection it is about to enter.
     */
    private boolean mustStopForRedLight() {
        Intersection target = getApproachingIntersection();
        if (target == null) {
            return false;
        }

        TrafficLightController lights = panel.getLightControllerFor(target);
        if (lights == null) {
            return false;
        }

        // Already cleared this intersection? Don't stop mid-crossing.
        if (clearedLightCheck) {
            return false;
        }

        switch (car.getDirection()) {
            case NORTH: return !lights.canNorthMove();
            case SOUTH: return !lights.canSouthMove();
            case EAST:  return !lights.canEastMove();
            case WEST:  return !lights.canWestMove();
        }
        return false;
    }

    // ------------------------------------------------------------------
    // Turn handlers
    // ------------------------------------------------------------------

    private void handleTopLeftTurn(TurnDirection dir) {
        switch (dir) {
            case LEFT:
                Lane southLane = panel.leftRoad.lanes.get(1);
                int lx = southLane.startX + southLane.width / 2;
                int ly = (int) car.getCenterY();
                car.startTurn(Direction.SOUTH, lx, ly);
                break;
            case RIGHT:
                Lane northLane = panel.leftRoad.lanes.get(0);
                lx = northLane.startX + northLane.width / 2;
                ly = (int) car.getCenterY();
                car.startTurn(Direction.NORTH, lx, ly);
                break;
            case STRAIGHT:
                Lane eastLane = panel.topRoad.lanes.get(0);
                lx = (int) car.getCenterX();
                ly = eastLane.startY + eastLane.width / 2;
                car.startTurn(Direction.EAST, lx, ly);
                break;
        }
    }

    private void handleTopCenterTurn(TurnDirection dir) {
        switch (dir) {
            case LEFT:
                if (car.direction == Direction.EAST) {
                    Lane northLane = panel.centerRoad.lanes.get(0);
                    car.startTurn(Direction.NORTH, northLane.startX + northLane.width / 2, (int) car.getCenterY());
                } else if (car.direction == Direction.WEST) {
                    Lane southLane = panel.centerRoad.lanes.get(1);
                    car.startTurn(Direction.SOUTH, southLane.startX + southLane.width / 2, (int) car.getCenterY());
                } else if (car.direction == Direction.NORTH) {
                    Lane westLane = panel.topRoad.lanes.get(1);
                    car.startTurn(Direction.WEST, (int) car.getCenterX(), westLane.startY + westLane.width / 2);
                } else if (car.direction == Direction.SOUTH) {
                    Lane eastLane = panel.topRoad.lanes.get(0);
                    car.startTurn(Direction.EAST, (int) car.getCenterX(), eastLane.startY + eastLane.width / 2);
                }
                break;
            case RIGHT:
                if (car.direction == Direction.EAST) {
                    Lane southLane = panel.centerRoad.lanes.get(1);
                    car.startTurn(Direction.SOUTH, southLane.startX + southLane.width / 2, (int) car.getCenterY());
                } else if (car.direction == Direction.WEST) {
                    Lane northLane = panel.centerRoad.lanes.get(0);
                    car.startTurn(Direction.NORTH, northLane.startX + northLane.width / 2, (int) car.getCenterY());
                } else if (car.direction == Direction.NORTH) {
                    Lane eastLane = panel.topRoad.lanes.get(0);
                    car.startTurn(Direction.EAST, (int) car.getCenterX(), eastLane.startY + eastLane.width / 2);
                } else if (car.direction == Direction.SOUTH) {
                    Lane westLane = panel.topRoad.lanes.get(1);
                    car.startTurn(Direction.WEST, (int) car.getCenterX(), westLane.startY + westLane.width / 2);
                }
                break;
            case STRAIGHT:
                if (car.direction == Direction.EAST) {
                    Lane eastLane = panel.topRoad.lanes.get(0);
                    car.startTurn(Direction.EAST, (int) car.getCenterX(), eastLane.startY + eastLane.width / 2);
                } else if (car.direction == Direction.WEST) {
                    Lane westLane = panel.topRoad.lanes.get(1);
                    car.startTurn(Direction.WEST, (int) car.getCenterX(), westLane.startY + westLane.width / 2);
                } else if (car.direction == Direction.NORTH) {
                    Lane northLane = panel.centerRoad.lanes.get(0);
                    car.startTurn(Direction.NORTH, northLane.startX + northLane.width / 2, (int) car.getCenterY());
                } else if (car.direction == Direction.SOUTH) {
                    Lane southLane = panel.centerRoad.lanes.get(1);
                    car.startTurn(Direction.SOUTH, southLane.startX + southLane.width / 2, (int) car.getCenterY());
                }
                break;
        }
    }

    private void handleBottomLeftTurn(TurnDirection dir) {
        switch (dir) {
            case LEFT:
                if (car.direction == Direction.EAST) {
                    Lane northLane = panel.leftRoad.lanes.get(0);
                    car.startTurn(Direction.NORTH, northLane.startX + northLane.width / 2, (int) car.getCenterY());
                } else if (car.direction == Direction.WEST) {
                    Lane southLane = panel.leftRoad.lanes.get(1);
                    car.startTurn(Direction.SOUTH, southLane.startX + southLane.width / 2, (int) car.getCenterY());
                } else if (car.direction == Direction.NORTH) {
                    Lane westLane = panel.bottomRoad.lanes.get(1);
                    car.startTurn(Direction.WEST, (int) car.getCenterX(), westLane.startY + westLane.width / 2);
                } else if (car.direction == Direction.SOUTH) {
                    Lane eastLane = panel.bottomRoad.lanes.get(0);
                    car.startTurn(Direction.EAST, (int) car.getCenterX(), eastLane.startY + eastLane.width / 2);
                }
                break;
            case RIGHT:
                if (car.direction == Direction.EAST) {
                    Lane southLane = panel.leftRoad.lanes.get(1);
                    car.startTurn(Direction.SOUTH, southLane.startX + southLane.width / 2, (int) car.getCenterY());
                } else if (car.direction == Direction.WEST) {
                    Lane northLane = panel.leftRoad.lanes.get(0);
                    car.startTurn(Direction.NORTH, northLane.startX + northLane.width / 2, (int) car.getCenterY());
                } else if (car.direction == Direction.NORTH) {
                    Lane eastLane = panel.bottomRoad.lanes.get(0);
                    car.startTurn(Direction.EAST, (int) car.getCenterX(), eastLane.startY + eastLane.width / 2);
                } else if (car.direction == Direction.SOUTH) {
                    Lane westLane = panel.bottomRoad.lanes.get(1);
                    car.startTurn(Direction.WEST, (int) car.getCenterX(), westLane.startY + westLane.width / 2);
                }
                break;
            case STRAIGHT:
                if (car.direction == Direction.EAST) {
                    Lane eastLane = panel.bottomRoad.lanes.get(0);
                    car.startTurn(Direction.EAST, (int) car.getCenterX(), eastLane.startY + eastLane.width / 2);
                } else if (car.direction == Direction.WEST) {
                    Lane westLane = panel.bottomRoad.lanes.get(1);
                    car.startTurn(Direction.WEST, (int) car.getCenterX(), westLane.startY + westLane.width / 2);
                } else if (car.direction == Direction.NORTH) {
                    Lane northLane = panel.leftRoad.lanes.get(0);
                    car.startTurn(Direction.NORTH, northLane.startX + northLane.width / 2, (int) car.getCenterY());
                } else if (car.direction == Direction.SOUTH) {
                    Lane southLane = panel.leftRoad.lanes.get(1);
                    car.startTurn(Direction.SOUTH, southLane.startX + southLane.width / 2, (int) car.getCenterY());
                }
                break;
        }
    }

    private void handleTopRight(TurnDirection dir) {
        switch (dir) {
            case LEFT:
                Lane southLane = panel.rightRoad.lanes.get(1);
                int lx = southLane.startX + southLane.width / 2;
                int ly = (int) car.getCenterY();
                car.startTurn(Direction.SOUTH, lx, ly);
                break;
            case RIGHT:
                Lane northLane = panel.rightRoad.lanes.get(0);
                lx = northLane.startX + northLane.width / 2;
                ly = (int) car.getCenterY();
                car.startTurn(Direction.NORTH, lx, ly);
                break;
            case STRAIGHT:
                Lane eastLane = panel.topRoad.lanes.get(0);
                lx = (int) car.getCenterX();
                ly = eastLane.startY + eastLane.width / 2;
                car.startTurn(Direction.EAST, lx, ly);
                break;
        }
    }

    private void handleBottomCenter(TurnDirection dir) {
        switch (dir) {
            case LEFT:
                Lane northLane = panel.centerRoad.lanes.get(0);
                int lx = northLane.startX + northLane.width / 2;
                int ly = (int) car.getCenterY();
                car.startTurn(Direction.NORTH, lx, ly);
                break;
            case RIGHT:
                Lane southLane = panel.centerRoad.lanes.get(1);
                lx = southLane.startX + southLane.width / 2;
                ly = (int) car.getCenterY();
                car.startTurn(Direction.SOUTH, lx, ly);
                break;
            case STRAIGHT:
                Lane eastLane = panel.bottomRoad.lanes.get(0);
                lx = (int) car.getCenterX();
                ly = eastLane.startY + eastLane.width / 2;
                car.startTurn(Direction.EAST, lx, ly);
                break;
        }
    }

    private void handleTIntersection(TurnDirection dir) {
        switch (dir) {
            case LEFT:
                Lane westLane = panel.bottomRoad.lanes.get(1);
                int lx = (int) car.getCenterX();
                int ly = westLane.startY + westLane.width / 2;
                car.startTurn(Direction.WEST, lx, ly);
                break;
            case RIGHT:
                Lane eastLane = panel.bottomRoad.lanes.get(0);
                lx = (int) car.getCenterX();
                ly = eastLane.startY + eastLane.width / 2;
                car.startTurn(Direction.EAST, lx, ly);
                break;
            case STRAIGHT:
                Lane northLane = panel.rightRoad.lanes.get(0);
                lx = northLane.startX + northLane.width / 2;
                ly = (int) car.getCenterY();
                car.startTurn(Direction.NORTH, lx, ly);
                break;
        }
    }

    // ------------------------------------------------------------------
    // Update loop
    // ------------------------------------------------------------------

    public void update() {
        // ------------------------------------------------------------
        // TRAFFIC LIGHT LOGIC
        // ------------------------------------------------------------
        if (!isAtUncontrolledIntersection() && mustStopForRedLight()) {
            car.stopped = true;
            return;
        }

        // ------------------------------------------------------------
        // COLLISION AVOIDANCE
        // ------------------------------------------------------------
        car.stopped = checkCollisionAhead();
        if (car.stopped) {
            return;
        }

        // ------------------------------------------------------------
        // INTERSECTION DETECTION
        // ------------------------------------------------------------
        double cx = car.getCenterX();
        double cy = car.getCenterY();

        boolean inTopLeft      = panel.topLeft.contains((int) cx, (int) cy);
        boolean inTopCenter    = panel.topCenter.contains((int) cx, (int) cy);
        boolean inTopRight     = panel.topRight.contains((int) cx, (int) cy);
        boolean inBottomLeft   = panel.bottomLeft.contains((int) cx, (int) cy);
        boolean inBottomCenter = panel.bottomCenter.contains((int) cx, (int) cy);
        boolean inT            = panel.bottomRight.contains((int) cx, (int) cy);

        boolean inAnyIntersection = inTopLeft || inTopCenter || inTopRight
                                 || inBottomLeft || inBottomCenter || inT;

        // If we've entered a controlled intersection, remember we're cleared
        // so we don't stop mid-crossing if the light changes.
        if (inAnyIntersection) {
            Intersection current = getCurrentIntersection();
            if (current != null && panel.hasTrafficLights(current)) {
                clearedLightCheck = true;
            }
        }

        if (!inAnyIntersection) {
            car.hasTurned = false;
            clearedLightCheck = false;
        }

        // ------------------------------------------------------------
        // TURN DECISION
        // ------------------------------------------------------------
        if (!car.hasTurned && inAnyIntersection) {
            TurnDirection randomTurn = car.chooseRandomTurn();

            if (inT) {
                handleTIntersection(randomTurn);
            } else if (inTopLeft) {
                handleTopLeftTurn(randomTurn);
            } else if (inTopCenter) {
                handleTopCenterTurn(randomTurn);
            } else if (inTopRight) {
                handleTopRight(randomTurn);
            } else if (inBottomLeft) {
                handleBottomLeftTurn(randomTurn);
            } else if (inBottomCenter) {
                handleBottomCenter(randomTurn);
            }

            car.hasTurned = true;
        }

        car.move();
    }
}