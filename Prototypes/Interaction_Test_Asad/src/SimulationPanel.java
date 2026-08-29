import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


public class SimulationPanel {
    private JFrame frame;
    private JPanel panel;
    private ArrayList<Car> cars = new ArrayList<>();
    
    public SimulationPanel() {
        cars.add(new Car(50, 50));
        cars.add(new Car(50, 250));

        panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(200, 20, 20));
                
                for (Car c : cars) {
                    c.draw(g);
                }
            }
        };
        
        panel.setBounds(0, 0, 800, 800);
		panel.setBackground(new Color(200, 200, 200));

        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                for (Car c : cars) {
                    if (c.isInside(me.getX(), me.getY())) {
                        c.isSelected = true;
                        c.onClick();
                    }
                    else if (c.isSelected)
                        c.isSelected = false;
                }
            }
        });

        frame = new JFrame("Traffic Simulation");
        frame.add(panel);
		frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void start() {
		frame.setVisible(true);

        Timer timer = new Timer(16, e -> {
            update();
            panel.repaint();
        });
        timer.start();
    }

    public void update() {
        for (Car c : cars) {
            c.move();
        }
    }
}
