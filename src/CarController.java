import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Point;
import java.util.List;

public class CarController {

    private final Car car;
    private final CarNavigator navigator;
    private final MapPanel panel;
    private final Timer timer;

    private Roundabout lastRoundabout = null;
    private Intersection lastIntersection = null;

    public CarController(Car car, CarNavigator navigator, MapPanel panel) {
        this.car = car;
        this.navigator = navigator;
        this.panel = panel;

        timer = new Timer(17, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });

        timer.start();
    }

    private void update() {
        if (navigator.followingArc()) {
            navigator.updateArc(car);
        } else {
            CarPositionProvider provider = navigator.getActiveProvider(car);

            if (provider instanceof Roundabout roundabout) {
                if (roundabout != lastRoundabout) {
                    lastRoundabout = roundabout;
                    TurnDecision decision = TurnDecision.random();
                    List<Point> arc = roundabout.generateArc(car.direction, decision);

                    if (arc != null && !arc.isEmpty()) {
                        navigator.setArc(arc);
                        navigator.updateArc(car);
                    } else {
                        navigator.update(car);
                    }
                } else {
                    navigator.update(car);
                }
            } else if (provider instanceof Intersection intersection) {
                if (intersection != lastIntersection) {
                    lastIntersection = intersection;
                    TurnDecision decision = TurnDecision.random();

                    if (decision == TurnDecision.LEFT) {
                        List<Point> arc = intersection.generateLeftTurnArc(car.direction);
                        if (arc != null && !arc.isEmpty()) {
                            navigator.setArc(arc);
                            navigator.updateArc(car);
                        } else {
                            navigator.update(car);
                        }
                    } else {
                        navigator.update(car);
                    }
                } else {
                    navigator.update(car);
                }
            } else {
                if (!(provider instanceof Roundabout)) {
                    lastRoundabout = null;
                }
                if (!(provider instanceof Intersection)) {
                    lastIntersection = null;
                }
                navigator.update(car);
            }
        }
        panel.repaint();
    }
}