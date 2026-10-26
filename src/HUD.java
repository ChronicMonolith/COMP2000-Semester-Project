import java.awt.*;
import java.util.stream.Collectors;

public class HUD {
    public int x;
    public int y;

    public HUD(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void draw(Graphics2D g, CarManager carManager) {
        // World Data
        g.setColor(
            new Color(100, 100, 100)
        );

        g.fillRect(
            x + 15,
            y + 15,
            250,
            100
        );

        g.setColor(Color.WHITE);

        g.setFont(new Font(
            "Arial",
            Font.BOLD,
            16
        ));

        int carsTotal = carManager.getCars().size();
        
        g.drawString(
            "Cars: " + carsTotal,
            x + 30,
            y + 40
        );

        g.setFont(new Font(
            "Arial",
            Font.PLAIN,
            16
        ));
                
        long carsDriving = carManager.countDrivingCars();
        
        g.drawString(
            "Driving: " + carsDriving,
            x + 30,
            y + 60
        );
        
        long carsStopped = carsTotal - carsDriving;
        
        g.drawString(
            "Stopped: " + carsStopped,
            x + 30,
            y + 80
        );

        float carsDrivingRatio = (float) carsDriving / carsTotal * 100;
        
        g.drawString(
            "% Driving: " + (int) carsDrivingRatio + "%",
            x + 30,
            y + 100
        );

        // Individual Car Data
        Car selectedCar = carManager.getSelectedCar();

        if (selectedCar != null) {
            g.setColor(
                new Color(100, 100, 100)
            );
        
            g.fillRect(
                x + 300,
                y + 15,
                250,
                100
            );

            g.setColor(Color.WHITE);

            g.setFont(new Font(
                "Arial",
                Font.BOLD,
                16
            ));

            g.drawString(
                "Vehicle: Car",
                x + 315,
                y + 40
            );

            g.setFont(new Font(
                "Arial",
                Font.PLAIN,
                16
            ));
            
            g.drawString(
                "Speed: " + selectedCar.speed,
                x + 315,
                y + 60
            );

            g.drawString(
                "Direction: " + selectedCar.direction,
                x + 315,
                y + 80
            );

            if (selectedCar.stopped)
                g.drawString(
                    "State: STOPPED",
                    x + 315,
                    y + 100
                );
            else
                g.drawString(
                    "State: " + selectedCar.state,
                    x + 315,
                    y + 100
                );
        }
    }
}
