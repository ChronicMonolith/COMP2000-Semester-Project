package TrialThree;

import java.util.ArrayList;
import javax.swing.*;

public class CarController {
    private final ArrayList<Car> cars;
    private final CarPanel panel;
    private final TrafficLight light;

    public CarController(ArrayList<Car> cars, CarPanel panel, TrafficLight light) {
        this.cars = cars;
        this.panel = panel;
        this.light = light;
    }

    public void start() {
        Timer timer = new Timer(17, e -> {

            light.update();

            for (int i = 0; i < cars.size(); i++) {
                cars.get(i).reactToLight(light);
                cars.get(i).updateMovement(panel.getWidth());
            }

            for (int i = 0; i < cars.size(); i++) {
                Car current = cars.get(i);

                int leaderIndex = (i - 1 + cars.size()) % cars.size();
                Car leader = cars.get(leaderIndex);

                current.updateSpeed(leader);
            }

            panel.repaint();
        });
        timer.start();
    }
}
