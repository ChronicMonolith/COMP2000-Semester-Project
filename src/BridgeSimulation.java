import javax.swing.*;

public class BridgeSimulation {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Vertical Bridge Traffic Simulation");

        SimulationPanel panel = new SimulationPanel();

        frame.add(panel);

        frame.setSize(1000, 700);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);

        panel.start();
    }
}