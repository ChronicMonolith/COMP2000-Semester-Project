public interface SignalPhase {
    int durationMs();

    void enter(TrafficLightController light);

    SignalPhase next(TrafficLightController light);

    class Green implements SignalPhase {
        private final Direction approach;

        public Green(Direction approach) {
            this.approach = approach;
        }

        public int durationMs() {
            return SimulationConfig.getInstance().greenMs;
        }

        public void enter(TrafficLightController light) {
            light.setAllRed();
            light.setLight(approach, TrafficLightController.LightState.GREEN);
        }

        public SignalPhase next(TrafficLightController light) {
            return new Yellow(approach);
        }
    }

    class Yellow implements SignalPhase {
        private final Direction approach;

        public Yellow(Direction approach) {
            this.approach = approach;
        }

        public int durationMs() {
            return SimulationConfig.getInstance().yellowMs;
        }

        public void enter(TrafficLightController light) {
            light.setLight(approach, TrafficLightController.LightState.YELLOW);
        }

        public SignalPhase next(TrafficLightController light) {
            return new AllRed(approach);
        }
    }

    class AllRed implements SignalPhase {
        private final Direction previous;

        public AllRed(Direction previous) {
            this.previous = previous;
        }

        public int durationMs() {
            return SimulationConfig.getInstance().allRedMs;
        }

        public void enter(TrafficLightController light) {
            light.setAllRed();
        }

        public SignalPhase next(TrafficLightController light) {
            return new Green(light.nextApproach(previous));
        }
    }
}
