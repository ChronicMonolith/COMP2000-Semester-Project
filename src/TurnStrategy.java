import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@FunctionalInterface
public interface TurnStrategy {

    TurnDirection choose(Car car, List<TurnDirection> options);

    TurnStrategy RANDOM = (car, options) -> options.get(ThreadLocalRandom.current().nextInt(options.size()));

    TurnStrategy ALWAYS_STRAIGHT = (car, options) -> options.contains(TurnDirection.STRAIGHT)
            ? TurnDirection.STRAIGHT
            : RANDOM.choose(car, options);

    static TurnStrategy preferStraight(double chance) {
        return (car, options) -> options.contains(TurnDirection.STRAIGHT)
                && ThreadLocalRandom.current().nextDouble() < chance
                        ? TurnDirection.STRAIGHT
                        : RANDOM.choose(car, options);
    }
}
