import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


class SimulationPanel extends JPanel {
    // Main simulation panel

    private ArrayList<Car> cars = new ArrayList<>();
    private Ship ship = new Ship();

    private boolean bridgeOpen = false;

    private Random random = new Random();

    private int spawnTimer = 0;

    public SimulationPanel() {
        setBackground(Color.WHITE);
    }

    public void start() {

        Timer timer = new Timer(30, e -> {

            updateSimulation();

            repaint();
        });

        timer.start();
    }

    private void updateSimulation() {

        spawnTimer++;

        // Spawn a car every so often
        if (spawnTimer > 50) {

            spawnCar();

            spawnTimer = 0;
        }

        // Update ship
        ship.update();

        // Open bridge when ship gets close
        if (ship.getX() > 350 && ship.getX() < 650) {

            bridgeOpen = true;

        } else if (ship.getX() > 700) {

            bridgeOpen = false;
        }

        // Update cars
        for (Car car : cars) {

            car.update(bridgeOpen);
        }

        // Remove cars that have left the screen
        Iterator<Car> iterator = cars.iterator();

        while (iterator.hasNext()) {

            Car car = iterator.next();

            if (car.getX() < -50 || car.getX() > 1050) {

                iterator.remove();
            }
        }
    }

    private void spawnCar() {

        // Random direction
        boolean movingRight = random.nextBoolean();

        if (movingRight) {

            cars.add(new Car(-40, 270, true));

        } else {

            cars.add(new Car(1040, 330, false));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        drawWater(g);
        drawBridge(g);
        drawCars(g);
        drawShip(g);
        drawInformation(g);
    }

    private void drawWater(Graphics g) {

        g.setColor(new Color(100, 180, 230));

        g.fillRect(0, 350, getWidth(), 250);
    }

    private void drawBridge(Graphics g) {

        if (bridgeOpen) {

            // Left side
            g.setColor(Color.DARK_GRAY);

            g.fillRect(0, 240, 400, 100);

            // Right side
            g.fillRect(600, 240, 400, 100);

            // Raised bridge sections
            g.setColor(Color.GRAY);

            g.fillRect(350, 170, 50, 70);
            g.fillRect(600, 170, 50, 70);

            // Water gap
            g.setColor(new Color(100, 180, 230));

            g.fillRect(400, 240, 200, 110);

        } else {

            // Normal bridge
            g.setColor(Color.DARK_GRAY);

            g.fillRect(0, 240, 1000, 100);

            // Road markings
            g.setColor(Color.WHITE);

            for (int x = 0; x < 1000; x += 40) {

                g.fillRect(x, 288, 25, 4);
            }
        }

        // Bridge supports
        g.setColor(Color.GRAY);

        g.fillRect(150, 340, 40, 150);
        g.fillRect(810, 340, 40, 150);
    }

    private void drawCars(Graphics g) {

        for (Car car : cars) {

            car.draw(g);
        }
    }

    private void drawShip(Graphics g) {

        ship.draw(g);
    }

    private void drawInformation(Graphics g) {

        g.setColor(Color.BLACK);

        g.setFont(new Font("Arial", Font.BOLD, 16));

        g.drawString(
                "Cars: " + cars.size(),
                20,
                30
        );

        if (bridgeOpen) {

            g.setColor(Color.RED);

            g.drawString(
                    "BRIDGE OPEN - TRAFFIC STOPPED",
                    350,
                    30
            );

        } else {

            g.setColor(Color.GREEN);

            g.drawString(
                    "BRIDGE CLOSED - TRAFFIC MOVING",
                    350,
                    30
            );
        }
    }
}
