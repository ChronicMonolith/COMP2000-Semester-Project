import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CarController {

    private final Car car;
    private final MapPanel panel;
    private final Timer timer;

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

    private void handleTopRight(TurnDirection dir) {

        switch (dir) {

            case LEFT:

                Lane southLane = panel.rightRoad.lanes.get(1);
                int lx = southLane.startX + southLane.width / 2;
                int ly = (int) car.getCenterY();
                car.startTurn(Direction.SOUTH, lx, ly);
                break;

            case RIGHT:
                // RIGHT → go NORTH on rightRoad
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

    public CarController(Car car, MapPanel panel) {
        this.car = car;
        this.panel = panel;

        timer = new Timer(17, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });

        timer.start();
    }

    private void update() {

        double cx = car.getCenterX();
        double cy = car.getCenterY();

        boolean inTopLeft = panel.topLeft.contains((int) cx, (int) cy);
        boolean inTopRight = panel.topRight.contains((int) cx, (int) cy);
        boolean inBottomCenter = panel.bottomCenter.contains((int) cx, (int) cy);
        boolean inT = panel.bottomRight.contains((int) cx, (int) cy);

        if (!inTopLeft && !inTopRight && !inBottomCenter && !inT) {
            car.hasTurned = false;
        }

        if (!car.hasTurned && inT) {
            TurnDirection dir = car.chooseRandomTurn();
            handleTIntersection(dir);
            car.hasTurned = true;
        }

        if (!inTopLeft && !inTopRight && !inBottomCenter) {
            car.hasTurned = false;
        }

        if (!car.hasTurned) {

            if (inTopLeft) {
                TurnDirection dir = car.chooseRandomTurn();
                handleTopLeftTurn(dir);
                car.hasTurned = true;
            }

            if (inTopRight) {
                TurnDirection dir = car.chooseRandomTurn();
                handleTopRight(dir);
                car.hasTurned = true;
            }

            if (inBottomCenter) {
                TurnDirection dir = car.chooseRandomTurn();
                handleBottomCenter(dir);
                car.hasTurned = true;
            }
        }

        car.move();
        panel.repaint();
    }
}