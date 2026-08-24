package TrialThree;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        ArrayList<Car> cars = new ArrayList<>();
        Road road = new Road(0, 100, 100, 1, 10);
        TrafficLight light = new TrafficLight(600, 80);

        cars.add(new Car(800, road.getLaneY(0), 2, 20, 15, Color.GREEN));
        cars.add(new Car(500, road.getLaneY(0), 3, 20, 15, Color.RED));
        cars.add(new Car(200, road.getLaneY(0), 4.5, 20, 15, Color.BLACK));
        cars.add(new Car(100, road.getLaneY(0), 4, 25, 15, Color.GRAY));

        CarPanel panel = new CarPanel(cars, road, light);
        CarController controller = new CarController(cars, panel, light);

        JFrame frame = new JFrame("Cars");
        frame.add(panel);
        frame.setSize(1200, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        controller.start();
    }
}
