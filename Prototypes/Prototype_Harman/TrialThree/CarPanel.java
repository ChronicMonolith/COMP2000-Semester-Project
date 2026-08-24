package TrialThree;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class CarPanel extends JPanel {
    private TrafficLight light;
    private final ArrayList<Car> cars;
    private Road road;

    public CarPanel(ArrayList<Car> cars, Road road, TrafficLight light) {
        this.light = light;
        this.road = road;
        this.cars = cars;

        setDoubleBuffered(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, road.getRoadY(), getWidth(), getHeight());

        switch (light.getState()) {
            case RED:
                g.setColor(Color.RED);
                break;
            case YELLOW:
                g.setColor(Color.YELLOW);
                break;
            case GREEN:
                g.setColor(Color.GREEN);
                break;
        }

        g.fillOval((light.getX()), light.getY(), 20, 20);

        for (Car car : cars) {
            g.setColor(car.getColor());
            g.fillRect(car.getPosX(), car.getPosY(), car.getLength(), car.getBreath());
        }
    }
}
