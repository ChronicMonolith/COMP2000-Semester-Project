import java.util.List;

public class CarController {

    private final Car car;
    private final MapPanel panel;
    private final List<Car> allCars;

    public CarController(Car car, MapPanel panel, List<Car> allCars) {
        this.car = car;
        this.panel = panel;
        this.allCars = allCars;
    }

    private boolean checkCollisionAhead() {
        double safetyDistance = 40.0;

        for (Car other : allCars) {
            if (other == this.car)
                continue; // Skip checking against itself

            double dx = other.getCenterX() - car.getCenterX();
            double dy = other.getCenterY() - car.getCenterY();
            double distance = Math.hypot(dx, dy);

            // Ignore cars that spawned directly on top of each other (distance < 15px)
            // to prevent overlapping cars from locking up permanently.
            if (distance < 15.0) {
                continue;
            }

            switch (car.getDirection()) {
                case EAST:
                    // Other car is ahead horizontally in the same lane
                    if (dx > 0 && dx <= safetyDistance && Math.abs(dy) < 15) {
                        return true;
                    }
                    break;

                case WEST:
                    // Other car is ahead horizontally (moving left) in the same lane
                    if (dx < 0 && Math.abs(dx) <= safetyDistance && Math.abs(dy) < 15) {
                        return true;
                    }
                    break;

                case SOUTH:
                    // Other car is ahead vertically (moving down) in the same lane
                    if (dy > 0 && dy <= safetyDistance && Math.abs(dx) < 15) {
                        return true;
                    }
                    break;

                case NORTH:
                    // Other car is ahead vertically (moving up) in the same lane
                    if (dy < 0 && Math.abs(dy) <= safetyDistance && Math.abs(dx) < 15) {
                        return true;
                    }
                    break;
            }
        }

        return false;
    }

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

    public void update() {
        car.stopped = checkCollisionAhead();
        if (car.stopped) {
            return;
        }


        double cx = car.getCenterX();
        double cy = car.getCenterY();

        boolean inTopLeft = panel.topLeft.contains((int) cx, (int) cy);
        boolean inTopCenter = panel.topCenter.contains((int) cx, (int) cy);
        boolean inTopRight = panel.topRight.contains((int) cx, (int) cy);
        boolean inBottomLeft = panel.bottomLeft.contains((int) cx, (int) cy);
        boolean inBottomCenter = panel.bottomCenter.contains((int) cx, (int) cy);
        boolean inT = panel.bottomRight.contains((int) cx, (int) cy);

        boolean inAnyIntersection = inTopLeft || inTopCenter || inTopRight || inBottomLeft || inBottomCenter || inT;

        if (!inAnyIntersection) {
            car.hasTurned = false;
        }

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