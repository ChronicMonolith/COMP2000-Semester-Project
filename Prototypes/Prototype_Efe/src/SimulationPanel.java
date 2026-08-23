import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class SimulationPanel extends JPanel {

    private ArrayList<Car> cars = new ArrayList<>();

    private Ship currentShip = null;

    private Bridge bridge = new Bridge();

    private Random random = new Random();

    private int carSpawnTimer = 0;
    private int shipSpawnTimer = 0;
    private int stateTimer = 0;

    // Road lights
    private TrafficLight roadLight1 =
            new TrafficLight(true, 2);

    private TrafficLight roadLight2 =
            new TrafficLight(true, 2);

    // Sea lights
    private TrafficLight seaLight1 =
            new TrafficLight(false, 0);

    private TrafficLight seaLight2 =
            new TrafficLight(false, 0);

    /*
     * STATES
     * 0 = Normal road traffic
     * 1 = Road yellow
     * 2 = Road red
     * 3 = Bridge opening
     * 4 = Safety wait
     * 5 = Ship crossing
     * 6 = Ship yellow
     * 7 = Bridge closing
     */

    private int state = 0;

    private static final int ROAD_YELLOW_TIME = 100;
    private static final int ROAD_CLEAR_TIME = 80;
    private static final int BRIDGE_OPEN_WAIT = 80;
    private static final int SHIP_YELLOW_TIME = 50;
    private static final int SHIP_SPAWN_DELAY = 250;


    public SimulationPanel() {

        setBackground(new Color(46, 125, 50));

        // Start with a ship immediately
        if (random.nextBoolean()) {

            currentShip = new Ship(-100, true);

        } else {

            currentShip = new Ship(850, false);
        }
    }


    public void start() {

        Timer timer = new Timer(30, e -> {

            updateSimulation();

            repaint();
        });

        timer.start();
    }


    private void updateSimulation() {

        carSpawnTimer++;

        if (carSpawnTimer >= 55) {

            spawnCar();

            carSpawnTimer = 0;
        }


        if (currentShip == null) {

            shipSpawnTimer++;

            if (shipSpawnTimer >= SHIP_SPAWN_DELAY) {

                spawnShip();

                shipSpawnTimer = 0;
            }
        }


        // =========================================
        // STATE MACHINE
        // =========================================

        switch (state) {

            // =====================================
            // 0 - NORMAL ROAD TRAFFIC
            // =====================================

            case 0:

                setRoadLights(2);
                setSeaLights(0);

                stateTimer = 0;

                /*
                 * SHIP MOVES TOWARD THE LIGHT
                 */

                if (currentShip != null) {

                    currentShip.update();

                    /*
                     * LEFT -> RIGHT
                     *
                     * Stop at x = 240
                     */

                    if (
                            currentShip.isMovingRight()
                            && currentShip.getX() >= 240
                    ) {

                        currentShip.setX(240);

                        setRoadLights(1);

                        stateTimer = 0;

                        state = 1;
                    }

                    /*
                     * RIGHT -> LEFT
                     *
                     * Stop at x = 630
                     */

                    else if (
                            !currentShip.isMovingRight()
                            && currentShip.getX() <= 630
                    ) {

                        currentShip.setX(630);

                        setRoadLights(1);

                        stateTimer = 0;

                        state = 1;
                    }
                }

                break;

            case 1:

                setRoadLights(1);
                setSeaLights(0);

                stateTimer++;

                /*
                 * Cars that are already past the
                 * light continue.
                 * Cars behind the light stop.
                 */

                if (
                        stateTimer >= ROAD_YELLOW_TIME
                ) {

                    setRoadLights(0);

                    stateTimer = 0;

                    state = 2;
                }

                break;

            case 2:

                setRoadLights(0);
                setSeaLights(1);

                stateTimer++;

                /*
                 * Give cars time to clear bridge.
                 */

                if (
                        stateTimer >= ROAD_CLEAR_TIME
                ) {

                    stateTimer = 0;

                    state = 3;
                }

                break;


            case 3:

                setRoadLights(0);
                setSeaLights(1);

                bridge.raise();

                if (bridge.isOpen()) {

                    stateTimer = 0;

                    state = 4;
                }

                break;


            case 4:

                setRoadLights(0);
                setSeaLights(1);

                stateTimer++;

                /*
                 * Wait after bridge is fully open.
                 */

                if (
                        stateTimer >= BRIDGE_OPEN_WAIT
                ) {

                    setSeaLights(2);

                    stateTimer = 0;

                    state = 5;
                }

                break;


            case 5:

                setRoadLights(0);
                setSeaLights(2);

                /*
                 * Ship moves freely across bridge.
                 */

                if (currentShip != null) {

                    currentShip.update();

                    /*
                     * Once ship reaches the other
                     * side of the bridge:
                     * GREEN -> YELLOW
                     */

                    if (
                            currentShip.hasPassedBridge()
                    ) {

                        setSeaLights(1);

                        stateTimer = 0;

                        state = 6;
                    }
                }

                break;

            case 6:

                setRoadLights(0);
                setSeaLights(1);

                /*
                 * VERY IMPORTANT:
                 * The ship DOES NOT STOP here.
                 * It continues moving while the
                 * sea light is yellow.
                 */

                if (currentShip != null) {

                    currentShip.update();
                }

                stateTimer++;

                if (
                        stateTimer >= SHIP_YELLOW_TIME
                ) {

                    stateTimer = 0;

                    state = 7;
                }

                break;


            case 7:

                setRoadLights(0);
                setSeaLights(0);

                /*
                 * Ship continues leaving.
                 */

                if (currentShip != null) {

                    currentShip.update();
                }

                bridge.lower();

                /*
                 * Bridge completely closed.
                 */

                if (bridge.isClosed()) {

                    setRoadLights(2);

                    state = 0;

                    /*
                     * Remove ship only once it has
                     * completely left the screen.
                     */

                    if (
                            currentShip != null
                            && currentShip.hasLeftScreen()
                    ) {

                        currentShip = null;

                        shipSpawnTimer = 0;
                    }
                }

                break;
        }

        // UPDATE CARS

        updateCars();

        // REMOVE CARS OFF SCREEN

        Iterator<Car> carIterator =
                cars.iterator();

        while (carIterator.hasNext()) {

            Car car = carIterator.next();

            if (
                    car.getY() < -60
                    || car.getY() > 760
            ) {

                carIterator.remove();
            }
        }
    }

    // UPDATE CARS
    private void updateCars() {

        boolean stopCars =
                roadLight1.getState() == 0
                || roadLight1.getState() == 1;

        for (int i = 0; i < cars.size(); i++) {

            Car currentCar = cars.get(i);

            double frontCarY = -1;

            for (int j = 0; j < cars.size(); j++) {

                if (i == j) {
                    continue;
                }

                Car other = cars.get(j);

                if (
                        other.isMovingDown()
                        != currentCar.isMovingDown()
                ) {
                    continue;
                }

                if (currentCar.isMovingDown()) {

                    if (
                            other.getY()
                            > currentCar.getY()
                    ) {

                        if (
                                frontCarY == -1
                                || other.getY()
                                < frontCarY
                        ) {

                            frontCarY =
                                    other.getY();
                        }
                    }

                } else {

                    if (
                            other.getY()
                            < currentCar.getY()
                    ) {

                        if (
                                frontCarY == -1
                                || other.getY()
                                > frontCarY
                        ) {

                            frontCarY =
                                    other.getY();
                        }
                    }
                }
            }

            currentCar.update(
                    stopCars,
                    frontCarY
            );
        }
    }

    // SPAWN CAR

    private void spawnCar() {

        boolean movingDown =
                random.nextBoolean();

        if (movingDown) {

            cars.add(
                    new Car(
                            468,
                            -50,
                            true
                    )
            );

        } else {

            cars.add(
                    new Car(
                            512,
                            720,
                            false
                    )
            );
        }
    }

    // SPAWN SHIP

    private void spawnShip() {

        if (random.nextBoolean()) {

            currentShip =
                    new Ship(
                            -100,
                            true
                    );

        } else {

            currentShip =
                    new Ship(
                            850,
                            false
                    );
        }
    }

    // LIGHT CONTROL

    private void setRoadLights(int newState) {

        roadLight1.setState(newState);
        roadLight2.setState(newState);
    }


    private void setSeaLights(int newState) {

        seaLight1.setState(newState);
        seaLight2.setState(newState);
    }

    // DRAW

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        drawEnvironment(g);

        bridge.draw(g);

        drawSignals(g);

        for (Car car : cars) {

            car.draw(g);
        }

        if (currentShip != null) {

            currentShip.draw(g);
        }

        drawHUD(g);
    }

    // ENVIRONMENT

    private void drawEnvironment(Graphics g) {

        // Water

        g.setColor(
                new Color(30, 136, 229)
        );

        g.fillRect(
                0,
                300,
                getWidth(),
                100
        );

        // Water lines

        g.setColor(
                new Color(66, 165, 245)
        );

        for (
                int x = 20;
                x < getWidth();
                x += 100
        ) {

            g.drawLine(
                    x,
                    335,
                    x + 40,
                    335
            );

            g.drawLine(
                    x + 50,
                    365,
                    x + 90,
                    365
            );
        }
    }

    // SIGNALS

    private void drawSignals(Graphics g) {

        roadLight1.draw(
                g,
                432,
                230
        );

        roadLight2.draw(
                g,
                548,
                425
        );

        seaLight1.draw(
                g,
                386,
                270
        );

        seaLight2.draw(
                g,
                596,
                270
        );
    }

    // HUD

    private void drawHUD(Graphics g) {

        g.setColor(
                new Color(0, 0, 0, 160)
        );

        g.fillRect(
                15,
                15,
                400,
                100
        );

        g.setColor(Color.WHITE);

        g.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        g.drawString(
                "Cars: " + cars.size(),
                25,
                38
        );

        String roadState;

        if (roadLight1.getState() == 2) {
            roadState = "GREEN";
        } else if (roadLight1.getState() == 1) {
            roadState = "YELLOW";
        } else {
            roadState = "RED";
        }

        String seaState;

        if (seaLight1.getState() == 2) {
            seaState = "GREEN";
        } else if (seaLight1.getState() == 1) {
            seaState = "YELLOW";
        } else {
            seaState = "RED";
        }

        g.drawString(
                "Road: " + roadState,
                25,
                62
        );

        g.drawString(
                "Sea: " + seaState,
                25,
                86
        );
    }
}