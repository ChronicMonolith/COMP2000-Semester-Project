package TrialThree;

import java.awt.Color;

public class Car {
    private int x;
    private int y;
    private double speed;
    private int length;
    private int breath;
    private Color color;
    private double baseSpeed;
    private boolean stoppedByLight = false;

    public Car(int x, int y, double speed, int length, int breath, Color color) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.length = length;
        this.breath = breath;
        this.color = color;
        this.baseSpeed = this.speed;
    }

    // when the next car is near a distance match the speed of that car
    public void updateSpeed(Car other) {
        if (stoppedByLight)
            return;

        double distance = other.getPosX() - this.getPosX();

        if (distance <= 0)
            return;

        if (distance < 50) {
            this.speed = Math.max(0, this.speed - 0.2);
        }

        if (distance >= 50 && !stoppedByLight) {
            this.speed = Math.min(baseSpeed, this.speed + 0.05);
        }
    }

    public void updateMovement(int panelWidth) {
        x += speed;
        if (x > panelWidth) {
            x = -length;
        }
    }

    public void reactToLight(TrafficLight light) {
        int stopLine = light.getX() - 30;
        if (this.x + this.length < light.getX()) {

            if (this.x + this.length >= stopLine) {

                if (light.getState() != TrafficLight.State.GREEN) {
                    stoppedByLight = true;
                    this.speed = 0;
                    return;
                }
            }
        }

        if (stoppedByLight && light.getState() == TrafficLight.State.GREEN) {
            stoppedByLight = false;
            this.speed = baseSpeed;
        }
    }

    public int getPosX() {
        return this.x;
    }

    public int getPosY() {
        return this.y;
    }

    public double getSpeed() {
        return this.speed;
    }

    public int getLength() {
        return this.length;
    }

    public int getBreath() {
        return this.breath;
    }

    public Color getColor() {
        return this.color;
    }

    public void setSpeed(int sp) {
        this.speed = sp;
    }
}
