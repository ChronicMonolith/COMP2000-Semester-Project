import java.util.Arrays;
import java.util.Collections;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class TrafficStats {

    public static final TrafficStats EMPTY = new TrafficStats(0, 0, 0, 0, Collections.emptyMap());

    public final int total;
    public final int driving;
    public final int stopped;
    public final double averageSpeed;
    public final double topSpeed;
    private final Map<Direction, Long> byDirection;

    private TrafficStats(int driving, int stopped, double averageSpeed, double topSpeed,
            Map<Direction, Long> byDirection) {
        this.driving = driving;
        this.stopped = stopped;
        this.total = driving + stopped;
        this.averageSpeed = averageSpeed;
        this.topSpeed = topSpeed;
        this.byDirection = byDirection;
    }

    /**
     * @param snapshot a copy of the car list - parallel streams must never read a
     *                 list
     *                 that the simulation could be changing at the same time.
     */
    public static TrafficStats compute(List<Car> snapshot) {
        int stoppedCount = snapshot.parallelStream()
                .collect(Collector.of(
                        () -> new int[] { 0 },

                        (count, car) -> {
                            if (car.stopped) {
                                count[0]++;
                            }
                        },

                        (a, b) -> {
                            a[0] += b[0];
                            return a;
                        }))[0];
        ConcurrentMap<Direction, Long> byDirection = snapshot.parallelStream()
                .collect(Collectors.groupingByConcurrent(Car::getDirection, Collectors.counting()));

        DoubleSummaryStatistics speed = snapshot.parallelStream()
                .collect(Collectors.summarizingDouble(c -> c.speed));

        return new TrafficStats(
                snapshot.size() - stoppedCount,
                stoppedCount,
                speed.getAverage(),
                speed.getCount() == 0 ? 0 : speed.getMax(),
                byDirection);
    }

    public double drivingPercent() {
        return total == 0 ? 0 : 100.0 * driving / total;
    }

    public long countFacing(Direction d) {
        return byDirection.getOrDefault(d, 0L);
    }

    public String directionSummary() {
        return Arrays.stream(Direction.values())
                .map(d -> d.name().charAt(0) + ":" + countFacing(d))
                .collect(Collectors.joining("  "));
    }
}
