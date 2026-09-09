public interface CarPositionProvider {
    int getX(Car car);

    int getY(Car car);

    boolean contains(Car car);
}