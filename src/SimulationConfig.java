public class SimulationConfig {
    private static final SimulationConfig INSTANCE = new SimulationConfig();

    public static SimulationConfig getInstance() {
        return INSTANCE;
    }

    private SimulationConfig() {
    }

    // map / loop
    public final int mapWidth = 1280;
    public final int mapHeight = 720;
    public final int frameDelayMs = 16;

    // spawning
    public final int maxCars = 30;
    public final int spawnIntervalFrames = 12; // ~200 ms
    public final double spawnClearance = 40.0;

    // driving (pixels per frame, pixels per frame^2)
    public final double minSpeed = 1.6;
    public final double maxSpeed = 2.4;
    // public final double acceleration = 0.05;
    public final double minAcceleration = 0.02;
    public final double maxAcceleration = 0.11;
    public final double brakeDeceleration = 0.12;
    public final double safeGap = 28.0; // min centre-to-centre distance behind another car

    // traffic lights
    public final int greenMs = 4000;
    public final int yellowMs = 1000;
    public final int allRedMs = 600;

    // adaptive lights: green time grows with the number of cars waiting on that approach
    public final boolean adaptiveLights = true;
    public final int minGreenMs = 1500;
    public final int maxGreenMs = 7000;
    public final int msPerQueuedCar = 600;

    // emergency vehicles
    public final double emergencyChance = 0.03; // per spawn
    public final double emergencySpeedBonus = 0.6;
}
