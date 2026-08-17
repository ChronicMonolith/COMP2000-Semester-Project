import javax.swing.*;


public class BridgeSimulation {
    // Main class

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