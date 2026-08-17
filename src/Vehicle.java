public class Vehicle {
    public Position vehiclePosition;
    public int Speed = 0;
    public int maxSpeed;
    public String type; // Don't know how to make this one

    public Vehicle(int x, int y, int maxSpeed) {
        this.vehiclePosition = new vehiclePosition(x, y);
        this.maxSpeed = maxSpeed;
    }
}