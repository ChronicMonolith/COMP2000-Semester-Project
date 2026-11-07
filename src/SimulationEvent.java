public final class SimulationEvent {
 
    public enum Type {
        CAR_SPAWNED,
        CAR_LEFT_MAP
    }
 
    public final Type type;
    public final String message;
 
    public SimulationEvent(Type type, String message) {
        this.type = type;
        this.message = message;
    }
}
 