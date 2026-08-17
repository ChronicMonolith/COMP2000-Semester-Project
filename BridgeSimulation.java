import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

// Main class
public class BridgeSimulation {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bridge Traffic Simulation");

        SimulationPanel panel = new SimulationPanel();

        frame.add(panel);
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        panel.start();
    }
}


// Main simulation panel
class SimulationPanel extends JPanel {

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


// Vehicle class
class Car {

    private double x;
    private double y;

    private boolean movingRight;

    private double speed = 2.5;

    public Car(double x, double y, boolean movingRight) {

        this.x = x;
        this.y = y;

        this.movingRight = movingRight;
    }

    public void update(boolean bridgeOpen) {

        // Stop when bridge is open
        if (bridgeOpen) {

            if (movingRight && x < 390) {

                x += speed;

            } else if (!movingRight && x > 610) {

                x -= speed;
            }

        } else {

            if (movingRight) {

                x += speed;

            } else {

                x -= speed;
            }
        }
    }

    public void draw(Graphics g) {

        g.setColor(Color.RED);

        g.fillRect(
                (int) x,
                (int) y,
                35,
                20
        );

        // Wheels
        g.setColor(Color.BLACK);

        g.fillOval(
                (int) x + 5,
                (int) y + 15,
                8,
                8
        );

        g.fillOval(
                (int) x + 23,
                (int) y + 15,
                8,
                8
        );
    }

    public double getX() {

        return x;
    }
}


// Ship class
class Ship {

    private double x = 0;

    private double speed = 1.5;

    public void update() {

        x += speed;

        // Reset ship after leaving screen
        if (x > 1050) {

            x = -150;
        }
    }

    public double getX() {

        return x;
    }

    public void draw(Graphics g) {

        int y = 430;

        // Ship body
        g.setColor(Color.BLACK);

        int[] xPoints = {
                (int) x,
                (int) x + 100,
                (int) x + 80,
                (int) x + 20
        };

        int[] yPoints = {
                y,
                y,
                y + 30,
                y + 30
        };

        g.fillPolygon(
                xPoints,
                yPoints,
                4
        );

        // Ship cabin
        g.setColor(Color.WHITE);

        g.fillRect(
                (int) x + 35,
                y - 25,
                35,
                25
        );

        // Smoke stack
        g.setColor(Color.GRAY);

        g.fillRect(
                (int) x + 55,
                y - 40,
                10,
                15
        );
    }
}